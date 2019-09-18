package com.github.richardjwild.blather.server;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TCPServerShould {

    @Rule
    public Timeout timeout = Timeout.seconds(2);

    private static final int PORT = 8080;

    @Test
    public void print_welcome_message_from_single_client() throws IOException {
        TCPServer server = new TCPServer();
        server.initializeOn(PORT);

        try (SocketConnection connection = new SocketConnection(PORT)) {
            assertThat(connection.readLine(), is("Welcome to Blather"));
        }
    }
}