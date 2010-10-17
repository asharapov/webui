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

import org.echosoft.framework.ui.core.compiler.ast.ASTVariableDecl;
import org.echosoft.framework.ui.core.compiler.ast.Variable;
import org.echosoft.framework.ui.core.compiler.ast.type.Type;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ASTVariableDeclExpr extends ASTExpression {

    private List<ASTAnnotationExpr> annotations;
    private int modifiers;
    private Type type;
    private List<ASTVariableDecl> vars;

    public ASTVariableDeclExpr() {
    }

    public ASTVariableDeclExpr(final int modifiers, final Type type) {
        this.modifiers = modifiers;
        if (type!=null) {
            this.type = type;
            this.type.setParent(this);
        }
    }

    public ASTVariableDeclExpr(final int modifiers, final Type type, final String name) {
        this.modifiers = modifiers;
        if (type!=null) {
            this.type = type;
            this.type.setParent(this);
        }
        if (name!=null) {
            this.addVariable( new ASTVariableDecl(name) );
        }
    }

    public ASTVariableDeclExpr(final int modifiers, final Type type, final String name, final ASTExpression initValue) {
        this.modifiers = modifiers;
        if (type!=null) {
            this.type = type;
            this.type.setParent(this);
        }
        if (name!=null) {
            this.addVariable( new ASTVariableDecl(name,initValue) );
        }
    }

    public ASTVariableDeclExpr(final int modifiers, final Variable var, final ASTExpression initValue) {
        this.modifiers = modifiers;
        if (var!=null) {
            this.type = var.getType();
            this.type.setParent(this);
            this.addVariable( new ASTVariableDecl(var.getName(),initValue) );
        }
    }


    public Iterable<ASTAnnotationExpr> getAnnotations() {
        return annotations!=null ? annotations : Collections.<ASTAnnotationExpr>emptyList();
    }

    public <T extends ASTAnnotationExpr> T addAnnotation(final T annotation) {
        if (annotations!=null)
            annotations = new ArrayList<ASTAnnotationExpr>(2);
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

    public Iterable<ASTVariableDecl> getVariables() {
        return vars!=null ? vars : Collections.<ASTVariableDecl>emptyList();
    }

    public ASTVariableDecl addVariable(final ASTVariableDecl var) {
        if (vars==null)
            vars = new ArrayList<ASTVariableDecl>(2);
        var.setParent(this);
        vars.add(var);
        return var;
    }

    public ASTVariableDecl addVariable(final String name, final ASTExpression initValue) {
        return addVariable( new ASTVariableDecl(name, initValue) );
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
