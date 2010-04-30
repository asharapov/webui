package org.echosoft.framework.ui.core.compiler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.echosoft.common.utils.StringUtil;

/**
 * Содержит информацию о всех переменных объявленных в отдельном транслируемом методе.
 * @author Anton Sharapov
 */
public class MethodContext {

    public final TranslationContext tc;
    public final Map<Class, Collection<Variable>> vars;
    private int vLevel;

    public MethodContext(final TranslationContext tc) {
        this.tc = tc;
        this.vars = new HashMap<Class,Collection<Variable>>();
        this.vLevel = 0;
    }

    public void incLevel() {
        vLevel++;
    }
    public void decLevel() {
        if (vLevel <=0)
            throw new IllegalStateException();
        vLevel--;
        for (Collection<Variable> list : vars.values()) {
            for (Variable v : list) {
                if (v.visibilityLevel!=null && v.visibilityLevel> vLevel) {
                    v.visibilityLevel = null;
                    v.used = false;
                }
            }
        }
    }

    public Variable allocateVariable(final Class cls, final String desiredName) {
        Collection<Variable> list = vars.get(cls);
        if (list==null) {
            list = new ArrayList<Variable>();
            vars.put(cls, list);
        }
        final String prefix = translateName(desiredName);
        Variable v;
        if ((v=findVariable(list, prefix))==null) {
            final boolean qualified = !tc.ensureClassImported(cls);
            v = new Variable(prefix, cls, qualified, vLevel);
        } else {
            int s = 2;
            while (findVariable(list, prefix+s)!=null) {
                s++;
            }
            v = new Variable(prefix+s, cls, v.qualified, vLevel);
        }
        list.add( v );
        return v;
    }

    private String translateName(String desiredName) {
        desiredName = StringUtil.trim(desiredName);
        final StringBuilder buf = new StringBuilder();
        if (desiredName!=null)
        for (int i=0,l=desiredName.length(); i<l; i++) {
            char c = desiredName.charAt(i);
            if (i==0) {
                if (Character.isJavaIdentifierStart(c)) {
                    buf.append(c);
                }
            } else {
                if (Character.isJavaIdentifierPart(c)) {
                    buf.append(c);
                } else
                if (c=='-' || c==':') {
                    buf.append('_');
                }
            }
        }
        return buf.length()>0 ? buf.toString() : "cmp";
    }

    private Variable findVariable(final Collection<Variable> vars, final String name) {
        for (Variable v : vars) {
            if (v.name.equals(name))
                return v;
        }
        return null;
    }
}
