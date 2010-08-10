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

import org.echosoft.framework.ui.core.compiler.ast.Parameter;
import org.echosoft.framework.ui.core.compiler.ast.VariablesContainer;
import org.echosoft.framework.ui.core.compiler.ast.stmt.BlockStmt;
import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.ast.type.TypeParameter;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * Описывает отдельный метод в определении класса.
 *
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class MethodDecl extends BodyDeclaration implements VariablesContainer {

    private int modifiers;
    private List<TypeParameter> typeParameters;
    private RefType type;
    private String name;
    private List<Parameter> parameters;
    private List<RefType> throwables;
    private BlockStmt body;

    public MethodDecl(final int modifiers, final RefType type, final String name) {
        this.modifiers = modifiers;
        this.type = type;
        if (type!=null)
            type.setParent( this );
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

    public RefType getType() {
        return type;
    }
    public void setType(final RefType type) {
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

    public BlockStmt getBody() {
        return body;
    }
    public BlockStmt setBody(final BlockStmt body) {
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

    public Iterable<Parameter> getParameters() {
        return parameters!=null ? parameters : Collections.<Parameter>emptyList();
    }
    public Parameter addParameter(final Parameter param) {
        if (parameters==null)
            parameters = new ArrayList<Parameter>(2);
        param.setParent( this );
        parameters.add( param );
        return param;
    }

    public Iterable<RefType> getThrowables() {
        return throwables!=null ? throwables : Collections.<RefType>emptyList();
    }
    public RefType addThrowable(final RefType type) {
        if (throwables==null)
            throwables = new ArrayList<RefType>(2);
        type.setParent( this );
        throwables.add( type );
        return type;
    }

    @Override
    public boolean containsVariable(final String name, final boolean findInParents) {
        for (Parameter param : parameters) {
            if (param.getName().equals(name))
                return true;
        }
        return false;
    }

    @Override
    public boolean containsVariable(final String name, final RefType type, final boolean findInParents) {
        for (Parameter param : parameters) {
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
