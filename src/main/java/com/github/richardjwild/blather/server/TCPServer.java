package com.github.richardjwild.blather.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static java.util.concurrent.CompletableFuture.runAsync;

class TCPServer {
    void startOn(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        runAsync(() -> {
            try {
                Socket connection = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    void stop() {
        throw new UnsupportedOperationException();
    }
}
