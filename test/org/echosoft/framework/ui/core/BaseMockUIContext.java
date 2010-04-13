package org.echosoft.framework.ui.core;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.Writer;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.echosoft.common.collections.IteratorEnumeration;
import org.echosoft.common.io.FastStringWriter;
import org.echosoft.framework.ui.core.spi.generic.StateAttributeMap;
import org.echosoft.framework.ui.core.theme.Theme;

/**
* @author Anton Sharapov
*/
public class BaseMockUIContext implements UIContext {
    private Resources resources;
    private Messages messages;
    private StateHolder states;
    private Theme theme;
    private EnumMap<Scope,Map<String,Object>> scopes;
    private Agent agent;
    private FastStringWriter out;
    private String paramsPrefix;

    public BaseMockUIContext() {
        this.resources = new Resources();
        this.messages = new Messages();
        this.states = new StateHolder();
        this.states.setCurrentDescriptor( new ViewStateDescriptor("/", "", 0) );
        this.theme = null; //Application.THEMES_MANAGER.getTheme(Application.DEFAULT_LOCALE);
        this.scopes = new EnumMap<Scope,Map<String,Object>>(Scope.class);
        this.scopes.put(Scope.PARAMS, new HashMap<String,Object>());
        this.scopes.put(Scope.PARAMSVALUES, this.scopes.get(Scope.PARAMS));
        this.scopes.put(Scope.REQUEST, new HashMap<String,Object>());
        this.scopes.put(Scope.SHARED, new HashMap<String,Object>());
        this.scopes.put(Scope.SESSION, new HashMap<String,Object>());
        this.scopes.put(Scope.STATE, new StateAttributeMap(states));
        this.scopes.put(Scope.APPLICATION, new HashMap<String,Object>());
        this.scopes.put(Scope.INIT, new HashMap<String,Object>());
        this.agent = new Agent(Agent.Type.GECKO, Agent.OS.WINDOWS, 1, 9, "");
        this.paramsPrefix = "";
        this.out = new FastStringWriter(100);
    }
    public BaseMockUIContext(final StateHolder states) {
        this();
        this.states = states;
    }
    public void switchState(String viewId, int rank) {
        states.setCurrentDescriptor(new ViewStateDescriptor("", viewId, rank));
    }
    public StateHolder getStates() {
        return states;
    }
    public Resources getResources() {
        return resources;
    }
    public Messages getMessages() {
        return messages;
    }
    public Theme getTheme() {
        return theme;
    }
    public Map<String, Object> getAttributesMap(Scope scope) {
        return scopes.get(scope);
    }
    public Enumeration<String> getAttributeNames(Scope scope) {
        return new IteratorEnumeration<String>( scopes.get(scope).keySet().iterator() );
    }
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String name, Scope scope) {
        return (T)scopes.get(scope).get(name);
    }
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String name, Scope[] scopes) {
        for (Scope scope : scopes) {
            final T result = (T)this.scopes.get(scope).get(name);
            if (result!=null)
                return result;
        }
        return null;
    }
    public void setAttribute(String name, Object value, Scope scope) {
        scopes.get(scope).put(name, value);
    }
    @SuppressWarnings("unchecked")
    public <T> T removeAttribute(String name, Scope scope) {
        return (T)scopes.get(scope).remove(name);
    }
    public Enumeration<String> getParameterNames() {
        return new IteratorEnumeration<String>( scopes.get(Scope.PARAMS).keySet().iterator() );
    }
    public String getParameter(String name) {
        final Object value = scopes.get(Scope.PARAMS).get(name);
        if (value==null)
            return null;
        if (value instanceof String[]) {
            final String[] arr = (String[])value;
            return arr.length>0 ? arr[0] : null;
        } else
            return value.toString();
    }
    public String[] getParameterValues(String name) {
        final Object value = scopes.get(Scope.PARAMSVALUES).get(name);
        return value instanceof String[]
                ? (String[])value
                : new String[]{value.toString()};
    }
    public Cookie[] getRequestCookies() {return null;}
    public String getRequestHeader(String name) {return null;}
    public Map<String, String[]> getRequestHeaders() {return null;}
    public void addResponseCookie(Cookie cookie) {}
    public boolean containsResponseHeader(String name) {return false;}
    public void addResponseHeader(String name, String value) {}
    public void setResponseHeader(String name, String value) {}
    public String getUserName() {return null;}
    public Agent getAgent() {return agent;}
    public Locale getLocale() {return Application.DEFAULT_LOCALE;}
    public String getCharacterEncoding() {return "UTF-8";}
    public String getSessionId() {return null;}
    public String getScheme() {return null;}
    public String getHost() {return null;}
    public int getPort() {return 0;}
    public String getContextPath() {return null;}
    public String getServletPath() {return null;}
    public String getPathInfo() {return null;}
    public String getRequestUrl() {return null;}
    public String encodeURL(String url) {return null;}
    public String encodeThemeURL(String path, boolean persistent) {return null;}
    public void invalidateSession() {}
    public void include(String url, Writer out) throws IOException {}
    public String getParamsPrefix() {return paramsPrefix;}
    public Writer getResponseWriter() {return out;}

    public void setParamsPrefix(String prefix) {this.paramsPrefix = prefix;}
}
