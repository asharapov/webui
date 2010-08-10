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

import org.echosoft.framework.ui.core.compiler.ast.CompilationUnit;
import org.echosoft.framework.ui.core.compiler.ast.PackageDecl;
import org.echosoft.framework.ui.core.compiler.ast.Parameter;
import org.echosoft.framework.ui.core.compiler.ast.VariableDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.AnnotationDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.AnnotationMemberDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ClassDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ConstructorDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.EnumConstantDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.EnumDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.FieldDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.InitializerDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.InterfaceDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.MethodDecl;
import org.echosoft.framework.ui.core.compiler.ast.comments.BlockComment;
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
import org.echosoft.framework.ui.core.compiler.ast.stmt.SwitchEntryStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.SwitchStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.SynchronizedStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ThrowStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.TryStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.TypeDeclarationStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.WhileStmt;
import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.ast.type.TypeParameter;
import org.echosoft.framework.ui.core.compiler.ast.type.WildcardType;


/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public interface VoidVisitor<A> {

    //- Compilation Unit ----------------------------------

    public void visit (CompilationUnit node, A arg);

    public void visit (PackageDecl node, A arg);

    public void visit (LineComment node, A arg);

    public void visit (BlockComment node, A arg);

    public void visit (JavadocComment node, A arg);

    public void visit (RefType node, A arg);

    public void visit (WildcardType node, A arg);

    public void visit (TypeParameter node, A arg);

    public void visit (VariableDecl node, A arg);

    public void visit (Parameter node, A arg);

    //- Body ----------------------------------------------

    public void visit (ClassDecl node, A arg);

    public void visit (InterfaceDecl node, A arg);

    public void visit (EnumDecl node, A arg);

    public void visit (EnumConstantDecl node, A arg);

    public void visit (AnnotationDecl node, A arg);

    public void visit (AnnotationMemberDecl node, A arg);

    public void visit (InitializerDecl node, A arg);

    public void visit (FieldDecl node, A arg);

    public void visit (ConstructorDecl node, A arg);

    public void visit (MethodDecl node, A arg);

    //- Statements ----------------------------------------

    public void visit (BlockStmt node, A arg);

    public void visit (SynchronizedStmt node, A arg);

    public void visit (IfStmt node, A arg);

    public void visit (ForStmt node, A arg);

    public void visit (ForeachStmt node, A arg);

    public void visit (WhileStmt node, A arg);

    public void visit (DoStmt node, A arg);

    public void visit (SwitchStmt node, A arg);

    public void visit (SwitchEntryStmt node, A arg);

    public void visit (TryStmt node, A arg);

    public void visit (CatchClause node, A arg);

    public void visit (AssertStmt node, A arg);

    public void visit (BreakStmt node, A arg);

    public void visit (ContinueStmt node, A arg);

    public void visit (ReturnStmt node, A arg);

    public void visit (ThrowStmt node, A arg);

    public void visit (ExplicitConstructorInvocationStmt node, A arg);

    public void visit (EmptyStmt node, A arg);

    public void visit (ExpressionStmt node, A arg);

    public void visit (LabeledStmt node, A arg);

    public void visit (TypeDeclarationStmt node, A arg);

    //- Expression ----------------------------------------

    public void visit (MarkerAnnotationExpr node, A arg);

    public void visit (SingleMemberAnnotationExpr node, A arg);

    public void visit (NormalAnnotationExpr node, A arg);

    public void visit (AssignExpr node, A arg);

    public void visit (VariableDeclExpr node, A arg);

    public void visit (ObjectCreationExpr node, A arg);

    public void visit (UnaryExpr node, A arg);

    public void visit (BinaryExpr node, A arg);

    public void visit (ConditionalExpr node, A arg);

    public void visit (ArrayAccessExpr node, A arg);

    public void visit (ArrayCreationExpr node, A arg);

    public void visit (FieldAccessExpr node, A arg);

    public void visit (MethodCallExpr node, A arg);

    public void visit (CastExpr node, A arg);

    public void visit (InstanceOfExpr node, A arg);

    public void visit(EnclosedExpr node, A arg);

    public void visit (ClassExpr node, A arg);

    public void visit (ThisExpr node, A arg);

    public void visit (SuperExpr node, A arg);

    public void visit (NameExpr node, A arg);

    public void visit (QualifiedNameExpr node, A arg);

    public void visit (NullLiteralExpr node, A arg);

    public void visit (StringLiteralExpr node, A arg);

    public void visit (CharLiteralExpr node, A arg);

    public void visit (BooleanLiteralExpr node, A arg);

    public void visit (IntegerLiteralExpr node, A arg);

    public void visit (LongLiteralExpr node, A arg);

    public void visit (DoubleLiteralExpr node, A arg);

    public void visit(RawExpr node, A arg);

}
