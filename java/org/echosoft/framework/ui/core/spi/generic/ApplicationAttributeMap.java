package org.echosoft.framework.ui.core.spi.generic;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.echosoft.framework.ui.core.spi.AbstractContextMap;

/**
 * Предоставляет доступ к объектам в пространстве имен {@link org.echosoft.framework.ui.core.Scope#APPLICATION}.
 * @author Anton Sharapov
 */
public final class ApplicationAttributeMap extends AbstractContextMap<String,Object> {

    private final ServletContext servletContext;

    public ApplicationAttributeMap(final ServletConfig servletConfig) {
        this.servletContext = servletConfig.getServletContext();
    }

    @Override
    public Object get(final Object key) {
        return servletContext.getAttribute( (String)key );
    }

    @Override
    public Object put(final String key, final Object value) {
        final Object result = servletContext.getAttribute(key);
        servletContext.setAttribute(key, value);
        return result;
    }

    @Override
    public Object remove(final Object key) {
        final Object result = servletContext.getAttribute((String)key);
        servletContext.removeAttribute((String)key);
        return result;
    }

    @Override
    public Set<Map.Entry<String,Object>> entrySet() {
        final HashSet<Map.Entry<String,Object>> entries = new HashSet<Map.Entry<String,Object>>();
        for (Enumeration e = servletContext.getAttributeNames(); e.hasMoreElements(); ) {
            final String key = (String)e.nextElement();
            final Entry<String,Object> entry = new Entry<String,Object>(key, servletContext.getAttribute(key));
            entries.add(entry);
        }
        return entries;
    }

}
