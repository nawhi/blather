package com.github.richardjwild.blather.server;

import java.io.IOException;
import java.io.PrintWriter;

class ClientSession {
    private Connection connection;

    ClientSession(Connection connection) {
        this.connection = connection;
        PrintWriter out = connection.getOutput();
        out.println("Welcome to Blather");
        out.flush();
    }

    void stop() {
        connection.close();
    }

}
