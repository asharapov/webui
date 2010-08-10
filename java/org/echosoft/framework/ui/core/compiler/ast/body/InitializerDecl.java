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
package org.echosoft.framework.ui.core.compiler.ast.body;

import org.echosoft.framework.ui.core.compiler.ast.stmt.BlockStmt;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * Описывает блок кода в расположенный вне методов класса.
 * 
 * @author Julio Vilmar Gesser
 * @author Julio Vilmar Gesser
 */
public final class InitializerDecl extends BodyDeclaration {

    private boolean isStatic;
    private BlockStmt block;

    public InitializerDecl(final boolean isStatic) {
        super();
        this.isStatic = isStatic;
        this.block = new BlockStmt();
        block.setParent( this );
    }

    public boolean isStatic() {
        return isStatic;
    }
    public void setStatic(final boolean isStatic) {
        this.isStatic = isStatic;
    }

    public BlockStmt getBlock() {
        return block;
    }
    public void setBlock(final BlockStmt block) {
        this.block = block;
        if (block!=null)
            block.setParent( this );
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
