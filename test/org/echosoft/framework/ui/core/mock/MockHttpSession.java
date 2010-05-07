package org.echosoft.framework.ui.core.mock;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.echosoft.common.collections.IteratorEnumeration;

/**
 * @author Anton Sharapov
 */
public class MockHttpSession implements HttpSession {

    private final Map<String,Object> attrs = new HashMap<String,Object>();

    @Override
    public long getCreationTime() {
        return 0;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public long getLastAccessedTime() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
    }

    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }

    @Override
    @Deprecated
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Enumeration getAttributeNames() {
        return new IteratorEnumeration<String>(attrs.keySet().iterator());
    }

    @Override
    public Object getAttribute(String name) {
        return attrs.get(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        attrs.put(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        attrs.remove(name);
    }

    @Override
    @Deprecated
    public String[] getValueNames() {
        return new String[0];
    }

    @Override
    @Deprecated
    public Object getValue(String name) {
        return null;
    }

    @Override
    @Deprecated
    public void putValue(String name, Object value) {
    }

    @Override
    @Deprecated
    public void removeValue(String name) {
    }

    @Override
    public void invalidate() {
        attrs.clear();
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
