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
    private boolean statical;
    private boolean unmodifiable;

    public FieldNode(Class cls, String name, Visibility visibility, boolean statical) {
        this.cls = cls;
        this.name = name;
        this.visibility = visibility;
        this.statical = statical;
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
        return statical;
    }
    public FieldNode setStatic(boolean statical) {
        this.statical = statical;
        return this;
    }

    public boolean isFinal() {
        return unmodifiable;
    }
    public FieldNode setFinal(boolean unmodifiable) {
        this.unmodifiable = unmodifiable;
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
        if (statical) {
            out.write(" static");
        }
        if (unmodifiable) {
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
