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

import org.echosoft.framework.ui.core.compiler.ast.VariableDecl;
import org.echosoft.framework.ui.core.compiler.ast.type.Type;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class VariableDeclExpr extends Expression {

    private List<AnnotationExpr> annotations;
    private int modifiers;
    private Type type;
    private List<VariableDecl> vars;

    public VariableDeclExpr() {
    }

    public VariableDeclExpr(final int modifiers, final Type type) {
        this.modifiers = modifiers;
        this.type = type;
        if (type!=null)
            type.setParent(this);
    }

    public VariableDeclExpr(final int modifiers, final Type type, final String name, final Expression initValue) {
        this.modifiers = modifiers;
        this.type = type;
        if (type!=null)
            type.setParent(this);
        if (name!=null) {
            final VariableDecl var = new VariableDecl(name,initValue);
            var.setParent(this);
            if (initValue!=null)
                initValue.setParent(var);
            vars = new ArrayList<VariableDecl>(2);
            vars.add(var);
        }
    }

    public Iterable<AnnotationExpr> getAnnotations() {
        return annotations!=null ? annotations : Collections.<AnnotationExpr>emptyList();
    }

    public <T extends AnnotationExpr> T addAnnotation(final T annotation) {
        if (annotations!=null)
            annotations = new ArrayList<AnnotationExpr>(2);
        annotation.setParent(this);
        annotations.add(annotation);
        return annotation;
    }

    /**
     * @return модификаторы данной группы локальных переменных.
     * @see org.echosoft.framework.ui.core.compiler.ast.Mods
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

    public Iterable<VariableDecl> getVariables() {
        return vars!=null ? vars : Collections.<VariableDecl>emptyList();
    }

    public VariableDecl addVariable(final VariableDecl var) {
        if (vars==null)
            vars = new ArrayList<VariableDecl>(2);
        var.setParent(this);
        vars.add(var);
        return var;
    }

    public VariableDecl addVariable(final String name, final Expression initValue) {
        return addVariable( new VariableDecl(name, initValue) );
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
