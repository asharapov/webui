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

import org.echosoft.framework.ui.core.compiler.ast.expr.Expression;
import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * Описывает отдельное свойство в определении аннотации.
 * 
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class AnnotationMemberDecl extends BodyDeclaration {

    private int modifiers;
    private RefType type;
    private String name;
    private Expression defaultValue;

    public AnnotationMemberDecl(final RefType type, final String name) {
        this(0, type, name, null);
    }

    public AnnotationMemberDecl(final int modifiers, final RefType type, final String name, final Expression defaultValue) {
        this.modifiers = modifiers;
        this.type = type;
        this.name = name;
        this.defaultValue = defaultValue;
        if (type!=null)
            type.setParent(this);
        if (defaultValue!=null)
            defaultValue.setParent(this);
    }


    /**
     * @return модификаторы для данного элемента аннотации.
     * @see org.echosoft.framework.ui.core.compiler.ast.Mods
     */
    public int getModifiers() {
        return modifiers;
    }
    public void setModifiers(final int modifiers) {
        this.modifiers = modifiers;
    }

    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }

    public RefType getType() {
        return type;
    }
    public void setType(final RefType type) {
        this.type = type;
        if (type!=null)
            type.setParent(this);
    }

    public Expression getDefaultValue() {
        return defaultValue;
    }
    public void setDefaultValue(final Expression defaultValue) {
        this.defaultValue = defaultValue;
        if (defaultValue!=null)
            defaultValue.setParent(this);
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
