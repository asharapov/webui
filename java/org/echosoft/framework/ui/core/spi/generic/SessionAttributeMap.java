package org.echosoft.framework.ui.core.spi.generic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.echosoft.framework.ui.core.spi.AbstractContextMap;


/**
 * Предоставляет доступ к объектам в пространстве имен {@link org.echosoft.framework.ui.core.Scope#SESSION}.
 * @author Anton Sharapov
 */
public final class SessionAttributeMap extends AbstractContextMap<String,Object> {

    private final HttpSession session;

    public SessionAttributeMap(final HttpServletRequest request) {
        this.session = request.getSession(true);
    }

    @Override
    public Object get(final Object key) {
        return session.getAttribute((String)key);
    }

    @Override
    public Object put(final String key, final Object value) {
        final Object result = session.getAttribute(key);
        session.setAttribute(key, value);
        return result;
    }

    @Override
    public Object remove(final Object key) {
        final Object result = session.getAttribute((String)key);
        session.removeAttribute((String)key);
        return result;
    }

    @Override
    public Set<Map.Entry<String,Object>> entrySet() {
        final HashSet<Map.Entry<String,Object>> entries = new HashSet<Map.Entry<String,Object>>();
        for (Enumeration e = session.getAttributeNames(); e.hasMoreElements(); ) {
            final String key = (String)e.nextElement();
            final Entry<String,Object> entry = new Entry<String,Object>(key, session.getAttribute(key));
            entries.add(entry);
        }
        return entries;
    }

}
