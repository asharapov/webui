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

import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.ast.type.TypeParameter;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * Описывает заголовок определения интерфейса.
 *
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class InterfaceDecl extends TypeDeclaration {

    private List<TypeParameter> typeParameters;
    private List<RefType> extTypes;

    public InterfaceDecl(final int modifiers, final String name) {
        super(modifiers, name);
    }

    public Iterable<TypeParameter> getTypeParameters() {
        return typeParameters!=null ? typeParameters : Collections.<TypeParameter>emptyList();
    }
    public TypeParameter addTypeParameter(final TypeParameter typeParam) {
        if (typeParameters==null)
            typeParameters = new ArrayList<TypeParameter>(2);
        typeParam.setParent( this );
        typeParameters.add( typeParam );
        return typeParam;
    }

    public Iterable<RefType> getSuperTypes() {
        return extTypes!=null ? extTypes : Collections.<RefType>emptyList();
    }
    public RefType addSuperType(final RefType type) {
        if (extTypes==null)
            extTypes = new ArrayList<RefType>(2);
        type.setParent( this );
        extTypes.add( type );
        return type;
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
