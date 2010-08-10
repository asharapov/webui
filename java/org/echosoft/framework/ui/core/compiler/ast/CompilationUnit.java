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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.echosoft.framework.ui.core.compiler.ast.body.MethodDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.TypeDeclaration;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;


/**
 * Содержит описание содержимого компилируемого файла.
 *
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class CompilationUnit extends ASTNode {

    private final PackageDecl pkg;
    private List<TypeDeclaration> types;
    private File dstFile;
    private MethodDecl mainMethod;

    /**
     * @param pkgName  название пакета в котором находится данный файл. Может быть пустой строкой или <code>null</code>.
     */
    public CompilationUnit(final String pkgName) {
        pkg = new PackageDecl(pkgName);
        pkg.setParent(this);
    }

    /**
     * @return  декларация пакета. Никогда не возвращает <code>null</code>.
     *  Для описания пустого пакета следует установить свойство {@link PackageDecl#setName(String)} в <code>null</code>.
     */
    public PackageDecl getPackage() {
        return pkg;
    }

    /**
     * @return итератор по описаниям всех классов определенным в данном файле. Никода не возвращает <code>null</code>.
     */
    public Iterable<TypeDeclaration> getTypes() {
        return types!=null ? types : Collections.<TypeDeclaration>emptyList();
    }

    /**
     * Добавляет описание нового класса в данный файл.
     * @param typeDecl заголовок описания нового класса.
     * @return то же значение что было указано в аргументе метода.
     */
    public <T extends TypeDeclaration> T addType(final T typeDecl) {
        if (types==null)
            types = new ArrayList<TypeDeclaration>(2);
        typeDecl.setParent( this );
        types.add( typeDecl );
        return typeDecl;
    }

    /**
     * @return имя файла по умолчанию куда должно быть сериализовано содержимое данного синтаксического дерева.
     */
    public File getDstFile() {
        return dstFile;
    }
    public void setDstFile(final File dstFile) {
        this.dstFile = dstFile;
    }

    /**
     * Возвращает ссылку на нижележащий узел в данном дереве, который соответствует главному методу основного класса.
     * @return  ссылка на метод, с которого начинается вызов данного класса.
     */
    public MethodDecl getMainMethod() {
        return mainMethod;
    }
    public void setMainMethod(final MethodDecl method) {
        this.mainMethod = method;
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
