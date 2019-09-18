package com.github.richardjwild.blather.server;

import java.io.IOException;

class BlatherServer {

    private final TCPServer server;
    private int port;

    BlatherServer(int port) {
        this.port = port;
        this.server = new TCPServer();

    }

    void start() throws IOException {
        server.initializeOn(port);
    }


    void stop() throws Exception {
        server.stop();
    }

    public static void main(String[] args) throws IOException {
        BlatherServer server = new BlatherServer(8080);
        server.start();
        while (true) {}
    }


}
