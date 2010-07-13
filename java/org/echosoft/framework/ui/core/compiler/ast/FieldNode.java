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

    public ExpressionNode getFieldValue() {
        return children.isEmpty() ? (ExpressionNode)children.get(0) : null;
    }
    public FieldNode setFieldValue(ExpressionNode node) {
        if (children.size()>0) {
            if (node!=null) {
                children.set(0, node);
            } else {
                final ASTNode oldNode = children.remove(0);
                oldNode.parent = null;
            }
        } else {
            if (node!=null)
                super.append(node);
        }
        return this;
    }

    @Override
    public ASTNode append(final ASTNode node) {
        if (children.size()>0)
            throw new IllegalStateException("Field value already specified");
        if (!(node instanceof ExpressionNode))
            throw new IllegalArgumentException("Attempt to append illegal node to expression: "+node);
        return super.append(node);

    }
}
