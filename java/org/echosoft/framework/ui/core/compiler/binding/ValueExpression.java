package org.echosoft.framework.ui.core.compiler.binding;

import org.echosoft.framework.ui.core.RequestContext;

/**
 * Описывает некоторые динамически вычисляемые выражения.
 * @author Anton Sharapov
 */
public interface ValueExpression {

    /**
     * Вычисляет значение выражения на основе указанного контекста выполнения.
     * @param ctx  контекст в котором должно происходить вычисление данного выражения.
     * @return  результат вычисления.
     * @throws Exception в случае каких-либо проблем.
     */
    public Object evaluate(RequestContext ctx) throws Exception;

    /**
     * Возвращает строку с исходным текстом вычисляемого выражения.
     * @return  исходный текст вычисляемого выражения.
     */
    public String getExpressionString();
}
