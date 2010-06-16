package org.echosoft.framework.ui.core.compiler.codegen;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.echosoft.common.io.FastStringWriter;
import org.echosoft.framework.ui.core.Application;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.UIContext;
import org.echosoft.framework.ui.core.web.wui.Options;
import org.echosoft.framework.ui.extjs.ExtJSPage;

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
        process(tc);
        store(tc);
        return tc.dstFile;
    }

    private static void process(final TranslationContext tc) throws IOException {
        final MethodContext mc = new MethodContext(tc,null,"main");
        mc.addIdentifier(HttpServletRequest.class, "request", false);
        mc.addIdentifier(HttpServletResponse.class, "response", false);
        mc.addIdentifier(UIContext.class, "uctx", false);
        mc.addIdentifier(ComponentContext.class, "ctx", false);
        tc.ensureClassImported(Application.class);
        final FastStringWriter out = mc.content;
        out.write("    @Override\n");
        out.write("    public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {\n");
        if (tc.options.charset!=null) {
            out.append("        request.setCharacterEncoding(\"").append(tc.options.charset).append("\");\n");
        }
        out.write("        response.setContentType(\"text/html; charset=UTF-8\");\n");
        out.write("        try {\n");
        mc.incLevel();
        out.write("            final UIContext uctx = Application.makeContext(request, response, getServletConfig());\n");
        out.write("            final ComponentContext ctx = new ComponentContext(uctx);\n");
        final Variable page = mc.allocateVariable(ExtJSPage.class, "page");
        mc.indent();
        page.writeDeclaration();
        out.write(" = new ");
        page.writeClass();
        out.write("(uctx);\n");
        mc.indent();
        out.write("// TODO: main content will be here...\n");
        mc.indent().append(page.name).append(".invokePage();\n");
        mc.decLevel();
        out.write("        } catch(Exception e) {\n");
        out.write("            throw new ServletException(e.getMessage(), e);\n");
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
                out.write(cls.getCanonicalName());
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
