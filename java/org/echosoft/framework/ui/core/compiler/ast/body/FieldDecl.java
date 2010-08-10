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

import org.echosoft.framework.ui.core.compiler.ast.VariableDecl;
import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * Описывает отдельное поле в определении класса.
 *
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class FieldDecl extends BodyDeclaration {

    private int modifiers;
    private RefType type;
    private List<VariableDecl> variables;

    public FieldDecl(final int modifiers, final RefType type, final String name) {
        this.modifiers = modifiers;
        this.type = type;
        if (type!=null)
            type.setParent( this );
        if (name!=null && name.length()>0) {
            final VariableDecl var = new VariableDecl(name);
            var.setParent( this );
            this.variables = new ArrayList<VariableDecl>(2);
            this.variables.add( var );
        }
    }
    public FieldDecl(final int modifiers, final RefType type, final VariableDecl... variables) {
        this.modifiers = modifiers;
        this.type = type;
        if (type!=null)
            type.setParent( this );
        if (variables.length>0) {
            this.variables = new ArrayList<VariableDecl>(variables.length);
            for (VariableDecl var : variables) {
                if (var!=null) {
                    var.setParent(this);
                    this.variables.add( var );
                }
            }
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

    public RefType getType() {
        return type;
    }
    public void setType(final RefType type) {
        this.type = type;
        if (type!=null)
            type.setParent( this );
    }

    public Iterable<VariableDecl> getVariables() {
        return variables!=null ? variables : Collections.<VariableDecl>emptyList();
    }
    public VariableDecl addVariable(final VariableDecl var) {
        if (variables==null)
            variables = new ArrayList<VariableDecl>(2);
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
