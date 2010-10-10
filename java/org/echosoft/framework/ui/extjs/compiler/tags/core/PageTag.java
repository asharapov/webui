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
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTExpressionStmt;
import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.binding.ExpressionUtils;
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
    public ASTBlockStmt doStartTag(final Tag tag) throws SAXException {
        final ASTBlockStmt container = tag.getContainer();
        final ASTClassDecl cdecl = (ASTClassDecl)container.findEnclosingMethod().getParent();
        final String renderPage = cdecl.findUnusedMethodName("renderPage");

        //  ExtJSPage page = new ExtJSPage(uctx);
        final Variable page = container.defineVariable(new RefType(ExtJSPage.class), "page", false);
        final ASTObjectCreationExpr ctorNode = new ASTObjectCreationExpr( new RefType(ExtJSPage.class) );
        ctorNode.addArgument( new ASTNameExpr(tag.getContext().getName()) );
        ASTVariableDeclExpr vdeclNode = new ASTVariableDeclExpr(Mods.FINAL, page.getType(), page.getName(), ctorNode);
        container.addStatement( new ASTExpressionStmt(vdeclNode) );
        tag.setBean( page );

        //  инициализация его свойств ...
        final Attributes attrs = tag.getAttrs();
        String value;
        if ((value=attrs.getValue("title"))!=null) {
            ExpressionUtils.setBeanProperty(tag, "setTitle", ExpressionUtils.makeStringExpression(tag,value));
        }
        if ((value=attrs.getValue("icon"))!=null) {
            ExpressionUtils.setBeanProperty(tag, "setIcon", ExpressionUtils.makeStringExpression(tag,value));
        }
        if ((value=attrs.getValue("viewId"))!=null) {
            ExpressionUtils.setBeanProperty(tag, "setViewId", ExpressionUtils.makeStringExpression(tag,value));
        }
        if ((value=attrs.getValue("viewRank"))!=null) {
            ExpressionUtils.setBeanProperty(tag, "setViewRank", ExpressionUtils.makeIntegerExpression(tag,value));
        }
        if ((value=attrs.getValue("cleanStrategy"))!=null) {
            final ASTMethodCallExpr expr = new ASTMethodCallExpr( new RefType(Application.class), "getCleanStrategy" );
            expr.addArgument( ExpressionUtils.makeStringExpression(tag,value) );
            ExpressionUtils.setBeanProperty(tag, "setCleanStrategy", expr);
        }

        //  renderPage(page);
        ASTMethodCallExpr callNode = new ASTMethodCallExpr(renderPage);
        callNode.addArgument( new ASTNameExpr(page.getName()) );
        container.addStatement( new ASTExpressionStmt(callNode) );

        //  page.invokePage()
//        callNode = new ASTMethodCallExpr( new ASTNameExpr(page.getName()), "invokePage");
//        container.addStatement( new ASTExpressionStmt(callNode) );

        // public void renderPage(final ExtJSPage page) throws Exception {
        final ASTMethodDecl mdecl = cdecl.addMember( new ASTMethodDecl(Mods.PUBLIC, new RefType(void.class), renderPage) );
        mdecl.addParameter( new ASTParameter(Mods.FINAL, new RefType(ExtJSPage.class), page.getName()) );
        mdecl.addThrowable( new RefType(Exception.class) );
        final ASTBlockStmt body = mdecl.setBody( new ASTBlockStmt() );

        //     final ComponentContext ctx = page.getContext();
        final Variable ctx = body.defineVariable(new RefType(ComponentContext.class), "ctx", false);
        vdeclNode = new ASTVariableDeclExpr(Mods.FINAL, ctx.getType(), ctx.getName(),
                                        new ASTMethodCallExpr(new ASTNameExpr(page.getName()), "getContext")
                                    );

        body.addStatement( new ASTExpressionStmt(vdeclNode) );
        tag.setContext( ctx );
        tag.setChildrenContainer( body );

        return body;
    }

    @Override
    public void doEndTag(final Tag tag) throws SAXException {
        final ASTMethodCallExpr mce = new ASTMethodCallExpr(new ASTNameExpr(tag.getBean().getName()), "invokePage");
        tag.getContainer().addStatement( new ASTExpressionStmt(mce) );
    }

    @Override
    public void doBodyText(final Tag tag, final char[] ch, final int start, final int length) throws SAXException {
    }

}
