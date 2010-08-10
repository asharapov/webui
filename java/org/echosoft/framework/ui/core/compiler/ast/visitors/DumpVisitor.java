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

import org.echosoft.framework.ui.core.compiler.ast.CompilationUnit;
import org.echosoft.framework.ui.core.compiler.ast.Mods;
import org.echosoft.framework.ui.core.compiler.ast.PackageDecl;
import org.echosoft.framework.ui.core.compiler.ast.Parameter;
import org.echosoft.framework.ui.core.compiler.ast.VariableDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.AnnotationDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.AnnotationMemberDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.BodyDeclaration;
import org.echosoft.framework.ui.core.compiler.ast.body.ClassDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ConstructorDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.EnumConstantDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.EnumDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.FieldDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.InitializerDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.InterfaceDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.MethodDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.TypeDeclaration;
import org.echosoft.framework.ui.core.compiler.ast.comments.BlockComment;
import org.echosoft.framework.ui.core.compiler.ast.comments.Comment;
import org.echosoft.framework.ui.core.compiler.ast.comments.JavadocComment;
import org.echosoft.framework.ui.core.compiler.ast.comments.LineComment;
import org.echosoft.framework.ui.core.compiler.ast.expr.*;
import org.echosoft.framework.ui.core.compiler.ast.stmt.AssertStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.BlockStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.BreakStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.CatchClause;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ContinueStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.DoStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.EmptyStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ExplicitConstructorInvocationStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ExpressionStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ForStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ForeachStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.IfStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.LabeledStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ReturnStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.Statement;
import org.echosoft.framework.ui.core.compiler.ast.stmt.SwitchEntryStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.SwitchStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.SynchronizedStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ThrowStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.TryStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.TypeDeclarationStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.WhileStmt;
import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.ast.type.Type;
import org.echosoft.framework.ui.core.compiler.ast.type.TypeParameter;
import org.echosoft.framework.ui.core.compiler.ast.type.WildcardType;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public final class DumpVisitor implements VoidVisitor<Object> {

    public static final Comparator<RefType> CLS_COMPARATOR =
            new Comparator<RefType>() {
                public int compare(final RefType t1, final RefType t2) {
                    return t1.getQName().compareTo(t2.getQName());
                }
            };


    private SourcePrinter printer;
    private final SortedSet<RefType> imports;

    public DumpVisitor() {
        this.printer = new SourcePrinter();
        this.imports = new TreeSet<RefType>(CLS_COMPARATOR);
    }

    public DumpVisitor(final SourcePrinter printer) {
        this.printer = printer;
        this.imports = new TreeSet<RefType>(CLS_COMPARATOR);
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
    private String ensureTypeImported(final RefType type) {
        if (!type.hasPackage())
            return type.getName();
        final RefType normType = type.isArray() || type.hasTypeArguments()
                ? new RefType(type.getQName())
                : type;
        if (imports.contains(normType))
            return normType.getName();
        for (RefType t : imports) {
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

    private void printMembers(final Iterable<BodyDeclaration> members, final Object arg) {
        for (BodyDeclaration member : members) {
            printer.printLn();
            member.accept(this, arg);
            printer.printLn();
        }
    }

    private void printMemberAnnotations(final Iterable<AnnotationExpr> annotations, final Object arg) {
        for (AnnotationExpr a : annotations) {
            a.accept(this, arg);
            printer.printLn();
        }
    }

    private void printAnnotations(final Iterable<AnnotationExpr> annotations, final Object arg) {
        for (AnnotationExpr a : annotations) {
            a.accept(this, arg);
            printer.print(" ");
        }
    }

    private void printTypeParameters(final Iterable<TypeParameter> args, final Object arg) {
        final Iterator<TypeParameter> it = args.iterator();
        if (!it.hasNext())
            return;
        printer.print("<");
        while ( it.hasNext() ) {
            final TypeParameter t = it.next();
            t.accept(this, arg);
            if (it.hasNext()) {
                printer.print(", ");
            }
        }
        printer.print(">");
    }

    private void printTypeArgs(final Iterable<Type> args, final Object arg) {
        final Iterator<Type> it = args.iterator();
        if (!it.hasNext())
            return;
        printer.print("<");
        while ( it.hasNext() ) {
            final Type t = it.next();
            t.accept(this, arg);
            if (it.hasNext()) {
                printer.print(", ");
            }
        }
        printer.print(">");
    }

    private void printArguments(final Iterable<Expression> args, final Object arg) {
        printer.print("(");
        for (Iterator<Expression> it = args.iterator(); it.hasNext();) {
            final Expression e = it.next();
            e.accept(this, arg);
            if (it.hasNext()) {
                printer.print(", ");
            }
        }
        printer.print(")");
    }

    private void printJavadoc(final JavadocComment javadoc, final Object arg) {
        if (javadoc != null) {
            javadoc.accept(this, arg);
        }
    }

    public void visit(final LineComment node, final Object arg) {
        printer.print("//");
        printer.printLn(node.getContent());
    }

    public void visit(final BlockComment node, final Object arg) {
        printer.print("/*");
        printer.print(node.getContent());
        printer.printLn("*/");
    }

    public void visit(final JavadocComment node, final Object arg) {
        printer.print("/**");
        printer.print(node.getContent());
        printer.printLn("*/");
    }



    public void visit(final CompilationUnit node, final Object arg) {
        node.getPackage().accept(this, arg);
        final SourcePrinter originalPrinter = this.printer;

        this.printer = new SourcePrinter();
        for (Iterator<TypeDeclaration> it = node.getTypes().iterator(); it.hasNext();) {
            it.next().accept(this, arg);
            printer.printLn();
            if (it.hasNext()) {
                printer.printLn();
            }
        }
        originalPrinter.printLn();
        for (RefType type : imports) {
            originalPrinter.print("import ");
            originalPrinter.print(type.getQName());
            originalPrinter.printLn(";");
        }
        originalPrinter.printLn();
        this.printer.writeOut(originalPrinter);
        this.printer = originalPrinter;
    }

    public void visit(final PackageDecl node, final Object arg) {
        for (Comment comment : node.getComments()) {
            comment.accept(this, arg);
        }
        printAnnotations(node.getAnnotations(), arg);
        printer.print("package ");
        printer.print(node.getName());
        printer.printLn(";");
        printer.printLn();
    }

    public void visit(final RefType node, final Object arg) {
        final String name = ensureTypeImported(node);
        printer.print(name);
        printTypeArgs(node.getTypeArguments(), arg);
        for (int i=node.getArrayDimension()-1; i>0; i--) {
            printer.print("[]");
        }
    }

    public void visit(final WildcardType node, final Object arg) {
        printer.print("?");
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
        final Iterator<RefType> it = node.getTypeBounds().iterator();
        printer.print(node.getName());
        if (it.hasNext()) {
            printer.print(" extends ");
            while (it.hasNext()) {
                final RefType type = it.next();
                type.accept(this, arg);
                if (it.hasNext()) {
                    printer.print(" & ");
                }
            }
        }
    }

    public void visit(final VariableDecl node, final Object arg) {
        printer.print(node.getName());
        for (int i=node.getArrayDimension()-1; i>=0; i--) {
            printer.print("[]");
        }
        if (node.getInitValue()!=null) {
            printer.print(" = ");
            node.getInitValue().accept(this, arg);
        }
    }

    public void visit(final Parameter node, final Object arg) {
        printAnnotations(node.getAnnotations(), arg);
        printModifiers(node.getModifiers());
        node.getType().accept(this, arg);
        if (node.isVarArgs()) {
            printer.print("...");
        }
        printer.print(" ");
        printer.print(node.getName());
    }


    public void visit(final AnnotationDecl node, final Object arg) {
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
        printer.print("}");
    }

    public void visit(final AnnotationMemberDecl node, final Object arg) {
        printJavadoc(node.getJavaDoc(), arg);
        printMemberAnnotations(node.getAnnotations(), arg);
        printModifiers(node.getModifiers());
        node.getType().accept(this, arg);
        printer.print(" ");
        printer.print(node.getName());
        printer.print("()");
        if (node.getDefaultValue() != null) {
            printer.print(" default ");
            node.getDefaultValue().accept(this, arg);
        }
        printer.print(";");
    }

    public void visit(final ClassDecl node, final Object arg) {
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
        final Iterator<RefType> it = node.getImplementations().iterator();
        if (it.hasNext()) {
            printer.print(" implements ");
            while (it.hasNext()) {
                final RefType type = it.next();
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
        printer.print("}");
    }

    public void visit(final InterfaceDecl node, final Object arg) {
        printJavadoc(node.getJavaDoc(), arg);
        printMemberAnnotations(node.getAnnotations(), arg);
        printModifiers(node.getModifiers());
        printer.print("class ");
        printer.print(node.getName());
        printTypeParameters(node.getTypeParameters(), arg);
        final Iterator<RefType> it = node.getSuperTypes().iterator();
        if (it.hasNext()) {
            printer.print(" extends ");
            while (it.hasNext()) {
                final RefType type = it.next();
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
        printer.print("}");
    }

    public void visit(final EnumDecl node, final Object arg) {
        printJavadoc(node.getJavaDoc(), arg);
        printMemberAnnotations(node.getAnnotations(), arg);
        printModifiers(node.getModifiers());
        printer.print("enum ");
        printer.print(node.getName());
        final Iterator<RefType> impl_it = node.getImplementations().iterator();
        if (impl_it.hasNext()) {
            printer.print(" implements ");
            while (impl_it.hasNext()) {
                final RefType type = impl_it.next();
                type.accept(this, arg);
                if (impl_it.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.printLn(" {");
        printer.indent();
        printer.printLn();
        for (Iterator<EnumConstantDecl> it = node.getEntries().iterator(); it.hasNext();) {
            final EnumConstantDecl e = it.next();
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
        printer.print("}");
    }

    public void visit(final EnumConstantDecl node, final Object arg) {
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

    public void visit(final FieldDecl node, final Object arg) {
        printJavadoc(node.getJavaDoc(), arg);
        printMemberAnnotations(node.getAnnotations(), arg);
        printModifiers(node.getModifiers());
        node.getType().accept(this, arg);
        printer.print(" ");
        for (Iterator<VariableDecl> i = node.getVariables().iterator(); i.hasNext();) {
            final VariableDecl var = i.next();
            var.accept(this, arg);
            if (i.hasNext()) {
                printer.print(", ");
            }
        }
        printer.print(";");
    }

    public void visit(final InitializerDecl node, final Object arg) {
        printJavadoc(node.getJavaDoc(), arg);
        if (node.isStatic()) {
            printer.print("static ");
        }
        node.getBlock().accept(this, arg);
    }

    public void visit(final ConstructorDecl node, final Object arg) {
        printJavadoc(node.getJavaDoc(), arg);
        printMemberAnnotations(node.getAnnotations(), arg);
        printModifiers(node.getModifiers());
        printTypeParameters(node.getTypeParameters(), arg);
        if (node.hasTypeParameters()) {
            printer.print(" ");
        }
        printer.print(node.getName());
        printer.print("(");
        for (Iterator<Parameter> it=node.getParameters().iterator(); it.hasNext(); ) {
            final Parameter p = it.next();
            p.accept(this, arg);
            if (it.hasNext()) {
                printer.print(", ");
            }
        }
        printer.print(")");
        final Iterator<RefType> throwables = node.getThrowables().iterator();
        if (throwables.hasNext()) {
            printer.print(" throws ");
            while (throwables.hasNext()) {
                final RefType type = throwables.next();
                type.accept(this, arg);
                if (throwables.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.print(" ");
        node.getBody().accept(this, arg);
    }

    public void visit(final MethodDecl node, final Object arg) {
        printJavadoc(node.getJavaDoc(), arg);
        printMemberAnnotations(node.getAnnotations(), arg);
        printModifiers(node.getModifiers());
        printTypeParameters(node.getTypeParameters(), arg);
        if (node.getTypeParameters() != null) {
            printer.print(" ");
        }
        node.getType().accept(this, arg);
        printer.print(" ");
        printer.print(node.getName());
        printer.print("(");
        for (Iterator<Parameter> it=node.getParameters().iterator(); it.hasNext(); ) {
            final Parameter p = it.next();
            p.accept(this, arg);
            if (it.hasNext()) {
                printer.print(", ");
            }
        }
        printer.print(")");
        final Iterator<RefType> throwables = node.getThrowables().iterator();
        if (throwables.hasNext()) {
            printer.print(" throws ");
            while (throwables.hasNext()) {
                final RefType type = throwables.next();
                type.accept(this, arg);
                if (throwables.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        if (node.getBody() == null) {
            printer.print(";");
        } else {
            printer.print(" ");
            node.getBody().accept(this, arg);
        }
    }


    public void visit(final BlockStmt node, final Object arg) {
        printer.printLn("{");
        printer.indent();
        for (Statement s : node.getStmts()) {
            s.accept(this, arg);
            printer.printLn();
        }
        printer.unindent();
        printer.print("}");
    }

    public void visit(final SynchronizedStmt node, final Object arg) {
        printer.print("synchronized (");
        node.getExpr().accept(this, arg);
        printer.print(") ");
        node.getBlock().accept(this, arg);
    }

    public void visit(final IfStmt node, final Object arg) {
        printer.print("if (");
        node.getCondition().accept(this, arg);
        printer.print(") ");
        node.getThenStmt().accept(this, arg);
        if (node.getElseStmt() != null) {
            printer.print(" else ");
            node.getElseStmt().accept(this, arg);
        }
    }

    public void visit(final ForStmt node, final Object arg) {
        printer.print("for (");
        for (Iterator<Expression> it = node.getInit().iterator(); it.hasNext();) {
            final Expression expr = it.next();
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
        for (Iterator<Expression> it = node.getUpdate().iterator(); it.hasNext();) {
            final Expression expr = it.next();
            expr.accept(this, arg);
            if (it.hasNext()) {
                printer.print(", ");
            }
        }
        printer.print(") ");
        node.getBody().accept(this, arg);
    }

    public void visit(final ForeachStmt node, final Object arg) {
        printer.print("for (");
        node.getVariable().accept(this, arg);
        printer.print(" : ");
        node.getIterable().accept(this, arg);
        printer.print(") ");
        node.getBody().accept(this, arg);
    }

    public void visit(final WhileStmt node, final Object arg) {
        printer.print("while (");
        node.getCondition().accept(this, arg);
        printer.print(") ");
        node.getBody().accept(this, arg);
    }

    public void visit(final DoStmt node, final Object arg) {
        printer.print("do ");
        node.getBody().accept(this, arg);
        printer.print(" while (");
        node.getCondition().accept(this, arg);
        printer.print(");");
    }

    public void visit(final SwitchStmt node, final Object arg) {
        printer.print("switch(");
        node.getSelector().accept(this, arg);
        printer.printLn(") {");
        printer.indent();
        for (SwitchEntryStmt entry : node.getEntries()) {
            entry.accept(this, arg);
        }
        printer.unindent();
        printer.print("}");
    }

    public void visit(final SwitchEntryStmt node, final Object arg) {
        if (node.getLabel() != null) {
            printer.print("case ");
            node.getLabel().accept(this, arg);
            printer.print(" : ");
        } else {
            printer.print("default : ");
        }
        printer.printLn();
        printer.indent();
        for (Statement stmt : node.getStatements()) {
            stmt.accept(this, arg);
            printer.printLn();
        }
        printer.unindent();
    }

    public void visit(final TryStmt node, final Object arg) {
        printer.print("try ");
        node.getTryBlock().accept(this, arg);
        for (CatchClause cc : node.getCatchs()) {
            cc.accept(this, arg);
        }
        if (node.getFinallyBlock() != null) {
            printer.print(" finally ");
            node.getFinallyBlock().accept(this, arg);
        }
    }

    public void visit(final CatchClause node, final Object arg) {
        printer.print(" catch (");
        node.getParam().accept(this, arg);
        printer.print(") ");
        node.getCatchBlock().accept(this, arg);
    }

    public void visit(final AssertStmt node, final Object arg) {
        printer.print("assert ");
        node.getCheck().accept(this, arg);
        if (node.getMessage() != null) {
            printer.print(" : ");
            node.getMessage().accept(this, arg);
        }
        printer.print(";");
    }

    public void visit(final BreakStmt node, final Object arg) {
        printer.print("break");
        if (node.getId() != null) {
            printer.print(" ");
            printer.print(node.getId());
        }
        printer.print(";");
    }

    public void visit(final ContinueStmt node, final Object arg) {
        printer.print("continue");
        if (node.getId() != null) {
            printer.print(" ");
            printer.print(node.getId());
        }
        printer.print(";");
    }

    public void visit(final ReturnStmt node, final Object arg) {
        printer.print("return");
        if (node.getExpr() != null) {
            printer.print(" ");
            node.getExpr().accept(this, arg);
        }
        printer.print(";");
    }

    public void visit(final ThrowStmt node, final Object arg) {
        printer.print("throw ");
        node.getExpr().accept(this, arg);
        printer.print(";");
    }

    public void visit(final ExplicitConstructorInvocationStmt node, final Object arg) {
        if (node.isThis()) {
            printer.print("this");
        } else {
            if (node.getClassExpr() != null) {
                node.getClassExpr().accept(this, arg);
                printer.print(".");
            }
            printer.print("super");
        }
        printArguments(node.getArguments(), arg);
        printer.print(";");
    }

    public void visit(final EmptyStmt node, final Object arg) {
        printer.print(";");
    }

    public void visit(final ExpressionStmt node, final Object arg) {
        node.getExpression().accept(this, arg);
        printer.print(";");
    }

    public void visit(final LabeledStmt node, final Object arg) {
        printer.print(node.getLabel());
        printer.print(": ");
        node.getStmt().accept(this, arg);
    }

    public void visit(final TypeDeclarationStmt node, final Object arg) {
        node.getTypeDeclaration().accept(this, arg);
    }




    public void visit(final MarkerAnnotationExpr node, Object arg) {
        printer.print("@");
        printer.print(node.getName());
    }

    public void visit(final SingleMemberAnnotationExpr node, final Object arg) {
        printer.print("@");
        printer.print(node.getName());
        printer.print("(");
        node.getMemberValue().accept(this, arg);
        printer.print(")");
    }

    public void visit(final NormalAnnotationExpr node, final Object arg) {
        printer.print("@");
        printer.print(node.getName());
        printer.print("(");
        for (Iterator<String> it=node.getParams().keySet().iterator(); it.hasNext(); ) {
            final String name = it.next();
            final Expression value = node.getParams().get(name);
            printer.print(name);
            printer.print(" = ");
            value.accept(this, arg);
            if (it.hasNext()) {
                printer.print(", ");
            }
        }
        printer.print(")");
    }

    public void visit(final AssignExpr node, final Object arg) {
        node.getTarget().accept(this, arg);
        printer.print(" ");
        printer.print( node.getOperator().getCode() );
        printer.print(" ");
        node.getValue().accept(this, arg);
    }

    public void visit(final VariableDeclarationExpr node, final Object arg) {
        printAnnotations(node.getAnnotations(), arg);
        printModifiers(node.getModifiers());
        node.getType().accept(this, arg);
        printer.print(" ");
        for (Iterator<VariableDecl> i = node.getVariables().iterator(); i.hasNext();) {
            final VariableDecl var = i.next();
            var.accept(this, arg);
            if (i.hasNext()) {
                printer.print(", ");
            }
        }
    }

    public void visit(final ObjectCreationExpr node, final Object arg) {
        if (node.getScope() != null) {
            node.getScope().accept(this, arg);
            printer.print(".");
        }
        printer.print("new ");
        node.getType().accept(this, arg);
        printArguments(node.getArguments(), arg);
        if (node.hasAnonymousClassItems()) {
            printer.printLn(" {");
            printer.indent();
            printMembers(node.getAnonymousClassItems(), arg);
            printer.unindent();
            printer.print("}");
        }
    }

    public void visit(final UnaryExpr node, final Object arg) {
        switch (node.getOperator()) {
            case POSITIVE:
                printer.print("+");
                node.getExpr().accept(this, arg);
                break;
            case NEGATIVE:
                printer.print("-");
                node.getExpr().accept(this, arg);
                break;
            case INVERSE:
                printer.print("~");
                node.getExpr().accept(this, arg);
                break;
            case NOT:
                printer.print("!");
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

    public void visit(final BinaryExpr node, final Object arg) {
        node.getLeft().accept(this, arg);
        printer.print(" ");
        printer.print( node.getOperator().getCode() );
        printer.print(" ");
        node.getRight().accept(this, arg);
    }

    public void visit(final ConditionalExpr node, final Object arg) {
        node.getCondition().accept(this, arg);
        printer.print(" ? ");
        node.getThenExpr().accept(this, arg);
        printer.print(" : ");
        node.getElseExpr().accept(this, arg);
    }

    public void visit(final ArrayAccessExpr node, final Object arg) {
        node.getName().accept(this, arg);
        printer.print("[");
        node.getIndex().accept(this, arg);
        printer.print("]");
    }

    public void visit(final ArrayCreationExpr node, final Object arg) {
        printer.print("new ");
        node.getType().accept(this, arg);

        if (node.hasDimensions()) {
            for (Expression dim : node.getDimensions()) {
                printer.print("[");
                dim.accept(this, arg);
                printer.print("]");
            }
        }
        for (int i = node.getArrayDimension(); i>0; i--) {
            printer.print("[]");
        }
        if (node.hasValues()) {
            printer.print(" {");
            if (node.getValues() != null) {
                printer.print(" ");
                for (Iterator<Expression> i = node.getValues().iterator(); i.hasNext();) {
                    Expression expr = i.next();
                    expr.accept(this, arg);
                    if (i.hasNext()) {
                        printer.print(", ");
                    }
                }
                printer.print(" ");
            }
            printer.print("}");
        }
    }


    public void visit(final FieldAccessExpr node, final Object arg) {
        node.getScope().accept(this, arg);
        printer.print(".");
        printer.print(node.getField());
    }

    public void visit(final MethodCallExpr node, final Object arg) {
        if (node.getScope() != null) {
            node.getScope().accept(this, arg);
            printer.print(".");
        }
        printTypeArgs(node.getTypeArgs(), arg);
        printer.print(node.getName());
        printArguments(node.getArguments(), arg);
    }

    public void visit(final CastExpr node, final Object arg) {
        printer.print("(");
        node.getType().accept(this, arg);
        printer.print(") ");
        node.getExpr().accept(this, arg);
    }

    public void visit(final InstanceOfExpr node, final Object arg) {
        node.getExpression().accept(this, arg);
        printer.print(" instanceof ");
        node.getType().accept(this, arg);
    }

    public void visit(final EnclosedExpr node, final Object arg) {
        printer.print("(");
        node.getInner().accept(this, arg);
        printer.print(")");
    }

    public void visit(final ClassExpr node, final Object arg) {
        node.getType().accept(this, arg);
        printer.print(".class");
    }

    public void visit(final ThisExpr node, final Object arg) {
        if (node.getClassExpr() != null) {
            node.getClassExpr().accept(this, arg);
            printer.print(".");
        }
        printer.print("this");
    }

    public void visit(final SuperExpr node, final Object arg) {
        if (node.getClassExpr() != null) {
            node.getClassExpr().accept(this, arg);
            printer.print(".");
        }
        printer.print("super");
    }

    public void visit(final NameExpr node, final Object arg) {
        printer.print(node.getName());
    }

    public void visit(final QualifiedNameExpr node, final Object arg) {
        node.getQualifier().accept(this, arg);
        printer.print(".");
        printer.print(node.getName());
    }

    public void visit(final NullLiteralExpr node, final Object arg) {
        printer.print("null");
    }

    public void visit(final StringLiteralExpr node, final Object arg) {
        printer.print("\"");
        printer.print(node.getValue());
        printer.print("\"");
    }

    public void visit(final CharLiteralExpr node, final Object arg) {
        printer.print("'");
        printer.print( String.valueOf(node.getValue()) );
        printer.print("'");
    }

    public void visit(final BooleanLiteralExpr node, final Object arg) {
        printer.print( String.valueOf(node.getValue()) );
    }

    public void visit(final IntegerLiteralExpr node, final Object arg) {
        printer.print( Integer.toString(node.getValue()) );
    }

    public void visit(final LongLiteralExpr node, final Object arg) {
        printer.print( Long.toString(node.getValue()) );
    }

    public void visit(final DoubleLiteralExpr node, final Object arg) {
        printer.print( node.getValue() );
    }

    public void visit(final RawExpr node, final Object arg) {
        printer.print(node.getText());
    }

}
