package org.dsd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.dsd.protocol.LoggingInputStream;
import org.dsd.protocol.LoggingOutputStream;
import org.dsd.protocol.RespParser;
import org.dsd.protocol.RespSerializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientHandler implements Runnable{
    private final Socket socket;
    private final RespParser respParser;
    private final RespSerializer respSerializer;
    private final ConcurrentHashMap<String, String> dataStore;

    public ClientHandler(Socket socket, ConcurrentHashMap<String, String> dataStore) {
        this.socket = socket;
        this.dataStore = dataStore;
        try{
            InputStream inputStream = new LoggingInputStream(socket.getInputStream());
            OutputStream outputStream = new LoggingOutputStream(socket.getOutputStream());

            this.respParser = new RespParser(inputStream);
            this.respSerializer = new RespSerializer(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialise client handler", e);
        }
    }

    public void run() {
        try{
            while(true){
                List<String> commandArgs = respParser.parseCommand();
                log.debug("Parsed command args: {}", commandArgs);
                String command = commandArgs.get(0);
                switch(command){
                    case "SET":
                        handleSet(commandArgs);
                        break;
                    case "GET":
                        handleGet(commandArgs);
                        break;
                    case "DEL":
                        handleDel(commandArgs);
                        break;
                    default:
                        respSerializer.writeError("ERR unknown command '" + command + "'");
                }

                respSerializer.flush();
            }
        } catch (IOException e) {
            log.error("Error handling client", e);
        } finally {
            try{
                socket.close();
            } catch (IOException e) {
                log.error("Error closing socket", e);
            }
        }
    }

    private void handleSet(List<String> args) throws IOException{
        if(args.size() != 3){
            respSerializer.writeError("Invalid number of arguments for SET command");
            return;
        }

        String key = args.get(1);
        String value = args.get(2);
        dataStore.put(key, value);
        respSerializer.writeSimpleString("OK");
    }
    

    private void handleGet(List<String> args) throws IOException{
        if(args.size() != 2){
            respSerializer.writeError("Invalid number of arguments for GET command");
            return;
        }

        String key = args.get(1);
        String value = dataStore.get(key);
        respSerializer.writeBulkString(value);
    }

    private void handleDel(List<String> args) throws IOException{
        if(args.size() != 2){
            respSerializer.writeError("Invalid number of arguments for DEL command");
            return;
        }

        String key = dataStore.get(1);
        String removed = dataStore.remove(key);
        respSerializer.writeInteger(removed != null ? 1 : 0);
    }


}
