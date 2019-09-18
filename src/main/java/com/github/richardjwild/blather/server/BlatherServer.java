package com.github.richardjwild.blather.server;

import java.io.IOException;

class BlatherServer {

    private final TCPServer server;

    BlatherServer(int port) throws IOException {
        this.server = new TCPServer();
        server.initializeOn(port);
    }


    void stop() {
        server.stop();
    }
}
