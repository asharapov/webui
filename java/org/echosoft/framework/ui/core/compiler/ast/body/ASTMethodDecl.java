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

import org.echosoft.framework.ui.core.compiler.ast.ASTParameter;
import org.echosoft.framework.ui.core.compiler.ast.VariablesContainer;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTBlockStmt;
import org.echosoft.framework.ui.core.compiler.ast.type.Type;
import org.echosoft.framework.ui.core.compiler.ast.type.TypeParameter;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * Описывает отдельный метод в определении класса.
 *
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ASTMethodDecl extends ASTBodyDecl implements VariablesContainer {

    private int modifiers;
    private List<TypeParameter> typeParameters;
    private Type type;
    private String name;
    private List<ASTParameter> parameters;
    private List<Type> throwables;
    private ASTBlockStmt body;

    public ASTMethodDecl(final int modifiers, final Type type, final String name) {
        this.modifiers = modifiers;
        if (type!=null) {
            this.type = type;
            this.type.setParent( this );
        }
        this.name = name;
    }

    public ASTMethodDecl(final int modifiers, final Class type, final String name) {
        this.modifiers = modifiers;
        if (type!=null) {
            this.type = new Type(type);
            this.type.setParent(this);
        }
        this.name = name;
    }

    /**
     * Return the modifiers of this member declaration.
     *
     * @see org.echosoft.framework.ui.core.compiler.ast.Mods
     * @return modifiers
     */
    public int getModifiers() {
        return modifiers;
    }
    public void setModifiers(final int modifiers) {
        this.modifiers = modifiers;
    }

    public Type getType() {
        return type;
    }
    public void setType(final Type type) {
        this.type = type;
        if (type!=null)
            type.setParent(this);
    }

    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }

    public ASTBlockStmt getBody() {
        return body;
    }
    public ASTBlockStmt setBody(final ASTBlockStmt body) {
        this.body = body;
        if (body!=null)
            body.setParent( this );
        return body;
    }

    public Iterable<TypeParameter> getTypeParameters() {
        return typeParameters!=null ? typeParameters : Collections.<TypeParameter>emptyList();
    }
    public TypeParameter addTypeParameter(final TypeParameter typeParam) {
        if (typeParameters==null)
            typeParameters = new ArrayList<TypeParameter>(2);
        typeParam.setParent(this);
        typeParameters.add(typeParam);
        return typeParam;
    }

    public Iterable<ASTParameter> getParameters() {
        return parameters!=null ? parameters : Collections.<ASTParameter>emptyList();
    }
    public ASTParameter addParameter(final ASTParameter param) {
        if (parameters==null)
            parameters = new ArrayList<ASTParameter>(2);
        param.setParent( this );
        parameters.add( param );
        return param;
    }

    public Iterable<Type> getThrowables() {
        return throwables!=null ? throwables : Collections.<Type>emptyList();
    }
    public Type addThrowable(final Type type) {
        if (throwables==null)
            throwables = new ArrayList<Type>(2);
        type.setParent( this );
        throwables.add( type );
        return type;
    }
    public Type addThrowable(final Class<? extends Throwable> type) {
        return addThrowable( new Type(type) );
    }

    @Override
    public boolean containsVariable(final String name, final boolean findInParents) {
        for (ASTParameter param : parameters) {
            if (param.getName().equals(name))
                return true;
        }
        return false;
    }

    @Override
    public boolean containsVariable(final String name, final Type type, final boolean findInParents) {
        for (ASTParameter param : parameters) {
            if (param.getName().equals(name) && param.getType().equals(type))
                return true;
        }
        return false;
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
