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
public final class WildcardTypeArgument extends TypeArgument {

    private Type ext;
    private Type sup;

    public WildcardTypeArgument() {
    }
    public WildcardTypeArgument(final Type ext, final Type sup) {
        this.setExtends(ext);
        this.setSuper(sup);
    }
    public WildcardTypeArgument(final Class ext, final Class sup) {
        this.setExtends(ext);
        this.setSuper(sup);
    }

    public Type getExtends() {
        return ext;
    }
    public void setExtends(final Type ext) {
        this.ext = ext;
        if (ext!=null) {
            ext.setParent(this);
            sup = null;
        }
    }
    public void setExtends(final Class ext) {
        if (ext!=null) {
            this.ext = new Type(ext);
            this.ext.setParent(this);
            this.sup = null;
        } else {
            this.ext = null;
        }
    }

    public Type getSuper() {
        return sup;
    }
    public void setSuper(final Type sup) {
        this.sup = sup;
        if (sup!=null) {
            ext = null;
            sup.setParent(this);
        }
    }
    public void setSuper(final Class sup) {
        if (sup!=null) {
            this.ext = null;
            this.sup = new Type(sup);
            this.sup.setParent(this);
        } else {
            this.sup = null;
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
