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
package org.echosoft.framework.ui.core.compiler.ast.expr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ArrayCreationExpr extends Expression {

    private RefType type;
    private int arrayDimension;
    private List<Expression> dimensions;
    private List<Expression> values;

    public ArrayCreationExpr(final RefType type) {
        this.type = type;
        if (type!=null)
            type.setParent(this);
    }

    public RefType getType() {
        return type;
    }
    public void setType(final RefType type) {
        this.type = type;
        if (type!=null)
            type.setParent(this);
    }

    public int getArrayDimension() {
        return arrayDimension;
    }
    public void setArrayDimension(int arrayDimension) {
        this.arrayDimension = arrayDimension;
    }

    public boolean hasDimensions() {
        return dimensions!=null && dimensions.size()>0;
    }

    public Iterable<Expression> getDimensions() {
        return dimensions!=null ? dimensions : Collections.<Expression>emptyList();
    }

    public <T extends Expression> T addDimension(final T expr) {
        if (dimensions==null)
            dimensions = new ArrayList<Expression>(2);
        expr.setParent(this);
        dimensions.add(expr);
        return expr;
    }


    public boolean hasValues() {
        return values!=null && values.size()>0;
    }

    public Iterable<Expression> getValues() {
        return values!=null ? values : Collections.<Expression>emptyList();
    }

    public <T extends Expression> T addValue(final T expr) {
        if (values==null)
            values = new ArrayList<Expression>(2);
        expr.setParent(this);
        values.add(expr);
        return expr;
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
