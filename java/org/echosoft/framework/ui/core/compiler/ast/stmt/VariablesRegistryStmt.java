package org.echosoft.framework.ui.core.compiler.ast.stmt;

import java.util.HashMap;
import java.util.Map;

import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.compiler.ast.ASTNode;
import org.echosoft.framework.ui.core.compiler.ast.Variable;
import org.echosoft.framework.ui.core.compiler.ast.VariablesContainer;
import org.echosoft.framework.ui.core.compiler.ast.VariablesRegistry;
import org.echosoft.framework.ui.core.compiler.ast.type.RefType;

/**
 * Базовый абстрактный класс для тех выражений java, которые могут содержать локальные переменные.
 * Ведет учет всех локальных переменных доступных в данном блоке кода и его дочерних блоках.
 * @author Anton Sharapov
 */
public abstract class VariablesRegistryStmt extends Statement implements VariablesRegistry {

    private Map<String, Variable> allocatedVars;

    @Override
    public Variable defineVariable(final RefType type, final String expectedName, final boolean reusable) {
        final String name = findUnusedVariableName(expectedName);
        final Variable v = new Variable(type, name, reusable);
        if (allocatedVars==null)
            allocatedVars = new HashMap<String,Variable>();
        allocatedVars.put(name, v);
        return v;
    }

    @Override
    public Variable findUnusedVariable(final RefType type) {
        if (allocatedVars!=null) {
            for (Variable v : allocatedVars.values()) {
                if (!v.isUsed() && v.isReusable() && v.getType().equals(type))
                    return v;
            }
        }
        return null;
    }

    @Override
    public String findUnusedVariableName(final String expectedName) {
        String name = StringUtil.isJavaIdentifier(expectedName) ? expectedName : "cmp";
        int counter = 1;
        while( variableExists(name) ) {
            name += ++counter;
        }
        return name;
    }

    @Override
    public boolean containsVariable(final String name) {
        return allocatedVars!=null && allocatedVars.containsKey(name);
    }

    private boolean variableExists(final String name) {
        if (allocatedVars!=null && allocatedVars.containsKey(name)) {
            return true;
        }
        for (ASTNode node=this.getParent(); (node instanceof VariablesContainer); node=node.getParent()) {
            final VariablesContainer vh = (VariablesContainer)node;
            if (vh.containsVariable(name)) {
                return true;
            }
        }
        return false;
    }

}
