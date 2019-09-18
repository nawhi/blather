package com.github.richardjwild.blather.server;

import com.github.richardjwild.blather.application.Application;

import java.util.concurrent.Future;

import static java.util.concurrent.CompletableFuture.runAsync;

class ClientSession {
    private Connection connection;
    private Application app;
    private Future<Void> runningApplication;

    ClientSession(Connection connection, Application app) {
        this.connection = connection;
        this.app = app;
    }

    ClientSession run() {
        System.out.println("Starting running application async");
        runningApplication = runAsync(app::run);
        return this;
    }

    void stop() {
        runningApplication.cancel(true);
        connection.close();
    }

}
