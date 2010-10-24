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

import org.echosoft.framework.ui.core.compiler.ast.ASTParameter;
import org.echosoft.framework.ui.core.compiler.ast.ASTVariableDecl;
import org.echosoft.framework.ui.core.compiler.ast.CompilationUnit;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTAnnotationDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTAnnotationMemberDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTClassDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTConstructorDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTEnumConstantDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTEnumDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTFieldDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTInitializerDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTInterfaceDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTMethodDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTPackageDecl;
import org.echosoft.framework.ui.core.compiler.ast.comments.ASTBlockComment;
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
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTSwitchEntryStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTSwitchStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTSynchronizedStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTThrowStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTTryStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTTypeDeclarationStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTWhileStmt;
import org.echosoft.framework.ui.core.compiler.ast.type.SimpleTypeArgument;
import org.echosoft.framework.ui.core.compiler.ast.type.Type;
import org.echosoft.framework.ui.core.compiler.ast.type.TypeParameter;
import org.echosoft.framework.ui.core.compiler.ast.type.WildcardTypeArgument;

/**
 * @author Julio Vilmar Gesser
 * @author Anton Sharapov
 */
public interface GenericVisitor<R, A> {

    //- Compilation Unit ----------------------------------

    public R visit(CompilationUnit node, A arg);

    public R visit(ASTPackageDecl node, A arg);

    public R visit(ASTLineComment node, A arg);

    public R visit(ASTBlockComment node, A arg);

    public R visit(ASTJavadocComment node, A arg);

    public R visit(Type node, A arg);

    public R visit(SimpleTypeArgument node, A arg);

    public R visit(WildcardTypeArgument node, A arg);

    public R visit(TypeParameter node, A arg);

    public R visit(ASTVariableDecl node, A arg);

    public R visit(ASTParameter node, A arg);

    //- Body ----------------------------------------------

    public R visit(ASTClassDecl node, A arg);

    public R visit(ASTInterfaceDecl node, A arg);

    public R visit(ASTEnumDecl node, A arg);

    public R visit(ASTEnumConstantDecl node, A arg);

    public R visit(ASTAnnotationDecl node, A arg);

    public R visit(ASTAnnotationMemberDecl node, A arg);

    public R visit(ASTInitializerDecl node, A arg);

    public R visit(ASTFieldDecl node, A arg);

    public R visit(ASTConstructorDecl node, A arg);

    public R visit(ASTMethodDecl node, A arg);

    //- Statements ----------------------------------------

    public R visit(ASTBlockStmt node, A arg);

    public R visit(ASTSynchronizedStmt node, A arg);

    public R visit(ASTIfStmt node, A arg);

    public R visit(ASTForStmt node, A arg);

    public R visit(ASTForeachStmt node, A arg);

    public R visit(ASTWhileStmt node, A arg);

    public R visit(ASTDoStmt node, A arg);

    public R visit(ASTSwitchStmt node, A arg);

    public R visit(ASTSwitchEntryStmt node, A arg);

    public R visit(ASTTryStmt node, A arg);

    public R visit(ASTCatchClause node, A arg);

    public R visit(ASTAssertStmt node, A arg);

    public R visit(ASTBreakStmt node, A arg);

    public R visit(ASTContinueStmt node, A arg);

    public R visit(ASTReturnStmt node, A arg);

    public R visit(ASTThrowStmt node, A arg);

    public R visit(ASTExplicitConstructorInvocationStmt node, A arg);

    public R visit(ASTEmptyStmt node, A arg);

    public R visit(ASTExpressionStmt node, A arg);

    public R visit(ASTLabeledStmt node, A arg);

    public R visit(ASTTypeDeclarationStmt node, A arg);

    //- Expression ----------------------------------------

    public R visit(ASTMarkerAnnotationExpr node, A arg);

    public R visit(ASTSingleMemberAnnotationExpr node, A arg);

    public R visit(ASTNormalAnnotationExpr node, A arg);

    public R visit(ASTAssignExpr node, A arg);

    public R visit(ASTVariableDeclExpr node, A arg);

    public R visit(ASTObjectCreationExpr node, A arg);

    public R visit(ASTUnaryExpr node, A arg);

    public R visit(ASTBinaryExpr node, A arg);

    public R visit(ASTConditionalExpr node, A arg);

    public R visit(ASTArrayAccessExpr node, A arg);

    public R visit(ASTArrayCreationExpr node, A arg);

    public R visit(ASTFieldAccessExpr node, A arg);

    public R visit(ASTMethodCallExpr node, A arg);

    public R visit(ASTCastExpr node, A arg);

    public R visit(ASTInstanceOfExpr node, A arg);

    public R visit(ASTEnclosedExpr node, A arg);

    public R visit(ASTClassExpr node, A arg);

    public R visit(ASTThisExpr node, A arg);

    public R visit(ASTSuperExpr node, A arg);

    public R visit(ASTNameExpr node, A arg);

    public R visit(ASTQualifiedNameExpr node, A arg);

    public R visit(ASTNullLiteralExpr node, A arg);

    public R visit(ASTStringLiteralExpr node, A arg);

    public R visit(ASTCharLiteralExpr node, A arg);

    public R visit(ASTBooleanLiteralExpr node, A arg);

    public R visit(ASTIntegerLiteralExpr node, A arg);

    public R visit(ASTLongLiteralExpr node, A arg);

    public R visit(ASTDoubleLiteralExpr node, A arg);

    public R visit(ASTRawExpr node, A arg);

}
