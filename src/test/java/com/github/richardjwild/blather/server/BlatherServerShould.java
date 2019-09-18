package com.github.richardjwild.blather.server;

import com.github.richardjwild.blather.time.Clock;
import org.junit.After;
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

    private static final int SERVER_PORT = 8080;

    @Mock
    private Clock clock;

    private BlatherServer app;

    @Before
    public void setUp() throws IOException {
        app = new BlatherServer(SERVER_PORT, clock);
        app.start();
    }

    @After
    public void tearDown() throws Exception {
        app.stop();
    }

    @Test(timeout = 1000)
    public void run_application_with_single_client() throws IOException {
        when (clock.now())
                .thenReturn(Instant.now().minusSeconds(TWO_MINUTES))
                .thenReturn(Instant.now());

        try (SocketConnection connection = new SocketConnection(SERVER_PORT)) {
            assertEquals("Welcome to Blather", connection.awaitLine());

            connection.writeLine("Alice -> My first message");
            connection.writeLine("Alice wall");

            assertEquals("Alice - My first message (2 minutes ago)", connection.awaitLine());
        }
    }
}