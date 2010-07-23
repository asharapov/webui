package org.echosoft.framework.ui.core.compiler.ast;

import java.io.IOException;
import java.io.Writer;

/**
 * Содержит описание поля класса.
 * @author Anton Sharapov
 */
public class FieldNode extends ASTNode {

    private Class cls;
    private String name;
    private Visibility visibility;
    private boolean statical;
    private boolean unmodifiable;
    private String expr;

    public FieldNode(Class cls, String name, Visibility visibility, boolean statical) {
        super();
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

    public String getFieldValue() {
        return expr;
    }
    public FieldNode setFieldValue(String expr) {
        this.expr = expr;
        return this;
    }

    @Override
    public FieldNode append(final ASTNode node) {
        throw new IllegalArgumentException("Field is a leaf node");
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
        if (expr!=null) {
            out.write(" = ");
            out.write(expr);
        }
        out.write(";\n");
    }
}
