package org.echosoft.framework.ui.core.compiler.binding;

import org.echosoft.framework.ui.core.ComponentContext;

/**
 * Описывает некоторое скомпилированное выражение, используемое для вычисления результата.
 * @author Anton
 */
public interface Expression {

    /**
     * Вычисляет значение выражения на основе указанного контекста выполнения.
     * @param ctx  контекст в котором должно происходить вычисление данного выражения.
     * @return  результат вычисления.
     * @throws Exception в случае каких-либо проблем.
     */
    public Object evaluate(ComponentContext ctx) throws Exception;

    /**
     * Возвращает строку с исходным текстом вычисляемого выражения.
     * @return  исходный текст вычисляемого выражения.
     */
    public String getExpressionString();
}
