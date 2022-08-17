package org.java.training.web.socket.ssl.client;

import lombok.Cleanup;
import org.java.training.web.socket.ssl.response.ResponseType;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import static java.lang.String.format;
import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.java.training.web.socket.constant.SocketConstants.HTTPS;
import static org.java.training.web.socket.constant.SocketConstants.HTTP_VERSION;
import static org.java.training.web.socket.constant.SocketConstants.NEWLINE;
import static org.java.training.web.socket.constant.SocketConstants.QUESTION_SIGN;

public class SSLHelper {

    public URL createUrl(String spec) {
        try {
            return new URL(spec);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("URL is malformed", e);
        }
    }

    public String getForImages(URL target, ResponseType responseType) {
        try {
            @Cleanup Socket client = createClient(target);
            if (client instanceof SSLSocket sslClient) {
                sslClient.startHandshake();
            }

            var writer = new PrintWriter(client.getOutputStream(), true);
            writer.println(constructGetRequest(target));

            return parseResponse(client.getInputStream(), responseType);
        } catch (IOException ex) {
            throw new IllegalStateException(format("Cannot send request to %s", target), ex);
        }
    }

    private Socket createClient(URL target) {
        String host = getHost(target);
        int port = getPort(target);
        try {
            if (isSecured(target)) {
                SSLSocketFactory socketFactory = SSLContext.getDefault().getSocketFactory();
                return socketFactory.createSocket(host, port);
            } else {
                return new Socket(host, port);
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException(format("Cannot create secure client to %s", target), ex);
        }
    }

    private String constructGetRequest(URL target) {
        String host = getHost(target);
        String requestLine;
        if (target.toString().contains(QUESTION_SIGN)) {
            requestLine = join(EMPTY, target.getPath(), QUESTION_SIGN, target.getQuery());
        } else {
            requestLine = join(EMPTY, target.getPath());
        }
        return format("""
                GET %s %s
                Host: %s
                                
                """, requestLine, HTTP_VERSION, host);
    }


    private String parseResponse(InputStream inputStream, ResponseType responseType) {
        return responseType == ResponseType.BODY ? parseResponseBody(inputStream) : parseResponseHeaders(inputStream);
    }

    private String parseResponseBody(InputStream inputStream) {
        try {
            @Cleanup var reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            var jsonBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.contains("{") || line.contains("}")) {
                    jsonBuilder.append(line);
                }
            }
            return jsonBuilder.toString();
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot parse response body from socket", ex);
        }
    }

    private String parseResponseHeaders(InputStream inputStream) {
        try {
            @Cleanup var reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            var responseHeaders = new StringBuilder();

            while (!isEmpty(line = reader.readLine())) {
                responseHeaders.append(line).append(NEWLINE);
            }
            return responseHeaders.toString();
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot parse response headers from socket", ex);
        }
    }

    private String getHost(URL url) {
        return url.getHost();
    }

    private int getPort(URL url) {
        int port = url.getPort();
        return port == -1 ? url.getDefaultPort() : port;
    }

    private boolean isSecured(URL url) {
        return url.getProtocol().equals(HTTPS);
    }
}
