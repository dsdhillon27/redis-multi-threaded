package org.dsd.protocol;

import lombok.extern.slf4j.Slf4j;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class LoggingInputStream extends FilterInputStream {

    public LoggingInputStream(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public int read() throws IOException {
        int b = super.read();
        if (b != -1) {
            log.debug("Read byte: {}", b);
        }
        return b;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = super.read(b, off, len);
        if (bytesRead > 0) {
            String data = new String(b, off, bytesRead);
            log.debug("Read {} bytes: {}", bytesRead, data);
        }
        return bytesRead;
    }
}
