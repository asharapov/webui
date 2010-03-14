package org.echosoft.framework.ui.core.web.ajax;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Writer;

import org.echosoft.common.types.TypeRegistry;

/**
 * Содержит информацию об отдельном AJAX запросе в системе.
 * 
 * @author Anton Sharapov
 */
public abstract class AjaxRequest {

    public static final TypeRegistry types = new TypeRegistry();

    protected final HttpServletRequest request;
    protected final ServletContext context;
    protected final String resource;

    public AjaxRequest(HttpServletRequest request, ServletContext context, String resource) {
        this.request = request;
        this.context = context;
        this.resource = resource;
    }

    public abstract void execute(Writer out) throws Throwable;
}
