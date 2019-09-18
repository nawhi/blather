package com.github.richardjwild.blather.server;

import java.io.*;
import java.net.Socket;
import java.util.stream.Collectors;

class SocketConnection implements AutoCloseable {

    private final BufferedReader in;
    private final PrintWriter out;
    private final Socket socket;

    SocketConnection(int port) throws IOException {
        this.socket = new Socket("localhost", port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    String readLine() throws IOException {
        return in.readLine();
    }

    String readAllLines() {
        return in.lines().collect(Collectors.joining("\n"));
    }

    void writeLine(String line) {
        out.println(line);
    }
}
