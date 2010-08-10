package org.echosoft.framework.ui.core.compiler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.echosoft.common.io.FastStringWriter;
import org.echosoft.framework.ui.core.compiler.ast.CompilationUnit;
import org.echosoft.framework.ui.core.compiler.ast.Mods;
import org.echosoft.framework.ui.core.compiler.ast.Parameter;
import org.echosoft.framework.ui.core.compiler.ast.body.ClassDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.MethodDecl;
import org.echosoft.framework.ui.core.compiler.ast.expr.MarkerAnnotationExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.MethodCallExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.NameExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.StringLiteralExpr;
import org.echosoft.framework.ui.core.compiler.ast.stmt.BlockStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ExpressionStmt;
import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.ast.visitors.DumpVisitor;
import org.echosoft.framework.ui.core.compiler.xml.Tag;
import org.echosoft.framework.ui.core.compiler.xml.TagHandler;
import org.echosoft.framework.ui.core.compiler.xml.TagLibrarySet;
import org.echosoft.framework.ui.core.web.wui.Options;
import org.xml.sax.SAXException;

/**
 * Отвечает за трансляцию .wui файлов в соответствуюшие .java сервлеты.
 * @author Anton Sharapov
 */
public class Translator {

    private static final TagHandler DEFAULT_HTML_TAG_HANDLER = new TagHandler() {
        @Override
        public BlockStmt doStartTag(final Tag tag) throws SAXException {
            final FastStringWriter buf = new FastStringWriter(64);
            buf.write('<');
            buf.write(tag.qname);
            for (int i=0,l=tag.attrs.getLength(); i<l; i++) {
                buf.write(' ');
                buf.write(tag.attrs.getQName(i));
                buf.write("=\"");
                buf.write(tag.attrs.getValue(i));
                buf.write('\"');
            }
            buf.write('>');
            final char[] c = buf.toString().toCharArray();
            tag.parent.handler.doBodyText(tag.parent, c, 0, c.length);
            return null;
        }
        @Override
        public void doEndTag(final Tag tag) throws SAXException {
            final FastStringWriter buf = new FastStringWriter(10);
            buf.write("</");
            buf.write(tag.qname);
            buf.write('>');
            final char[] c = buf.toString().toCharArray();
            tag.parent.handler.doBodyText(tag.parent, c, 0, c.length);
        }
        @Override
        public void doBodyText(final Tag tag, final char[] ch, final int start, final int length) throws SAXException {
            tag.parent.handler.doBodyText(tag.parent, ch, start, length);
        }
    };
    private static TagLibrarySet instance;
    private static TagLibrarySet getTagLibraries() throws IOException {
        if (instance==null) {
            final TagLibrarySet set = TagLibrarySet.findLibariesInClasspath();
            set.ensureLibraryExists("http://www.w3.org/1999/xhtml")
                    .setDefaultHandler(DEFAULT_HTML_TAG_HANDLER);
            set.ensureLibraryExists("http://www.w3.org/2001/XMLSchema-instance");
            set.ensureLibraryExists("http://www.w3.org/2001/XInclude");
            instance = set;
        }
        return instance;
    }

    /**
     * Выполняет трансляцию .wui Файла в соответствующий ему .java файл.
     * @param uri  относительный путь до данного ресурса на сервере. Декодируется в имя класса и пакет java.
     * @param options  конфигурационные параметры.
     * @return  путь к сгенерированному .java классу.
     * @throws IOException  в случае каких-либо проблем с сохранением генерируемого файла на диск.
     */
    public static File translate(final String uri, final Options options) throws IOException {
        final File srcFile = new File(options.rootSrcDir, uri);
        final CompilationUnit cu = makeCompilationUnit(uri, options);
        final TagLibrarySet taglibs = Translator.getTagLibraries();
        final XMLContentHandler handler = new XMLContentHandler(taglibs, cu.getMainMethod().getBody());

        final SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            factory.setNamespaceAware(true);
            factory.setXIncludeAware(true);
//            factory.setValidating(true);
            final SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(srcFile, handler);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        final File file = cu.getDstFile();
        final FileWriter out = new FileWriter( file );
        try {
            final DumpVisitor visitor = new DumpVisitor();
            cu.accept(visitor, null);
            visitor.getPrinter().writeOut(out);
            out.flush();
        } finally {
            out.close();
        }
        return file;
    }


    private static CompilationUnit makeCompilationUnit(final String uri, final Options options) {
        int p;
        String path = (p=uri.lastIndexOf('.')) >= 0 ? uri.substring(0,p) : uri; // убрали расширение исходного файла
        if (options.basePkgName!=null)
            path = '/' + options.basePkgName.replace('.','/') + path;
        p = path.lastIndexOf('/');
        final String pkgName, clsName;
        if (p>0) {
            pkgName = path.substring(1,p).replace('/','.');
            clsName = path.substring(p+1);
        } else {
            pkgName = "";
            clsName = path.substring(1);
        }
        final CompilationUnit result = new CompilationUnit(pkgName);
        final ClassDecl cd = result.addType( new ClassDecl(Mods.PUBLIC|Mods.FINAL, clsName, new RefType(HttpServlet.class)) );
        final MethodDecl md = cd.addMember( new MethodDecl(Mods.PUBLIC, new RefType(void.class), "service") );
        md.addAnnotation( new MarkerAnnotationExpr("Override") );
        md.addParameter( new Parameter(Mods.FINAL, new RefType(HttpServletRequest.class), "request") );
        md.addParameter( new Parameter(Mods.FINAL, new RefType(HttpServletResponse.class), "response") );
        md.addThrowable( new RefType(ServletException.class) );
        md.addThrowable( new RefType(IOException.class) );
        final BlockStmt body = new BlockStmt();
        md.setBody(body);
        final String charset = options.charset;
        if (charset!=null) {
            final MethodCallExpr mce1 = new MethodCallExpr(new NameExpr("request"), "setCharacterEncoding");
            mce1.addArgument( new StringLiteralExpr(charset) );
            body.addStatement( new ExpressionStmt(mce1) );
            final MethodCallExpr mce2 = new MethodCallExpr(new NameExpr("response"), "setContentType");
            mce2.addArgument( new StringLiteralExpr("text/html; charset="+charset) );
            body.addStatement( new ExpressionStmt(mce2) );
        } else {
            final MethodCallExpr mce2 = new MethodCallExpr(new NameExpr("response"), "setContentType");
            mce2.addArgument( new StringLiteralExpr("text/html") );
            body.addStatement( new ExpressionStmt(mce2) );
        }
        result.setDstFile( new File( options.rootDstDir, path+".java" ) );
        result.setMainMethod( md );
        return result;
    }
}
