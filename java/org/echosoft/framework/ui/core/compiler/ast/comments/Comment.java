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
package org.echosoft.framework.ui.core.compiler.ast.comments;

import org.echosoft.framework.ui.core.compiler.ast.ASTNode;

/**
 * Абстрактный класс для всех AST узлов, описывающих коментарии в тексте программы.
 *
 * @see LineComment
 * @see BlockComment
 * @see JavadocComment
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public abstract class Comment extends ASTNode {

    private String content;

    public Comment(final String content) {
        this.content = content;
    }


    /**
     * @return основной текст коментария.
     */
    public final String getContent() {
        return content;
    }
    /**
     * @param content  основной текст комментария.
     */
    public void setContent(final String content) {
        this.content = content;
    }
}
