package org.echosoft.framework.ui.core.spi.generic;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.echosoft.common.collections.IteratorEnumeration;
import org.echosoft.framework.ui.core.Agent;
import org.echosoft.framework.ui.core.Application;
import org.echosoft.framework.ui.core.Messages;
import org.echosoft.framework.ui.core.Resources;
import org.echosoft.framework.ui.core.Scope;
import org.echosoft.framework.ui.core.StateHolder;
import org.echosoft.framework.ui.core.UIContext;
import org.echosoft.framework.ui.core.Utils;
import org.echosoft.framework.ui.core.ViewStateDescriptor;
import org.echosoft.framework.ui.core.spi.Constants;
import org.echosoft.framework.ui.core.theme.Theme;

/**
 * Базовая реализация интерфейса {@link UIContext}.
 * 
 * @author Anton Sharapov
 */
public class ServletUIContext implements UIContext {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
//    private final ServletConfig config;
    private final Resources resources;
    private final Messages messages;
    private final StateHolder states;
    private final Theme theme;
    private final Agent agent;
    private final Locale locale;
    private final EnumMap<Scope,Map<String,Object>> scopes;
    private final String pkg;
    private Map<String,String[]> headers;

    public ServletUIContext(final HttpServletRequest request, final HttpServletResponse response, final ServletConfig config) throws Exception {
        this.request = request;
        this.response = response;
//        this.config = config;
        this.pkg = request.getPathInfo()!=null ? request.getServletPath() + request.getPathInfo() : request.getServletPath();

        this.resources = new Resources();
        this.messages = new Messages();
        this.states = new StateHolder();
        this.agent = Utils.detectUserAgent(request);
        this.locale = Utils.detectEffectiveLocale(request);
        final String themeName = (String)request.getSession().getAttribute(Constants.CURRENT_THEME);
        this.theme = Application.THEMES_MANAGER.getTheme(locale, themeName);
        this.scopes = new EnumMap<Scope,Map<String,Object>>(Scope.class);
        this.scopes.put(Scope.PARAMS, new RequestParameterMap(request));
        this.scopes.put(Scope.PARAMSVALUES, new RequestParameterValuesMap(request));
        this.scopes.put(Scope.REQUEST, new RequestAttributeMap(request));
        this.scopes.put(Scope.SHARED, new SharedAttributeMap(request));
        this.scopes.put(Scope.SESSION, new SessionAttributeMap(request));
        this.scopes.put(Scope.STATE, new StateAttributeMap(states));
        this.scopes.put(Scope.APPLICATION, new ApplicationAttributeMap(config));
        this.scopes.put(Scope.INIT, new InitParameterMap(config));

        final String encodedState = request.getParameter(Constants.VIEWSTATE_PARAMNAME);
        Application.getStateSerializer().decodeState(this, encodedState);
        if (states.getCurrentDescriptor()==null) {
            states.setCurrentDescriptor( new ViewStateDescriptor(pkg, "", 0) );
        }
    }

    @Override
    public Resources getResources() {
        return resources;
    }

    @Override
    public Messages getMessages() {
        return messages;
    }

    @Override
    public Theme getTheme() {
        return theme;
    }

    @Override
    public Map<String, Object> getAttributesMap(final Scope scope) {
        return scopes.get(scope);
    }

