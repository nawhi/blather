package com.github.richardjwild.blather.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.CompletableFuture.runAsync;

class TCPServer {

    private Future<Void> listenerThread;
    private ServerSocket serverSocket;
    private List<ClientSession> sessions = new ArrayList<>();
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    void initializeOn(int port) throws IOException {
        isRunning.set(true);
        serverSocket = new ServerSocket(port);

        listenerThread = runAsync(() -> {
            while (isRunning.get()) {
                try {
                    Connection connection = new Connection(serverSocket.accept());
                    ClientSession session = new ClientSession(connection);
                    sessions.add(session);
                } catch (IOException ignored) {

                }
            }
        });
    }

    void stop() throws Exception {
        if (!isRunning.get())
            return;
        isRunning.set(false);
        sessions.forEach(ClientSession::stop);
        serverSocket.close();
        listenerThread.cancel(true);
    }
}
