package org.echosoft.framework.ui.core.compiler.ast;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Anton Sharapov
 */
public class RawExpressionNode extends ExpressionNode {

    private String expr;

    public RawExpressionNode(final String expr) {
        super();
        this.expr = expr;
    }

    public String getExpression() {
        return expr;
    }
    public RawExpressionNode setExpression(final String expr) {
        this.expr = expr;
        return this;
    }

    @Override
    public ExpressionNode append(final ASTNode node) {
        throw new IllegalStateException("RawExpression node can't have any children");
    }

    @Override
    public void translate(final Writer out) throws IOException {
        if (expr!=null)
            out.write(expr);
    }
}
