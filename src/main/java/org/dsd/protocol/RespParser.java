package org.dsd.protocol;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RespParser {

    private final BufferedReader bufferedReader;

    public RespParser(InputStream inputStream) {
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public List<String> parseCommand() throws IOException {
        String line = bufferedReader.readLine();

        if(line == null || !line.startsWith("*")){
            throw new IOException("Invalid RESP array: expected '*'");
        }

        int numOfArguments = Integer.parseInt(line.substring(1));
        if(numOfArguments < 0){
            throw new IOException("Number of arguments should be greater than 0");
        }

        List<String> commandArgs = new ArrayList<>();
        for (int i = 0; i < numOfArguments; i++) {
            line = bufferedReader.readLine();

            if (line == null || !line.startsWith("$")) {
                throw new IOException("Expected bulk string starting with '$'");
            }

            int length = Integer.parseInt(line.substring(1));
            if(length < 0){
                throw new IOException("Negative bulk string length not allowed in commands");
            }

            line = bufferedReader.readLine();
            if (line == null || line.length() != length) {
                throw new IOException("Bulk string length mismatched: expected " + length +
                        "got: " + (line == null ? "null" : line.length()));
            }

            commandArgs.add(line);
        }

        return commandArgs;
    }
}
