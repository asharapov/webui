package org.echosoft.framework.ui.core.compiler.ast;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.compiler.Variable;

/**
 * Содержит описание метода класса.
 * @author Anton Sharapov
 */
public class MethodNode extends ASTNode implements LocalVariablesManager {

    private Class cls;
    private String name;
    private Visibility visibility;
    private boolean statical;
    private boolean unmodifiable;
    private boolean overrided;
    private final List<Variable> args;
    private final SortedSet<Class> exceptions;
    private final Map<String,Variable> accessibleVars;
    private final Map<String,Variable> allocatedVars;

    public MethodNode(Class cls, String name, Visibility visibility, boolean statical) {
        super();
        this.cls = cls;
        this.name = name;
        this.visibility = visibility;
        this.statical = statical;
        this.args = new ArrayList<Variable>();
        this.exceptions = new TreeSet<Class>(ASTNode.CLS_COMPARATOR);
        this.accessibleVars = new HashMap<String,Variable>();
        this.allocatedVars = new HashMap<String,Variable>();
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

    public List<Variable> getArguments() {
        return args;
    }
    public MethodNode addArgument(final Class cls, final String name, final boolean modifiable) {
        if (!StringUtil.isJavaIdentifier(name))
            throw new IllegalArgumentException("Illegal argument name");
        if (accessibleVars.containsKey(name))
            throw new IllegalArgumentException("Argument "+name+" already exists.");
        final Variable v = new Variable(cls, name, modifiable, false);
        args.add( v );
        accessibleVars.put(name, v);
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
    public MethodNode append(final ASTNode node) {
        if (!(node instanceof StatementNode))
            throw new IllegalArgumentException("Attempt to append illegal node to expression: "+node);
        return (MethodNode)super.append(node);

    }

    @Override
    public void translate(final Writer out) throws IOException {
        out.write('\n');
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
        out.write( cls!=null ? getRoot().ensureClassImported(cls) : "void" );
        out.write(' ');
        out.write(name);
        out.write('(');
        for (Iterator<Variable> itar=args.iterator(); itar.hasNext(); ) {
            final Variable arg = itar.next();
            if (!arg.modifiable)
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



    @Override
    public Map<String, Variable> getAccessibleVariables() {
        return accessibleVars;
    }

    @Override
    public Variable defineVariable(final Class cls, final String expectedName, final boolean modifiable, final boolean reusable) {
        final String name = findUnusedVariableName(expectedName);
        final Variable v = new Variable(cls, name, modifiable, reusable);
        allocatedVars.put(name, v);
        accessibleVars.put(name, v);
        return v;
    }

    @Override
    public Variable findUnusedVariable(final Class cls) {
        for (Variable v : allocatedVars.values()) {
            if (!v.used && v.cls==cls)
                return v;
        }
        return null;
    }

    @Override
    public String findUnusedVariableName(final String expectedName) {
        final Map<String,Variable> accessible = getAccessibleVariables();
        String name = StringUtil.isJavaIdentifier(expectedName) ? expectedName : "cmp";
        int counter = 1;
        while (accessible.containsKey(name)) {
            name += ++counter;
        }
        return name;
    }


    @Override
    protected void resetState() {
    }
}
