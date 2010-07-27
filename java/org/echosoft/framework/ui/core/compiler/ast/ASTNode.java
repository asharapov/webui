package org.echosoft.framework.ui.core.compiler.ast;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

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
     * @param cls  указывает на интерфейс или класс экземпляром которого должен быть искомый узел.
     * @return искомый узел или <code>null</code>.  
     */
    @SuppressWarnings("unchecked")
    public <T, E extends T> E findParent(final Class<T> cls) {
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
     * Добавляет дочерний узел. По возможности устанавливает этому узлу (и всем его дочерним рекурсивно)
     * ссылку на корневой узел дерева.
     * @param node  узел, который должен быть добавлен в качестве дочернего.
     * @return  текущий узел.
     */
    public ASTNode append( final ASTNode node ) {
        if (node.parent!=null)
            throw new IllegalStateException("An attempt to attach already attached node");
        checkCandidateToChild(node);
        children.add( node );
        node.parent = this;
        // установим в добавляемом узле и всех его дочерних узлах (если они есть) ссылку на корневой узел дерева...
        final FileNode root = this.getRoot();
        if (root!=null) {
            node.root = root;
            node.resetState();
            if (node.hasChildren()) {
                for (Iterator<ASTNode> it = node.traverseChildNodes(); it.hasNext(); ) {
                    final ASTNode n = it.next();
                    n.root = root;
                    n.resetState();
                }
            }
        }
        return this;
    }

    public ASTNode replace( final ASTNode node, int position ) {
        if (node.parent!=null)
            throw new IllegalStateException("An attempt to attach already attached node");
        if (children.size()<=position)
            throw new IllegalStateException("An attempt to replace non existent node");
        checkCandidateToChild(node);

        final ASTNode oldnode = children.get(position);
        oldnode.excludeFromTree();
        children.set(position, node);
        node.parent = this;
        // установим в добавляемом узле и всех его дочерних узлах (если они есть) ссылку на корневой узел дерева...
        final FileNode root = this.getRoot();
        if (root!=null) {
            node.root = root;
            node.resetState();
            if (node.hasChildren()) {
                for (Iterator<ASTNode> it = node.traverseChildNodes(); it.hasNext(); ) {
                    final ASTNode n = it.next();
                    n.root = root;
                    n.resetState();
                }
            }
        }
        return this;
    }

    /**
     * Исключает текущий узел из дерева, удаляя информацию о нем из родительского узла.
     * Ссылка на корень дерева у данного узла и всех его дочерних узлов устанавливается в null.
     * @return  текущий узел.
     */
    public ASTNode excludeFromTree() {
        if (parent != null) {
            parent.children.remove(this);
            parent = null;
            root = null;
            resetState();
            if (this.hasChildren()) {
                for (Iterator<ASTNode> it = this.traverseChildNodes(); it.hasNext(); ) {
                    final ASTNode node = it.next();
                    node.root = null;
                    node.resetState();
                }
            }
        }
        return this;
    }

    /**
     * Осуществляет трансляцию данного узла дерева и всех его дочерних узлов в
     * соответствующий фрагмент .java файла.
     * Метод должен перекрываться в узлах-потомках.
     * @param out  выходной поток для формируемого .java файла.
     * @throws IOException  в случае каких-либо проблем с записью в поток.
     */
    public void translate(final Writer out) throws IOException {
    }


    /**
     * Используется для отладочной печати структуры дерева.
     * @return строка, отображающая иерархию узлов, начиная от данного узла.
     */
    public String debugInfo() {
        final StringBuilder out = new StringBuilder(512);
        final int startLevel = getLevel();
        out.append(this.toString());
        for (Iterator<ASTNode> it = this.traverseChildNodes(); it.hasNext();) {
            final ASTNode node = it.next();
            final char[] buf = new char[2*(node.getLevel()-startLevel)];
            Arrays.fill(buf, ' ');
            out.append('\n');
            out.append(buf);
            out.append(node.toString());
        }
        out.append('\n');
        return out.toString();
    }

    /**
     * Вызывается при добавлении или исключению узла из дерева.
     * Метод перекрывается в классах-потомках для своевременного сброса своего внутреннего состояния,
     * зависящего от положения узла в дереве.
     */
    protected void resetState() {
    }

    /**
     * Метод автоматически вызывается всякий раз перед добавлением очередного дочернего элемента.
     * Задача метода - проверка допустимости добавления указанного в аргументе узла в качестве дочернего для текущего узла.
     * @param node узел, добавляемый в список дочерних для данного узла.
     * @throws RuntimeException  в случае если указанный узел не может быть добавлен.
     */
    protected void checkCandidateToChild(final ASTNode node) {
    }

    protected void indent(final Writer out) throws IOException {
        if (parent==null)
            return;
        final int level = getIndentLevel();
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

    protected int getIndentLevel() {
        return parent==null || parent.parent==null ? 0 : parent.getIndentLevel() + 1;
    }

    public String toString() {
        return "["+this.getClass().getSimpleName()+"{hash:"+System.identityHashCode(this)+"}]"; 
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
