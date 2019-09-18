package com.github.richardjwild.blather;

class BlatherServer {

    private final TCPServer server;
    private final int port;

    BlatherServer(int port) {
        this.port = port;

        this.server = new TCPServer();

        server.startOn(port);
    }


    void stop() {
        server.stop();
    }
}
