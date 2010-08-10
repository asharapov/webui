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

import org.echosoft.framework.ui.core.compiler.ast.expr.Expression;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class AssertStmt extends Statement {

    private Expression check;
    private Expression msg;

    public AssertStmt() {
    }

    public AssertStmt(final Expression check) {
        this.check = check;
        if (check!=null)
            check.setParent(this);
    }

    public AssertStmt(final Expression check, final Expression msg) {
        this.check = check;
        this.msg = msg;
        if (check!=null)
            check.setParent(this);
        if (msg!=null)
            msg.setParent(this);
    }

    public Expression getCheck() {
        return check;
    }
    public <T extends Expression> T setCheck(final T check) {
        this.check = check;
        if (check!=null)
            check.setParent(this);
        return check;
    }

    public Expression getMessage() {
        return msg;
    }
    public <T extends Expression> T setMessage(final T msg) {
        this.msg = msg;
        if (msg!=null)
            msg.setParent(this);
        return msg;
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
