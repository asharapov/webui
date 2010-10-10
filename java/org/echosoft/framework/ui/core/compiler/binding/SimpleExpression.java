package org.echosoft.framework.ui.core.compiler.binding;

import java.text.ParseException;
import java.util.ArrayList;

import org.echosoft.common.utils.BeanUtil;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.RequestContext;
import org.echosoft.framework.ui.core.Scope;

/**
 * @author Anton Sharapov
 */
public class SimpleExpression implements ValueExpression {

    /**
     * Оригинальный текст вычисляемого выражения.
     */
    private final String expr;

    /**
     * Разобранная версия выражения.
     */
    private final Choice[] choices;

    /**
     * Если в процессе разбора выражения ошибки возникла ошибка то сохраняем информацию о ней
     * с тем, чтобы бросить ее наверх при первом обращении к методу {@link #evaluate(RequestContext)}.
     * Такая отложенная реакция на ошибки используется для того чтобы мы могли спокойно создавать определения любых выражений где-нибудь в объявлениях статических полей класса
     * без риска в тот же момент свалиться по ошибке.
     */
    private Exception parseException;

    private static final class Choice {
        private final Scope scope;
        private final String attr;
        private final String property;
        public Choice(final Scope scope, final String attr, final String property) {
            if (scope==null || attr==null)
                throw new IllegalArgumentException("Scope and attribute in scope must be specified");
            this.scope = scope;
            this.attr = attr;
            this.property = property;
        }
        public String toString() {
            return "[Choice{scope:"+scope+", attr:"+attr+", property:"+property+"}]";
        }
    }

    public SimpleExpression(final String expr) {
        this.expr = expr;
        Choice[] choices;
        try {
            choices = parseExpression(expr);
        } catch (Exception e) {
            this.parseException = e;
            choices = null;
        }
        this.choices = choices;
    }

    @Override
    public Object evaluate(final RequestContext ctx) throws Exception {
        if (choices == null) {
            throw parseException!=null ? parseException : new ParseException("Illegal expression: @{"+expr+"}",0);
        }
        for (final Choice choice : choices) {
            Object result = ctx.getAttribute(choice.attr, choice.scope);
            if (choice.property!=null)
                result = BeanUtil.getProperty(result, choice.property);
            if (result!=null)
                return result;
        }
        return null;
    }

    @Override
    public String getExpressionString() {
        return expr;
    }


    private static Choice[] parseExpression(String expr) throws Exception {
        final ArrayList<Choice> result = new ArrayList<Choice>(4);
        if (expr!=null) {
            if (expr.startsWith("@{") && expr.endsWith("}")) {
                expr = expr.substring(2,expr.length()-1);
            }
            int s = 0, e=expr.length()-1;
            Choice choice;
            for (int d=expr.indexOf("|",s);  d>=s && d<e;  d=expr.indexOf('|',s)) {
                if (d>s) {
                    choice = parseChunk(expr.substring(s,d));
                    if (choice !=null)
                        result.add(choice);
                }
                s = d+1;
            }
            if (s<e) {
                choice = parseChunk( expr.substring(s) );
                if (choice !=null)
                    result.add(choice);
            }
        }
        return result.toArray(new Choice[result.size()]);
    }
    private static Choice parseChunk(final String expr) throws Exception {
        final int ss = expr.indexOf(':');
        final Scope scope;
        if (ss>=0) {
            scope = Scope.valueOf( expr.substring(0,ss).trim().toUpperCase() );
        } else {
            scope = Scope.REQUEST;  // пространство имен по умолчанию.
        }
        String name = expr.substring(ss+1).trim();
        String property = null;
        if (!scope.isKeepStringsOnly()) {
            final int length = name.length();
            for (int i=0; i<length; i++) {
                final char c = name.charAt(i);
                if (c==BeanUtil.NESTED_DELIM) {
                    property = StringUtil.trim( name.substring(i+1) );
                    name = StringUtil.trim( name.substring(0, i) );
                    break;
                }
            }
        }
        return new Choice(scope, name, property);
    }


    @Override
    public int hashCode() {
        return expr!=null ? expr.hashCode() : 0;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!getClass().equals(obj.getClass()))
            return false;
        final SimpleExpression other = (SimpleExpression)obj;
        return expr!=null ? expr.equals(other.expr) : other.expr==null;
    }

    @Override
    public String toString() {
        return "[SimpleExpression{"+expr+"}]";
    }

}
