package org.java.training.web.servlet.session;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.java.training.web.servlet.constant.ServletConstants.COOKIE_HEADER;
import static org.java.training.web.servlet.constant.ServletConstants.SESSIONID_PARAM;

@Slf4j
public class SessionProvider {

    private static final Map<UUID, Session> EXISTING_SESSIONS = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService SESSION_SCHEDULER = Executors.newScheduledThreadPool(1);

    private static SessionProvider instance;

    private SessionProvider() {
        log.info("Scheduled session cleanup");
        SESSION_SCHEDULER.scheduleAtFixedRate(this::removeExpiredSessions, 60, 60, TimeUnit.SECONDS);
    }

    public static SessionProvider create() {
        if (isNull(instance)) {
            synchronized (SessionProvider.class) {
                if (isNull(instance)) {
                    log.info("SessionProvider instance created");
                    instance = new SessionProvider();
                }
            }
        }
        return instance;
    }

    public Session getOrCreateSession(HttpServletRequest request) {
        Session session = getSessionOrNull(request);
        return nonNull(session) ? session : createNewSession(request);
    }

    public Session getOrCreateSession(HttpServletRequest request, long minutesToLive) {
        Session session = getSessionOrNull(request);
        if (nonNull(session)) {
            if (session.getTimeToLiveMinutes() != minutesToLive) {
                EXISTING_SESSIONS.remove(session.getSessionId());
            } else {
                return session;
            }
        }
        return createNewSession(request, minutesToLive);
    }

    private Session getSessionOrNull(HttpServletRequest request) {
        log.info("Getting session from request...");
        return EXISTING_SESSIONS.getOrDefault(getIdFromCookies(request), null);
    }

    private Session createNewSession(HttpServletRequest request) {
        log.info("Creating new session from request `{}`", request.getRequestURI());
        Session newSession = Session.fromRequest(request);
        EXISTING_SESSIONS.put(newSession.getSessionId(), newSession);
        return newSession;
    }

    private Session createNewSession(HttpServletRequest request, long minutesToLive) {
        log.info("Creating new session with from request `{}` with TTL {} minutes",
                request.getRequestURI(), minutesToLive);

        Session newSession = Session.fromRequest(request).withTimeToLiveMinutes(minutesToLive);
        EXISTING_SESSIONS.put(newSession.getSessionId(), newSession);
        return newSession;
    }

    private UUID getIdFromCookies(HttpServletRequest request) {
        String cookies = request.getHeader(COOKIE_HEADER);
        String sessionId = StringUtils.substringAfter(cookies, SESSIONID_PARAM);

        return isNotEmpty(sessionId) ? UUID.fromString(sessionId) : UUID.randomUUID();
    }

    private void removeExpiredSessions() {
        log.info("Cleaning expired sessions...");
        EXISTING_SESSIONS.values()
                .removeIf(session -> ZonedDateTime.now()
                        .isAfter(session.getCreationTime().plusMinutes(session.getTimeToLiveMinutes())));
    }
}
