package org.echosoft.framework.ui.core.compiler.ast;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Anton Sharapov
 */
public class MethodNode extends ASTNode {

    private static final class Argument implements Serializable {
        private final Class cls;
        private final String name;
        private final boolean unmodifiable;
        private Argument(Class cls, String name, boolean unmodifiable) {
            this.cls = cls;
            this.name = name;
            this.unmodifiable = unmodifiable;
        }
    }

    private Class cls;
    private String name;
    private Visibility visibility;
    private boolean statical;
    private boolean unmodifiable;
    private boolean overrided;
    private final List<Argument> args;
    private final SortedSet<Class> exceptions;

    public MethodNode(Class cls, String name, Visibility visibility, boolean statical) {
        this.cls = cls;
        this.name = name;
        this.visibility = visibility;
        this.statical = statical;
        this.args = new ArrayList<Argument>();
        this.exceptions = new TreeSet<Class>(ASTNode.CLS_COMPARATOR);
    }

    public Class getType() {
        return cls;
    }
    public MethodNode setType(Class cls) {
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
        return statical;
    }
    public MethodNode setStatic(boolean statical) {
        this.statical = statical;
        return this;
    }

    public boolean isFinal() {
        return unmodifiable;
    }
    public MethodNode setFinal(boolean unmodifiable) {
        this.unmodifiable = unmodifiable;
        return this;
    }

    public boolean isOverrided() {
        return overrided;
    }
    public MethodNode setOverrided(boolean overrided) {
        this.overrided = overrided;
        return this;
    }

    public List<Argument> getArguments() {
        return args;
    }
    public MethodNode addArgument(final Class cls, final String name, final boolean unmodifiable) {
        args.add( new Argument(cls, name, unmodifiable) );
        return this;
    }

    public Set<Class> getExceptions() {
        return exceptions;
    }
    public <T extends Throwable> MethodNode addException(final Class<T> errCls) {
        if (!Throwable.class.isAssignableFrom(errCls))
            throw new IllegalStateException("Throwable class must be specified");
        this.exceptions.add( errCls );
        return this;
    }

    @Override
    public ASTNode append(final ASTNode node) {
        return super.append(node);
    }

    @Override
    public void translate(final Writer out) throws IOException {
        if (overrided) {
            indent(out);
            out.write("@Override\n");
        }
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
        out.write('(');
        for (Iterator<Argument> itar=args.iterator(); itar.hasNext(); ) {
            final Argument arg = itar.next();
            if (arg.unmodifiable)
                out.write("final ");
            out.write( getRoot().ensureClassImported(arg.cls) );
            out.write(' ');
            out.write(arg.name);
            if (itar.hasNext()) {
                out.write(", ");
            }
        }
        out.write(')');
        if (exceptions.size()>0) {
            out.write(" throws ");
            for (Iterator<Class> itcl=exceptions.iterator(); itcl.hasNext(); ) {
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
