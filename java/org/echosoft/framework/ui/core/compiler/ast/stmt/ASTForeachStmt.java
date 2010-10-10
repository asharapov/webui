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

import org.echosoft.framework.ui.core.compiler.ast.ASTParameter;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTExpression;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ASTForeachStmt extends ASTVariablesRegistryStmt {

    private ASTParameter var;
    private ASTExpression iterable;
    private ASTStatement body;

    public ASTForeachStmt() {
    }

    public ASTForeachStmt(final ASTParameter var, final ASTExpression iterable, final ASTStatement body) {
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

    public ASTParameter getVariable() {
        return var;
    }
    public void setVariable(final ASTParameter var) {
        this.var = var;
        if (var!=null)
            var.setParent(this);
    }

    public ASTExpression getIterable() {
        return iterable;
    }
    public <T extends ASTExpression> T setIterable(final T iterable) {
        this.iterable = iterable;
        if (iterable!=null)
            iterable.setParent(this);
        return iterable;
    }

    public ASTStatement getBody() {
        return body;
    }
    public <T extends ASTStatement> T setBody(final T body) {
        this.body = body;
        if (body!=null)
            body.setParent(this);
        return body;
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
