package com.github.richardjwild.blather.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static java.util.concurrent.CompletableFuture.runAsync;

class TCPServer {

    void initializeOn(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        runAsync(() -> {
            try {
                Socket connection = serverSocket.accept();
                PrintWriter out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
                out.println("Welcome to Blather");
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    void stop() {
        throw new UnsupportedOperationException();
    }
}
