package com.github.richardjwild.blather.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

class Connection {
    private PrintWriter out;
    private Socket socket;

    Connection(Socket socket) {
        this.socket = socket;
        try {
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PrintWriter getOutput() {
        return out;
    }

    void close() {
        try {
            socket.close();
        } catch (IOException ignored) {

        }
    }
}
