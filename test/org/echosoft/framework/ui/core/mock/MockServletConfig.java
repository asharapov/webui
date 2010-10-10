package org.echosoft.framework.ui.core.mock;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.echosoft.common.collections.IteratorEnumeration;

/**
 * @author Anton
 */
public class MockServletConfig implements ServletConfig {

    private final String name;
    private final MockServletContext context;
    private final Map<String,String> initParams;

    public MockServletConfig(File rootDir, String contextPath, String servletPath) {
        this.name = servletPath!=null ? servletPath : "/";
        this.context = new MockServletContext(rootDir, contextPath, servletPath);
        this.initParams = new HashMap<String,String>();
    }

    @Override
    public String getServletName() {
        return name;
    }

    @Override
    public ServletContext getServletContext() {
        return context;
    }

    @Override
    public String getInitParameter(String name) {
        return initParams.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return new IteratorEnumeration<String>(initParams.keySet().iterator());
    }

    public Map<String,String> getInitParameters() {
        return initParams;
    }
}
