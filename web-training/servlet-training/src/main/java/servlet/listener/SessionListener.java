package servlet.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        String sessionId = session.getId();

        ZonedDateTime creationTime = ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(session.getCreationTime()),
                ZoneId.systemDefault()
        );

        System.out.printf("""
                        Session created.
                        Creation time: %s.
                        Session id: %s
                        """,
                creationTime, sessionId);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        String sessionId = session.getId();

        System.out.printf("""
                        Session destroyed.
                        Destruction time: %s.
                        Session id: %s
                        """,
                ZonedDateTime.now(), sessionId);
    }
}
