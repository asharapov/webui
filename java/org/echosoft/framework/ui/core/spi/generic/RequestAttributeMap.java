package org.echosoft.framework.ui.core.spi.generic;

import javax.servlet.ServletRequest;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.echosoft.framework.ui.core.spi.AbstractContextMap;

/**
 * Предоставляет доступ к объектам в пространстве имен {@link org.echosoft.framework.ui.core.Scope#REQUEST}.
 * @author Anton Sharapov
 */
public final class RequestAttributeMap extends AbstractContextMap<String,Object> {

    private final ServletRequest request;

    public RequestAttributeMap(final ServletRequest request) {
        this.request = request;
    }

    @Override
    public Object get(final Object key) {
        return request.getAttribute((String)key);
    }

    @Override
    public Object put(final String key, final Object value) {
        final Object result = request.getAttribute(key);
        request.setAttribute(key, value);
        return result;
    }

    @Override
    public Object remove(final Object key) {
        final Object result = request.getAttribute((String)key);
        request.removeAttribute((String)key);
        return result;
    }

    @Override
    public Set<Map.Entry<String,Object>> entrySet() {
        final HashSet<Map.Entry<String,Object>> entries = new HashSet<Map.Entry<String,Object>>();
        for (Enumeration e = request.getAttributeNames(); e.hasMoreElements(); ) {
            final String key = (String)e.nextElement();
            final AbstractContextMap.Entry<String,Object> entry = new Entry<String,Object>(key, request.getAttribute(key));
            entries.add(entry);
        }
        return entries;
    }

}
