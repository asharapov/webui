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

import org.echosoft.framework.ui.core.compiler.ast.expr.AnnotationExpr;
import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * Данный класс используется для описывания параметров в методах, выражениях ForEach и Try-Catch.
 *
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class Parameter extends ASTNode {

    private int modifiers;
    private RefType type;
    private boolean isVarArgs;
    private String name;
    private List<AnnotationExpr> annotations;

    public Parameter(final RefType type, final String name) {
        this.type = type;
        this.name = name;
    }

    public Parameter(final int modifiers, final RefType type, final String name) {
        this.modifiers = modifiers;
        this.type = type;
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
    public Iterable<AnnotationExpr> getAnnotations() {
        return annotations!=null ? annotations : Collections.<AnnotationExpr>emptyList();
    }

    /**
     * Регистрирует новую аннотацию для данного параметра.
     * @param annotation  новая аннотация для параметра.
     * @return то же значение что было указано в аргументе метода.
     */
    public <T extends AnnotationExpr> T addAnnotation(final T annotation) {
        if (annotations==null)
            this.annotations = new ArrayList<AnnotationExpr>(2);
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
