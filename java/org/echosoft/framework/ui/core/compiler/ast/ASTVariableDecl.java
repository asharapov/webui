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

import org.echosoft.framework.ui.core.compiler.ast.expr.ASTExpression;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * Данный класс является частью описания новых локальных переменных и их начальных значений.<br/>
 * Пример: <code> int <b>x</b>,  <b>y[][]</b>,  <b>z=1</b>;</code>
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ASTVariableDecl extends ASTNode {

    private String name;
    private int arrayDimension;
    private ASTExpression initValue;

    public ASTVariableDecl(final String name) {
        this.name = name;
        this.arrayDimension = 0;
    }
    public ASTVariableDecl(final String name, final int arrayDimension) {
        this.name = name;
        this.arrayDimension = arrayDimension;
    }

    public ASTVariableDecl(final String name, final ASTExpression initValue) {
        this.name = name;
        this.initValue = initValue;
        if (initValue!=null)
            initValue.setParent(this);
    }

    public ASTVariableDecl(final String name, final int arrayDimension, final ASTExpression initValue) {
        this.name = name;
        this.arrayDimension = arrayDimension;
        this.initValue = initValue;
        if (initValue!=null)
            initValue.setParent(this);
    }

    /**
     * @return имя переменной.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name новое имя переменной.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return Для переменных являющихся массивами возвращает их размерность.
     *      Во всех прочих случаях метод возвращает <code>0</code>.
     */
    public int getArrayDimension() {
        return arrayDimension;
    }
    /**
     * Для переменных являющихся массивами указывает их размерность.
     * @param arrayDimension  размерность массива или <code>0</code>.
     */
    public void setArrayDimension(final int arrayDimension) {
        this.arrayDimension = arrayDimension;
    }

    /**
     * @return Начальное значение переменной.
     */
    public ASTExpression getInitValue() {
        return initValue;
    }
    /**
     * @param initValue начальное значение переменной.
     */
    public void setInitValue(final ASTExpression initValue) {
        this.initValue = initValue;
        if (initValue!=null)
            initValue.setParent(this);
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
