package com.github.richardjwild.blather;

import com.github.richardjwild.blather.time.Clock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.*;
import java.net.Socket;
import java.time.Instant;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BlatherServerShould {

    private static final long TWO_MINUTES = 120;
    private static final long ONE_MINUTE = 60;
    private static final long FIFTEEN_SECONDS = 15;
    private static final long ONE_SECOND = 1;

    private static final int PORT = 8080;

    @Mock
    private Clock clock;

    private BlatherServer app;

    @Before
    public void setUp() {
        app = new BlatherServer(PORT);
    }

    @After
    public void tearDown() {
        app.stop();
    }

    @Test
    public void display_a_users_posted_messages() throws IOException {

        Instant now = Instant.now();
        when(clock.now())
                .thenReturn(now.minusSeconds(TWO_MINUTES))
                .thenReturn(now.minusSeconds(ONE_MINUTE))
                .thenReturn(now.minusSeconds(FIFTEEN_SECONDS))
                .thenReturn(now.minusSeconds(ONE_SECOND))
                .thenReturn(now);

        try (TelnetConnection connection = new TelnetConnection()) {

            connection.writeLine("Alice -> My first message");
            connection.writeLine("Bob -> Hello world!");
            connection.writeLine("Alice -> Sup everyone?");
            connection.writeLine("Bob -> I wanna party :)");
            connection.writeLine("Emma follows Bob");
            connection.writeLine("Emma follows Alice");
            connection.writeLine("Emma wall");
            connection.writeLine("quit");

            String expectedOutput = "Welcome to Blather\n" +
                    "My first message (2 minutes ago)\n" +
                    "Sup everyone? (15 seconds ago)\n" +
                    "Hello world! (1 minute ago)\n" +
                    "I wanna party :) (1 second ago)\n" +
                    "Bye!\n";

            assertThat(connection.readAll(), is(expectedOutput));
        }


    }

    private class TelnetConnection implements AutoCloseable {

        private final BufferedReader in;
        private final PrintWriter out;
        private final Socket socket;

        TelnetConnection() throws IOException {
            this.socket = new Socket("localhost", BlatherServerShould.PORT);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        }

        @Override
        public void close() throws IOException {
            socket.close();
        }

        String readAll() {
            return in.lines().collect(Collectors.joining("\n"));
        }

        void writeLine(String line) {
            out.println(line);
        }
    }
}