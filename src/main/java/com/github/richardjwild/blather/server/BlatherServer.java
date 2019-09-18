package com.github.richardjwild.blather.server;

class BlatherServer {

    private final TCPServer server;

    BlatherServer(int port) {
        this.server = new TCPServer();
        server.startOn(port);
    }


    void stop() {
        server.stop();
    }
}
