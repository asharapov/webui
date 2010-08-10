package org.echosoft.framework.ui.core.compiler.ast.expr;

import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * @author Anton Sharapov
 */
public final class RawExpr extends Expression {

    private String text;

    public RawExpr(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    public void setText(final String text) {
        this.text = text;
    }


    @Override
    public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(final VoidVisitor<A> v, final A arg) {
        v.visit(this, arg);
    }

}
