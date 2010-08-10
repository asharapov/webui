package org.echosoft.framework.ui.extjs.compiler.tags.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.echosoft.framework.ui.core.UIContext;
import org.echosoft.framework.ui.core.compiler.ast.Mods;
import org.echosoft.framework.ui.core.compiler.ast.Parameter;
import org.echosoft.framework.ui.core.compiler.ast.Variable;
import org.echosoft.framework.ui.core.compiler.ast.expr.RawExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.VariableDeclExpr;
import org.echosoft.framework.ui.core.compiler.ast.stmt.BlockStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.CatchClause;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ExpressionStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ThrowStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.TryStmt;
import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.xml.Tag;
import org.echosoft.framework.ui.core.compiler.xml.TagHandler;
import org.xml.sax.SAXException;

/**
 * Обработчик тега &lt;core&gt;. Отвечает за инициализацию фреймворка на странице.<br/>
 * Тег входит в стандартную библиотеку тегов <code>http://echosoft.org/webui/0.8/taglib/core</code>
 * @author Anton Sharapov
 */
public class PageTag implements TagHandler {

    @Override
    public BlockStmt doStartTag(final Tag tag) throws SAXException {
        final BlockStmt container = tag.getContainer();
        if (!container.containsVariable("request", new RefType(HttpServletRequest.class), true))
            throw new IllegalStateException("No 'request' argument defined");
        if (!container.containsVariable("response", new RefType(HttpServletResponse.class), true))
            throw new IllegalStateException("No 'response' argument defined");

        final TryStmt tr1 = container.addStatement( new TryStmt() );
        final BlockStmt tryBlock = tr1.setTryBlock( new BlockStmt() );
        final CatchClause catchClause = tr1.addCatch( new CatchClause() );
        final BlockStmt catchBlock = catchClause.setCatchBlock( new BlockStmt() );

        final Variable uctx = tryBlock.defineVariable(new RefType(UIContext.class), "uctx", false);
        final String e = container.findUnusedVariableName("e");

        final VariableDeclExpr vde1 = new VariableDeclExpr(Mods.FINAL, uctx.getType(), uctx.getName(),
                new RawExpr("Application.makeContext(request,response,getServletConfig()"));
        tryBlock.addStatement( new ExpressionStmt(vde1) );

        catchClause.setParam( new Parameter(Mods.FINAL, new RefType(Exception.class), e) );
        final ThrowStmt ts1 = catchBlock.addStatement( new ThrowStmt() );
        ts1.setExpr( new RawExpr("new ServletException("+e+".getMessage(), "+e+")") );
        return tryBlock;
    }

    @Override
    public void doEndTag(final Tag tag) throws SAXException {
    }

    @Override
    public void doBodyText(final Tag tag, final char[] ch, final int start, final int length) throws SAXException {
    }

}
