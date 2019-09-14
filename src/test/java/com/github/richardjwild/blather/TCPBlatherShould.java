package com.github.richardjwild.blather;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class TCPBlatherShould {

    private static final int PORT = 8080;
    private final TCPBlather app = new TCPBlather();

    @Before
    public void setUp() throws Exception {
        app.startOn(PORT);
    }

    @After
    public void tearDown() throws Exception {
        app.stop();
    }

    @Test
    public void display_welcome_message() throws IOException {
        try (TelnetConnection connection = new TelnetConnection()) {
            assertEquals(connection.readLine(), "Welcome to Blather");
        }
    }

    private class TelnetConnection implements AutoCloseable {

        private final BufferedReader in;
        private final Socket socket;

        TelnetConnection() throws IOException {
            this.socket = new Socket("localhost", TCPBlatherShould.PORT);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        String readLine() throws IOException {
            return in.readLine();
        }

        @Override
        public void close() throws IOException {
            socket.close();
        }
    }
}