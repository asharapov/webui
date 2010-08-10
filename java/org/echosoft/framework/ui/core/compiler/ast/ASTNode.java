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

import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * Базовый класс для всех узлов абстрактного синтаксического дерева.
 *
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public abstract class ASTNode {

    private ASTNode parent;

    /**
     * @return родительский узел.
     */
    public final ASTNode getParent() {
        return parent;
    }
    /**
     * @param parent  ссылка на родительский узел.
     */
    public void setParent(final ASTNode parent) {
        this.parent = parent;
    }

    /**
     * Accept method for visitor support.
     * 
     * @param <R>  the type the return value of the visitor
     * @param <A>  the type the argument passed for the visitor
     * @param v    the visitor implementation
     * @param arg  any value relevant for the visitor
     * @return the result of the visit
     */
    public abstract <R, A> R accept(GenericVisitor<R, A> v, A arg);

    /**
     * Accept method for visitor support.
     * 
     * @param <A> the type the argument passed for the visitor
     * @param v   the visitor implementation
     * @param arg any value relevant for the visitor
     */
    public abstract <A> void accept(VoidVisitor<A> v, A arg);


    /**
     * Return the String representation of this node.
     * 
     * @return the String representation of this node
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName()+"{hash:"+System.identityHashCode(this)+"}";
    }

}
