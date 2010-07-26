package org.echosoft.framework.ui.core.compiler.ast;

/**
 * Содержит описание отдельной команды в теле методов класса или внутри статических блоков кода класса.
 * @author Anton Sharapov
 */
public abstract class StatementNode extends ASTNode {

    @Override
    public StatementNode append(final ASTNode node) {
        return (StatementNode)super.append(node);
    }

}
