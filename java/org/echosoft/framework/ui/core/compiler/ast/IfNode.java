package org.echosoft.framework.ui.core.compiler.ast;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Anton Sharapov
 */
public class IfNode extends StatementListNode {

    public IfNode() {
        super();
    }

    public ExpressionNode getExpressionNode() {
        return children.size()>0 ? (ExpressionNode)children.get(0) : null;
    }

    public StatementNode getThenNode() {
        return children.size()>1 ? (StatementNode)children.get(1) : null;
    }

    public StatementNode getElseNode() {
        return children.size()>2 ? (StatementNode)children.get(2) : null;
    }

    @Override
    public IfNode append(final ASTNode node) {
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
        final StatementNode tn = getThenNode(), en = getElseNode();
        if (!(tn instanceof StatementListNode))
            out.write('\n'); 
        tn.translate(out);
        if (en!=null) {
            if (tn instanceof StatementListNode)
                out.write(' ');
            else {
                indent(out);
            }
            out.write("else ");
            if (!(en instanceof StatementListNode))
                out.write('\n');
            en.translate(out);
        }
    }

    @Override
    protected void checkCandidateToChild(final ASTNode node) {
        final int size = getChildren().size();
        if (size==0) {
            if (!(node instanceof ExpressionNode))
                throw new IllegalArgumentException("Attempt to attach illegal node: "+node);
        } else
        if (size<3) {
            if (!(node instanceof StatementNode))
                throw new IllegalArgumentException("Attempt to attach illegal node: "+node);
        } else
            throw new IllegalArgumentException("Only 3 child nodes supported");
    }
}
