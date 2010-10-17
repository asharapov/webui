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
import org.echosoft.framework.ui.core.Application;
import org.echosoft.framework.ui.core.UIContext;
import org.echosoft.framework.ui.core.compiler.ast.ASTParameter;
import org.echosoft.framework.ui.core.compiler.ast.CompilationUnit;
import org.echosoft.framework.ui.core.compiler.ast.Mods;
import org.echosoft.framework.ui.core.compiler.ast.Variable;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTClassDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTMethodDecl;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTExpression;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTMarkerAnnotationExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTMethodCallExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTNameExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTObjectCreationExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTRawExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTStringLiteralExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTVariableDeclExpr;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTBlockStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTCatchClause;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTExpressionStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTThrowStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTTryStmt;
import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.ast.visitors.DumpVisitor;
import org.echosoft.framework.ui.core.compiler.xml.BaseTag;
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

    /**
     * Обработчик по умолчанию для всех тегов в .wui файлах, для которых не было найдено специальных инструкций по их обработке.
     * Как правило, данный обработчик используется для обработки фрагментов html кода.
     */
    private static final TagHandler DEFAULT_HTML_TAG_HANDLER = new TagHandler() {
        @Override
        public void doStartTag(final Tag tag) throws SAXException {
            final FastStringWriter buf = new FastStringWriter(64);
            buf.write('<');
            buf.write(tag.getQName());
            for (int i=0,l= tag.getAttrs().getLength(); i<l; i++) {
                buf.write(' ');
                buf.write(tag.getAttrs().getQName(i));
                buf.write("=\"");
                buf.write(tag.getAttrs().getValue(i));
                buf.write('\"');
            }
            buf.write('>');
            final char[] c = buf.toString().toCharArray();
            tag.getParent().getHandler().doBodyText(tag.getParent(), c, 0, c.length);
        }
        @Override
        public void doEndTag(final Tag tag) throws SAXException {
            final FastStringWriter buf = new FastStringWriter(10);
            buf.write("</");
            buf.write(tag.getQName());
            buf.write('>');
            final char[] c = buf.toString().toCharArray();
            tag.getParent().getHandler().doBodyText(tag.getParent(), c, 0, c.length);
        }
        @Override
        public void doBodyText(final Tag tag, final char[] ch, final int start, final int length) throws SAXException {
            tag.getParent().getHandler().doBodyText(tag.getParent(), ch, start, length);
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
     * Выполняет трансляцию .wui файла в соответствующий ему .java файл (класс, унаследованный от {@link HttpServlet}).
     * @param uri  относительный путь до данного ресурса на сервере. Декодируется в имя класса и пакет java.
     * @param options  конфигурационные параметры.
     * @return  путь к сгенерированному .java классу.
     * @throws IOException  в случае каких-либо проблем с сохранением генерируемого файла на диск.
     */
    public static File translate(final String uri, final Options options) throws IOException {
        final File srcFile = new File(options.rootSrcDir, uri);
        final CompilationUnit cu = makeCompilationUnit(uri, options);
        final TagLibrarySet taglibs = Translator.getTagLibraries();
        final XMLContentHandler handler = new XMLContentHandler(taglibs, new BaseTag(cu.getMainBlockNode(), cu.getUIContextVar()) );

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


    /**
     * Подготавливаем корневой узел AST-дерева узлы которого в последствии будут сериализованы в соответствующие элементы будущего .java файла.
     * @param uri  относительный путь до данного ресурса на сервере. Декодируется в имя класса и пакет java.
     * @param options  конфигурационные параметры.
     * @return  инициализированный экземпляр класса CompilationUnit, с описанием заголовка будущего сервлета и его
     *          стартового метода <code>service(HttpServletRequest,HttpServletResponse)</code>.
     */
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
        final ASTClassDecl classNode = result.addType( new ASTClassDecl(Mods.PUBLIC|Mods.FINAL, clsName, HttpServlet.class) );
        final ASTMethodDecl methodNode = classNode.addMember( new ASTMethodDecl(Mods.PUBLIC, void.class, "service") );
        methodNode.addAnnotation( new ASTMarkerAnnotationExpr("Override") );
        methodNode.addParameter( new ASTParameter(Mods.FINAL, HttpServletRequest.class, "request") );
        methodNode.addParameter( new ASTParameter(Mods.FINAL, HttpServletResponse.class, "response") );
        methodNode.addThrowable( ServletException.class );
        methodNode.addThrowable( IOException.class );
        final ASTBlockStmt body = methodNode.setBody( new ASTBlockStmt() );
        if (options.charset!=null) {
            final ASTMethodCallExpr mce1 = new ASTMethodCallExpr(new ASTNameExpr("request"), "setCharacterEncoding");
            mce1.addArgument( new ASTStringLiteralExpr(options.charset) );
            body.addStatement( new ASTExpressionStmt(mce1) );
            final ASTMethodCallExpr mce2 = new ASTMethodCallExpr(new ASTNameExpr("response"), "setContentType");
            mce2.addArgument( new ASTStringLiteralExpr("text/html; charset="+options.charset) );
            body.addStatement( new ASTExpressionStmt(mce2) );
        } else {
            final ASTMethodCallExpr mce2 = new ASTMethodCallExpr(new ASTNameExpr("response"), "setContentType");
            mce2.addArgument( new ASTStringLiteralExpr("text/html") );
            body.addStatement( new ASTExpressionStmt(mce2) );
        }

        // try {
        final ASTTryStmt trs = body.addStatement( new ASTTryStmt() );
        final ASTBlockStmt tryBlock = trs.setTryBlock( new ASTBlockStmt() );
        //     final UIContext uctx = Application.makeContext(request, response, getServletConfig());
        final Variable uctx = tryBlock.defineVariable(UIContext.class, "uctx", false);
        final ASTMethodCallExpr mce = new ASTMethodCallExpr(Application.class, "makeContext",
                                            new ASTNameExpr("request"), new ASTNameExpr("response"), new ASTMethodCallExpr("getServletConfig"));
        tryBlock.addExpressionStmt( new ASTVariableDeclExpr(Mods.FINAL, uctx, mce) );
        // ... ... ...
        // } catch (Exception e) {
        final String e = body.findUnusedVariableName("e");
        final ASTCatchClause catchClause = trs.addCatch( new ASTCatchClause() );
        final ASTBlockStmt catchBlock = catchClause.setCatchBlock( new ASTBlockStmt() );
        catchClause.setParam( new ASTParameter(Mods.FINAL, new RefType(Exception.class), e) );
        //     throw new ServletException(e.getMessage(), e);
        final ASTExpression throwExpr = new ASTObjectCreationExpr(ServletException.class, new ASTRawExpr(e+".getMessage()"), new ASTNameExpr(e));
        catchBlock.addStatement( new ASTThrowStmt(throwExpr) );
        // }

        result.setDstFile( new File( options.rootDstDir, path+".java" ) );
        result.setMainBlockNode( tryBlock );
        result.setUIContextVar( uctx );
        return result;
    }
}
