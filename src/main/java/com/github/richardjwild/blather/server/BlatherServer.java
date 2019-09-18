package com.github.richardjwild.blather.server;

import com.github.richardjwild.blather.time.Clock;
import com.github.richardjwild.blather.time.SystemClock;

import java.io.IOException;

class BlatherServer {

    private final TCPServer server;
    private int port;

    BlatherServer(int port, Clock clock) {
        this.port = port;
        this.server = new TCPServer(clock);
    }

    void start() throws IOException {
        server.initializeOn(port);
    }


    void stop() throws Exception {
        server.stop();
    }


    public static void main(String[] args) throws IOException {
        BlatherServer server = new BlatherServer(8080, new SystemClock());
        server.start();
        while (true) {}
    }
}
