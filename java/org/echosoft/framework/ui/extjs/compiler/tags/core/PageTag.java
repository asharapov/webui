package org.echosoft.framework.ui.extjs.compiler.tags.core;

import javax.servlet.ServletException;

import org.echosoft.framework.ui.core.Application;
import org.echosoft.framework.ui.core.UIContext;
import org.echosoft.framework.ui.core.compiler.ast.Mods;
import org.echosoft.framework.ui.core.compiler.ast.Parameter;
import org.echosoft.framework.ui.core.compiler.ast.Variable;
import org.echosoft.framework.ui.core.compiler.ast.body.ClassDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.MethodDecl;
import org.echosoft.framework.ui.core.compiler.ast.expr.IntegerLiteralExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.MethodCallExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.NameExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ObjectCreationExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.RawExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.StringLiteralExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.VariableDeclExpr;
import org.echosoft.framework.ui.core.compiler.ast.stmt.BlockStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.CatchClause;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ExpressionStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ThrowStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.TryStmt;
import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.xml.Tag;
import org.echosoft.framework.ui.core.compiler.xml.TagHandler;
import org.echosoft.framework.ui.extjs.ExtJSPage;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Обработчик тега &lt;page&gt;. Отвечает за инициализацию фреймворка на странице.<br/>
 * Тег входит в стандартную библиотеку тегов <code>http://echosoft.org/webui/0.8/taglib/core</code>
 * @author Anton Sharapov
 */
public class PageTag implements TagHandler {

    @Override
    public BlockStmt doStartTag(final Tag tag) throws SAXException {
        final BlockStmt container = tag.getContainer();
//        if (!container.containsVariable("request", new RefType(HttpServletRequest.class), true))
//            throw new IllegalStateException("No 'request' argument defined");
//        if (!container.containsVariable("response", new RefType(HttpServletResponse.class), true))
//            throw new IllegalStateException("No 'response' argument defined");
        final TryStmt tr1 = container.addStatement( new TryStmt() );
        // содержимое блока 'try' ...
        final BlockStmt tryBlock = tr1.setTryBlock( new BlockStmt() );
        final Variable uctx = tryBlock.defineVariable(new RefType(UIContext.class), "uctx", false);
        final MethodCallExpr mce1 = new MethodCallExpr(new RefType(Application.class),"makeContext");
        mce1.addArgument( new NameExpr("request") );
        mce1.addArgument( new NameExpr("response") );
        mce1.addArgument( new MethodCallExpr("getServletConfig") );
        final VariableDeclExpr vde1 = new VariableDeclExpr(Mods.FINAL, uctx.getType(), uctx.getName(), mce1);
        tryBlock.addStatement( new ExpressionStmt(vde1) );
        // содержимое блока 'catch' ...
        final String e = container.findUnusedVariableName("e");
        final CatchClause catchClause = tr1.addCatch( new CatchClause() );
        final BlockStmt catchBlock = catchClause.setCatchBlock( new BlockStmt() );
        catchClause.setParam( new Parameter(Mods.FINAL, new RefType(Exception.class), e) );
        final ThrowStmt ts1 = catchBlock.addStatement( new ThrowStmt() );
        final ObjectCreationExpr oce1 = ts1.setExpr( new ObjectCreationExpr(new RefType(ServletException.class)) );
        oce1.addArgument(new RawExpr(e+".getMessage()"));
        oce1.addArgument(new NameExpr(e));
        // заголовок метода 'renderPage' ...
        final ClassDecl cdecl = (ClassDecl)container.findEnclosingMethod().getParent();
        final String name = cdecl.findUnusedMethodName("renderPage");
        final MethodDecl mdecl = cdecl.addMember( new MethodDecl(Mods.PUBLIC, new RefType(void.class), name) );
        mdecl.addParameter( new Parameter(Mods.FINAL, new RefType(UIContext.class), "uctx") );
        mdecl.addThrowable( new RefType(Exception.class) );
        final BlockStmt body = mdecl.setBody( new BlockStmt() );
        // создание бина ExtJSPage ...
        final Variable page = body.defineVariable(new RefType(ExtJSPage.class), "page", false);
        final ObjectCreationExpr oce2 = new ObjectCreationExpr( new RefType(ExtJSPage.class) );
        oce2.addArgument( new NameExpr("uctx") );
        final VariableDeclExpr vde2 = new VariableDeclExpr(Mods.FINAL, page.getType(), page.getName(), oce2);
        body.addStatement( new ExpressionStmt(vde2) );
        // инициализация его свойств ...
        final Attributes attrs = tag.getAttrs();
        String value;
        if ((value=attrs.getValue("title"))!=null) {
            final MethodCallExpr mce3 = new MethodCallExpr(new NameExpr(page.getName()), "setTitle");
            mce3.addArgument( new StringLiteralExpr(value) );
            body.addStatement( new ExpressionStmt(mce3) );
        }
        if ((value=attrs.getValue("icon"))!=null) {
            final MethodCallExpr mce3 = new MethodCallExpr(new NameExpr(page.getName()), "setIcon");
            mce3.addArgument( new StringLiteralExpr(value) );
            body.addStatement( new ExpressionStmt(mce3) );
        }
        if ((value=attrs.getValue("viewId"))!=null) {
            final MethodCallExpr mce3 = new MethodCallExpr(new NameExpr(page.getName()), "setViewId");
            mce3.addArgument( new StringLiteralExpr(value) );
            body.addStatement( new ExpressionStmt(mce3) );
        }
        if ((value=attrs.getValue("viewRank"))!=null) {
            final MethodCallExpr mce3 = new MethodCallExpr(new NameExpr(page.getName()), "setViewRank");
            mce3.addArgument( new IntegerLiteralExpr(value) );
            body.addStatement( new ExpressionStmt(mce3) );
        }
        if ((value=attrs.getValue("cleanStrategy"))!=null) {
            final MethodCallExpr mce3a = new MethodCallExpr( new RefType(Application.class), "getCleanStrategy" );
            mce3a.addArgument( new StringLiteralExpr(value) );
            final MethodCallExpr mce3 = new MethodCallExpr(new NameExpr(page.getName()), "setCleanStrategy");
            mce3.addArgument( mce3a );
            body.addStatement( new ExpressionStmt(mce3) );
        }

        tag.setBean( page );
        return body;
    }

    @Override
    public void doEndTag(final Tag tag) throws SAXException {
        final Variable page = tag.getBean();
        final MethodCallExpr mce = new MethodCallExpr(new NameExpr(page.getName()), "invokePage");
        tag.getChildrenContainer().addStatement( new ExpressionStmt(mce) );
    }

    @Override
    public void doBodyText(final Tag tag, final char[] ch, final int start, final int length) throws SAXException {
    }

}
