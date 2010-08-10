package org.echosoft.framework.ui.core.compiler.ast;

import org.echosoft.framework.ui.core.compiler.ast.type.RefType;

/**
 * @author Anton Sharapov
 */
public interface VariablesContainer {

    /**
     * Возвращает <code>true</code> если данный AST-узел содержит переменную с указанным именем (любого типа).
     * @param name  некоторое имя переменной.
     * @param findInParents  следует ли осуществлять поиск указанной переменной в родительских узлах дерева.
     * @return <code>true</code> если данный блок содержит переменную с указанным именем (любого типа).
     */
    public boolean containsVariable(String name, boolean findInParents);

    /**
     * Возвращает <code>true</code> если данный AST-узел содержит переменную с указанным именем и типом.
     * @param name  имя переменной.
     * @param type  тип переменной.
     * @param findInParents  следует ли осуществлять поиск указанной переменной в родительских узлах дерева.
     * @return <code>true</code> если данный блок содержит переменную с указанным именем и типом.
     */
    public boolean containsVariable(String name, RefType type, boolean findInParents);

}
