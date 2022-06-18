package org.java.training.web.net.util;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

@UtilityClass
public class Sockets {

    @SneakyThrows
    public void connectAndWrite(String host, int port, String message) {
        @Cleanup var client = new Socket();
        var address = new InetSocketAddress(host, port);
        client.connect(address);
        OutputStream outputStream = client.getOutputStream();

        @Cleanup var writer = new PrintWriter(new OutputStreamWriter(outputStream));
        String currentHost = InetAddress.getLocalHost().getHostAddress();
        writer.printf("%s : (%s)", message, currentHost);
        writer.flush();
    }

    @SneakyThrows
    public void sendHttpRequest(String host, int port, String httpMethod, String url) {
        @Cleanup var socket = new Socket();
        var inetAddress = new InetSocketAddress(host, port);
        socket.connect(inetAddress);
        OutputStream outputStream = socket.getOutputStream();

        @Cleanup var writer = new PrintWriter(outputStream);
        writer.printf("%s %s HTTP/1.1", httpMethod, url);
        writer.printf("Host: %s\n", host);
        writer.flush();

        @Cleanup var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
