package org.echosoft.framework.ui.core.mock;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.echosoft.common.collections.ArrayIterator;
import org.echosoft.common.collections.IteratorEnumeration;

/**
 * @author Anton Sharapov
 */
public class MockHttpServletRequest implements HttpServletRequest {

    private final Map<String,Object> attrs = new HashMap<String,Object>();
    private final Map<String,String[]> params = new HashMap<String,String[]>();
    private final Map<String,String[]> headers = new HashMap<String,String[]>();
    private final MockHttpSession session = new MockHttpSession();

    @Override
    public HttpSession getSession(boolean create) {
        return session;
    }

    @Override
    public HttpSession getSession() {
        return session;
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
    public Map getParameterMap() {
        return params;
    }

    @Override
    public Enumeration getParameterNames() {
        return new IteratorEnumeration<String>(params.keySet().iterator());
    }

    @Override
    public String getParameter(String name) {
        final String[] values = params.get(name);
        return values!=null && values.length>0 ? values[0] : null;
    }

    @Override
    public String[] getParameterValues(String name) {
        return params.get(name);
    }


    @Override
    public Enumeration getHeaderNames() {
        return new IteratorEnumeration<String>(headers.keySet().iterator());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Enumeration getHeaders(String name) {
        final String[] values = headers.get(name);
        return values!=null
                ? new IteratorEnumeration<String>(new ArrayIterator(values))
                : new IteratorEnumeration<String>(Collections.<String>emptyList().iterator());
    }

    @Override
    public String getHeader(String name) {
        final String[] values = headers.get(name);
        return values!=null && values.length>0 ? values[0] : null;
    }

    @Override
    public int getIntHeader(String name) {
        return -1;
    }

    @Override
    public long getDateHeader(String name) {
        return -1;
    }


    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public String getQueryString() {
        return null;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String role) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getRequestURI() {
        return null;
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    @Deprecated
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public Locale getLocale() {
        return Locale.ENGLISH;
    }

    @Override
    public Enumeration getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Override
    @Deprecated
    public String getRealPath(String path) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }
}

