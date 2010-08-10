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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class TryStmt extends Statement {

    private BlockStmt tryBlock;
    private List<CatchClause> catchs;
    private BlockStmt finallyBlock;

    public TryStmt() {
        tryBlock = new BlockStmt();
        tryBlock.setParent(this);
    }

    public BlockStmt getTryBlock() {
        return tryBlock;
    }
    public void setTryBlock(final BlockStmt tryBlock) {
        this.tryBlock = tryBlock;
        if (tryBlock!=null)
            tryBlock.setParent(this);
    }

    public Iterable<CatchClause> getCatchs() {
        return catchs!=null ? catchs : Collections.<CatchClause>emptyList();
    }
    public CatchClause addCatch(final CatchClause catchClause) {
        if (catchs==null)
            catchs = new ArrayList<CatchClause>(1);
        catchClause.setParent(this);
        catchs.add( catchClause );
        return catchClause;
    }

    public BlockStmt getFinallyBlock() {
        return finallyBlock;
    }
    public void setFinallyBlock(final BlockStmt finallyBlock) {
        this.finallyBlock = finallyBlock;
        if (finallyBlock!=null)
            finallyBlock.setParent(this);
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
