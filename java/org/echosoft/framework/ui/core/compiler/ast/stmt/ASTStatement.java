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
import org.echosoft.framework.ui.core.compiler.ast.body.ASTClassDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTMethodDecl;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public abstract class ASTStatement extends ASTNode {

    public ASTMethodDecl findEnclosingMethod() {
        ASTNode node = getParent();
        while ( node!=null && !(node instanceof ASTMethodDecl) ) {
            node = node.getParent();
        }
        return (ASTMethodDecl)node;
    }

    public ASTClassDecl findEnclosingClass() {
        ASTNode node = getParent();
        while ( node!=null && !(node instanceof ASTClassDecl) ) {
            node = node.getParent();
        }
        return (ASTClassDecl)node;
    }

}
