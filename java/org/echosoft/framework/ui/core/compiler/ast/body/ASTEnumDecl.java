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

import org.echosoft.framework.ui.core.compiler.ast.Mods;
import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * Описывает заголовок определения перечислимого типа.
 *
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ASTEnumDecl extends ASTTypeDecl {

    private List<RefType> implTypes;
    private List<ASTEnumConstantDecl> entries;

    public ASTEnumDecl(final int modifiers, final String name) {
        super(modifiers, name);
    }

    public ASTEnumDecl(final int modifiers, final String name, final ASTEnumConstantDecl... entries) {
        super(modifiers, name);
        if (entries.length>0) {
            this.entries = new ArrayList<ASTEnumConstantDecl>(entries.length);
            for (ASTEnumConstantDecl entry : entries) {
                if (entry!=null) {
                    entry.setParent( this );
                    this.entries.add( entry );
                }
            }
        }
    }
    
    public ASTEnumDecl(final String name, final String... entries) {
        super(Mods.PUBLIC, name);
        if (entries.length>0) {
            this.entries = new ArrayList<ASTEnumConstantDecl>(entries.length);
            for (String literal : entries) {
                if (literal!=null) {
                    final ASTEnumConstantDecl entry = new ASTEnumConstantDecl(literal);
                    entry.setParent( this );
                    this.entries.add( entry );
                }
            }
        }
    }

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

    public boolean hasEntries() {
        return entries!=null && entries.size()>0;
    }
    public Iterable<ASTEnumConstantDecl> getEntries() {
        return entries!=null ? entries : Collections.<ASTEnumConstantDecl>emptyList();
    }
    public ASTEnumConstantDecl addEntry(final ASTEnumConstantDecl entry) {
        if (entries==null)
            entries = new ArrayList<ASTEnumConstantDecl>(2);
        entry.setParent( this );
        entries.add( entry );
        return entry;
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
