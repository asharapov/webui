package org.echosoft.framework.ui.core.compiler.ast;

/**
 * @author Anton Sharapov
 */
public class FieldNode extends ASTNode {

    private Class cls;
    private String name;
    private Visibility visibility;
    private boolean stat;
    private boolean fin;

    public FieldNode(Class cls, String name, Visibility visibility, boolean stat) {
        this.cls = cls;
        this.name = name;
        this.visibility = visibility;
        this.stat = stat;
    }

    public Class getFieldClass() {
        return cls;
    }
    public FieldNode setFieldClass(Class cls) {
        this.cls = cls;
        return this;
    }

    public String getFieldName() {
        return name;
    }
    public FieldNode setFieldName(String name) {
        this.name = name;
        return this;
    }

    public Visibility getVisibility() {
        return visibility;
    }
    public FieldNode setVisibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public boolean isStatic() {
        return stat;
    }
    public FieldNode setStatic(boolean stat) {
        this.stat = stat;
        return this;
    }

    public boolean isFinal() {
        return fin;
    }
    public FieldNode setFinal(boolean fin) {
        this.fin = fin;
        return this;
    }

    @Override
    public ASTNode append(final ASTNode node) {
        if (!isLe)
        return super.append(node);
    }
}
