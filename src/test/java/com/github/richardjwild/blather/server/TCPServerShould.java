package com.github.richardjwild.blather.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TCPServerShould {

    private static final int PORT = 8080;

    @Rule
    public Timeout timeout = Timeout.seconds(2);
    private TCPServer server = new TCPServer();

    @Before
    public void setUp() throws Exception {
        server.initializeOn(PORT);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void does_not_crash_if_stopped_before_being_started() throws Exception {
        new TCPServer().stop();
    }

    @Test
    public void print_welcome_message_to_one_client() throws IOException {
        try (SocketConnection connection = new SocketConnection(PORT)) {
            assertEquals("Welcome to Blather", connection.readLine());
        }
    }

    @Test
    public void print_welcome_message_to_two_clients() throws IOException {
        try (SocketConnection connection = new SocketConnection(PORT);
             SocketConnection connection2 = new SocketConnection(PORT);
             SocketConnection connection3 = new SocketConnection(PORT)) {
            assertEquals("Welcome to Blather", connection.readLine());
            assertEquals("Welcome to Blather", connection2.readLine());
            assertEquals("Welcome to Blather", connection3.readLine());
        }
    }
}