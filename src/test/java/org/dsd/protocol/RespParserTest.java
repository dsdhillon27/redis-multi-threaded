package org.dsd.protocol;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RespParserTest {

    @Test
    void parseCommands() throws IOException {
        String command = "*3\r\n$3\r\nSET\r\n$3\r\nAGE\r\n$2\r\n27\r\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(command.getBytes());
        RespParser respParser = new RespParser(inputStream);
        List<String> args = respParser.parseCommands();
        assertEquals(3, args.size());
        assertEquals("SET", args.get(0));
        assertEquals("AGE", args.get(1));
        assertEquals("27", args.get(2));
    }
}