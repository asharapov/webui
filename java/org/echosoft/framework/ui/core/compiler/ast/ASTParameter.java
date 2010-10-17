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
package org.echosoft.framework.ui.core.compiler.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.echosoft.framework.ui.core.compiler.ast.expr.ASTAnnotationExpr;
import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * Данный класс используется для описывания параметров в методах, выражениях ForEach и Try-Catch.
 *
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ASTParameter extends ASTNode {

    private int modifiers;
    private RefType type;
    private boolean isVarArgs;
    private String name;
    private List<ASTAnnotationExpr> annotations;

    public ASTParameter(final RefType type, final String name) {
        this(0, type, name);
    }

    public ASTParameter(final int modifiers, final RefType type, final String name) {
        this.modifiers = modifiers;
        if (type!=null) {
            this.type = type;
            this.type.setParent(this);
        }
        this.name = name;
    }

    public ASTParameter(final Class type, final String name) {
        this(0, type, name);
    }

    public ASTParameter(final int modifiers, final Class type, final String name) {
        this.modifiers = modifiers;
        if (type!=null) {
            this.type = new RefType(type);
            this.type.setParent(this);
        }
        this.name = name;
    }

    /**
     * @return модификаторы параметра.
     * @see Mods
     */
    public int getModifiers() {
        return modifiers;
    }
    /**
     * @param modifiers  новые значения модификаторов данного параметра.
     * @see Mods
     */
    public void setModifiers(final int modifiers) {
        this.modifiers = modifiers;
    }


    /**
     * @return тип данного параметра.
     */
    public RefType getType() {
        return type;
    }
    /**
     * @param type  тип параметра.
     */
    public void setType(final RefType type) {
        this.type = type;
        if (type!=null)
            type.setParent( this );
    }

    public boolean isVarArgs() {
        return isVarArgs;
    }
    public void setVarArgs(final boolean isVarArgs) {
        this.isVarArgs = isVarArgs;
    }

    /**
     * @return название параметра.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name  название параметра.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return итератор по всем аннотациям относящимся к данному параметру.
     *      В случае отсутствия таких аннотаций метод возвращает пустой итератор.
     */
    public Iterable<ASTAnnotationExpr> getAnnotations() {
        return annotations!=null ? annotations : Collections.<ASTAnnotationExpr>emptyList();
    }

    /**
     * Регистрирует новую аннотацию для данного параметра.
     * @param annotation  новая аннотация для параметра.
     * @return то же значение что было указано в аргументе метода.
     */
    public <T extends ASTAnnotationExpr> T addAnnotation(final T annotation) {
        if (annotations==null)
            this.annotations = new ArrayList<ASTAnnotationExpr>(2);
        annotation.setParent( this );
        annotations.add( annotation );
        return annotation;
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
