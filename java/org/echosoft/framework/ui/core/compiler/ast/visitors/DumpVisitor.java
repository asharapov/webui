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

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.echosoft.framework.ui.core.compiler.ast.ASTParameter;
import org.echosoft.framework.ui.core.compiler.ast.ASTVariableDecl;
import org.echosoft.framework.ui.core.compiler.ast.CompilationUnit;
import org.echosoft.framework.ui.core.compiler.ast.Mods;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTAnnotationDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTAnnotationMemberDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTBodyDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTClassDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTConstructorDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTEnumConstantDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTEnumDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTFieldDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTInitializerDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTInterfaceDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTMethodDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTPackageDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTTypeDecl;
import org.echosoft.framework.ui.core.compiler.ast.comments.ASTBlockComment;
import org.echosoft.framework.ui.core.compiler.ast.comments.ASTComment;
import org.echosoft.framework.ui.core.compiler.ast.comments.ASTJavadocComment;
import org.echosoft.framework.ui.core.compiler.ast.comments.ASTLineComment;
import org.echosoft.framework.ui.core.compiler.ast.expr.*;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTAssertStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTBlockStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTBreakStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTCatchClause;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTContinueStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTDoStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTEmptyStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTExplicitConstructorInvocationStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTExpressionStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTForStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTForeachStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTIfStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTLabeledStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTReturnStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTStatement;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTSwitchEntryStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTSwitchStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTSynchronizedStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTThrowStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTTryStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTTypeDeclarationStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTWhileStmt;
import org.echosoft.framework.ui.core.compiler.ast.type.SimpleTypeArgument;
import org.echosoft.framework.ui.core.compiler.ast.type.Type;
import org.echosoft.framework.ui.core.compiler.ast.type.TypeArgument;
import org.echosoft.framework.ui.core.compiler.ast.type.TypeParameter;
import org.echosoft.framework.ui.core.compiler.ast.type.WildcardTypeArgument;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class DumpVisitor implements VoidVisitor<Object> {

    public static final Comparator<Type> CLS_COMPARATOR =
            new Comparator<Type>() {
                public int compare(final Type t1, final Type t2) {
                    return t1.getQName().compareTo(t2.getQName());
                }
            };


    private SourcePrinter printer;
    private final SortedSet<Type> imports;

    public DumpVisitor() {
        this.printer = new SourcePrinter();
        this.imports = new TreeSet<Type>(CLS_COMPARATOR);
    }

    public DumpVisitor(final SourcePrinter printer) {
        this.printer = printer;
        this.imports = new TreeSet<Type>(CLS_COMPARATOR);
    }

    public SourcePrinter getPrinter() {
        return printer;
    }

    /**
     * Регистрирует очередной тип данных, являющийся java классом в секции "import".
     * @param type класс чьи объекты будут использоваться в генерируемом классе.
     * @return  краткая форма записи если класс успешно занесен в секцию "import", в противном случае возвращает строку,
     *      соответствующую полной форме записи имени класса.
     */
    private String ensureTypeImported(final Type type) {
        if (!type.hasPackage())
            return type.getName();
        final Type normType = type.isArray() || type.hasArguments()
                ? new Type(type.getQName())
                : type;
        if (imports.contains(normType))
            return normType.getName();
        for (Type t : imports) {
            if (t.getName().equals(normType.getName()))
                return normType.getQName();
        }
        imports.add( normType );
        return normType.getName();
    }

    private void printModifiers(final int modifiers) {
        if (Mods.isPrivate(modifiers)) {
            printer.print("private ");
        }
        if (Mods.isProtected(modifiers)) {
            printer.print("protected ");
        }
        if (Mods.isPublic(modifiers)) {
            printer.print("public ");
        }
        if (Mods.isAbstract(modifiers)) {
            printer.print("abstract ");
        }
        if (Mods.isStatic(modifiers)) {
            printer.print("static ");
        }
        if (Mods.isFinal(modifiers)) {
            printer.print("final ");
        }
        if (Mods.isNative(modifiers)) {
            printer.print("native ");
        }
//        if (Mods.isStrictfp(modifiers)) {
//            printer.print("strictfp ");
//        }
        if (Mods.isSynchronized(modifiers)) {
            printer.print("synchronized ");
        }
        if (Mods.isTransient(modifiers)) {
            printer.print("transient ");
        }
        if (Mods.isVolatile(modifiers)) {
            printer.print("volatile ");
        }
    }

    private void printMembers(final Iterable<ASTBodyDecl> members, final Object arg) {
        for (ASTBodyDecl member : members) {
            printer.printLn();
            member.accept(this, arg);
            printer.printLn();
        }
    }

    private void printMemberAnnotations(final Iterable<ASTAnnotationExpr> annotations, final Object arg) {
        for (ASTAnnotationExpr a : annotations) {
            a.accept(this, arg);
            printer.printLn();
        }
    }

    private void printAnnotations(final Iterable<ASTAnnotationExpr> annotations, final Object arg) {
        for (ASTAnnotationExpr a : annotations) {
            a.accept(this, arg);
            printer.print(' ');
        }
    }

    private void printTypeParameters(final Iterable<TypeParameter> args, final Object arg) {
        final Iterator<TypeParameter> it = args.iterator();
        if (!it.hasNext())
            return;
        printer.print('<');
        while ( it.hasNext() ) {
            final TypeParameter t = it.next();
            t.accept(this, arg);
            if (it.hasNext()) {
                printer.print(", ");
            }
        }
        printer.print('>');
    }

    private <T extends TypeArgument> void printTypeArgs(final Iterable<T> args, final Object arg) {
        final Iterator<T> it = args.iterator();
        if (!it.hasNext())
            return;
        printer.print('<');
        while ( it.hasNext() ) {
            final TypeArgument targ = it.next();
            targ.accept(this, arg);
            if (it.hasNext()) {
                printer.print(", ");
            }
        }
        printer.print('>');
    }

    private void printArguments(final Iterable<ASTExpression> args, final Object arg) {
        printer.print('(');
        for (Iterator<ASTExpression> it = args.iterator(); it.hasNext();) {
            final ASTExpression e = it.next();
            e.accept(this, arg);
            if (it.hasNext()) {
                printer.print(", ");
            }
        }
        printer.print(')');
    }

    private void printJavadoc(final ASTJavadocComment javadoc, final Object arg) {
        if (javadoc != null) {
            javadoc.accept(this, arg);
        }
    }

    public void visit(final ASTLineComment node, final Object arg) {
        printer.print("//");
        printer.printLn(node.getContent());
    }

    public void visit(final ASTBlockComment node, final Object arg) {
        printer.print("/*");
        printer.print(node.getContent());
        printer.printLn("*/");
    }

    public void visit(final ASTJavadocComment node, final Object arg) {
        printer.print("/**");
        printer.print(node.getContent());
        printer.printLn("*/");
    }



    public void visit(final CompilationUnit node, final Object arg) {
        node.getPackage().accept(this, arg);
        final SourcePrinter originalPrinter = this.printer;

        this.printer = new SourcePrinter();
        for (Iterator<ASTTypeDecl> it = node.getTypes().iterator(); it.hasNext();) {
            it.next().accept(this, arg);
            printer.printLn();
            if (it.hasNext()) {
                printer.printLn();
            }
        }
        originalPrinter.printLn();
        for (Type type : imports) {
            originalPrinter.print("import ");
            originalPrinter.print(type.getQName());
            originalPrinter.printLn(";");
        }
        originalPrinter.printLn();
        this.printer.writeOut(originalPrinter);
        this.printer = originalPrinter;
    }

    public void visit(final ASTPackageDecl node, final Object arg) {
        for (ASTComment comment : node.getComments()) {
            comment.accept(this, arg);
        }
        printAnnotations(node.getAnnotations(), arg);
        printer.print("package ");
        printer.print(node.getName());
        printer.printLn(";");
        printer.printLn();
    }

    public void visit(final Type node, final Object arg) {
        final String name = ensureTypeImported(node);
        printer.print(name);
        printTypeArgs(node.getArguments(), arg);
        for (int i=node.getArrayDimension()-1; i>0; i--) {
            printer.print("[]");
        }
    }

    public void visit(final SimpleTypeArgument node, final Object arg) {
        node.getType().accept(this, arg);
    }

    public void visit(final WildcardTypeArgument node, final Object arg) {
        printer.print('?');
        if (node.getExtends() != null) {
            printer.print(" extends ");
            node.getExtends().accept(this, arg);
        }
        if (node.getSuper() != null) {
            printer.print(" super ");
            node.getSuper().accept(this, arg);
        }
    }

    public void visit(final TypeParameter node, final Object arg) {
        final Iterator<Type> it = node.getTypeBounds().iterator();
        printer.print(node.getName());
        if (it.hasNext()) {
            printer.print(" extends ");
            while (it.hasNext()) {
                final Type type = it.next();
                type.accept(this, arg);
                if (it.hasNext()) {
                    printer.print(" & ");
                }
            }
        }
    }

    public void visit(final ASTVariableDecl node, final Object arg) {
        printer.print(node.getName());
        for (int i=node.getArrayDimension()-1; i>=0; i--) {
            printer.print("[]");
        }
        if (node.getInitValue()!=null) {
            printer.print(" = ");
            node.getInitValue().accept(this, arg);
        }
    }

    public void visit(final ASTParameter node, final Object arg) {
        printAnnotations(node.getAnnotations(), arg);
        printModifiers(node.getModifiers());
        node.getType().accept(this, arg);
        if (node.isVarArgs()) {
            printer.print("...");
        }
        printer.print(' ');
        printer.print(node.getName());
    }


    public void visit(final ASTAnnotationDecl node, final Object arg) {
        printJavadoc(node.getJavaDoc(), arg);
        printMemberAnnotations(node.getAnnotations(), arg);
        printModifiers(node.getModifiers());
        printer.print("@interface ");
        printer.print(node.getName());
        printer.printLn(" {");
        printer.indent();
        if (node.getMembers() != null) {
            printMembers(node.getMembers(), arg);
        }
        printer.unindent();
        printer.print('}');
    }

    public void visit(final ASTAnnotationMemberDecl node, final Object arg) {
        printJavadoc(node.getJavaDoc(), arg);
        printMemberAnnotations(node.getAnnotations(), arg);
        printModifiers(node.getModifiers());
        node.getType().accept(this, arg);
        printer.print(' ');
        printer.print(node.getName());
        printer.print("()");
        if (node.getDefaultValue() != null) {
            printer.print(" default ");
            node.getDefaultValue().accept(this, arg);
        }
        printer.print(';');
    }

    public void visit(final ASTClassDecl node, final Object arg) {
        printJavadoc(node.getJavaDoc(), arg);
        printMemberAnnotations(node.getAnnotations(), arg);
        printModifiers(node.getModifiers());
        printer.print("class ");
        printer.print(node.getName());
        printTypeParameters(node.getTypeParameters(), arg);
        if (node.getSuperType()!=null) {
            printer.print(" extends ");
            node.getSuperType().accept(this, arg);
        }
        final Iterator<Type> it = node.getImplementations().iterator();
        if (it.hasNext()) {
            printer.print(" implements ");
            while (it.hasNext()) {
                final Type type = it.next();
                type.accept(this, arg);
                if (it.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.printLn(" {");
        printer.indent();
        printMembers(node.getMembers(), arg);
        printer.unindent();
        printer.print('}');
    }

    public void visit(final ASTInterfaceDecl node, final Object arg) {
        printJavadoc(node.getJavaDoc(), arg);
        printMemberAnnotations(node.getAnnotations(), arg);
        printModifiers(node.getModifiers());
        printer.print("class ");
        printer.print(node.getName());
        printTypeParameters(node.getTypeParameters(), arg);
        final Iterator<Type> it = node.getSuperTypes().iterator();
        if (it.hasNext()) {
            printer.print(" extends ");
            while (it.hasNext()) {
                final Type type = it.next();
                type.accept(this, arg);
                if (it.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.printLn(" {");
        printer.indent();
        printMembers(node.getMembers(), arg);
        printer.unindent();
        printer.print('}');
    }

    public void visit(final ASTEnumDecl node, final Object arg) {
        printJavadoc(node.getJavaDoc(), arg);
        printMemberAnnotations(node.getAnnotations(), arg);
        printModifiers(node.getModifiers());
        printer.print("enum ");
        printer.print(node.getName());
        final Iterator<Type> impl_it = node.getImplementations().iterator();
        if (impl_it.hasNext()) {
            printer.print(" implements ");
            while (impl_it.hasNext()) {
                final Type type = impl_it.next();
                type.accept(this, arg);
                if (impl_it.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.printLn(" {");
        printer.indent();
        printer.printLn();
        for (Iterator<ASTEnumConstantDecl> it = node.getEntries().iterator(); it.hasNext();) {
            final ASTEnumConstantDecl e = it.next();
            e.accept(this, arg);
            if (it.hasNext()) {
                printer.print(", ");
            }
        }
        if (node.hasMembers()) {
            printer.printLn(";");
            printMembers(node.getMembers(), arg);
        } else {
            if (node.hasEntries()) {
                printer.printLn();
            }
        }
        printer.unindent();
        printer.print('}');
    }

    public void visit(final ASTEnumConstantDecl node, final Object arg) {
        printJavadoc(node.getJavaDoc(), arg);
        printMemberAnnotations(node.getAnnotations(), arg);
        printer.print(node.getName());
        printArguments(node.getArguments(), arg);
        if (node.hasBodyDeclarations()) {
            printer.printLn(" {");
            printer.indent();
            printMembers(node.getBodyDeclarations(), arg);
            printer.unindent();
            printer.printLn("}");
        }
    }

    public void visit(final ASTFieldDecl node, final Object arg) {
        printJavadoc(node.getJavaDoc(), arg);
        printMemberAnnotations(node.getAnnotations(), arg);
        printModifiers(node.getModifiers());
        node.getType().accept(this, arg);
        printer.print(' ');
        for (Iterator<ASTVariableDecl> i = node.getVariables().iterator(); i.hasNext();) {
            final ASTVariableDecl var = i.next();
            var.accept(this, arg);
            if (i.hasNext()) {
                printer.print(", ");
            }
        }
        printer.print(';');
    }

    public void visit(final ASTInitializerDecl node, final Object arg) {
        printJavadoc(node.getJavaDoc(), arg);
        if (node.isStatic()) {
            printer.print("static ");
        }
        node.getBlock().accept(this, arg);
    }

    public void visit(final ASTConstructorDecl node, final Object arg) {
        printJavadoc(node.getJavaDoc(), arg);
        printMemberAnnotations(node.getAnnotations(), arg);
        printModifiers(node.getModifiers());
        printTypeParameters(node.getTypeParameters(), arg);
        if (node.hasTypeParameters()) {
            printer.print(' ');
        }
        printer.print(node.getName());
        printer.print('(');
        for (Iterator<ASTParameter> it=node.getParameters().iterator(); it.hasNext(); ) {
            final ASTParameter p = it.next();
            p.accept(this, arg);
            if (it.hasNext()) {
                printer.print(", ");
            }
        }
        printer.print(')');
        final Iterator<Type> throwables = node.getThrowables().iterator();
        if (throwables.hasNext()) {
            printer.print(" throws ");
            while (throwables.hasNext()) {
                final Type type = throwables.next();
                type.accept(this, arg);
                if (throwables.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.print(' ');
        node.getBody().accept(this, arg);
    }

    public void visit(final ASTMethodDecl node, final Object arg) {
        printJavadoc(node.getJavaDoc(), arg);
        printMemberAnnotations(node.getAnnotations(), arg);
        printModifiers(node.getModifiers());
        printTypeParameters(node.getTypeParameters(), arg);
        if (node.getTypeParameters() != null) {
            printer.print(' ');
        }
        node.getType().accept(this, arg);
        printer.print(' ');
        printer.print(node.getName());
        printer.print('(');
        for (Iterator<ASTParameter> it=node.getParameters().iterator(); it.hasNext(); ) {
            final ASTParameter p = it.next();
            p.accept(this, arg);
            if (it.hasNext()) {
                printer.print(", ");
            }
        }
        printer.print(')');
        final Iterator<Type> throwables = node.getThrowables().iterator();
        if (throwables.hasNext()) {
            printer.print(" throws ");
            while (throwables.hasNext()) {
                final Type type = throwables.next();
                type.accept(this, arg);
                if (throwables.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        if (node.getBody() == null) {
            printer.print(';');
        } else {
            printer.print(' ');
            node.getBody().accept(this, arg);
        }
    }


    public void visit(final ASTBlockStmt node, final Object arg) {
        printer.printLn("{");
        printer.indent();
        for (ASTStatement s : node.getStatements()) {
            s.accept(this, arg);
            printer.printLn();
        }
        printer.unindent();
        printer.print('}');
    }

    public void visit(final ASTSynchronizedStmt node, final Object arg) {
        printer.print("synchronized (");
        node.getExpr().accept(this, arg);
        printer.print(") ");
        node.getBlock().accept(this, arg);
    }

    public void visit(final ASTIfStmt node, final Object arg) {
        printer.print("if (");
        node.getCondition().accept(this, arg);
        printer.print(") ");
        node.getThenStmt().accept(this, arg);
        if (node.getElseStmt() != null) {
            printer.print(" else ");
            node.getElseStmt().accept(this, arg);
        }
    }

    public void visit(final ASTForStmt node, final Object arg) {
        printer.print("for (");
        for (Iterator<ASTExpression> it = node.getInit().iterator(); it.hasNext();) {
            final ASTExpression expr = it.next();
            expr.accept(this, arg);
            if (it.hasNext()) {
                printer.print(", ");
            }
        }
        printer.print("; ");
        if (node.getCompare() != null) {
            node.getCompare().accept(this, arg);
        }
        printer.print("; ");
        for (Iterator<ASTExpression> it = node.getUpdate().iterator(); it.hasNext();) {
            final ASTExpression expr = it.next();
            expr.accept(this, arg);
            if (it.hasNext()) {
                printer.print(", ");
            }
        }
        printer.print(") ");
        node.getBody().accept(this, arg);
    }

    public void visit(final ASTForeachStmt node, final Object arg) {
        printer.print("for (");
        node.getVariable().accept(this, arg);
        printer.print(" : ");
        node.getIterable().accept(this, arg);
        printer.print(") ");
        node.getBody().accept(this, arg);
    }

    public void visit(final ASTWhileStmt node, final Object arg) {
        printer.print("while (");
        node.getCondition().accept(this, arg);
        printer.print(") ");
        node.getBody().accept(this, arg);
    }

    public void visit(final ASTDoStmt node, final Object arg) {
        printer.print("do ");
        node.getBody().accept(this, arg);
        printer.print(" while (");
        node.getCondition().accept(this, arg);
        printer.print(");");
    }

    public void visit(final ASTSwitchStmt node, final Object arg) {
        printer.print("switch(");
        node.getSelector().accept(this, arg);
        printer.printLn(") {");
        printer.indent();
        for (ASTSwitchEntryStmt entry : node.getEntries()) {
            entry.accept(this, arg);
        }
        printer.unindent();
        printer.print('}');
    }

    public void visit(final ASTSwitchEntryStmt node, final Object arg) {
        if (node.getLabel() != null) {
            printer.print("case ");
            node.getLabel().accept(this, arg);
            printer.print(" : ");
        } else {
            printer.print("default : ");
        }
        printer.printLn();
        printer.indent();
        for (ASTStatement stmt : node.getStatements()) {
            stmt.accept(this, arg);
            printer.printLn();
        }
        printer.unindent();
    }

    public void visit(final ASTTryStmt node, final Object arg) {
        printer.print("try ");
        node.getTryBlock().accept(this, arg);
        for (ASTCatchClause cc : node.getCatchs()) {
            cc.accept(this, arg);
        }
        if (node.getFinallyBlock() != null) {
            printer.print(" finally ");
            node.getFinallyBlock().accept(this, arg);
        }
    }

    public void visit(final ASTCatchClause node, final Object arg) {
        printer.print(" catch (");
        node.getParam().accept(this, arg);
        printer.print(") ");
        node.getCatchBlock().accept(this, arg);
    }

    public void visit(final ASTAssertStmt node, final Object arg) {
        printer.print("assert ");
        node.getCheck().accept(this, arg);
        if (node.getMessage() != null) {
            printer.print(" : ");
            node.getMessage().accept(this, arg);
        }
        printer.print(';');
    }

    public void visit(final ASTBreakStmt node, final Object arg) {
        printer.print("break");
        if (node.getId() != null) {
            printer.print(' ');
            printer.print(node.getId());
        }
        printer.print(';');
    }

    public void visit(final ASTContinueStmt node, final Object arg) {
        printer.print("continue");
        if (node.getId() != null) {
            printer.print(' ');
            printer.print(node.getId());
        }
        printer.print(';');
    }

    public void visit(final ASTReturnStmt node, final Object arg) {
        printer.print("return");
        if (node.getExpr() != null) {
            printer.print(' ');
            node.getExpr().accept(this, arg);
        }
        printer.print(';');
    }

    public void visit(final ASTThrowStmt node, final Object arg) {
        printer.print("throw ");
        node.getExpr().accept(this, arg);
        printer.print(';');
    }

    public void visit(final ASTExplicitConstructorInvocationStmt node, final Object arg) {
        if (node.isThis()) {
            printer.print("this");
        } else {
            if (node.getClassExpr() != null) {
                node.getClassExpr().accept(this, arg);
                printer.print('.');
            }
            printer.print("super");
        }
        printArguments(node.getArguments(), arg);
        printer.print(';');
    }

    public void visit(final ASTEmptyStmt node, final Object arg) {
        printer.print(';');
    }

    public void visit(final ASTExpressionStmt node, final Object arg) {
        node.getExpression().accept(this, arg);
        printer.print(';');
    }

    public void visit(final ASTLabeledStmt node, final Object arg) {
        printer.print(node.getLabel());
        printer.print(": ");
        node.getStmt().accept(this, arg);
    }

    public void visit(final ASTTypeDeclarationStmt node, final Object arg) {
        node.getTypeDeclaration().accept(this, arg);
    }




    public void visit(final ASTMarkerAnnotationExpr node, Object arg) {
        printer.print('@');
        printer.print(node.getName());
    }

    public void visit(final ASTSingleMemberAnnotationExpr node, final Object arg) {
        printer.print('@');
        printer.print(node.getName());
        printer.print('(');
        node.getMemberValue().accept(this, arg);
        printer.print(')');
    }

    public void visit(final ASTNormalAnnotationExpr node, final Object arg) {
        printer.print('@');
        printer.print(node.getName());
        printer.print('(');
        for (Iterator<String> it=node.getParams().keySet().iterator(); it.hasNext(); ) {
            final String name = it.next();
            final ASTExpression value = node.getParams().get(name);
            printer.print(name);
            printer.print(" = ");
            value.accept(this, arg);
            if (it.hasNext()) {
                printer.print(", ");
            }
        }
        printer.print(')');
    }

    public void visit(final ASTAssignExpr node, final Object arg) {
        node.getTarget().accept(this, arg);
        printer.print(' ');
        printer.print( node.getOperator().getCode() );
        printer.print(' ');
        node.getValue().accept(this, arg);
    }

    public void visit(final ASTVariableDeclExpr node, final Object arg) {
        printAnnotations(node.getAnnotations(), arg);
        printModifiers(node.getModifiers());
        node.getType().accept(this, arg);
        printer.print(' ');
        for (Iterator<ASTVariableDecl> i = node.getVariables().iterator(); i.hasNext();) {
            final ASTVariableDecl var = i.next();
            var.accept(this, arg);
            if (i.hasNext()) {
                printer.print(", ");
            }
        }
    }

    public void visit(final ASTObjectCreationExpr node, final Object arg) {
        if (node.getScope() != null) {
            node.getScope().accept(this, arg);
            printer.print('.');
        }
        printer.print("new ");
        node.getType().accept(this, arg);
        printArguments(node.getArguments(), arg);
        if (node.hasAnonymousClassItems()) {
            printer.printLn(" {");
            printer.indent();
            printMembers(node.getAnonymousClassItems(), arg);
            printer.unindent();
            printer.print('}');
        }
    }

    public void visit(final ASTUnaryExpr node, final Object arg) {
        switch (node.getOperator()) {
            case POSITIVE:
                printer.print('+');
                node.getExpr().accept(this, arg);
                break;
            case NEGATIVE:
                printer.print('-');
                node.getExpr().accept(this, arg);
                break;
            case INVERSE:
                printer.print('~');
                node.getExpr().accept(this, arg);
                break;
            case NOT:
                printer.print('!');
                node.getExpr().accept(this, arg);
                break;
            case PRE_INCREMENT:
                printer.print("++");
                node.getExpr().accept(this, arg);
                break;
            case PRE_DECREMENT:
                printer.print("--");
                node.getExpr().accept(this, arg);
                break;
            case POST_INCREMENT:
                node.getExpr().accept(this, arg);
                printer.print("++");
                break;
            case POST_DECREMENT:
                node.getExpr().accept(this, arg);
                printer.print("--");
                break;
        }
    }

    public void visit(final ASTBinaryExpr node, final Object arg) {
        node.getLeft().accept(this, arg);
        printer.print(' ');
        printer.print( node.getOperator().getCode() );
        printer.print(' ');
        node.getRight().accept(this, arg);
    }

    public void visit(final ASTConditionalExpr node, final Object arg) {
        node.getCondition().accept(this, arg);
        printer.print(" ? ");
        node.getThenExpr().accept(this, arg);
        printer.print(" : ");
        node.getElseExpr().accept(this, arg);
    }

    public void visit(final ASTArrayAccessExpr node, final Object arg) {
        node.getName().accept(this, arg);
        printer.print('[');
        node.getIndex().accept(this, arg);
        printer.print(']');
    }

    public void visit(final ASTArrayCreationExpr node, final Object arg) {
        printer.print("new ");
        node.getType().accept(this, arg);

        if (node.hasDimensions()) {
            for (ASTExpression dim : node.getDimensions()) {
                printer.print('[');
                dim.accept(this, arg);
                printer.print(']');
            }
        }
        for (int i = node.getArrayDimension(); i>0; i--) {
            printer.print("[]");
        }
        if (node.hasValues()) {
            printer.print(" {");
            if (node.getValues() != null) {
                printer.print(' ');
                for (Iterator<ASTExpression> i = node.getValues().iterator(); i.hasNext();) {
                    ASTExpression expr = i.next();
                    expr.accept(this, arg);
                    if (i.hasNext()) {
                        printer.print(", ");
                    }
                }
                printer.print(' ');
            }
            printer.print('}');
        }
    }


    public void visit(final ASTFieldAccessExpr node, final Object arg) {
        node.getScope().accept(this, arg);
        printer.print('.');
        printer.print(node.getField());
    }

    public void visit(final ASTMethodCallExpr node, final Object arg) {
        if (node.getScopeExpr() != null) {
            node.getScopeExpr().accept(this, arg);
            printer.print('.');
        } else
        if (node.getScopeType() != null) {
            node.getScopeType().accept(this, arg);
            printer.print('.');
        }
        printTypeArgs(node.getTypeArguments(), arg);
        printer.print(node.getName());
        printArguments(node.getArguments(), arg);
    }

    public void visit(final ASTCastExpr node, final Object arg) {
        printer.print('(');
        node.getType().accept(this, arg);
        printer.print(") ");
        node.getExpr().accept(this, arg);
    }

    public void visit(final ASTInstanceOfExpr node, final Object arg) {
        node.getExpression().accept(this, arg);
        printer.print(" instanceof ");
        node.getType().accept(this, arg);
    }

    public void visit(final ASTEnclosedExpr node, final Object arg) {
        printer.print('(');
        node.getInner().accept(this, arg);
        printer.print(')');
    }

    public void visit(final ASTClassExpr node, final Object arg) {
        node.getType().accept(this, arg);
        printer.print(".class");
    }

    public void visit(final ASTThisExpr node, final Object arg) {
        if (node.getType() != null) {
            node.getType().accept(this, arg);
            printer.print('.');
        }
        printer.print("this");
    }

    public void visit(final ASTSuperExpr node, final Object arg) {
        if (node.getType() != null) {
            node.getType().accept(this, arg);
            printer.print('.');
        }
        printer.print("super");
    }

    public void visit(final ASTNameExpr node, final Object arg) {
        printer.print(node.getName());
    }

    public void visit(final ASTQualifiedNameExpr node, final Object arg) {
        node.getQualifier().accept(this, arg);
        printer.print('.');
        printer.print(node.getName());
    }

    public void visit(final ASTNullLiteralExpr node, final Object arg) {
        printer.print("null");
    }

    public void visit(final ASTStringLiteralExpr node, final Object arg) {
        printer.print("\"");
        printer.print(node.getValue());
        printer.print("\"");
    }

    public void visit(final ASTCharLiteralExpr node, final Object arg) {
        printer.print("'");
        printer.print( String.valueOf(node.getValue()) );
        printer.print("'");
    }

    public void visit(final ASTBooleanLiteralExpr node, final Object arg) {
        printer.print( String.valueOf(node.getValue()) );
    }

    public void visit(final ASTIntegerLiteralExpr node, final Object arg) {
        printer.print( node.getValue() );
    }

    public void visit(final ASTLongLiteralExpr node, final Object arg) {
        printer.print( node.getValue() );
    }

    public void visit(final ASTDoubleLiteralExpr node, final Object arg) {
        printer.print( node.getValue() );
    }

    public void visit(final ASTRawExpr node, final Object arg) {
        printer.print(node.getText());
    }

}
