package org.echosoft.framework.ui.core.spi.generic;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Adapts <code>java.io.Writer</code> to <code>ServletOutputStream</code>.
 * @author Anton Sharapov
 */
public final class ServletOutputStreamAdapter extends ServletOutputStream {

    private final OutputStream out;

    public ServletOutputStreamAdapter(final OutputStream out) {
        super();
        this.out = out;
    }

    @Override
    public void write(final int b) throws IOException {
        out.write( b );
    }

    public String toString() {
        return out.toString();
    }

}
