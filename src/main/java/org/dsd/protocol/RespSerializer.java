package org.dsd.protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class RespSerializer {
    private final OutputStream outputStream;
    public RespSerializer(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeSimpleString(String msg) throws IOException {
        if(msg == null) throw new IllegalArgumentException("Simple string cannot be null");
        outputStream.write(("+" + msg + "\r\n").getBytes());
    }

    public void writeError(String msg) throws IOException{
        if(msg == null) throw new IllegalArgumentException("Error message cannot be null");
        outputStream.write(("-" + msg + "\r\n").getBytes());
    }

    public void writeInteger(long num) throws IOException {
        outputStream.write((":" + num + "\r\n").getBytes());
    }

    public void writeBulkString(String msg) throws IOException{
        if(msg == null){
            outputStream.write("-1\r\n".getBytes());
        } else{
            byte[] msgBytes = msg.getBytes();
            outputStream.write(("$" + msgBytes.length + "\r\n").getBytes());
            outputStream.write(msgBytes);
            outputStream.write("\r\n".getBytes());
        }
    }

    public void writeArray(List<String> elements) throws IOException{
        if(elements == null) throw new IllegalArgumentException("Array cannot be null");
        outputStream.write(("*" + elements.size() + "\r\n").getBytes());
        for(String element : elements){
            writeBulkString(element);
        }
    }

    public void flush() throws IOException{
        outputStream.flush();
    }

}
