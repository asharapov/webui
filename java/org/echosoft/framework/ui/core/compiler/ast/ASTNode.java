package org.echosoft.framework.ui.core.compiler.ast;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.echosoft.common.utils.StringUtil;

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

    public ASTNode findParent(final Class cls) {
        for (ASTNode p=parent; p!=null; p=p.parent) {
            if (cls.isAssignableFrom(p.getClass()))
                return p;
        }
        return null;
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

    public Iterator<ASTNode> traverseChildNodes() {
        return new ASTNodeWalker(this);
    }

    public ASTNode append( final ASTNode node ) {
        node.parent = this;
        children.add( node );
        return this;
    }

    public void translate(final Writer out) throws IOException {
    }

    public String debugInfo() {
        try {
            final StringBuilder out = new StringBuilder(512);
            debugInfo(out);
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    public void debugInfo(final Appendable out) throws IOException {
        final String prefix = StringUtil.leadLeft("", ' ', getLevel() * 2);
        out.append(prefix);
        out.append( this.toString() );
        out.append("\n");
        for (ASTNode node : children) {
            node.debugInfo(out);
        }
    }


    private static final class ASTNodeWalker implements Iterator<ASTNode> {
        private final int startLevel;
        private final ArrayList<Integer> stack;
        private ASTNode p;
        private ASTNode next;

        public ASTNodeWalker(final ASTNode parent) {
            startLevel = parent.getLevel();
            p = parent;
            stack = new ArrayList<Integer>();
            stack.add(0);
            findNextGroup();
        }

        public boolean hasNext() {
            return next != null;
        }

        public ASTNode next() {
            if (next == null)
                throw new NoSuchElementException();
            final ASTNode result = next;
            findNextGroup();
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void findNextGroup() {
            assert p != null && (p.getLevel() + 1 - startLevel == stack.size());
            while (stack.size() > 0) {
                int pos = stack.get(stack.size() - 1);
                if (pos >= p.children.size()) {
                    stack.remove(stack.size() - 1);
                    if (stack.size() > 0) {
                        stack.set(stack.size() - 1, stack.get(stack.size() - 1) + 1);
                        p = p.parent;
                    }
                } else {
                    next = p.children.get(pos);
                    p = next;
                    stack.add(0);
                    return;
                }
            }
            next = null;
        }
    }
}
