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
package org.echosoft.framework.ui.core.compiler.ast;

import org.echosoft.framework.ui.core.compiler.ast.expr.NameExpr;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * <p>
 * This class represents a import declaration. Imports are optional for the
 * {@link japa.parser.ast.CompilationUnit}.
 * </p>
 * The ImportDeclaration is constructed following the syntax:<br>
 * <code>
 * <table>
 * <tr valign=baseline>
 *   <td align=right>ImportDeclaration</td>
 *   <td align=center>::=</td>
 *   <td align=left>
 *       "import" ( "static" )? {@link japa.parser.ast.expr.NameExpr} ( "." "*" )? ";"
 *   </td>
 * </tr>
 * </table> 
 * </code>
 * 
 * @author Julio Vilmar Gesser
 */
public final class ImportDeclaration extends ASTNode {

    private NameExpr name;
    private boolean statical;
    private boolean entirePackage;

    public ImportDeclaration() {
    }

    public ImportDeclaration(final NameExpr name, final boolean statical, final boolean entirePackage) {
        this.name = name;
        this.statical = statical;
        this.entirePackage = entirePackage;
        if (name!=null)
            name.setParent(this);
    }

    /**
     * Retrieves the name of the import.
     * 
     * @return the name of the import
     */
    public NameExpr getName() {
        return name;
    }

    /**
     * Sets the name this import.
     *
     * @param name the name to set
     */
    public void setName(NameExpr name) {
        this.name = name;
    }

    /**
     * Return if the import ends with "*".
     * 
     * @return <code>true</code> if the import ends with "*", <code>false</code> otherwise
     */
    public boolean isAsterisk() {
        return entirePackage;
    }
    /**
     * Sets if this import is asterisk.
     *
     * @param asterisk <code>true</code> if this import is asterisk
     */
    public void setAsterisk(boolean asterisk) {
        this.entirePackage = asterisk;
    }


    /**
     * Return if the import is static.
     * 
     * @return <code>true</code> if the import is static, <code>false</code> otherwise
     */
    public boolean isStatic() {
        return statical;
    }

    /**
     * Sets if this import is static.
     * 
     * @param statical  <code>true</code> if this import is static
     */
    public void setStatic(boolean statical) {
        this.statical = statical;
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
