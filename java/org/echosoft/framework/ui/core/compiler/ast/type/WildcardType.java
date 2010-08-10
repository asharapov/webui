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
package org.echosoft.framework.ui.core.compiler.ast.type;

import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * Описывает типы данных следующего вида:
 * <ul>
 *  <li> <code>?</code>
 *  <li> <code>? extends List</code>
 *  <li> <code>? super List</code>
 * </ul>

 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class WildcardType extends Type {

    private RefType ext;
    private RefType sup;

    public WildcardType() {
    }

    public WildcardType(final RefType ext) {
        setExtends(ext);
    }

    public WildcardType(final RefType ext, final RefType sup) {
        setExtends(ext);
        setSuper(sup);
    }

    public RefType getExtends() {
        return ext;
    }
    public void setExtends(final RefType ext) {
        this.ext = ext;
        if (ext!=null) {
            ext.setParent(this);
            sup = null;
        }
    }

    public RefType getSuper() {
        return sup;
    }
    public void setSuper(final RefType sup) {
        this.sup = sup;
        if (sup!=null) {
            ext = null;
            sup.setParent(this);
        }
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
