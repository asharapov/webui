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
package org.echosoft.framework.ui.core.compiler.ast.visitors;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

import org.echosoft.common.io.FastStringWriter;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class SourcePrinter {

    private static final int INDENT = 4;
    private static final int MAX_DEFAULT_LEVEL = 25;
    private static final char[] PREFIXES = new char[MAX_DEFAULT_LEVEL * INDENT];
    static {
        Arrays.fill(PREFIXES,' ');
    }

    private int level;
    private boolean indented;
    private final FastStringWriter out;

    public SourcePrinter() {
        level = 0;
        indented = false;
        out = new FastStringWriter(2048);
    }

    public void indent() {
        level++;
    }
    public void unindent() {
        level--;
    }
    private void makeIndent() {
        if (level<MAX_DEFAULT_LEVEL) {
            out.write(PREFIXES,0,level*INDENT);
        } else {
            int lv = level;
            while (lv>MAX_DEFAULT_LEVEL) {
                out.write(PREFIXES);
                lv -= MAX_DEFAULT_LEVEL;
            }
            out.write(PREFIXES,0,lv*INDENT);
        }
    }

    public void print(final String arg) {
        if (!indented) {
            makeIndent();
            indented = true;
        }
        out.write(arg);
    }

    public void print(final char arg) {
        if (!indented) {
            makeIndent();
            indented = true;
        }
        out.write(arg);
    }

    public void printLn(final String arg) {
        print(arg);
        printLn();
    }

    public void printLn() {
        out.write('\n');
        indented = false;
    }

    public void writeOut(final SourcePrinter other) {
        out.writeOut( other.out );
    }

    public void writeOut(final Writer other) throws IOException {
        out.writeOut(other);
    }

    public String getString() {
        return out.toString();
    }

    @Override
    public String toString() {
        return out.toString();
    }
}
