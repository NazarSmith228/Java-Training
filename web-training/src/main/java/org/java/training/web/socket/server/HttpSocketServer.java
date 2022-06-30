package org.java.training.web.socket.server;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.java.training.web.common.HttpStatusCode;
import org.java.training.web.socket.server.exception.SocketException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpSocketServer {

    private static final ExecutorService WORKERS = Executors.newFixedThreadPool(50);
    private static final int DEFAULT_PORT = 8080;

    private final ServerSocket socket;
    private final SocketRequestHandler requestHelper;

    public HttpSocketServer() {
        this(DEFAULT_PORT);
    }

    private HttpSocketServer(int port) {
        try {
            socket = new ServerSocket(port);
            requestHelper = SocketRequestHandler.newInstance();

            System.out.println("Server is starting....");
            start();
        } catch (IOException e) {
            throw new SocketException("Error creating server socket", e,
                    HttpStatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    private void start() {
        while (true) {
            try {
                Socket acceptedSocket = socket.accept();
                WORKERS.submit(() -> processRequest(acceptedSocket));
            } catch (IOException ex) {
                throw new SocketException("Error accepting/processing socket request",
                        ex, HttpStatusCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @SneakyThrows
    private void processRequest(Socket accepted) {
        @Cleanup InputStream is = accepted.getInputStream();
        @Cleanup OutputStream os = accepted.getOutputStream();
        var writer = new PrintWriter(os, true, Charset.defaultCharset());
        try {
            String response = requestHelper.parseRequest(is);
            writer.println(response);
            writer.println();
        } catch (SocketException ex) {
            requestHelper.writeOutExceptionally(writer, ex);
        }
    }

}
