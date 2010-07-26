package org.echosoft.framework.ui.core.compiler.ast;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Содержит описание отдельного класса.
 * @author Anton Sharapov
 */
public class ClassNode extends ASTNode {

    private String clsName;
    private boolean abstrct;
    private boolean unmodifiable;
    private boolean statical;
    private Visibility visibility;
    private Class extended;
    private final SortedSet<Class> implemented;

    public ClassNode(final String clsName, final Class extended) {
        super();
        this.clsName = clsName;
        this.extended = extended;
        this.visibility = Visibility.PUBLIC;
        this.implemented = new TreeSet<Class>(ASTNode.CLS_COMPARATOR);
    }

    public String getClassName() {
        return clsName;
    }
    public ClassNode setClassName(final String className) {
        this.clsName = className;
        return this;
    }

    public boolean isAbstract() {
        return abstrct;
    }
    public ClassNode setAbstract(final boolean abstrct) {
        this.abstrct = abstrct;
        return this;
    }

    public boolean isFinal() {
        return unmodifiable;
    }
    public ClassNode setFinal(final boolean unmodifiable) {
        this.unmodifiable = unmodifiable;
        return this;
    }

    public boolean isStatic() {
        return statical;
    }
    public ClassNode setStatic(final boolean statical) {
        this.statical = statical;
        return this;
    }

    public Visibility getVisibility() {
        return visibility;
    }
    public ClassNode setVisibility(final Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public Class getExtends() {
        return extended;
    }
    public ClassNode setExtends(final Class extended) {
        this.extended = extended;
        return this;
    }

    public Set<Class> getImplements() {
        return implemented;
    }
    public ClassNode addImplementation(final Class cls) {
        if (!cls.isInterface())
            throw new IllegalArgumentException("Interface must be specified");
        this.implemented.add( cls );
        return this;
    }

    @Override
    public ClassNode append(final ASTNode node) {
        return (ClassNode)super.append(node);
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
        out.write(" class ");
        out.write(clsName);
        if (extended!=null) {
            out.write(" extends ");
            out.write( getRoot().ensureClassImported(extended) );
        }
        if (implemented.size()>0) {
            out.write(" implements ");
            for (Iterator<Class> itcl=implemented.iterator(); itcl.hasNext(); ) {
                out.write( getRoot().ensureClassImported(itcl.next()) );
                if (itcl.hasNext()) {
                    out.write(", ");
                }
            }
        }
        out.write(" {\n");
        for (ASTNode node : children) {
            node.translate(out);
        }
        indent(out);
        out.write("}\n");
    }

    @Override
    protected void checkCandidateToChild(final ASTNode node) {
        if ( !(node instanceof MethodNode) && !(node instanceof FieldNode) && !(node instanceof ClassNode) )
            throw new IllegalArgumentException("Attempt to append illegal node to expression: "+node);
    }
}
