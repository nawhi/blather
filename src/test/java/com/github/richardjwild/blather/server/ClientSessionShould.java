package com.github.richardjwild.blather.server;

import com.github.richardjwild.blather.application.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ClientSessionShould {

    @Mock
    private Connection connection;

    @Mock
    private Application application;

    @Test
    public void close_connection_when_app_finishes_running() {
        ClientSession session = new ClientSession(connection, application);
        session.start();

        // mock application.run() returns immediately

        verify(connection, timeout(100)).close();
    }
}