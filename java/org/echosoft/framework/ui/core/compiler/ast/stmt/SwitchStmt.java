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

import org.echosoft.framework.ui.core.compiler.ast.expr.Expression;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class SwitchStmt extends Statement {

    private Expression selector;
    private List<SwitchEntryStmt> entries;

    public SwitchStmt() {
    }

    public SwitchStmt(final Expression selector) {
        this.selector = selector;
        if (selector!=null)
            selector.setParent(this);
    }

    public Expression getSelector() {
        return selector;
    }
    public void setSelector(final Expression selector) {
        this.selector = selector;
        if (selector!=null)
            selector.setParent(this);
    }

    public Iterable<SwitchEntryStmt> getEntries() {
        return entries!=null ? entries : Collections.<SwitchEntryStmt>emptyList();
    }

    public SwitchEntryStmt addEntry(final SwitchEntryStmt entry) {
        if (entries==null)
            entries = new ArrayList<SwitchEntryStmt>(5);
        entry.setParent(this);
        entries.add(entry);
        return entry;
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
