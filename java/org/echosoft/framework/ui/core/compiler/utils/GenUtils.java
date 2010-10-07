package org.echosoft.framework.ui.core.compiler.utils;

import org.echosoft.framework.ui.core.compiler.ast.Variable;
import org.echosoft.framework.ui.core.compiler.ast.expr.Expression;
import org.echosoft.framework.ui.core.compiler.ast.expr.MethodCallExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.NameExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.StringLiteralExpr;
import org.echosoft.framework.ui.core.compiler.ast.stmt.BlockStmt;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ExpressionStmt;

/**
 * @author Anton Sharapov
 */
public final class GenUtils {

    private GenUtils() {}

    public static void setStringProperty(final BlockStmt place, final Variable bean, final String methodName, final String exprValue) {
        final Expression arg;
        if (isDynamicExpression(exprValue)) {
            arg = new NameExpr(null);   // TODO: implement expression handling.
        } else {
            arg = new StringLiteralExpr(exprValue);
        }
        final MethodCallExpr mce = new MethodCallExpr(new NameExpr(bean.getName()), methodName);
        mce.addArgument( arg );
        place.addStatement( new ExpressionStmt(mce) );
    }

    public static void setIntProperty(final BlockStmt place, final Variable bean, final String methodName, final String exprValue) {
    }

    public static void setLongProperty(final BlockStmt place, final Variable bean, final String methodName, final String exprValue) {
    }

    public static void setDoubleProperty(final BlockStmt place, final Variable bean, final String methodName, final String exprValue) {
    }

    public static void setProperty(final BlockStmt place, final Variable bean, final String methodName, final String exprValue, final Class cls) {
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
