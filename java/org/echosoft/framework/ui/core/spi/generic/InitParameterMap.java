package org.echosoft.framework.ui.core.spi.generic;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.echosoft.framework.ui.core.spi.AbstractContextMap;


/**
 * Предоставляет доступ к объектам в пространстве имен {@link org.echosoft.framework.ui.core.Scope#INIT}.
 * @author Anton Sharapov
 */
public final class InitParameterMap extends AbstractContextMap<String,Object> {

    private final ServletContext servletContext;

    public InitParameterMap(final ServletConfig servletConfig) {
        this.servletContext = servletConfig.getServletContext();
    }

    @Override
    public Object get(final Object key) {
        return servletContext.getInitParameter( (String)key );
    }

    @Override
    public Set<Map.Entry<String,Object>> entrySet() {
        final HashSet<Map.Entry<String,Object>> entries = new HashSet<Map.Entry<String,Object>>();
        for (Enumeration e = servletContext.getInitParameterNames(); e.hasMoreElements(); ) {
            final String key = (String)e.nextElement();
            final Entry<String,Object> entry = new Entry<String,Object>(key, servletContext.getInitParameter(key));
            entries.add(entry);
        }
        return entries;
    }

}
