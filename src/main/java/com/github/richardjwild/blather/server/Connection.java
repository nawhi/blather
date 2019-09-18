package com.github.richardjwild.blather.server;

import com.github.richardjwild.blather.io.Input;
import com.github.richardjwild.blather.io.Output;
import jdk.jshell.spi.ExecutionControl;

import java.io.*;
import java.net.Socket;

class Connection {
    private final BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    Connection(Socket socket) {
        this.socket = socket;
        try {
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Output getOutput() {
        return out::println;
    }

    void close() {
        try {
            socket.close();
        } catch (IOException ignored) {

        }
    }

    public Input getInput() {
        return () -> {
            try {
                return in.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
