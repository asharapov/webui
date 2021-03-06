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
public final class ASTConditionalExpr extends ASTExpression {

    private ASTExpression condition;
    private ASTExpression thenExpr;
    private ASTExpression elseExpr;

    public ASTConditionalExpr() {
    }

    public ASTConditionalExpr(final ASTExpression condition, final ASTExpression thenExpr, final ASTExpression elseExpr) {
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

    public ASTExpression getCondition() {
        return condition;
    }
    public <T extends ASTExpression> T setCondition(final T condition) {
        this.condition = condition;
        if (condition!=null)
            condition.setParent(this);
        return condition;
    }

    public ASTExpression getThenExpr() {
        return thenExpr;
    }
    public <T extends ASTExpression> T setThenExpr(final T thenExpr) {
        this.thenExpr = thenExpr;
        if (thenExpr!=null)
            thenExpr.setParent(this);
        return thenExpr;
    }

    public ASTExpression getElseExpr() {
        return elseExpr;
    }
    public <T extends ASTExpression> T setElseExpr(final T elseExpr) {
        this.elseExpr = elseExpr;
        if (elseExpr!=null)
            elseExpr.setParent(this);
        return elseExpr;
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
