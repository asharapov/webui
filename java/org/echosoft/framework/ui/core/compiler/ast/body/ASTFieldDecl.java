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

import org.echosoft.framework.ui.core.compiler.ast.ASTVariableDecl;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTExpression;
import org.echosoft.framework.ui.core.compiler.ast.type.Type;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * Описывает отдельное поле в определении класса.
 *
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ASTFieldDecl extends ASTBodyDecl {

    private int modifiers;
    private Type type;
    private List<ASTVariableDecl> variables;

    public ASTFieldDecl(final int modifiers, final Type type, final String name) {
        this.modifiers = modifiers;
        if (type!=null) {
            this.type = type;
            this.type.setParent( this );
        }
        if (name!=null && name.length()>0) {
            this.addVariable( new ASTVariableDecl(name) );
        }
    }
    public ASTFieldDecl(final int modifiers, final Type type, final String name, final ASTExpression initValue) {
        this.modifiers = modifiers;
        if (type!=null) {
            this.type = type;
            this.type.setParent( this );
        }
        if (name!=null && name.length()>0) {
            this.addVariable( new ASTVariableDecl(name,initValue) );
        }
    }
    public ASTFieldDecl(final int modifiers, final Type type, final ASTVariableDecl... variables) {
        this.modifiers = modifiers;
        if (type!=null) {
            this.type = type;
            this.type.setParent( this );
        }
        for (ASTVariableDecl var : variables) {
            if (var!=null)
                this.addVariable(var);
        }
    }

    public ASTFieldDecl(final int modifiers, final Class type, final String name) {
        this.modifiers = modifiers;
        if (type!=null) {
            this.type = new Type(type);
            this.type.setParent( this );
        }
        if (name!=null && name.length()>0) {
            this.addVariable( new ASTVariableDecl(name) );
        }
    }
    public ASTFieldDecl(final int modifiers, final Class type, final String name, final ASTExpression initValue) {
        this.modifiers = modifiers;
        if (type!=null) {
            this.type = new Type(type);
            this.type.setParent( this );
        }
        if (name!=null && name.length()>0) {
            this.addVariable( new ASTVariableDecl(name,initValue) );
        }
    }
    public ASTFieldDecl(final int modifiers, final Class type, final ASTVariableDecl... variables) {
        this.modifiers = modifiers;
        if (type!=null) {
            this.type = new Type(type);
            this.type.setParent( this );
        }
        for (ASTVariableDecl var : variables) {
            if (var!=null)
                this.addVariable(var);
        }
    }

    /**
     * @return модификаторы данного поля.
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
            type.setParent( this );
    }

    public Iterable<ASTVariableDecl> getVariables() {
        return variables!=null ? variables : Collections.<ASTVariableDecl>emptyList();
    }
    public ASTVariableDecl addVariable(final ASTVariableDecl var) {
        if (variables==null)
            variables = new ArrayList<ASTVariableDecl>(2);
        var.setParent( this );
        variables.add( var );
        return var;
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
