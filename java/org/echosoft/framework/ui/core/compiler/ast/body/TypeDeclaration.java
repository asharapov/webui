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

/**
 * Базовый класс для описания классов, интерфейсов, перечислимых типов и аннотаций.
 *
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public abstract class TypeDeclaration extends BodyDeclaration {

    private int modifiers;
    private String name;
    private List<BodyDeclaration> members;

    public TypeDeclaration(final int modifiers, final String name) {
        this.modifiers = modifiers;
        this.name = name;
    }

    /**
     * @return modifiers  определяет перечень флагов, установленных для данного элемента.
     * @see org.echosoft.framework.ui.core.compiler.ast.Mods
     */
    public int getModifiers() {
        return modifiers;
    }
    /**
     * @param modifiers  устанавливает новые значения флагов для данного элемента>
     */
    public void setModifiers(final int modifiers) {
        this.modifiers = modifiers;
    }

    /**
     * @return  Название данного элемента.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name  устанавливает новое название данного элемента.
     */
    public void setName(final String name) {
        this.name = name;
    }


    public boolean hasMembers() {
        return members!=null && members.size()>0;
    }

    /**
     * @return  итератор по содержимому данного класса/интерфейса/ ... Никогда не возвращает <code>null</code>.
     */
    public Iterable<BodyDeclaration> getMembers() {
        return members!=null ? members : Collections.<BodyDeclaration>emptyList();
    }

    /**
     * Добавляет указанный элемент к данному классу (интерфейсу, перечислимому типу, аннотации).
     * @param member  добавляемый элемент.
     * @return то же значение что было указано в аргументе метода.
     */
    public <T extends BodyDeclaration> T addMember(final T member) {
        if (members==null)
            this.members = new ArrayList<BodyDeclaration>(2);
        member.setParent( this );
        members.add( member );
        return member;
    }

}