    @Override
    public Enumeration<String> getAttributeNames(final Scope scope) {
        return new IteratorEnumeration<String>( scopes.get(scope).keySet().iterator() );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(final String name, final Scope scope) {
        return (T)scopes.get(scope).get(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(final String name, final Scope[] scopes) {
        for (Scope scope : scopes) {
            final T result = (T)this.scopes.get(scope).get(name);
            if (result!=null)
                return result;
        }
        return null;
    }

    @Override
    public void setAttribute(final String name, final Object value, final Scope scope) {
        scopes.get(scope).put(name, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T removeAttribute(final String name, final Scope scope) {
        return (T)scopes.get(scope).remove(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Enumeration<String> getParameterNames() {
        return request.getParameterNames();
    }

    @Override
    public String getParameter(final String name) {
        return request.getParameter(name);
    }

    @Override
    public String[] getParameterValues(final String name) {
        return request.getParameterValues(name);
    }

    public Cookie[] getRequestCookies() {
        return request.getCookies();
    }

    public String getRequestHeader(final String name) {
        return request.getHeader(name);
    }

    public Map<String,String[]> getRequestHeaders() {
        if (headers==null) {
            headers = new HashMap<String,String[]>();
            for (Enumeration en = request.getHeaderNames(); en.hasMoreElements(); ) {
                final String name = (String)en.nextElement();
                final ArrayList<String> values = new ArrayList<String>(4);
                for (Enumeration ev = request.getHeaders(name); ev.hasMoreElements(); ) {
                    values.add( (String)en.nextElement() );
                }
                headers.put(name, values.toArray(new String[values.size()]));
            }
        }
        return headers;
    }

    public void addResponseCookie( final Cookie cookie ) {
        response.addCookie( cookie );
    }
    public boolean containsResponseHeader(final String name) {
        return response.containsHeader(name);
    }
    public void addResponseHeader(final String name, final String value) {
        response.addHeader(name, value);
    }
    public void setResponseHeader(final String name, final String value) {
        response.setHeader(name, value);
    }




    @Override
    public void invalidateSession() {
        final HttpSession session = request.getSession();
        if (session!=null)
            session.invalidate();
    }

    @Override
    public void include(final String url, final Writer out) throws IOException {
        try {
            final ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
            request.getRequestDispatcher(url).include(request, new HttpServletResponseRedirector(response,stream));
            out.write( stream.toString(response.getCharacterEncoding()) );
        } catch (ServletException e) {
            final IOException exception = new IOException(e.getMessage());
            exception.initCause( e );
            throw exception;
        }
    }

    @Override
    public String getUserName() {
        return request.getRemoteUser();
    }

    @Override
    public Agent getAgent() {
        return agent;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public String getCharacterEncoding() {
        return request.getCharacterEncoding();
    }

    @Override
    public String getSessionId() {
        return request.getRequestedSessionId();
    }

    @Override
    public String getScheme() {
        return request.getScheme();
    }

    @Override
    public String getHost() {
        return request.getServerName();
    }

    @Override
    public int getPort() {
        return request.getServerPort();
    }

    @Override
    public String getContextPath() {
        return request.getContextPath();
    }

    @Override
    public String getServletPath() {
        return request.getServletPath();
    }

    @Override
    public String getPathInfo() {
        return request.getPathInfo();
    }

    @Override
    public String getRequestUrl() {
        return request.getRequestURI();
    }

    @Override
    public String encodeURL(final String url) {
        if (url==null) {
            return request.getRequestURI();
        }
        final String u = url.trim();
        final int q = u.indexOf('?',0);
        final int s = u.indexOf(':',0);
        if ( s>0 && (q<0 || s<q) ) {
            return u;
        } else
        if (u.length()==0) {
            return request.getRequestURI();
        } else
        if ( u.charAt(0)!='/') {
            return request.getRequestURI() + '/' + u;
        } else {
            return request.getContextPath() + u;
        }
    }

    @Override
    public String encodeThemeURL(final String path, final boolean persistent) {
        if (persistent) {
            return theme.getResourceURL(path);
        } else {
            if (path==null || path.length()==0) {
                return request.getContextPath() + Application.RESOURCES_SERVLET_CONTEXT + "/theme";
            } else
            if (path.charAt(0)=='/') {
                return request.getContextPath() + Application.RESOURCES_SERVLET_CONTEXT + "/theme" + path;
            } else {
                return request.getContextPath() + Application.RESOURCES_SERVLET_CONTEXT + "/theme/" + path;
            }
        }
    }


    //
    // Методы для интерфейса UIContext
    //

    @Override
    public void switchState(final String viewId, final int rank) {
        this.states.setCurrentDescriptor( new ViewStateDescriptor(this.pkg, viewId, rank) );
    }

    @Override
    public StateHolder getStates() {
        return states;
    }

    @Override
    public String getParamsPrefix() {
        return "";
    }

    @Override
    public Writer getResponseWriter() throws IOException {
        response.setCharacterEncoding("uTF-8");
        return response.getWriter();
    }
}
