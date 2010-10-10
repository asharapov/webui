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
package org.echosoft.framework.ui.core.compiler.ast.body;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.echosoft.framework.ui.core.compiler.ast.expr.ASTExpression;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * Описывает отдельное значение в определении перечислимого типа.
 *
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ASTEnumConstantDecl extends ASTBodyDecl {

    private String name;
    private List<ASTExpression> args;
    private List<ASTBodyDecl> classBody;

    public ASTEnumConstantDecl(final String name) {
        this.name = name;
    }

    public ASTEnumConstantDecl(final String name, final ASTExpression... args) {
        this(name);
        if (args.length>0) {
            this.args = new ArrayList<ASTExpression>(args.length);
            for (ASTExpression e : args) {
                if (e!=null) {
                    e.setParent( this );
                    this.args.add( e );
                }
            }
        }
    }


    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }

    public Iterable<ASTExpression> getArguments() {
        return args !=null ? args : Collections.<ASTExpression>emptyList();
    }
    public <T extends ASTExpression> T addArgument(final T expr) {
        if (args==null)
            args = new ArrayList<ASTExpression>(2);
        expr.setParent( this );
        args.add( expr );
        return expr;
    }

    public boolean hasBodyDeclarations() {
        return classBody!=null && classBody.size()>0;
    }
    public Iterable<ASTBodyDecl> getBodyDeclarations() {
        return classBody!=null ? classBody : Collections.<ASTBodyDecl>emptyList();
    }
    public <T extends ASTBodyDecl> T addBodyDeclaration(final T item) {
        if (classBody==null)
            classBody = new ArrayList<ASTBodyDecl>(2);
        item.setParent( this );
        classBody.add( item );
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
