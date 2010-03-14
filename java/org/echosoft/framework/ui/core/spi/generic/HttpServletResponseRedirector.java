package org.echosoft.framework.ui.core.spi.generic;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * This response wrapper object is passed to RequestDispatcher.include(), so
 * that the output of the included resource is appended to specified stream.
 * @author Anton Sharapov
 */
public final class HttpServletResponseRedirector extends HttpServletResponseWrapper {

    private final ServletOutputStream stream;
    private final PrintWriter writer;

    public HttpServletResponseRedirector(final ServletResponse response, final OutputStream out) throws UnsupportedEncodingException  {
        super((HttpServletResponse)response);

        this.stream = new ServletOutputStreamAdapter(out);
        if (response.getCharacterEncoding() != null) {
            writer = new PrintWriter( new OutputStreamWriter(stream, response.getCharacterEncoding()) );
        } else
            writer = new PrintWriter(stream);
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public ServletOutputStream getOutputStream() {
        return stream;
    }
}

