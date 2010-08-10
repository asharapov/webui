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

import java.lang.reflect.Modifier;

/**
 * Class to hold modifiers.<br>
 * The modifier constants declared here holds equivalent values to
 * {@link java.lang.reflect.Modifier} constants.
 */
public final class Mods extends Modifier {

    public static boolean hasModifier(final int modifiers, final int modifier) {
        return (modifiers & modifier) != 0;
    }

    /**
     * Adds the given modifier.
     */
    public static int combine(final int... mods) {
        int result = 0;
        for (int mod : mods) {
            result |= mod;
        }
        return result;
    }

    /**
     * Removes the given modifier.
     */
    public static int removeModifier(final int modifiers, final int mod) {
        return modifiers & ~mod;
    }

    private Mods() {
    }
}