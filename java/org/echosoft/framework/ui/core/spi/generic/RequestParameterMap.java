package org.echosoft.framework.ui.core.spi.generic;

import javax.servlet.ServletRequest;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.echosoft.framework.ui.core.spi.AbstractContextMap;
import org.echosoft.framework.ui.core.spi.RequestParametersWrapper;

/**
 * Предоставляет доступ к объектам в пространстве имен {@link org.echosoft.framework.ui.core.Scope#PARAMS}.
 * @author Anton Sharapov
 */
public final class RequestParameterMap extends AbstractContextMap<String,Object> {

    private final ServletRequest request;
    private final boolean wrappable;

    public RequestParameterMap(final ServletRequest request) {
        this.request = request;
        this.wrappable = request instanceof RequestParametersWrapper;
    }

    @Override
    public Object get(final Object key) {
        return request.getParameter( (String)key );
    }

    @Override
    public Object put(final String key, final Object value) {
        if (key==null || value==null)
            throw new NullPointerException();

        if (wrappable) {
            final Object result = request.getParameter(key);
            ((RequestParametersWrapper)request).setParameter(key, value.toString());
            return result;
        } else
            throw new UnsupportedOperationException();
    }

    @Override
    public Object remove(final Object key) {
        if (wrappable) {
            final Object result = request.getParameter((String)key);
            ((RequestParametersWrapper)request).removeParameter((String)key);
            return result;
        } else
            throw new UnsupportedOperationException();
    }

    @Override
    public Set<Map.Entry<String,Object>> entrySet() {
        final HashSet<Map.Entry<String,Object>> entries = new HashSet<Map.Entry<String,Object>>();
        for (Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
            final String key = (String)e.nextElement();
            final Entry<String,Object> entry = new Entry<String,Object>(key, request.getParameter(key));
            entries.add(entry);
        }
        return entries;
    }

}
