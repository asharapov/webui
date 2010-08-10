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
import org.echosoft.framework.ui.core.compiler.ast.comments.JavadocComment;
import org.echosoft.framework.ui.core.compiler.ast.expr.AnnotationExpr;

/**
 * Базовый класс для всех классов, интерфейсов, перечислимых типов а также их элементов.
 * 
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public abstract class BodyDeclaration extends ASTNode {

    private JavadocComment javaDoc;
    private List<AnnotationExpr> annotations;


    /**
     * @return Комментарий javadoc.
     */
    public JavadocComment getJavaDoc() {
        return javaDoc;
    }
    public void setJavaDoc(final JavadocComment javaDoc) {
        this.javaDoc = javaDoc;
        if (javaDoc!=null)
            javaDoc.setParent( this );
    }

    /**
     * @return итератор по перечню аннотаций, ассоциированных с данным элементом.
     *      Если с данным элементом не ассоциировано ни одной аннотации то метод возвращает пустой итератор.
     */
    public Iterable<AnnotationExpr> getAnnotations() {
        return annotations!=null ? annotations : Collections.<AnnotationExpr>emptyList();
    }

    /**
     * Назначает новую аннотацию для данного элемента.
     * @param annotation  назначаемая аннотация
     * @return то же значение что было указано в аргументе метода.
     */
    public <T extends AnnotationExpr> T addAnnotation(final T annotation) {
        if (annotations==null)
            this.annotations = new ArrayList<AnnotationExpr>(2);
        annotation.setParent( this );
        annotations.add( annotation );
        return annotation;
    }
}
