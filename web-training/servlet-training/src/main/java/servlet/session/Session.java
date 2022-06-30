package servlet.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Session {

    private static final long DEFAULT_TTL = 2;

    UUID sessionId;

    ZonedDateTime creationTime;

    @With
    long timeToLiveMinutes;

    List<Cookie> sessionCookies;

    public static Session fromRequest(@NonNull HttpServletRequest request) {
        Cookie[] requestCookies = request.getCookies();
        List<Cookie> cookies = new ArrayList<>();

        if (nonNull(requestCookies)) {
            cookies.addAll(asList(requestCookies));
        }

        UUID sessionId = UUID.randomUUID();
        ZonedDateTime now = ZonedDateTime.now();
        return new Session(sessionId, now, TimeUnit.MINUTES.toMinutes(DEFAULT_TTL), cookies);
    }

    public void addCookie(Cookie cookie) {
        sessionCookies.add(cookie);
    }

    public Optional<String> getAttribute(String attrName) {
        return sessionCookies.stream()
                .filter(cookie -> cookie.getName().equals(attrName))
                .findFirst()
                .map(Cookie::getValue)
                .or(Optional::empty);
    }

    public String textUUID() {
        return sessionId.toString();
    }
}
