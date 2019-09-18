package com.github.richardjwild.blather.server;

import com.github.richardjwild.blather.application.Application;
import com.github.richardjwild.blather.message.MessageRepository;
import com.github.richardjwild.blather.persistence.InMemoryMessageRepository;
import com.github.richardjwild.blather.persistence.InMemoryUserRepository;
import com.github.richardjwild.blather.time.Clock;
import com.github.richardjwild.blather.user.UserRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.richardjwild.blather.application.ApplicationBuilder.anApplication;
import static java.util.concurrent.CompletableFuture.runAsync;

class TCPServer {

    private Future<Void> listenerThread;
    private ServerSocket serverSocket;
    private List<ClientSession> activeSessions = new ArrayList<>();
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    private final Clock clock;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    TCPServer(Clock clock) {
        this.clock = clock;
        this.messageRepository = new InMemoryMessageRepository();
        this.userRepository = new InMemoryUserRepository();
    }

    void initializeOn(int port) throws IOException {
        isRunning.set(true);
        serverSocket = new ServerSocket(port);
        listenerThread = runAsync(this::runListenerThread);
    }

    private void runListenerThread() {
        while (isRunning.get()) {
            try {
                Connection connection = new Connection(serverSocket.accept());
                ClientSession session = createSession(connection);
                session.start();
                activeSessions.add(session);
            } catch (IOException ignored) {

            }
        }
    }

    void stop() throws Exception {
        if (!isRunning.get())
            return;
        isRunning.set(false);
        activeSessions.forEach(ClientSession::stop);
        serverSocket.close();
        listenerThread.cancel(true);
    }

    private ClientSession createSession(Connection connection) {
        Application app = anApplication()
                .withInput(connection.getInput())
                .withOutput(connection.getOutput())
                .withMessageRepository(messageRepository)
                .withUserRepository(userRepository)
                .withClock(clock)
                .build();
        return new ClientSession(connection, app);
    }
}
