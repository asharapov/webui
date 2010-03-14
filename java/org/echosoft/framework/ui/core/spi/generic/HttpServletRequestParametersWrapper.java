package org.echosoft.framework.ui.core.spi.generic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.HashMap;

import org.echosoft.common.collections.IteratorEnumeration;
import org.echosoft.framework.ui.core.spi.RequestParametersWrapper;


/**
 * Адаптер для экземпляров класса {@link HttpServletRequest}, расширяющий последний путем поддержки интерфейса RequestParameterWrapper.
 * @author Anton Sharapov
 */
public final class HttpServletRequestParametersWrapper extends HttpServletRequestWrapper implements RequestParametersWrapper {

    private final HashMap<String,String[]> parameters;

    public HttpServletRequestParametersWrapper(final HttpServletRequest request) {
        super(request);
        this.parameters = new HashMap<String,String[]>();
        for (Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
            final String name = (String)e.nextElement();
            this.parameters.put(name, request.getParameterValues(name));
        }
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return new IteratorEnumeration<String>(parameters.keySet().iterator());
    }

    @Override
    public String getParameter(final String name) {
        final String[] values = parameters.get(name);
        return values!=null && values.length>0 ? values[0] : null;
    }

    @Override
    public String[] getParameterValues(final String name) {
        return parameters.get(name);
    }

    @Override
    public void setParameter(final String name, final String value) {
        if (name==null || value==null)
            throw new NullPointerException("All parameters must be specified");

        parameters.put(name, new String[]{value});
    }

    @Override
    public void setParameterValues(final String name, final String[] values) {
        if (name==null || values==null || values.length==0)
            throw new NullPointerException("All parameters must be specified");

        parameters.put(name, values);
    }

    @Override
    public void removeParameter(final String name) {
        if (name==null)
            throw new NullPointerException("Unknown parameter name");
        parameters.remove(name);
    }

}
