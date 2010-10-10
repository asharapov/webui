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

import org.echosoft.framework.ui.core.compiler.ast.expr.ASTExpression;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ASTIfStmt extends ASTVariablesRegistryStmt {

    private ASTExpression condition;
    private ASTStatement thenStmt;
    private ASTStatement elseStmt;

    public ASTIfStmt() {
    }

    public ASTIfStmt(final ASTExpression condition, final ASTStatement thenStmt, final ASTStatement elseStmt) {
        this.condition = condition;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
        if (condition!=null)
            condition.setParent(this);
        if (thenStmt!=null)
            thenStmt.setParent(this);
        if (elseStmt!=null)
            elseStmt.setParent(this);
    }

    public ASTExpression getCondition() {
        return condition;
    }
    public <T extends ASTExpression> T setCondition(final T condition) {
        this.condition = condition;
        if (condition!=null)
            condition.setParent(this);
        return condition;
    }

    public ASTStatement getThenStmt() {
        return thenStmt;
    }
    public <T extends ASTStatement> T setThenStmt(final T thenStmt) {
        this.thenStmt = thenStmt;
        if (thenStmt!=null)
            thenStmt.setParent(this);
        return thenStmt;
    }

    public ASTStatement getElseStmt() {
        return elseStmt;
    }
    public <T extends ASTStatement> T setElseStmt(final T elseStmt) {
        this.elseStmt = elseStmt;
        if (elseStmt!=null)
            elseStmt.setParent(this);
        return elseStmt;
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
