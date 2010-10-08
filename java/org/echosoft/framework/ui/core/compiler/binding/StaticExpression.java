package org.echosoft.framework.ui.core.compiler.binding;

import org.echosoft.framework.ui.core.ComponentContext;

/**
 * @author Anton
 */
public final class StaticExpression implements Expression {

    private final String text;

    public StaticExpression(final String text) {
        this.text = text;
    }

    @Override
    public Object evaluate(final ComponentContext ctx) {
        return text;
    }

    @Override
    public String getExpressionString() {
        return text;
    }


    @Override
    public int hashCode() {
        return text!=null ? text.hashCode() : 0;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!getClass().equals(obj.getClass()))
            return false;
        final StaticExpression other = (StaticExpression)obj;
        return text!=null ? text.equals(other.text) : other.text==null;
    }

    @Override
    public String toString() {
        return "[StaticExpression{"+text+"}]";
    }
}
