package org.echosoft.framework.ui.core.compiler.utils;

import org.echosoft.framework.ui.core.compiler.ast.Variable;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTExpression;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTMethodCallExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTNameExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTStringLiteralExpr;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTBlockStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTExpressionStmt;

/**
 * @author Anton Sharapov
 */
public final class GenUtils {

    private GenUtils() {}

    public static void setStringProperty(final ASTBlockStmt place, final Variable bean, final String methodName, final String exprValue) {
        final ASTExpression arg;
        if (isDynamicExpression(exprValue)) {
            arg = new ASTNameExpr(null);   // TODO: implement expression handling.
        } else {
            arg = new ASTStringLiteralExpr(exprValue);
        }
        final ASTMethodCallExpr mce = new ASTMethodCallExpr(new ASTNameExpr(bean.getName()), methodName);
        mce.addArgument( arg );
        place.addStatement( new ASTExpressionStmt(mce) );
    }

    public static void setIntProperty(final ASTBlockStmt place, final Variable bean, final String methodName, final String exprValue) {
    }

    public static void setLongProperty(final ASTBlockStmt place, final Variable bean, final String methodName, final String exprValue) {
    }

    public static void setDoubleProperty(final ASTBlockStmt place, final Variable bean, final String methodName, final String exprValue) {
    }

    public static void setProperty(final ASTBlockStmt place, final Variable bean, final String methodName, final String exprValue, final Class cls) {
    }


    private static boolean isDynamicExpression(final String exprValue) {
        if (exprValue==null)
            return false;
        final int start = exprValue.indexOf("${");
        if (start<0)
            return false;
        final int end = exprValue.indexOf('}', start);
        return end>0;
    }

}
