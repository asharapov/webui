package org.echosoft.framework.ui.core.compiler.binding;

import org.echosoft.framework.ui.core.ComponentContext;

/**
 * Описывает некоторое скомпиоированное выражение, используемое для вычисления результата. 
 * @author Anton
 */
public interface Expression {

    /**
     * Вычисляет данное выражение.
     * @param ctx  контекст в котором должно происходить вычисление данного выражения.
     * @return  результат вычисления.
     */
    public Object evaluate(ComponentContext ctx);
}
