package com.github.richardjwild.blather.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
    private List<Session> sessions = new ArrayList<>();
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    void initializeOn(int port) throws IOException {
        isRunning.set(true);
        serverSocket = new ServerSocket(port);

        listenerThread = runAsync(() -> {
            while (isRunning.get()) {
                try {
                    Session session = new Session(serverSocket.accept());
                    sessions.add(session);
                } catch (IOException ignored) {

                }
            }
        });
    }

    void stop() throws Exception {
        if (!isRunning.get()) return;

        isRunning.set(false);
        sessions.forEach(Session::stop);
        serverSocket.close();
        listenerThread.cancel(true);
    }

    private class Session {
        private Socket socket;

        Session(Socket socket) {
            this.socket = socket;
            try {
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                out.println("Welcome to Blather");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void stop() {
            try {
                socket.close();
            } catch (IOException ignored) {

            }
        }
    }
}
