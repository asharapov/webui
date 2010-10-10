package org.echosoft.framework.ui.core.compiler.binding;

import org.echosoft.framework.ui.core.RequestContext;

/**
 * @author Anton Sharapov
 */
public final class StaticExpression implements ValueExpression {

    private final String expr;

    public StaticExpression(final String expr) {
        this.expr = expr;
    }

    @Override
    public Object evaluate(final RequestContext ctx) {
        return expr;
    }

    @Override
    public String getExpressionString() {
        return expr;
    }


    @Override
    public int hashCode() {
        return expr!=null ? expr.hashCode() : 0;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!getClass().equals(obj.getClass()))
            return false;
        final StaticExpression other = (StaticExpression)obj;
        return expr!=null ? expr.equals(other.expr) : other.expr==null;
    }

    @Override
    public String toString() {
        return "[StaticExpression{"+expr+"}]";
    }
}
