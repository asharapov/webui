package org.echosoft.framework.ui.core.compiler.ast;

import java.io.IOException;
import java.io.Writer;

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
        return hasChildren() ? (ExpressionNode)children.get(0) : null;
    }
    public FieldNode setFieldValue(ExpressionNode node) {
        if (hasChildren()) {
            children.get(0).excludeFromTree();
        }
        if (node!=null)
            super.append(node);
        return this;
    }

    @Override
    public ASTNode append(final ASTNode node) {
        if (hasChildren())
            throw new IllegalStateException("Field value already specified");
        if (!(node instanceof ExpressionNode))
            throw new IllegalArgumentException("Attempt to append illegal node to expression: "+node);
        return super.append(node);
    }

    @Override
    public void translate(final Writer out) throws IOException {
        indent(out);
        out.write(visibility.toJavaString());
        if (stat) {
            out.write(" static");
        }
        if (fin) {
            out.write(" final");
        }
        out.write(' ');
        out.write( getRoot().ensureClassImported(cls) );
        out.write(' ');
        out.write(name);
        if (hasChildren()) {
            out.write(" = ");
            getFieldValue().translate(out);
        }
        out.write(";\n");
    }
}
