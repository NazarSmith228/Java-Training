package servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import servlet.session.Session;
import servlet.session.SessionProvider;

import java.io.PrintWriter;
import java.util.Optional;

import static servlet.constant.ServletConstants.*;

@WebServlet(name = "EveningServlet", value = "/evening")
@Slf4j
public class EveningServlet extends HttpServlet {

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String withCustomSession = req.getHeader(CUSTOM_SESSION_HEADER);
        if (Boolean.parseBoolean(withCustomSession)) {
            doGetWithCustomSession(req, resp);
            return;
        }

        log.info("GET request without session mechanism");
        PrintWriter writer = resp.getWriter();

        String name = Optional.ofNullable(req.getParameter(NAME)).orElse(DEFAULT_NAME);
        writer.printf("<P> Good evening, %s <P>", name);
    }

    @SneakyThrows
    private void doGetWithCustomSession(HttpServletRequest req, HttpServletResponse resp) {
        log.info("GET request with custom session mechanism");

        PrintWriter writer = resp.getWriter();

        SessionProvider sessionProvider = SessionProvider.create();
        Session session = sessionProvider.getOrCreateSession(req);

        Optional.ofNullable(req.getParameter(NAME))
                .ifPresent(name -> session.addCookie(new Cookie(NAME, name)));

        String nameAttr = session.getAttribute(NAME).orElse(DEFAULT_NAME);

        resp.addCookie(new Cookie(SESSIONID_COOKIE, session.textUUID()));
        resp.addHeader(SESSION_TTL_HEADER, String.valueOf(session.getTimeToLiveMinutes()));

        writer.printf("<P> Good evening, %s <P>", nameAttr);

    }
}
