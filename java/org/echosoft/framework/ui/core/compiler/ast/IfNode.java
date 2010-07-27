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
    public IfNode setExpressionNode(final ExpressionNode node) {
        if (children.size()==0) {
            append( node );
        } else {
            replace(node, 0);
        }
        return this;
    }

    public StatementListNode getThenNode() {
        return children.size()>1 ? (StatementListNode)children.get(1) : null;
    }
    public IfNode setThenNode(final StatementListNode node) {
        final int size = children.size();
        if (size==0) {
            append( new RawExpressionNode("true") );
            append( node );
        } else
        if (size==1) {
            append( node );
        } else
            replace(node, 1);
        return this;
    }

    public StatementListNode getElseNode() {
        return children.size()>2 ? (StatementListNode)children.get(2) : null;
    }
    public IfNode setElseNode(final StatementListNode node) {
        final int size = children.size();
        if (size==0) {
            append( new RawExpressionNode("true") );
            append( new StatementListNode().noLeadIndent() );
            append( node );
        } else
        if (size==1) {
            append( new StatementListNode().noLeadIndent() );
            append( node );
        } else
        if (size==2) {
            append( node );
        } else
            replace(node, 2);
        return this;
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
        getThenNode().translate(out);
        final StatementNode en = getElseNode();
        if (en!=null && !en.isLeaf()) {
            out.write(" else ");
            en.translate(out);
        }
        out.write('\n');
    }

    @Override
    protected void checkCandidateToChild(final ASTNode node) {
        final int size = getChildren().size();
        if (size==0) {
            if (!(node instanceof ExpressionNode))
                throw new IllegalArgumentException("Attempt to attach illegal node: "+node);
        } else
        if (size<3) {
            if (!(node instanceof StatementListNode))
                throw new IllegalArgumentException("Attempt to attach illegal node: "+node);
        } else
            throw new IllegalArgumentException("Only 3 child nodes supported");
    }
}
