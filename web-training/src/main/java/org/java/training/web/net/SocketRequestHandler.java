package org.java.training.web.net;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;
import org.java.training.web.common.HttpStatusCode;
import org.java.training.web.net.exception.SocketException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

import static org.apache.commons.lang3.StringUtils.*;
import static org.java.training.web.net.constant.NetConstants.*;

@NoArgsConstructor(staticName = "newInstance")
public class SocketRequestHandler {
    private static final Set<String> AVAILABLE_METHODS;
    private static final Set<String> AVAILABLE_ENDPOINTS;
    private static final Map<String, String> ACCEPTABLE_HEADERS;
    private static final Map<String, UnaryOperator<String>> ENDPOINTS_HANDLERS;

    static {
        AVAILABLE_METHODS = Set.of(HttpGet.METHOD_NAME);
        AVAILABLE_ENDPOINTS = Set.of("/hello");

        ACCEPTABLE_HEADERS = Map.of(
                HttpHeaders.HOST, "localhost:8080",
                HttpHeaders.ACCEPT, "text/html",
                HttpHeaders.ACCEPT_CHARSET, "utf-8",
                HttpHeaders.ACCEPT_LANGUAGE, "en-US");

        ENDPOINTS_HANDLERS = Map.of(
                "/hello", EndpointsHandler::responseToHello);
    }

    private String requestLine;

    public String parseRequest(InputStream requestContent) {
        var reader = new BufferedReader(new InputStreamReader(requestContent));
        try {
            String firstLine = reader.readLine();
            setAndValidateRequestLine(firstLine);
            parseAndValidateHeaders(reader);
            String requestUrl = extractRequestUrl();

            return getResponse(requestUrl);
        } catch (Exception e) {
            throw new SocketException("Error during parsing request", e, HttpStatusCode.BAD_REQUEST);
        }
    }

    private String getResponse(String url) {
        String endpoint = parseEndpoint(url);
        String response = ENDPOINTS_HANDLERS.get(endpoint).apply(url);
        return String.format("""
                        HTTP/1.1 200 OK
                        Connection: Keep-Alive
                        Date: %s
                        Content-Type: text/html; charset=utf-8
                        Server: Apache
                                                               
                        <P>%s</P>
                        """,
                ZonedDateTime.now(),
                response
        );
    }

    public void writeOutExceptionally(PrintWriter writer, SocketException exception) {
        HttpStatusCode exceptionStatus = exception.getHttpStatus();
        writer.printf("""
                        HTTP/1.1 %d %s
                        Connection: Keep-Alive
                        Date: %s
                        Content-Type: text/html; charset=utf-8
                        Server: Apache
                                                   
                        <P>ERROR: %s</P>
                        """,
                exceptionStatus.getValue(),
                exceptionStatus.getDescription(),
                ZonedDateTime.now(),
                String.join(" - ", exception.getMessage(), exception.getCause().getMessage())
        );
    }

    private void setAndValidateRequestLine(String requestLine) {
        if (requestLine.isBlank() || !requestLine.contains(HTTP_VERSION)) {
            throw new IllegalArgumentException("Invalid request line: " + requestLine);
        }
        String httpMethod = requestLine.substring(0, 3);
        if (!AVAILABLE_METHODS.contains(httpMethod)) {
            throw new UnsupportedOperationException("Unsupported method: " + httpMethod);
        }
        this.requestLine = requestLine;
    }

    @SneakyThrows
    private void parseAndValidateHeaders(BufferedReader reader) {
        String header;
        while (!isEmpty(header = reader.readLine())) {
            int idx = header.indexOf(SEMICOLON);
            if (idx == -1) {
                throw new IllegalArgumentException("Invalid header: " + header);
            } else {
                String headerName = header.substring(0, idx);
                String headerValue = header.substring(idx + 2);

                String headerValueFromMap = ACCEPTABLE_HEADERS.getOrDefault(headerName, EMPTY);
                if (!headerValueFromMap.equals(headerValue) && !headerValue.contains(headerValueFromMap)) {
                    throw new IllegalArgumentException("Invalid header: " + header);
                }
            }
        }
    }

    private String extractRequestUrl() {
        int spaceIdx = requestLine.indexOf(SPACE);
        int versionIdx = requestLine.indexOf(HTTP_VERSION);
        String url = requestLine.substring(spaceIdx + 1, versionIdx).stripTrailing();

        return url.startsWith(BACKSLASH) ? url : BACKSLASH.concat(url);
    }

    private String parseEndpoint(String url) {
        StringBuilder endpointBuilder = new StringBuilder(EMPTY);
        int idx = 0;
        while (idx < url.length() &&
                (Character.isLetter(url.charAt(idx)) || url.charAt(idx) == '/')) {
            endpointBuilder.append(url.charAt(idx++));
        }

        String endpoint = endpointBuilder.toString();
        if (!AVAILABLE_ENDPOINTS.contains(endpoint)) {
            throw new UnsupportedOperationException("Endpoint is not supported: " + endpoint);
        }
        return endpoint;
    }

    private static class EndpointsHandler {

        private static String responseToHello(String url) {
            int queryIdx = url.indexOf(QUERY);
            if (queryIdx != -1) {
                String queryParam = url.substring(queryIdx + 1);
                String name = queryParam.split(EQUAL_SIGN)[1];
                return String.format("Hello, %s!", name);
            } else {
                return "Hello, Anonymous";
            }
        }
    }
}
