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
public final class EnumDecl extends TypeDeclaration {

    private List<RefType> implTypes;
    private List<EnumConstantDecl> entries;

    public EnumDecl(final int modifiers, final String name) {
        super(modifiers, name);
    }

    public EnumDecl(final int modifiers, final String name, final EnumConstantDecl... entries) {
        super(modifiers, name);
        if (entries.length>0) {
            this.entries = new ArrayList<EnumConstantDecl>(entries.length);
            for (EnumConstantDecl entry : entries) {
                if (entry!=null) {
                    entry.setParent( this );
                    this.entries.add( entry );
                }
            }
        }
    }
    
    public EnumDecl(final String name, final String... entries) {
        super(Mods.PUBLIC, name);
        if (entries.length>0) {
            this.entries = new ArrayList<EnumConstantDecl>(entries.length);
            for (String literal : entries) {
                if (literal!=null) {
                    final EnumConstantDecl entry = new EnumConstantDecl(literal);
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
    public Iterable<EnumConstantDecl> getEntries() {
        return entries!=null ? entries : Collections.<EnumConstantDecl>emptyList();
    }
    public EnumConstantDecl addEntry(final EnumConstantDecl entry) {
        if (entries==null)
            entries = new ArrayList<EnumConstantDecl>(2);
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
