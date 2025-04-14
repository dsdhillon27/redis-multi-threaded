package org.dsd.protocol;

import lombok.extern.slf4j.Slf4j;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class LoggingOutputStream extends FilterOutputStream {

    public LoggingOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        log.debug("Wrote byte: {}", b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
        String data = new String(b, off, len);
        log.debug("Wrote {} bytes: {}", len, data);
    }
}
