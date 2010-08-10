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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.echosoft.framework.ui.core.compiler.ast.ASTNode;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;


/**
 * Данный класс описывает объявление параметра типа.</br>
 * Конструируется используя следующий синтаксис:<br/>
 * <code style="white-space:nowrap">
 *  TypeParameter ::= &lt;IDENTIFIER ( "extends" {@link RefType} ( "&" {@link RefType} )* )? &gt;
 * <code>
 *
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class TypeParameter extends ASTNode {

    private String name;
    private List<RefType> typeBounds;

    public TypeParameter(final String name) {
        this.name = name;
    }

    public TypeParameter(final String name, final String... bounds) {
        this.name = name;
        if (bounds.length>0) {
            typeBounds = new ArrayList<RefType>(bounds.length);
            for (String tb : bounds) {
                if (tb!=null) {
                    final RefType rt = new RefType(tb);
                    rt.setParent(this);
                    typeBounds.add( rt );
                }
            }
        }
    }

    /**
     * @return Название параметра.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name  Название параметра. Не может быть пустой строкой или <code>null</code>.
     */
    public void setName(final String name) {
        this.name = name;
    }


    /**
     * @return Итератор по перечню типов (классов или интерфейсов), ограничивающих данный параметр.
     *  В отсутствии данных возвращает пустой список.
     */
    public Iterable<RefType> getTypeBounds() {
        return typeBounds !=null ? typeBounds : Collections.<RefType>emptyList();
    }

    public RefType addTypeBound(final RefType bound) {
        if (typeBounds==null)
            this.typeBounds = new ArrayList<RefType>(2);
        bound.setParent( this );
        typeBounds.add( bound );
        return bound;
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
