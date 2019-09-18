package com.github.richardjwild.blather.server;

import com.github.richardjwild.blather.application.Application;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.richardjwild.blather.application.ApplicationBuilder.anApplication;
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
                    Application app = anApplication()
                            .withInput(connection.getInput())
                            .withOutput(connection.getOutput())
                            .build();
                    ClientSession session = new ClientSession(connection, app).start();
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
