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
package org.echosoft.framework.ui.core.compiler.ast.stmt;

import org.echosoft.framework.ui.core.compiler.ast.Parameter;
import org.echosoft.framework.ui.core.compiler.ast.expr.Expression;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ForeachStmt extends VariablesRegistryStmt {

    private Parameter var;
    private Expression iterable;
    private Statement body;

    public ForeachStmt() {
    }

    public ForeachStmt(final Parameter var, final Expression iterable, final Statement body) {
        this.var = var;
        this.iterable = iterable;
        this.body = body;
        if (var!=null)
            var.setParent(this);
        if (iterable!=null)
            iterable.setParent(this);
        if (body!=null)
            body.setParent(this);
    }

    public Parameter getVariable() {
        return var;
    }
    public void setVariable(final Parameter var) {
        this.var = var;
        if (var!=null)
            var.setParent(this);
    }

    public Expression getIterable() {
        return iterable;
    }
    public void setIterable(final Expression iterable) {
        this.iterable = iterable;
        if (iterable!=null)
            iterable.setParent(this);
    }

    public Statement getBody() {
        return body;
    }
    public void setBody(final Statement body) {
        this.body = body;
        if (body!=null)
            body.setParent(this);
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
