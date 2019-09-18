package com.github.richardjwild.blather.server;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TCPServerShould {

    private static final int PORT = 8080;

    @Test(timeout = 2000)
    public void print_welcome_message_from_single_client() throws IOException {
        TCPServer server = new TCPServer();
        server.initializeOn(PORT);

        try (SocketConnection connection = new SocketConnection(PORT)) {
            assertThat(connection.readLine(), is("Welcome to Blather"));
        }
    }
}