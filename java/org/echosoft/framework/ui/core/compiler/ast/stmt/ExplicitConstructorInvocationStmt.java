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
package org.echosoft.framework.ui.core.compiler.ast.stmt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.echosoft.framework.ui.core.compiler.ast.expr.Expression;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * Описывает выражения вида:
 * <ul>
 *  <li> this(a,b);
 *  <li> super(a,b);
 *  <li> List.super(a,b);
 *  <li> <strike>Map.&lt;String,Integer&gt;super(a,b);</strike>
 * </ul>
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ExplicitConstructorInvocationStmt extends Statement {

    private boolean isThis;
    private Expression classExpr;
    private List<Expression> args;

    public ExplicitConstructorInvocationStmt(final boolean isThis) {
        this.isThis = isThis;
    }

    public boolean isThis() {
        return isThis;
    }
    public void setThis(final boolean isThis) {
        this.isThis = isThis;
    }

    public Expression getClassExpr() {
        return classExpr;
    }
    public <T extends Expression> T setClassExpr(final T classExpr) {
        this.classExpr = classExpr;
        if (classExpr !=null)
            classExpr.setParent(this);
        return classExpr;
    }

    public Iterable<Expression> getArguments() {
        return args!=null ? args : Collections.<Expression>emptyList();
    }

    public <T extends Expression> T addArgument(final T argument) {
        if (args==null)
            args = new ArrayList<Expression>(2);
        argument.setParent(this);
        args.add( argument );
        return argument;
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
