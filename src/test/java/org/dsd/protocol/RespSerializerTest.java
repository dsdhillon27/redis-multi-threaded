package org.dsd.protocol;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RespSerializerTest {

    @Test
    void testWriteSimpleString() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        RespSerializer respSerializer = new RespSerializer(outputStream);
        respSerializer.writeSimpleString("OK");
        assertEquals("+OK\r\n", outputStream.toString());
    }

    @Test
    void testWriteError() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        RespSerializer respSerializer = new RespSerializer(outputStream);
        respSerializer.writeError("ERR unknown command");
        assertEquals("-ERR unknown command\r\n", outputStream.toString());
    }

    @Test
    void testWriteInteger() throws IOException{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        RespSerializer respSerializer = new RespSerializer(outputStream);
        respSerializer.writeInteger(123);
        assertEquals(":123\r\n", outputStream.toString());
    }

    @Test
    void testBulkString() throws IOException{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        RespSerializer respSerializer = new RespSerializer(outputStream);
        respSerializer.writeBulkString("Hello");
        assertEquals("$5\r\nHello\r\n", outputStream.toString());
    }

    @Test
    void testArray() throws IOException{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        RespSerializer respSerializer = new RespSerializer(outputStream);
        List<String> commandArgs = new ArrayList<>(Arrays.asList("AGE", "27"));
        respSerializer.writeArray(commandArgs);
        assertEquals("*2\r\n$3\r\nAGE\r\n$2\r\n27\r\n", outputStream.toString());
    }
}