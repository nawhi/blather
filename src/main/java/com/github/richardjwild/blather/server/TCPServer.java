package com.github.richardjwild.blather.server;

import com.github.richardjwild.blather.application.Application;
import com.github.richardjwild.blather.io.Input;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
                System.out.println("starting accept() loop");
                try {
                    Connection connection = new Connection(serverSocket.accept());
                    System.out.println("got connection");
                    Application app = anApplication()
                            .withInput(connection.getInput())
                            .withOutput(connection.getOutput())
                            .build();
                    System.out.println("built app");
                    ClientSession session = new ClientSession(connection, app).run();
                    System.out.println("added session");
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
