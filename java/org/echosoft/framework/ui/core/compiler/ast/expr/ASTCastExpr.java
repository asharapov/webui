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
package org.echosoft.framework.ui.core.compiler.ast.expr;

import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.ast.type.Type;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ASTCastExpr extends ASTExpression {

    private Type type;
    private ASTExpression expr;

    public ASTCastExpr() {
    }

    public ASTCastExpr(final Type type, final ASTExpression expr) {
        if (type!=null) {
            this.type = type;
            this.type.setParent(this);
        }
        if (expr!=null) {
            this.expr = expr;
            this.expr.setParent(this);
        }
    }

    public ASTCastExpr(final Class type, final ASTExpression expr) {
        if (type!=null) {
            this.type = new RefType(type);
            this.type.setParent(this);
        }
        if (expr!=null) {
            this.expr = expr;
            this.expr.setParent(this);
        }
    }

    public Type getType() {
        return type;
    }
    public void setType(final Type type) {
        this.type = type;
        if (type!=null)
            type.setParent(this);
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

    @Override
    public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(final VoidVisitor<A> v, final A arg) {
        v.visit(this, arg);
    }


}
