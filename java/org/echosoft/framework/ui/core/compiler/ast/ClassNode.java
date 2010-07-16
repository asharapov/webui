package org.echosoft.framework.ui.core.compiler.ast;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;
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
    private final TreeSet<Class> implemented;

    public ClassNode(final String clsName, final Class extended) {
        this.clsName = clsName;
        this.extended = extended;
        this.visibility = Visibility.PUBLIC;
        this.implemented = new TreeSet<Class>();
    }

    public String getClassName() {
        return clsName;
    }
    public void setClassName(final String className) {
        this.clsName = className;
    }

    public boolean isAbstract() {
        return abstrct;
    }
    public void setAbstract(final boolean abstrct) {
        this.abstrct = abstrct;
    }

    public boolean isFinal() {
        return fin;
    }
    public void setFinal(final boolean fin) {
        this.fin = fin;
    }

    public boolean isStatic() {
        return stat;
    }
    public void setStatic(final boolean stat) {
        this.stat = stat;
    }

    public Visibility getVisibility() {
        return visibility;
    }
    public void setVisibility(final Visibility visibility) {
        this.visibility = visibility;
    }

    public Class getExtends() {
        return extended;
    }
    public void setExtends(final Class extended) {
        this.extended = extended;
    }

    public Set<Class> getImplements() {
        return implemented;
    }
    public void addImplementation(final Class cls) {
        if (!cls.isInterface())
            throw new IllegalArgumentException("Interface must be specified");
        this.implemented.add( cls );
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
        out.write(" class");
        out.write(clsName);
        if (extended!=null) {
            out.write(' ');
            
        }
    }
}
