package org.echosoft.framework.ui.core;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import org.echosoft.common.io.FastStringWriter;
import org.echosoft.framework.ui.core.theme.Theme;

/**
* @author Anton Sharapov
*/
public class BaseMockUIContext implements UIContext {
    private Resources resources;
    private Messages messages;
    private StateHolder states;
    private Theme theme;
    private Agent agent;
    private FastStringWriter out;
    public BaseMockUIContext() {
        this.resources = new Resources();
        this.messages = new Messages();
        this.states = new StateHolder();
        this.states.setCurrentDescriptor( new ViewStateDescriptor("/", "", 0) );
        this.theme = null; //Application.THEMES_MANAGER.getTheme(Application.DEFAULT_LOCALE);
        this.agent = new Agent(Agent.Type.GECKO, Agent.OS.WINDOWS, 1, 9, "");
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
    public Messages getMessages() {return messages;}
    public Theme getTheme() {return theme;}
    public Map<String, Object> getAttributesMap(Scope scope) {return null;}
    public Enumeration<String> getAttributeNames(Scope scope) {return null;}
    public <T> T getAttribute(String name, Scope scope) {return null;}
    public <T> T getAttribute(String name, Scope[] scopes) {return null;}
    public void setAttribute(String name, Object value, Scope scope) {}
    public <T> T removeAttribute(String name, Scope scope) {return null;}
    public Enumeration<String> getParameterNames() {return null;}
    public String getParameter(String name) {return null;}
    public String[] getParameterValues(String name) {return new String[0];}
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
    public String getParamsPrefix() {return "";}
    public Writer getResponseWriter() {return out;}
}
