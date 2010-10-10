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
public final class ASTUnaryExpr extends ASTExpression {

    public static enum Operator {
        POSITIVE("+"),
        NEGATIVE("-"),
        PRE_INCREMENT("++"),
        PRE_DECREMENT("--"),
        NOT("-"),
        INVERSE("~"),
        POST_INCREMENT("++"),
        POST_DECREMENT("--");
        private final String opcode;
        private Operator(final String opcode) {
            this.opcode = opcode;
        }
        public String getCode() {
            return opcode;
        }
    }

    private Operator op;
    private ASTExpression expr;

    public ASTUnaryExpr() {
    }

    public ASTUnaryExpr(final Operator op, final ASTExpression expr) {
        this.op = op;
        this.expr = expr;
        if (expr!=null)
            expr.setParent(this);
    }

    public Operator getOperator() {
        return op;
    }
    public void setOperator(final Operator op) {
        this.op = op;
    }

    public ASTExpression getExpr() {
        return expr;
    }
    public <T extends ASTExpression> T setExpr(final T expr) {
        this.expr = expr;
        if (expr!=null)
            expr.setParent(this);
        return expr;
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
