package org.echosoft.framework.ui.core.compiler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Отвечает за трансляцию .wui файлов в соответствуюшие .java сервлеты.
 * @author Anton Sharapov
 */
public class Translator {

    /**
     * Выполняет трансляцию .wui Файла в соответствующий ему .java файл.
     * @param uri  относительный путь до данного ресурса на сервере. Декодируется в имя класса и пакет java.
     * @param options  конфигурационные параметры.
     * @return  путь к сгенерированному .java классу.
     * @throws IOException  в случае каких-либо проблем с сохранением генерируемого файла на диск.
     */
    public static File translate(final String uri, final Options options) throws IOException {
        final TranslationContext tc = new TranslationContext(uri, options);
        tc.ensureClassImported(IOException.class);
        tc.ensureClassImported(ServletException.class);
        tc.ensureClassImported(HttpServlet.class);
        tc.ensureClassImported(HttpSession.class);
        tc.ensureClassImported(HttpServletRequest.class);
        tc.ensureClassImported(HttpServletResponse.class);
        tc.current = new MethodContext(tc,null,"main",
                new Variable("request", HttpServletRequest.class, false, 0),
                new Variable("response", HttpServletResponse.class, false, 0)
                );
        tc.methods.add( tc.current );
        saveTranslated(tc);
        return tc.dstFile;
    }


    private static void saveTranslated(final TranslationContext tc) throws IOException {
        tc.ensureClassImported(java.util.Date.class);
        final FileWriter out = new FileWriter(tc.dstFile);
        try {
            if (!tc.pkgName.isEmpty()) {
                out.write("package ");
                out.write(tc.pkgName);
                out.write(";\n\n");
            }
            for (Class cls : tc.imports) {
                out.write("import ");
                out.write(cls.getName());
                out.write(";\n");
            }
            out.write('\n');
            out.write("public final class ");
            out.write(tc.clsName);
            out.write(" extends HttpServlet {\n");
            out.write("    private Date timestamp = null;\n");
            out.write("    public void init() throws ServletException {\n");
            out.write("        timestamp = new Date();\n");
            out.write("        System.out.println(\"initialized = \"+timestamp);\n");
            out.write("    }\n");
            out.write("    public void destroy() {\n");
            out.write("        timestamp = null;\n");
            out.write("        System.out.println(\"destroyed = \"+timestamp);\n");
            out.write("    }\n");
            out.write("    public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {\n");
            out.write("        response.getWriter().write(\"Hi guys!\");\n");
            out.write("        response.getWriter().write(getClass().getName());\n");
            out.write("        response.getWriter().flush();\n");
            out.write("    }\n");
            out.write("}\n");
            out.flush();
        } finally {
            out.close();
        }
    }
}
