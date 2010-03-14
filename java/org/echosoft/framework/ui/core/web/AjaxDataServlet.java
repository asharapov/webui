package org.echosoft.framework.ui.core.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

import org.echosoft.common.io.FastStringWriter;
import org.echosoft.framework.ui.core.Application;
import org.echosoft.framework.ui.core.web.ajax.AjaxRequest;
import org.echosoft.framework.ui.core.web.ajax.DataProviderAjaxRequest;
import org.echosoft.framework.ui.core.web.ajax.SimpleAjaxRequest;

/**
 * Данный сервлет предназначен для отдачи клиенту данных в формате JSON.
 * Является частью поддержки AJAX запросов в системе. 
 *
 * @author Anton Sharapov
 */
public class AjaxDataServlet extends HttpServlet {

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            final String path = request.getPathInfo();
            if (path==null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            final int s = path.indexOf('/',1);
            if (s<0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            final String service = path.substring(1,s);
            final String resource = path.substring(s);
            final AjaxRequest ar;

            if ("simple".equals(service)) {
                ar = new SimpleAjaxRequest(request, getServletContext(), resource);
            } else
            if ("provider".equals(service)) {
                ar = new DataProviderAjaxRequest(request, getServletContext(), resource);
            } else {
                response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                return;
            }

            final FastStringWriter out = new FastStringWriter(1024);
            try {
                ar.execute(out);
                if (out.length()==0) {
                    response.sendError(HttpServletResponse.SC_NO_CONTENT);
                    return;
                }
            } catch (Exception e) {
                Application.log.error(e.getMessage(), e);
                response.setHeader("X-ERR-MSG", URLEncoder.encode(e.getMessage(),"UTF-8"));
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            response.setContentType("application/json; charset=UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-store, no-cache, max-age=0, must-revalidate");
            out.writeOut(response.getWriter());
            response.getWriter().flush();
        } catch (Throwable e) {
            Application.log.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
