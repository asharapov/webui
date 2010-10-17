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

import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.compiler.ast.ASTVariableDecl;
import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.ast.type.TypeParameter;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * Описывает заголовок определения класса.
 *
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ASTClassDecl extends ASTTypeDecl {

    private List<TypeParameter> typeParameters;
    private RefType superType;
    private List<RefType> implTypes;

    public ASTClassDecl(final int modifiers, final String name, final RefType superType) {
        super(modifiers, name);
        if (superType!=null) {
            this.superType = superType;
            this.superType.setParent( this );
        }
    }

    public ASTClassDecl(final int modifiers, final String name, final Class superType) {
        super(modifiers, name);
        if (superType!=null) {
            this.superType = new RefType(superType);
            this.superType.setParent(this);
        }
    }

    /**
     * @return объявленные параметры для данного классаю
     *  При отсутствии параметров метод возвращает итератор по пустому списку.
     */
    public Iterable<TypeParameter> getTypeParameters() {
        return typeParameters!=null ? typeParameters : Collections.<TypeParameter>emptyList();
    }

    /**
     * Добавляет новый параметр к классу.
     * @param typeParam  новый параметр.
     * @return то же значение что было указано в аргументе метода.
     */
    public TypeParameter addTypeParameter(final TypeParameter typeParam) {
        if (typeParameters==null)
            this.typeParameters = new ArrayList<TypeParameter>(2);
        typeParam.setParent( this );
        typeParameters.add( typeParam );
        return typeParam;
    }

    /**
     * @return  класс, от которого данный класс наследуется.
     */
    public RefType getSuperType() {
        return superType;
    }
    public void setSuperType(final RefType type) {
        this.superType = type;
        if (type!=null)
            type.setParent( this );
    }

    /**
     * @return Перечисление интерфейсов, которые реализует данный класc.
     */
    public Iterable<RefType> getImplementations() {
        return implTypes!=null ? implTypes : Collections.<RefType>emptyList();
    }
    public RefType addImplementation(final RefType type) {
        if (implTypes==null)
            implTypes = new ArrayList<RefType>(2);
        type.setParent( this );
        implTypes.add( type );
        return type;
    }
    public RefType addImplementation(final Class type) {
        if (!type.isInterface())
            throw new IllegalArgumentException("Interface should be specified instead of "+type);
        return addImplementation( new RefType(type) );
    }


    public String findUnusedMethodName(String expectedName) {
        String name;
        if (!StringUtil.isJavaIdentifier(expectedName)) {
            expectedName = "method";
            name = "method1";
        } else {
            name = expectedName;
        }
        int cnt = 1;
        while (methodExists(name)) {
            name = expectedName + (++cnt);
        }
        return name;
    }
    public String findUnusedFieldName(String expectedName) {
        String name;
        if (!StringUtil.isJavaIdentifier(expectedName)) {
            expectedName = "field";
            name = "field1";
        } else {
            name = expectedName;
        }
        int cnt = 1;
        while (fieldExists(name)) {
            name = expectedName + (++cnt);
        }
        return name;
    }
    private boolean methodExists(final String name) {
        for (ASTBodyDecl member : getMembers()) {
            if (member instanceof ASTMethodDecl) {
                final ASTMethodDecl method = (ASTMethodDecl)member;
                if (method.getName().equals(name))
                    return true;
            }
        }
        return false;
    }
    private boolean fieldExists(final String name) {
        for (ASTBodyDecl member : getMembers()) {
            if (member instanceof ASTFieldDecl) {
                final ASTFieldDecl fields = (ASTFieldDecl)member;
                for (ASTVariableDecl var : fields.getVariables()) {
                    if (var.getName().equals(name))
                        return true;
                }
            }
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
