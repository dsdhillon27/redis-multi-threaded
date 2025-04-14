package org.dsd;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RedisServer {
    private final int port;
    private volatile boolean running = true;
    private final ConcurrentHashMap<String, String> dataStore = new ConcurrentHashMap<>();

    public RedisServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Redis server started on port: {}", port);
            while (running) {
                Socket clientSocket = serverSocket.accept();
                log.info("New client connection: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, dataStore);
                new Thread(clientHandler).start();
            }
        }
    }

    public void stop(){
        running = false;
    }

}
