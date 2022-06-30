package servlet.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import servlet.request.MutableHttpServletRequest;

import java.util.concurrent.ThreadLocalRandom;

import static servlet.constant.ServletConstants.CUSTOM_SESSION_HEADER;

@WebFilter(servletNames = {"EveningServlet"}, dispatcherTypes = DispatcherType.REQUEST)
@Slf4j
public class HttpServletRequestFilter implements Filter {

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        var httpRequest = (HttpServletRequest) request;
        var mutableHttpRequest = new MutableHttpServletRequest(httpRequest);

        boolean isCustomSession = ThreadLocalRandom.current().nextBoolean();

        mutableHttpRequest.addHeader(CUSTOM_SESSION_HEADER, isCustomSession ? "true" : "false");

        log.info("Custom session header set to {}", mutableHttpRequest.getHeader(CUSTOM_SESSION_HEADER));

        chain.doFilter(mutableHttpRequest, response);
    }
}
