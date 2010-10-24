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
import java.util.Iterator;
import java.util.List;

import org.echosoft.framework.ui.core.compiler.ast.ASTNode;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * Содержит название определенного типа данных.
 * Примеры описания типов:
 * <ul>
 *  <li> <code>int</code>
 *  <li> <code>java.util.List</code>
 *  <li> <code>java.util.List&lt;String&gt;</code>
 *  <li> <code>java.util.List&lt;String&gt;[][]</code>
 * </ul>
 *
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class Type extends ASTNode {

    private final String qname;
    private final String name;
    private final int arrayDimension;
    private boolean hasPackage;
    private List<TypeArgument> args;

    public Type(final Class cls) {
        Class cl = cls;
        int ai = 0;
        while (cl.isArray()) {
            cl = cl.getComponentType();
            ai++;
        }
        qname = cl.getCanonicalName();
        name = cl.getSimpleName();
        arrayDimension = ai;
        hasPackage = cl.getPackage()!=null;
    }

    /**
     * @param qname  строка вида  <code>java.util.List[][]</code>.
     */
    public Type(String qname) {
        qname = qname.trim();
        if (qname.isEmpty())
            throw new IllegalArgumentException("No type specified");
        int ai = 0;
        while (qname.endsWith("[]")) {
            qname = qname.substring(0,qname.length()-2);
            ai++;
        }
        final int p = qname.lastIndexOf('.');
        this.qname = qname;
        this.name = p>0 ? qname.substring(p+1,qname.length()) : qname;
        this.arrayDimension = ai;
        this.hasPackage = p>0;
        if (name.isEmpty())
            throw new IllegalArgumentException("Invalid type: "+qname);
    }

    /**
     * @param qname  строка вида  <code>java.util.List[][]</code>.
     * @param args  дополнительные параметры данного типа.
     */
    public Type(final String qname, final TypeArgument... args) {
        this(qname);
        for (TypeArgument arg : args) {
            addArgument( arg );
        }
    }

    /**
     * @return Полное название java класса включая название пакета.
     */
    public String getQName() {
        return qname;
    }
    /**
     * @return Краткое название java класса.
     */
    public String getName() {
        return name;
    }

    /**
     * @return для типов являющихся описанием массивов указывается их размерность. В остальных случаях возвращает <code>0</code>.
     */
    public int getArrayDimension() {
        return arrayDimension;
    }

    public boolean hasPackage() {
        return hasPackage;
    }

    /**
     * @return <code>true</code> если данный тип является массивом.
     */
    public boolean isArray() {
        return arrayDimension>0;
    }

    /**
     * @return <code>true</code> если данный тип является параметризованным.
     */
    public boolean hasArguments() {
        return args !=null && args.size()>0;
    }

    /**
     * @return Для параметризованных классов возвращает сопутствующую информацию.
     *         Во всех прочих случаях метод вернет пустой итератор.
     */
    public List<TypeArgument> getArguments() {
        return args !=null ? args : Collections.<TypeArgument>emptyList();
    }

    /**
     * Добавляет описание параметра типа.
     * @param arg новый параметр.
     * @return  то же значение что и в аргументе.
     */
    public <T extends TypeArgument> T addArgument(final T arg) {
        if (args==null)
            args = new ArrayList<TypeArgument>(2);
        arg.setParent(this);
        args.add(arg);
        return arg;
    }

    public SimpleTypeArgument addArgument(final Class arg) {
        return addArgument( new SimpleTypeArgument(arg) );
    }

    @Override
    public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(final VoidVisitor<A> v, final A arg) {
        v.visit(this, arg);
    }

    @Override
    public int hashCode() {
        return qname.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj==null || !getClass().equals(obj.getClass()))
            return false;
        final Type other = (Type)obj;
        return qname.equals(other.qname) && arrayDimension==other.arrayDimension;
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(qname);
        for (int i=0; i<arrayDimension; i++) {
            buf.append("[]");
        }
        if (args !=null && !args.isEmpty()) {
            buf.append('<');
            for (Iterator it= args.iterator(); it.hasNext(); ) {
                buf.append(it.next());
                if (it.hasNext())
                    buf.append(',');
            }
            buf.append('>');
        }
        return buf.toString();
    }
}
