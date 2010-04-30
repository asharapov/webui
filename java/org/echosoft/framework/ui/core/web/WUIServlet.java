package org.echosoft.framework.ui.core.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

import org.echosoft.framework.ui.core.compiler.Translator;

/**
 * Отвечает за трансляцию .wui файлов в сервлеты в которых выполняеются соответствующие вызовы компонентов.
 * @author Anton Sharapov
 */
public class WUIServlet extends HttpServlet {

    @Override
    public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final File root = new File( getServletContext().getRealPath("/") ).getCanonicalFile();
        final File wuiFile = new File( getServletContext().getRealPath(request.getServletPath()) ).getCanonicalFile();
        // проверим что запрошенный .wui файл есть в наличии и он относится к нашему приложению ...
        if (!wuiFile.getPath().startsWith(root.getPath()) || !wuiFile.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, wuiFile.getPath());
            return;
        }

        // вычислим соответственно пути до соответствующих .java и .class файлов ...
        final File dir = wuiFile.getParentFile();
        final String name = wuiFile.getName().substring(0, wuiFile.getName().length()-4);
        final File classFile = new File(dir, name+".class");
        final File javaFile = new File(dir, name+".java");

        if (classFile.isFile()) {   // уже имеем скомпилированную версию.
            if (classFile.lastModified() > wuiFile.lastModified()) {    // это актуальная версия.
                response.getWriter().write("\n wui file: " + wuiFile);
                response.getWriter().write("\n wui class: " + classFile);
                response.getWriter().flush();
                return;
            }
            if (!classFile.delete())
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "can't drop staled class file");
        }

        if (javaFile.isFile()) {
            if (!javaFile.delete())
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "can't drop translated java file");
        }

        Translator.translate(root, request.getServletPath());

        response.getWriter().write("\n wui file: " + wuiFile);
        response.getWriter().write("\n wui java: " + javaFile);
        response.getWriter().write("\n wui class: " + classFile);
        response.getWriter().flush();
    }

}
