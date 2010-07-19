package org.echosoft.framework.ui.core.compiler.ast;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Anton Sharapov
 */
public class MethodNode extends ASTNode {

    private Class cls;
    private String name;
    private Visibility visibility;
    private boolean stat;
    private boolean fin;
    private final SortedSet<Class> exceptions;

    public MethodNode(Class cls, String name, Visibility visibility, boolean stat) {
        this.cls = cls;
        this.name = name;
        this.visibility = visibility;
        this.stat = stat;
        this.exceptions = new TreeSet<Class>(ASTNode.CLS_COMPARATOR);
    }

    public Class getType() {
        return cls;
    }
    public MethodNode setMethodClass(Class cls) {
        this.cls = cls;
        return this;
    }

    public String getName() {
        return name;
    }
    public MethodNode setName(String name) {
        this.name = name;
        return this;
    }

    public Visibility getVisibility() {
        return visibility;
    }
    public MethodNode setVisibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public boolean isStatic() {
        return stat;
    }
    public MethodNode setStatic(boolean stat) {
        this.stat = stat;
        return this;
    }

    public boolean isFinal() {
        return fin;
    }
    public MethodNode setFinal(boolean fin) {
        this.fin = fin;
        return this;
    }

    public Set<Class> getExceptions() {
        return exceptions;
    }
    public <T extends Throwable> MethodNode addException(final Class<T> errCls) {
        this.exceptions.add( errCls );
        return this;
    }


}
