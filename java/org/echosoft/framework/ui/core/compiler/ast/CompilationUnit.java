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

import org.echosoft.framework.ui.core.compiler.ast.body.ASTPackageDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTTypeDecl;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTBlockStmt;
import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;


/**
 * Содержит описание содержимого компилируемого файла.
 *
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class CompilationUnit extends ASTNode {

    private final ASTPackageDecl pkg;
    private List<ASTTypeDecl> types;
    private File dstFile;
    private ASTBlockStmt mainBlockNode;
    private Variable uictx;

    /**
     * @param pkgName  название пакета в котором находится данный файл. Может быть пустой строкой или <code>null</code>.
     */
    public CompilationUnit(final String pkgName) {
        pkg = new ASTPackageDecl(pkgName);
        pkg.setParent(this);
    }

    /**
     * @return  декларация пакета. Никогда не возвращает <code>null</code>.
     *  Для описания пустого пакета следует установить свойство {@link org.echosoft.framework.ui.core.compiler.ast.body.ASTPackageDecl#setName(String)} в <code>null</code>.
     */
    public ASTPackageDecl getPackage() {
        return pkg;
    }

    /**
     * @return итератор по описаниям всех классов определенным в данном файле. Никода не возвращает <code>null</code>.
     */
    public Iterable<ASTTypeDecl> getTypes() {
        return types!=null ? types : Collections.<ASTTypeDecl>emptyList();
    }

    /**
     * Добавляет описание нового класса в данный файл.
     * @param typeDecl заголовок описания нового класса.
     * @return то же значение что было указано в аргументе метода.
     */
    public <T extends ASTTypeDecl> T addType(final T typeDecl) {
        if (types==null)
            types = new ArrayList<ASTTypeDecl>(2);
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
     * Возвращает ссылку на узел в дереве, под которым начнется создание узлов, соответствующих тегам в .wui файле (т.е. основной его контент).
     * @return  ссылка на инициализированный блок кода с которого начнется генерация основного контента.
     */
    public ASTBlockStmt getMainBlockNode() {
        return mainBlockNode;
    }
    public void setMainBlockNode(final ASTBlockStmt blockNode) {
        this.mainBlockNode = blockNode;
    }

    /**
     * Возвращает ссылку на имя переменной в которой хранится экземпляр объекта {@link org.echosoft.framework.ui.core.UIContext} используемый для инициализации фреймворка.
     * @return  ссылка на переменную.
     */
    public Variable getUIContextVar() {
        return uictx;
    }
    public void setUIContextVar(final Variable uictx) {
        this.uictx = uictx;
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
