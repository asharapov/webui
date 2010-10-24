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

import org.echosoft.framework.ui.core.compiler.ast.body.ASTBodyDecl;
import org.echosoft.framework.ui.core.compiler.ast.type.Type;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ASTObjectCreationExpr extends ASTExpression {

    private ASTExpression scope;
    private Type type;
    private List<ASTExpression> args;
    private List<ASTBodyDecl> anonymousClassBody;

    public ASTObjectCreationExpr() {
    }

    public ASTObjectCreationExpr(final Type type) {
        if (type!=null) {
            this.type = type;
            this.type.setParent(this);
        }
    }

    public ASTObjectCreationExpr(final Type type, final ASTExpression... args) {
        if (type!=null) {
            this.type = type;
            this.type.setParent(this);
        }
        for (ASTExpression arg : args) {
            addArgument( arg );
        }
    }

    public ASTObjectCreationExpr(final Class type) {
        if (type!=null) {
            this.type = new Type(type);
            this.type.setParent(this);
        }
    }

    public ASTObjectCreationExpr(final Class type, final ASTExpression... args) {
        if (type!=null) {
            this.type = new Type(type);
            this.type.setParent(this);
        }
        for (ASTExpression arg : args) {
            addArgument( arg );
        }
    }


    public ASTExpression getScope() {
        return scope;
    }
    public <T extends ASTExpression> T setScope(final T scope) {
        this.scope = scope;
        if (scope!=null)
            scope.setParent(this);
        return scope;
    }

    public Type getType() {
        return type;
    }
    public void setType(final Type type) {
        this.type = type;
        if (type!=null)
            type.setParent(this);
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


    public boolean hasAnonymousClassItems() {
        return anonymousClassBody!=null && anonymousClassBody.size()>0;
    }

    public Iterable<ASTBodyDecl> getAnonymousClassItems() {
        return anonymousClassBody!=null ? anonymousClassBody : Collections.<ASTBodyDecl>emptyList();
    }
    
    public <T extends ASTBodyDecl> T addAnonymousClassItem(final T item) {
        if (anonymousClassBody==null)
            anonymousClassBody = new ArrayList<ASTBodyDecl>(2);
        item.setParent(this);
        anonymousClassBody.add(item);
        return item;
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
