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

import org.echosoft.framework.ui.core.compiler.ast.type.SimpleTypeArgument;
import org.echosoft.framework.ui.core.compiler.ast.type.Type;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ASTMethodCallExpr extends ASTExpression {

    private ASTExpression scopeExpr;
    private Type scopeType;
    private List<SimpleTypeArgument> typeArgs;
    private String name;
    private List<ASTExpression> args;

    public ASTMethodCallExpr(final String name) {
        this.name = name;
    }

    public ASTMethodCallExpr(final String name, final ASTExpression... args) {
        this.name = name;
        for (ASTExpression expr : args) {
            this.addArgument(expr);
        }
    }

    public ASTMethodCallExpr(final ASTExpression scope, final String name) {
        if (scope!=null) {
            this.scopeExpr = scope;
            this.scopeExpr.setParent(this);
        }
        this.name = name;
    }

    public ASTMethodCallExpr(final ASTExpression scope, final String name, final ASTExpression... args) {
        if (scope!=null) {
            this.scopeExpr = scope;
            this.scopeExpr.setParent(this);
        }
        this.name = name;
        for (ASTExpression expr : args) {
            this.addArgument(expr);
        }
    }

    public ASTMethodCallExpr(final Type scope, final String name) {
        if (scope!=null) {
            this.scopeType = scope;
            this.scopeType.setParent(this);
        }
        this.name = name;
    }

    public ASTMethodCallExpr(final Type scope, final String name, final ASTExpression... args) {
        if (scope!=null) {
            this.scopeType = scope;
            this.scopeType.setParent(this);
        }
        this.name = name;
        for (ASTExpression expr : args) {
            this.addArgument(expr);
        }
    }

    public ASTMethodCallExpr(final Class scope, final String name) {
        if (scope!=null) {
            this.scopeType = new Type(scope);
            this.scopeType.setParent(this);
        }
        this.name = name;
    }

    public ASTMethodCallExpr(final Class scope, final String name, final ASTExpression... exprs) {
        if (scope!=null) {
            this.scopeType = new Type(scope);
            this.scopeType.setParent(this);
        }
        this.name = name;
        for (ASTExpression expr : exprs) {
            this.addArgument(expr);
        }
    }



    public ASTExpression getScopeExpr() {
        return scopeExpr;
    }
    public <T extends ASTExpression> T setScopeExpr(final T scope) {
        this.scopeExpr = scope;
        if (scope!=null) {
            scopeExpr.setParent(this);
            scopeType = null;
        }
        return scope;
    }

    public Type getScopeType() {
        return scopeType;
    }
    public void setScopeType(final Type scope) {
        this.scopeType = scope;
        if (scope!=null) {
            scopeType.setParent(this);
            scopeExpr = null;
        }
    }

    public boolean hasTypeArguments() {
        return typeArgs!=null && typeArgs.size()>0;
    }
    public Iterable<SimpleTypeArgument> getTypeArguments() {
        return typeArgs!=null ? typeArgs : Collections.<SimpleTypeArgument>emptyList();
    }
    public SimpleTypeArgument addTypeArgument(final SimpleTypeArgument typeArg) {
        if (typeArgs==null)
            typeArgs = new ArrayList<SimpleTypeArgument>(2);
        typeArg.setParent(this);
        typeArgs.add(typeArg);
        return typeArg;
    }
    public SimpleTypeArgument addTypeArgument(final Class typeArg) {
        return addTypeArgument( new SimpleTypeArgument(typeArg) );
    }

    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }


    public boolean hasArguments() {
        return args!=null && args.size()>0;
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
