package com.github.richardjwild.blather.server;

import com.github.richardjwild.blather.time.Clock;

import java.io.IOException;

class BlatherServer {

    private final TCPServer server;
    private int port;

    BlatherServer(int port, Clock clock) {
        this.port = port;
        Clock clock1 = clock;
        this.server = new TCPServer(clock);

    }

    void start() throws IOException {
        server.initializeOn(port);
    }


    void stop() throws Exception {
        server.stop();
    }

}
