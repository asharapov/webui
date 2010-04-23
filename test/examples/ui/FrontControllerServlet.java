package examples.ui;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import org.echosoft.common.json.Serializers;
import org.echosoft.framework.ui.core.Application;
import org.echosoft.framework.ui.core.UIContext;

import examples.ui.dispatchers.BlankDispatcher;
import examples.ui.dispatchers.ComboBoxDispatcher;
import examples.ui.dispatchers.FormsDispatcher;
import examples.ui.services.Facade;

/**
 * @author Anton Sharapov
 */
public class FrontControllerServlet extends HttpServlet {

    private HashMap<String,Dispatcher> bindings;

    public void init() throws ServletException {
        super.init();
        this.getServletContext().setAttribute("services", new Facade());
        Application.jsonContext.setWriterFactory( Serializers.PRINTABLE_JSON_WRITER_FACTORY );
        bindings = new HashMap<String,Dispatcher>();
        bindings.put("/blank", new BlankDispatcher());
        bindings.put("/forms", new FormsDispatcher());
        bindings.put("/combobox", new ComboBoxDispatcher());
    }

    public void destroy() {
        if (bindings!=null) {
            bindings.clear();
            bindings = null;
        }
    }

    public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        // Specify default settings ...
        request.setCharacterEncoding("UTF-8");  // bug's workaround on serveral kinds of servers.
        response.setContentType("text/html; charset=UTF-8");
        printRequest(request);

        // Resolve requested path ...
        String path = request.getPathInfo() != null ? request.getPathInfo() : "/";
        if (path.length() > 1 && path.charAt(path.length() - 1) == '/')
            path = path.substring(0, path.length() - 1);

        final Dispatcher dispatcher = bindings.get(path);
        if (dispatcher == null) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, path);
            return;
        }

        try {
            final UIContext uctx = Application.makeContext(request, response, getServletConfig());
            dispatcher.dispatch(uctx);
        } catch (Exception e) {
            throw new ServletException(e.getMessage(), e);
        }
    }


    private void printRequest(final HttpServletRequest request) {
        final StringBuilder buf = new StringBuilder(256);
        final HttpSession session = request.getSession(false);
        final String action = request.getParameter("action");

        buf.append("request [uri:");
        buf.append(request.getRequestURI());
        buf.append(", action:");
        if (action!=null)
            buf.append(action);
        buf.append(", user:");
        buf.append(request.getRemoteUser());
        buf.append(", host:");
        buf.append(request.getRemoteHost());
        if (session!=null) {
            buf.append(", session:");
            buf.append(session.getId());
            if (session.isNew()) {
                buf.append(" new");
            }
        }
        buf.append("]\n");
        for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
            final String name = (String) e.nextElement();
            final String values[] = request.getParameterValues(name);
            buf.append(name);
            buf.append('=');
            for (int i = 0; i < values.length; i++) {
                if (i > 0)
                    buf.append(',');
                if (values[i].length()<1024) {
                    buf.append(values[i]);
                } else {
                    buf.append("< ").append(values[i].length()).append(" chars >");
                }
            }
            buf.append('\n');
        }
        buf.append('\n');
//        log(buf.toString());
        System.out.println(buf.toString());
    }

}
