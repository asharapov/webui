package org.echosoft.framework.ui.core.compiler.ast;

/**
 * @author Anton Sharapov
 */
public interface VariablesContainer {

    /**
     * Возвращает <code>true</code> если данный AST-узел содержит переменную с указанным именем (любого типа).
     * @param name  некоторое имя переменной.
     * @return <code>true</code> если данный блок содержит переменную с указанным именем (любого типа).
     */
    public boolean containsVariable(String name);

}
