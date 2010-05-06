package org.echosoft.framework.ui.core.compiler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.echosoft.common.io.FastStringWriter;
import org.echosoft.framework.ui.core.Application;
import org.echosoft.framework.ui.core.UIComponent;
import org.echosoft.framework.ui.core.UIContext;
import org.echosoft.framework.ui.core.web.wui.Options;

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
        generateServiceMethod(tc);
        store(tc);
        return tc.dstFile;
    }

    private static void generateServiceMethod(final TranslationContext tc) throws IOException {
        final MethodContext mc = new MethodContext(tc,null,"main");
        mc.addArgument(HttpServletRequest.class, "request");
        mc.addArgument(HttpServletResponse.class, "response");
        tc.ensureClassImported(Application.class);
        tc.ensureClassImported(UIContext.class);
        final FastStringWriter out = mc.content;
        out.write("    @Override\n");
        out.write("    public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {\n");
        if (tc.options.charset!=null) {
            out.append("        request.setCharacterEncoding(\"").append(tc.options.charset).append("\");\n");
        }
        out.write("        response.setContentType(\"text/html; charset=UTF-8\");\n");
        out.write("        try {\n");
        final Variable uctx = mc.allocateVariable(UIComponent.class, "uctx");
        out.write("        } catch(Exception e) {\n");
        out.write("        }\n");
        out.write("        response.getWriter().write(getClass().getName());\n");
        out.write("        response.getWriter().flush();\n");
        out.write("    }\n\n");
    }


    private static void store(final TranslationContext tc) throws IOException {
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
            out.write("    @Override\n");
            out.write("    public void init() throws ServletException {\n");
            out.write("        timestamp = new Date();\n");
            out.write("        System.out.println(\"initialized = \"+timestamp);\n");
            out.write("    }\n\n");
            out.write("    @Override\n");
            out.write("    public void destroy() {\n");
            out.write("        timestamp = null;\n");
            out.write("        System.out.println(\"destroyed = \"+timestamp);\n");
            out.write("    }\n\n");
            for (MethodContext mc : tc.methods) {
                mc.content.writeOut(out);
            }
            out.write("}\n");
            out.flush();
        } finally {
            out.close();
        }
    }
}
