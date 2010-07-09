package org.echosoft.framework.ui.core.compiler.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton Sharapov
 */
public abstract class ASTNode {
    protected ASTNode parent;
    protected final List<ASTNode> children;

    public ASTNode() {
        parent = null;
        children = new ArrayList<ASTNode>();
    }

    public ASTNode getParent() {
        return parent;
    }

    public List<ASTNode> getChildren() {
        return children;
    }

    public int getLevel() {
        int level = 0;
        for (ASTNode p = parent; p != null; p = p.parent) level++;
        return level;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public boolean hasChildren() {
        return children.size()>0;
    }

    public ASTNode append( final ASTNode node ) {
        node.parent = this;
        children.add( node );
        return node;
    }
}
