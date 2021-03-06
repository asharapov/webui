package org.echosoft.framework.ui.core.web.wui;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.echosoft.common.utils.StringUtil;

/**
 * Отвечает за трансляцию .wui файлов в сервлеты в которых выполняеются соответствующие вызовы компонентов.
 * @author Anton Sharapov
 */
public class WUIServlet extends HttpServlet {

    private RuntimeContext rctx;

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        try {
            final Options options = new Options(config);
            rctx = new RuntimeContext(options, config);
        } catch (Exception e) {
            log("WUIServlet fatal error: "+e.getMessage());
            throw new ServletException(e.getMessage(), e);
        }
    }

    @Override
    public void destroy() {
        rctx = null;
        super.destroy();
    }

    @Override
    public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        String uri, pathInfo;
        uri = (String) request.getAttribute("javax.servlet.include.servlet_path");
        if (uri != null) {
            // Проверим, не является ли данный вызов результатом выполнения инструкции RequestDispatcher.include().
            pathInfo = (String)request.getAttribute("javax.servlet.include.path_info");
            if (pathInfo != null) {
                uri += pathInfo;
            }
        } else {
            // это простое обращение к ресурсу ..
            uri = request.getServletPath();
            pathInfo = request.getPathInfo();
            if (pathInfo!=null)
                uri += pathInfo;
        }
        final Resource resource = rctx.getResource(uri);
        if (resource == null) {
            handleMissingResource(request, response, uri);
            return;
        }
        try {
            resource.service(request, response);
        } catch (FileNotFoundException e) {
            handleMissingResource(request, response, uri);
        }
    }


    private void handleMissingResource(final HttpServletRequest request, final HttpServletResponse response, final String uri) throws ServletException, IOException {
        final String msg = StringUtil.encodeHTMLText("File \""+uri+"\" not found");
        if (request.getAttribute("javax.servlet.include.request_uri") != null) {
            // Отсутствующий ресурс был инициирован инструкцией <code>RequestDispatcher.include()</code>. 
            // Так как в данном режиме вызов <code>response.sendError()</code> бесполезен то просто поднимем исключение.
            throw new ServletException(msg);
        } else {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
            } catch (Exception ise) {
                log(msg, ise);
            }
        }
    }

}
