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
public final class ASTSynchronizedStmt extends ASTStatement {

    private ASTExpression expr;
    private ASTBlockStmt block;

    public ASTSynchronizedStmt() {
        this.block = new ASTBlockStmt();
        block.setParent(this);
    }

    public ASTSynchronizedStmt(final ASTExpression expr) {
        this();
        this.expr = expr;
        if (expr!=null)
            expr.setParent(this);
    }

    public ASTSynchronizedStmt(final ASTExpression expr, final ASTBlockStmt block) {
        this.expr = expr;
        this.block = block;
    }

    public ASTExpression getExpr() {
        return expr;
    }
    public <T extends ASTExpression> T setExpr(final T expr) {
        this.expr = expr;
        if (expr!=null)
            expr.setParent(this);
        return expr;
    }

    public ASTBlockStmt getBlock() {
        return block;
    }
    public ASTBlockStmt setBlock(final ASTBlockStmt block) {
        this.block = block;
        if (block!=null)
            block.setParent(this);
        return block;
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
