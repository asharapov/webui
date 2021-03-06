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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.echosoft.framework.ui.core.compiler.ast.ASTNode;
import org.echosoft.framework.ui.core.compiler.ast.comments.ASTComment;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTAnnotationExpr;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;


/**
 * <p>
 * Класс описывает декларацию пакета, которая используется в {@link org.echosoft.framework.ui.core.compiler.ast.CompilationUnit}.
 * </p>
 * Конструкция PackageDeclaration следует синтаксису:<br>
 * <code>
 * <table>
 * <tr valign=baseline>
 *  <pre>
 *  PackageDeclaration ::= ({@link org.echosoft.framework.ui.core.compiler.ast.comments.ASTComment})* ({@link org.echosoft.framework.ui.core.compiler.ast.expr.ASTAnnotationExpr})* ("package" name ";")?
 *  </pre>
 *
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class ASTPackageDecl extends ASTNode {

    private List<ASTComment> comments;
    private List<ASTAnnotationExpr> annotations;
    private String name;

    public ASTPackageDecl(final String name) {
        this.name = name!=null ? name.trim() : "";
    }

    /**
     * @return имя пакета. Для пустого пакета всегда возвращает "".
     */
    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name!=null ? name.trim() : "";
    }

    /**
     * @return Все примечания, расположенные над заголовком пакета. Никогда не возвращает <code>null</code>.
     */
    public Iterable<ASTComment> getComments() {
        return comments!=null ? comments : Collections.<ASTComment>emptyList();
    }

    /**
     * Добавляет примечание к пакету классов.
     * @param comment примечание.
     * @return то же значение что было указано в аргументе метода.
     */
    public <T extends ASTComment> T addComment(final T comment) {
        if (comments==null)
            comments = new ArrayList<ASTComment>(2);
        comment.setParent( this );
        comments.add( comment );
        return comment;
    }

    /**
     * @return список аннотаций, ассоциированных с данным пакетом. Никогда не возвращает <code>null</code>.
     */
    public Iterable<ASTAnnotationExpr> getAnnotations() {
        return annotations!=null ? annotations : Collections.<ASTAnnotationExpr>emptyList();
    }

    /**
     * Добавляет аннотацию к пакету классов.
     * @param annotation добавляемая аннотация.
     * @return то же значение что было указано в аргументе метода.
     */
    public <T extends ASTAnnotationExpr> T addAnnotation(final T annotation) {
        if (annotations==null)
            annotations = new ArrayList<ASTAnnotationExpr>(2);
        annotation.setParent(this);
        annotations.add( annotation );
        return annotation;
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
