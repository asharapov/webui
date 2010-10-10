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

import org.echosoft.framework.ui.core.compiler.ast.ASTNode;
import org.echosoft.framework.ui.core.compiler.ast.ASTParameter;
import org.echosoft.framework.ui.core.compiler.ast.VariablesContainer;
import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ASTCatchClause extends ASTNode implements VariablesContainer {

    private ASTParameter param;
    private ASTBlockStmt catchBlock;

    public ASTCatchClause() {
        this.catchBlock = new ASTBlockStmt();
        this.catchBlock.setParent(this);
    }

    public ASTCatchClause(final ASTParameter param) {
        this();
        this.param = param;
        if (param !=null)
            param.setParent(this);
    }

    public ASTCatchClause(final ASTParameter param, final ASTBlockStmt catchBlock) {
        this.param = param;
        this.catchBlock = catchBlock;
        if (param !=null)
            param.setParent(this);
        if (catchBlock!=null)
            catchBlock.setParent(this);
    }

    public ASTParameter getParam() {
        return param;
    }
    public void setParam(final ASTParameter param) {
        this.param = param;
        if (param !=null)
            param.setParent(this);
    }

    public ASTBlockStmt getCatchBlock() {
        return catchBlock;
    }
    public ASTBlockStmt setCatchBlock(final ASTBlockStmt catchBlock) {
        this.catchBlock = catchBlock;
        if (catchBlock!=null)
            catchBlock.setParent(this);
        return catchBlock;
    }

    @Override
    public boolean containsVariable(final String name, final boolean findInParents) {
        if (param.getName().equals(name))
            return true;
        if (findInParents) {
            for (ASTNode node=this.getParent(); (node instanceof VariablesContainer); node=node.getParent()) {
                final VariablesContainer vh = (VariablesContainer)node;
                if (vh.containsVariable(name, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsVariable(final String name, final RefType type, final boolean findInParents) {
        if (param.getName().equals(name) && param.getType().equals(type))
            return true;
        if (findInParents) {
            for (ASTNode node=this.getParent(); (node instanceof VariablesContainer); node=node.getParent()) {
                final VariablesContainer vh = (VariablesContainer)node;
                if (vh.containsVariable(name, type, false)) {
                    return true;
                }
            }
        }
        return false;
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
