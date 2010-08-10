/*
 * Copyright (C) 2007 Julio Vilmar Gesser.
 * 
 * This file is part of Java 1.5 parser and Abstract Syntax Tree.
 *
 * Java 1.5 parser and Abstract Syntax Tree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Java 1.5 parser and Abstract Syntax Tree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Java 1.5 parser and Abstract Syntax Tree.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.echosoft.framework.ui.core.compiler.ast.expr;

import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ConditionalExpr extends Expression {

    private Expression condition;
    private Expression thenExpr;
    private Expression elseExpr;

    public ConditionalExpr() {
    }

    public ConditionalExpr(final Expression condition, final Expression thenExpr, final Expression elseExpr) {
        this.condition = condition;
        this.thenExpr = thenExpr;
        this.elseExpr = elseExpr;
        if (condition!=null)
            condition.setParent(this);
        if (thenExpr!=null)
            thenExpr.setParent(this);
        if (elseExpr!=null)
            elseExpr.setParent(this);
    }

    public Expression getCondition() {
        return condition;
    }
    public void setCondition(final Expression condition) {
        this.condition = condition;
        if (condition!=null)
            condition.setParent(this);
    }

    public Expression getThenExpr() {
        return thenExpr;
    }
    public void setThenExpr(final Expression thenExpr) {
        this.thenExpr = thenExpr;
        if (thenExpr!=null)
            thenExpr.setParent(this);
    }

    public Expression getElseExpr() {
        return elseExpr;
    }
    public void setElseExpr(final Expression elseExpr) {
        this.elseExpr = elseExpr;
        if (elseExpr!=null)
            elseExpr.setParent(this);
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
