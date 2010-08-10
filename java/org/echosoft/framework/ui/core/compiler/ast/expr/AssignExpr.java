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
public final class AssignExpr extends Expression {

    public static enum Operator {
        ASSIGN("="),
        PLUS("+="),
        MINUS("-="),
        MULTIPLY("*="),
        DIVIDE("/="),
        AND("&="),
        OR("|="),
        XOR("^="),
        REMAINDER("%="),
        L_SHIFT("<<="),
        R_SGN_SHIFT(">>="),
        R_USGN_SHIFT(">>>=");
        private final String opcode;
        private Operator(final String opcode) {
            this.opcode = opcode;
        }
        public String getCode() {
            return opcode;
        }
    }

    private Expression target;
    private Operator op;
    private Expression value;

    public AssignExpr() {
    }

    public AssignExpr(final Operator op) {
        this.op = op;
    }

    public AssignExpr(final Expression target, final Operator op, final Expression value) {
        this.target = target;
        this.op = op;
        this.value = value;
        if (target!=null)
            target.setParent(this);
        if (value!=null)
            value.setParent(this);
    }

    public Expression getTarget() {
        return target;
    }
    public void setTarget(final Expression target) {
        this.target = target;
        if (target!=null)
            target.setParent(this);
    }

    public Operator getOperator() {
        return op;
    }
    public void setOperator(final Operator op) {
        this.op = op;
    }

    public Expression getValue() {
        return value;
    }
    public void setValue(final Expression value) {
        this.value = value;
        if (value!=null)
            value.setParent(this);
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
