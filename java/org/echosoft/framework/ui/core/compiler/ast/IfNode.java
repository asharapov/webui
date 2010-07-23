package org.echosoft.framework.ui.core.compiler.ast;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Anton Sharapov
 */
public class IfNode extends StatementListNode {

    public IfNode() {
        super();
        append( new RawExpressionNode(null) );
        append( new StatementListNode().noLeadIndent() );
        append( new StatementListNode().noLeadIndent() );
    }

    public ExpressionNode getExpressionNode() {
        return (ExpressionNode)children.get(0);
    }
    public IfNode setExpressionNode(ExpressionNode exprNode) {
        children.set(0, exprNode);
        return this;
    }

    public StatementListNode getThenNode() {
        return (StatementListNode)children.get(1);
    }

    public StatementListNode getElseNode() {
        return (StatementListNode)children.get(2);
    }

    @Override
    public IfNode append(final ASTNode node) {
        if (getChildren().size()>2)
            throw new IllegalArgumentException("Only 3 child nodes supported");
        return (IfNode)super.append(node);
    }

    @Override
    public void translate(final Writer out) throws IOException {
        if (getExpressionNode()==null)
            throw new IllegalStateException("Expression not specified");
        indent(out);
        out.write("if ( ");
        getExpressionNode().translate(out);
        out.write(" ) ");
        getThenNode().translate(out);
        if (!getElseNode().isLeaf()) {
            out.write(" else ");
            getElseNode().translate(out);
        }
    }
}
