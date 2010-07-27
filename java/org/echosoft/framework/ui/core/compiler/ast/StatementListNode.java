package org.echosoft.framework.ui.core.compiler.ast;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.compiler.Variable;

/**
 * @author Anton Sharapov
 */
public class StatementListNode extends StatementNode implements LocalVariablesManager {

    private boolean leadIndent;
    private final Map<String,Variable> allocatedVars;
    private Map<String,Variable> accessibleVars;

    public StatementListNode() {
        super();
        allocatedVars = new HashMap<String,Variable>();
        leadIndent = true;
    }

    public StatementListNode noLeadIndent() {
        leadIndent = false;
        return this;
    }

    @Override
    public StatementListNode append(final ASTNode node) {
        return (StatementListNode)super.append(node);
    }

    @Override
    public void translate(final Writer out) throws IOException {
        if (leadIndent) {
            indent(out);
        }
        out.write("{\n");
        for (ASTNode node : children) {
            node.translate(out);
        }
        indent(out);
        out.write("}");
        if (leadIndent)
            out.write('\n');
    }

    @Override
    protected int getIndentLevel() {
        return leadIndent ? super.getIndentLevel() : getParent().getIndentLevel();
    }


    @Override
    public Map<String, Variable> getAccessibleVariables() {
        if (accessibleVars==null) {
            final Map<String,Variable> map = new HashMap<String,Variable>();
            final LocalVariablesManager parent = findParent(LocalVariablesManager.class);
            if (parent==null)
                throw new IllegalStateException("Operation accessible only for connected nodes");
            map.putAll(parent.getAccessibleVariables());
            map.putAll(allocatedVars);
            accessibleVars = map;
        }
        return accessibleVars;
    }

    @Override
    public Variable defineVariable(final Class cls, final String expectedName, final boolean modifiable, final boolean reusable) {
        final String name = findUnusedVariableName(expectedName);
        final Variable v = new Variable(cls, name, modifiable, reusable);
        allocatedVars.put(name, v);
        if (accessibleVars!=null)
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
        if (!allocatedVars.isEmpty())
            throw new IllegalStateException("Unable to reset state for nodes ");
        accessibleVars = null;
    }

    @Override
    protected void checkCandidateToChild(final ASTNode node) {
        if (!(node instanceof StatementNode))
            throw new IllegalArgumentException("Attempt to attach illegal node: "+node);
    }

}
