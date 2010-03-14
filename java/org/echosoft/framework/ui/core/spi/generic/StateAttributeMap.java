package org.echosoft.framework.ui.core.spi.generic;

import java.util.Map;
import java.util.Set;

import org.echosoft.framework.ui.core.StateHolder;
import org.echosoft.framework.ui.core.spi.AbstractContextMap;


/**
 * Предоставляет доступ к объектам в пространстве имен {@link org.echosoft.framework.ui.core.Scope#STATE}.
 * @author Anton Sharapov
 */
public final class StateAttributeMap extends AbstractContextMap<String,Object> {

    private final StateHolder states;

    public StateAttributeMap(final StateHolder states) {
        this.states = states;
    }

    @Override
    public Object get(final Object key) {
        return  states.getCurrentState().get(key);
    }

    @Override
    public Object put(final String key, final Object value) {
        return states.getCurrentState().put(key,value);
    }

    @Override
    public Object remove(final Object key) {
        return states.getCurrentState().remove(key);
    }

    @Override
    public Set<Map.Entry<String,Object>> entrySet() {
        return states.getCurrentState().entrySet();
    }

}