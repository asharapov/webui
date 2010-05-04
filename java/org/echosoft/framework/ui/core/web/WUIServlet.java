package org.echosoft.framework.ui.core.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
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
        // транслируем ...
        final File javaFile = Translator.translate(root, request.getServletPath());
        // компилируем ...
        final JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
        final StandardJavaFileManager sjfm = jc.getStandardFileManager(null, null, null);
        final Iterable<? extends JavaFileObject> files = sjfm.getJavaFileObjects(javaFile);
        final JavaCompiler.CompilationTask task = jc.getTask(null, sjfm, null, null, null, files);
        boolean result = task.call();

        response.getWriter().write("\n wui file: " + wuiFile);
        response.getWriter().write("\n wui java: " + javaFile);
        response.getWriter().flush();
    }

}
