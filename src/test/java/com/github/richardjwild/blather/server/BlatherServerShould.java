package com.github.richardjwild.blather.server;

import com.github.richardjwild.blather.time.Clock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.time.Instant;

import static org.junit.Assert.assertEquals;
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
    public void setUp() throws IOException {
        app = new BlatherServer(PORT);
    }

    @After
    public void tearDown() throws Exception {
        app.stop();
    }

    @Test(timeout = 1000)
    public void connect_to_single_client() throws IOException {

        Instant now = Instant.now();
        when(clock.now())
                .thenReturn(now.minusSeconds(TWO_MINUTES))
                .thenReturn(now.minusSeconds(ONE_MINUTE))
                .thenReturn(now.minusSeconds(FIFTEEN_SECONDS))
                .thenReturn(now.minusSeconds(ONE_SECOND))
                .thenReturn(now);

        try (SocketConnection connection = new SocketConnection(BlatherServerShould.PORT)) {

            assertEquals("Welcome to Blather", connection.readLine());

            connection.writeLine("Alice -> My first message");
            connection.writeLine("Bob -> Hello world!");
            connection.writeLine("Alice -> Sup everyone?");
            connection.writeLine("Bob -> I wanna party :)");
            connection.writeLine("Emma follows Bob");
            connection.writeLine("Emma follows Alice");
            connection.writeLine("Emma wall");
            connection.writeLine("quit");

            String expectedLines = "My first message (2 minutes ago)\n" +
            "Sup everyone? (15 seconds ago)\n" +
            "Hello world! (1 minute ago)\n" +
            "I wanna party :) (1 second ago)\n" +
            "Bye!\n";

            assertEquals(expectedLines, connection.getAllLines());
        }


    }

}