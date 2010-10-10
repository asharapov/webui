package org.echosoft.framework.ui.core.mock;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.echosoft.common.collections.IteratorEnumeration;
import org.echosoft.common.io.FastStringWriter;

/**
 * @author Anton
 */
public class MockServletContext implements ServletContext {

    private final File rootDir;
    private final String contextPath;
    private Map<String,String> initParams;
    private Map<String,Object> attrs;
    private final FastStringWriter log;

    public MockServletContext(final File rootDir, final String contextPath, final String servletPath) {
        this.rootDir = rootDir;
        this.contextPath = contextPath;
        this.initParams = new HashMap<String,String>();
        this.attrs = new HashMap<String,Object>();
        this.log = new FastStringWriter();
    }

    @Override
    public String getServerInfo() {
        return "TestServer";
    }

    @Override
    public String getServletContextName() {
        return "TestApp";
    }

    @Override
    public String getContextPath() {
        return contextPath;
    }

    @Override
    public ServletContext getContext(String uripath) {
        return new MockServletContext(rootDir, uripath, null);
    }

    @Override
    public int getMajorVersion() {
        return 2;
    }

    @Override
    public int getMinorVersion() {
        return 5;
    }

    @Override
    public String getMimeType(String file) {
        return null;
    }

    @Override
    public Set getResourcePaths(String path) {
        return null;
    }

    @Override
    public String getRealPath(String path) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public URL getResource(String path) throws MalformedURLException {
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        return null;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String name) {
        return null;
    }

    @Override
    @Deprecated
    public Servlet getServlet(String name) throws ServletException {
        return null;
    }

    @Override
    @Deprecated
    public Enumeration getServlets() {
        return null;
    }

    @Override
    @Deprecated
    public Enumeration getServletNames() {
        return null;
    }

    @Override
    public void log(String msg) {
        log.write(msg);
        log.write('\n');
    }

    @Override
    @Deprecated
    public void log(Exception exception, String msg) {
    }

    @Override
    public void log(String message, Throwable throwable) {
        log.write(message);
        log.write('\n');
        throwable.printStackTrace(new PrintWriter(log,true));
    }

    @Override
    public String getInitParameter(String name) {
        return initParams.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return new IteratorEnumeration<String>(initParams.keySet().iterator());
    }

    @Override
    public Object getAttribute(String name) {
        return attrs.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return new IteratorEnumeration<String>(attrs.keySet().iterator());
    }

    @Override
    public void setAttribute(String name, Object object) {
        attrs.put(name, object);
    }

    @Override
    public void removeAttribute(String name) {
        attrs.remove(name);
    }


    public Map<String,String> getInitParameters() {
        return initParams;
    }

    public FastStringWriter getLog() {
        return log;
    }
}
