package servlet.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.*;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MutableHttpServletRequest extends HttpServletRequestWrapper {

    Map<String, String> customHeaders;

    public MutableHttpServletRequest(HttpServletRequest request) {
        super(request);
        customHeaders = new HashMap<>();
    }

    public void addHeader(String headerName, String headerValue) {
        customHeaders.put(headerName, headerValue);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        var headerNames = new HashSet<>(customHeaders.keySet());

        Enumeration<String> requestHeaderNames = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (requestHeaderNames.hasMoreElements()) {
            String headerName = requestHeaderNames.nextElement();
            headerNames.add(headerName);
        }

        return Collections.enumeration(headerNames);
    }

    @Override
    public String getHeader(String name) {
        return defaultIfNull(
                customHeaders.get(name),
                ((HttpServletRequest) getRequest()).getHeader(name)
        );
    }
}
