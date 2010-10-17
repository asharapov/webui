package org.echosoft.framework.ui.extjs.compiler.tags.core;

import org.echosoft.framework.ui.core.Application;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.compiler.ast.ASTParameter;
import org.echosoft.framework.ui.core.compiler.ast.Mods;
import org.echosoft.framework.ui.core.compiler.ast.Variable;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTClassDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTMethodDecl;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTMethodCallExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTNameExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTObjectCreationExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTVariableDeclExpr;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTBlockStmt;
import org.echosoft.framework.ui.core.compiler.utils.ExpressionUtils;
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
    public void doStartTag(final Tag tag) throws SAXException {
        final ASTBlockStmt container = tag.getParent().getContainer();
        final Variable ownctx = tag.getParent().getContext();
        final ASTClassDecl cdecl = container.findEnclosingClass();
        final String renderPage = cdecl.findUnusedMethodName("renderPage");

        //  ExtJSPage page = new ExtJSPage(uctx);
        final Variable page = container.defineVariable(ExtJSPage.class, "page", false);
        final ASTObjectCreationExpr ctorNode = new ASTObjectCreationExpr(ExtJSPage.class, new ASTNameExpr(tag.getContext()));
        container.addExpressionStmt( new ASTVariableDeclExpr(Mods.FINAL, page, ctorNode) );

        //  инициализация его свойств ...
        final Attributes attrs = tag.getAttrs();
        String value;
        if ((value=attrs.getValue("title"))!=null) {
            ExpressionUtils.invoke(container,page,"setTitle", ExpressionUtils.makeStringExpression(cdecl,ownctx,value));
        }
        if ((value=attrs.getValue("icon"))!=null) {
            ExpressionUtils.invoke(container,page,"setIcon", ExpressionUtils.makeStringExpression(cdecl,ownctx,value));
        }
        if ((value=attrs.getValue("viewId"))!=null) {
            ExpressionUtils.invoke(container,page,"setViewId", ExpressionUtils.makeStringExpression(cdecl,ownctx,value));
        }
        if ((value=attrs.getValue("viewRank"))!=null) {
            ExpressionUtils.invoke(container,page,"setViewRank", ExpressionUtils.makeIntegerExpression(cdecl,ownctx,value));
        }
        if ((value=attrs.getValue("cleanStrategy"))!=null) {
            final ASTMethodCallExpr expr = new ASTMethodCallExpr(Application.class, "getCleanStrategy" );
            expr.addArgument( ExpressionUtils.makeStringExpression(cdecl,ownctx,value) );
            ExpressionUtils.invoke(container,page,"setCleanStrategy", expr);
        }

        //  renderPage(page);
        container.addExpressionStmt( new ASTMethodCallExpr(renderPage, new ASTNameExpr(page)) );

        //  page.invokePage()
        //ExpressionUtils.invoke(container, page, "invokePage");

        // public void renderPage(final ExtJSPage page) throws Exception {
        final ASTMethodDecl mdecl = cdecl.addMember( new ASTMethodDecl(Mods.PUBLIC, void.class, renderPage) );
        mdecl.addParameter( new ASTParameter(Mods.FINAL, ExtJSPage.class, page.getName()) );
        mdecl.addThrowable( Exception.class );
        final ASTBlockStmt body = mdecl.setBody( new ASTBlockStmt() );

        //     final ComponentContext ctx = page.getContext();
        final Variable ctx = body.defineVariable(ComponentContext.class, "ctx", false);
        body.addExpressionStmt( new ASTVariableDeclExpr(Mods.FINAL, ctx, new ASTMethodCallExpr(new ASTNameExpr(page), "getContext")) );

        tag.setContainer( body );
        tag.setBean( page );
        tag.setContext( ctx );
    }


    @Override
    public void doEndTag(final Tag tag) throws SAXException {
        ExpressionUtils.invoke(tag.getParent().getContainer(), tag.getBean(), "invokePage");
    }


    @Override
    public void doBodyText(final Tag tag, final char[] ch, final int start, final int length) throws SAXException {
    }

}
