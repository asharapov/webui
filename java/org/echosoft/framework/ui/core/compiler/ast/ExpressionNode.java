package org.echosoft.framework.ui.core.compiler.ast;

/**
 * @author Anton Sharapov
 */
public abstract class ExpressionNode extends ASTNode {

    @Override
    public ExpressionNode append(final ASTNode node) {
        if (!(node instanceof ExpressionNode))
            throw new IllegalArgumentException("Attempt to append illegal node to expression: "+node);
        return (ExpressionNode)super.append(node);
    }
}
