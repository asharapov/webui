package org.echosoft.framework.ui.core.spi.generic;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.echosoft.framework.ui.core.spi.AbstractContextMap;
import org.echosoft.framework.ui.core.spi.Constants;

/**
 * Предоставляет доступ к объектам в пространстве имен {@link org.echosoft.framework.ui.core.Scope#SHARED}.
 * @author Anton Sharapov
 */
public final class SharedAttributeMap extends AbstractContextMap<String,Object> {

    private final Map<String,Object> data;

    @SuppressWarnings("unchecked")
    public SharedAttributeMap(final ServletRequest request) {
        Map<String,Object> data = (Map<String,Object>)request.getAttribute(Constants.SHARED_ATTRS_MAP);
        if (data==null) {
            data = new HashMap<String,Object>();
            request.setAttribute(Constants.SHARED_ATTRS_MAP, data);
        }
        this.data = data;
    }

    @Override
    public Object get(final Object key) {
        return data.get(key);
    }

    @Override
    public Object put(final String key, final Object value) {
        return data.put(key, value);
    }

    @Override
    public Object remove(final Object key) {
        return data.remove(key);
    }

    @Override
    public Set<Map.Entry<String,Object>> entrySet() {
        return data.entrySet();
    }

}
