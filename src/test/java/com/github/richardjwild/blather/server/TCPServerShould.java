package com.github.richardjwild.blather.server;

import org.junit.Test;

import java.io.IOException;

public class TCPServerShould {
    private static final int PORT = 8080;

    @Test
    public void accept_client_connection_on_given_port() throws IOException {
        new TCPServer().startOn(PORT);

        try (SocketConnection connection = new SocketConnection(PORT)) {

        }
    }
}