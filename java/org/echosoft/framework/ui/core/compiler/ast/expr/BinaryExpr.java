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
public final class BinaryExpr extends Expression {

    public static enum Operator {
        OR("||"),
        AND("&&"),
        BINARY_OR("|"),
        BINARY_AND("&"),
        XOR("^"),
        EQUALS("=="),
        NOT_EQUALS("!="),
        LESS("<"),
        GREATER(">"),
        LESS_EQUALS("<="),
        GREATER_EQUALS(">="),
        L_SHIFT("<<"),
        R_SGN_SHIFT(">>"),
        R_USGN_SHIFT(">>>"),
        PLUS("+"),
        MINUS("-"),
        MULTIPLY("*"),
        DIVIDE("/"),
        REMAINDER("%");
        private final String opcode;
        private Operator(final String opcode) {
            this.opcode = opcode;
        }
        public String getCode() {
            return opcode;
        }
    }

    private Expression left;
    private Expression right;
    private Operator op;

    public BinaryExpr() {
    }

    public BinaryExpr(final Expression left, final Expression right, final Operator op) {
        this.left = left;
        this.right = right;
        this.op = op;
        if (left!=null)
            left.setParent(this);
        if (right!=null)
            right.setParent(this);
    }

    public Expression getLeft() {
        return left;
    }
    public void setLeft(final Expression left) {
        this.left = left;
        if (left!=null)
            left.setParent(this);
    }

    public Operator getOperator() {
        return op;
    }
    public void setOperator(final Operator op) {
        this.op = op;
    }

    public Expression getRight() {
        return right;
    }
    public void setRight(final Expression right) {
        this.right = right;
        if (right!=null)
            right.setParent(this);
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
