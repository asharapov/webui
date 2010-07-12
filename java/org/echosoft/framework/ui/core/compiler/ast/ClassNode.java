package org.echosoft.framework.ui.core.compiler.ast;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Anton Sharapov
 */
public class ClassNode extends ASTNode {

    private static final Comparator<Class> CLS_COMPARATOR =
            new Comparator<Class>() {
                public int compare(final Class o1, final Class o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            };

    private String pkgName;
    private String clsName;
    private boolean abstrct;
    private boolean fin;
    private boolean stat;
    private Visibility visibility;
    private Class extended;
    private final TreeSet<Class> implemented;
    private final TreeSet<Class> imports;

    public ClassNode(String pkgName, String clsName, Class extended) {
        this.pkgName = pkgName!=null ? pkgName.trim() : "";
        this.clsName = clsName;
        this.extended = extended;
        this.visibility = Visibility.PUBLIC;
        this.implemented = new TreeSet<Class>(CLS_COMPARATOR);
        this.imports = new TreeSet<Class>(CLS_COMPARATOR);
    }

    public String getPackageName() {
        return pkgName;
    }
    public void setPackageName(final String pkgName) {
        this.pkgName = pkgName!=null ? pkgName.trim() : "";
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
        final char[] prefix = new char[getLevel()*4];
        Arrays.fill(prefix, ' ');
        if (pkgName.length()>0) {
            out.write(prefix);
            out.write("package ");
            out.write(pkgName);
            out.write(";\n\n");
        }

        for (Class cls : imports) {
            out.write("import ");
            out.write(cls.getCanonicalName());
            out.write(";\n");
        }
        out.write('\n');
        out.write("public final class ");

    }
}
