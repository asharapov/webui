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
public final class UnaryExpr extends Expression {

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
    private Expression expr;

    public UnaryExpr() {
    }

    public UnaryExpr(final Operator op, final Expression expr) {
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

    public Expression getExpr() {
        return expr;
    }
    public void setExpr(final Expression expr) {
        this.expr = expr;
        if (expr!=null)
            expr.setParent(this);
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
