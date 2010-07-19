package org.echosoft.framework.ui.core.compiler.ast;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Anton Sharapov
 */
public class ClassNode extends ASTNode {

    private String clsName;
    private boolean abstrct;
    private boolean fin;
    private boolean stat;
    private Visibility visibility;
    private Class extended;
    private final SortedSet<Class> implemented;

    public ClassNode(final String clsName, final Class extended) {
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
        return fin;
    }
    public ClassNode setFinal(final boolean fin) {
        this.fin = fin;
        return this;
    }

    public boolean isStatic() {
        return stat;
    }
    public ClassNode setStatic(final boolean stat) {
        this.stat = stat;
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
    public ASTNode append(final ASTNode node) {
        if ( !(node instanceof MethodNode) && !(node instanceof FieldNode) && !(node instanceof ClassNode) )
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
        out.write(" class ");
        out.write(clsName);
        if (extended!=null) {
            out.write(' ');
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
}
