package org.echosoft.framework.ui.core.compiler.binding;

/**
 * Анализирует строки текста в которых может быть указаны вычисляемые выражения
 *
 * @author Anton
 */
public class ExpressionFactory {

    public boolean containsExpression(final String text) {
        if (text==null)
            return false;
        final int start = text.indexOf("@{");
        if (start<0)
            return false;
        final int end = text.indexOf('}', start);
        return end>0;
    }

    public Expression getExpression(final String text) {
        return null;
    }
}
