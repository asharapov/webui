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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.ast.type.Type;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ASTMethodCallExpr extends ASTExpression {

    private ASTExpression scopeExpr;
    private RefType scopeType;
    private List<Type> typeArgs;
    private String name;
    private List<ASTExpression> args;

    public ASTMethodCallExpr() {
    }

    public ASTMethodCallExpr(final String name) {
        this.name = name;
    }

    public ASTMethodCallExpr(final ASTExpression scope, final String name) {
        this.scopeExpr = scope;
        this.name = name;
        if (scope!=null)
            scope.setParent(this);
    }

    public ASTMethodCallExpr(final RefType scope, final String name) {
        this.scopeType = scope;
        this.name = name;
        if (scope!=null)
            scope.setParent(this);
    }

    public ASTExpression getScopeExpr() {
        return scopeExpr;
    }
    public <T extends ASTExpression> T setScopeExpr(final T scope) {
        this.scopeExpr = scope;
        if (scope!=null) {
            scope.setParent(this);
            scopeType = null;
        }
        return scope;
    }

    public RefType getScopeType() {
        return scopeType;
    }
    public void setScopeType(final RefType scope) {
        this.scopeType = scope;
        if (scope!=null) {
            scope.setParent(this);
            scopeExpr = null;
        }
    }

    public Iterable<Type> getTypeArgs() {
        return typeArgs!=null ? typeArgs : Collections.<Type>emptyList();
    }

    public <T extends Type> T addTypeArgument(final T typeArg) {
        if (typeArgs==null)
            typeArgs = new ArrayList<Type>(2);
        typeArg.setParent(this);
        typeArgs.add(typeArg);
        return typeArg;
    }

    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }

    public Iterable<ASTExpression> getArguments() {
        return args!=null ? args : Collections.<ASTExpression>emptyList();
    }

    public <T extends ASTExpression> T addArgument(final T expr) {
        if (args==null)
            args = new ArrayList<ASTExpression>(2);
        expr.setParent(this);
        args.add(expr);
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
