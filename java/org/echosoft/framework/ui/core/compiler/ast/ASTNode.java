package org.echosoft.framework.ui.core.compiler.ast;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.echosoft.common.utils.StringUtil;

/**
 * @author Anton Sharapov
 */
public abstract class ASTNode {

    public static final Comparator<Class> CLS_COMPARATOR =
            new Comparator<Class>() {
                public int compare(final Class o1, final Class o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            };

    private static final int INDENT = 4;
    private static final int MAX_DEFAULT_LEVEL = 25;
    private static final char[] PREFIXES = new char[MAX_DEFAULT_LEVEL * INDENT];
    static {
        Arrays.fill(PREFIXES,' ');
    }

    private FileNode root;
    private ASTNode parent;
    protected final List<ASTNode> children;

    public ASTNode() {
        parent = null;
        children = new ArrayList<ASTNode>();
    }

    /**
     * Возвращает корневой узел дерева.
     * @return корневой узел дерева или <code>null</code>.
     */
    public FileNode getRoot() {
        return root;
    }

    /**
     * Возвращает родительский узел дерева.
     * @return родительский узел дерева или <code>null</code>.
     */
    public ASTNode getParent() {
        return parent;
    }

    /**
     * Осуществляет рекурсивный поиск родительского узла, который соответствует заданному в аргументе классу.
     * @return искомый узел или <code>null</code>.  
     */
    @SuppressWarnings("unchecked")
    public <T extends ASTNode, E extends T> E findParent(final Class<T> cls) {
        for (ASTNode p=parent; p!=null; p=p.parent) {
            if (cls.isAssignableFrom(p.getClass()))
                return (E)p;
        }
        return null;
    }

    /**
     * Возвращает список всех узлов лежащих непосредственно под данным узлом.
     * @return список дочерних узлов.
     */
    public List<ASTNode> getChildren() {
        return children;
    }

    /**
     * Возвращает уровень данного узла относительно корня. Корневой узел имеет уровень 0.
     * @return уровень вложенности данного узла.
     */
    public int getLevel() {
        int level = 0;
        for (ASTNode p = parent; p != null; p = p.parent) level++;
        return level;
    }

    /**
     * @return <code>true</code> если узел не имеет дочерних узлов.
     */
    public boolean isLeaf() {
        return children.isEmpty();
    }

    /**
     * @return <code>true</code> если узел имеет хотя бы один дочерний узел.
     */
    public boolean hasChildren() {
        return children.size()>0;
    }

    /**
     * Возвращает рекурсивный итератор по всем дочерним узлам относительно данного.
     * @return  рекурсивный итератор по всем дочерним узлам.
     */
    public Iterator<ASTNode> traverseChildNodes() {
        return new ASTNodeWalker(this);
    }

    /**
     * Добавляет дочерний узел.
     * @param node  узел, который должен быть добавлен в качестве дочернего.
     * @return  текущий узел.
     */
    public ASTNode append( final ASTNode node ) {
        children.add( node );
        node.parent = this;
        if (node.hasChildren() && node.getRoot()!=this.getRoot()) {
            for (Iterator<ASTNode> it = node.traverseChildNodes(); it.hasNext(); ) {
                final ASTNode cn = it.next();
                cn.root = this.getRoot();
            }
        }
        node.root = this.getRoot();
        return this;
    }

    /**
     * Исключает текущий узел из дерева, удаляя информацию о нем из родительского узла.
     * Ссылка на корень дерева остается без изменения.
     * @return  текущий узел.
     */
    public ASTNode excludeFromTree() {
        if (parent!=null) {
            parent.children.remove(this);
            parent = null;
        }
        return this;
    }

    /**
     * Осуществляет трансляцию данного узла дерева и всех его дочерних узлов в
     * соответствующий фрагмент .java файла.
     * @param out  выходной поток для формируемого .java файла.
     * @throws IOException  в случае каких-либо проблем с записью в поток.
     */
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

    protected void indent(final Writer out) throws IOException {
        if (parent==null)
            return;
        final int level = getLevel() - 1;
        if (level<=MAX_DEFAULT_LEVEL) {
            out.write(PREFIXES,0,level*INDENT);
        } else {
            int lv = level;
            while (lv>MAX_DEFAULT_LEVEL) {
                out.write(PREFIXES);
                lv -= MAX_DEFAULT_LEVEL;
            }
            out.write(PREFIXES,0,lv*INDENT);
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
