package org.echosoft.framework.ui.core.compiler.binding;

import java.text.ParseException;
import java.util.ArrayList;

import org.echosoft.common.utils.BeanUtil;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.Scope;

/**
 * @author Anton
 */
public final class SimpleExpression implements Expression {

    /**
     * Оригинальный текст вычисляемого выражения.
     */
    private final String expr;

    /**
     * Разобранная версия выражения.
     */
    private final Chunk[] chunks;

    private static final class Chunk {
        private final Scope scope;
        private final String attr;
        private final String property;
        public Chunk(final Scope scope, final String attr, final String property) {
            if (scope==null || attr==null)
                throw new IllegalArgumentException("Scope and attribute in scope must be specified");
            this.scope = scope;
            this.attr = attr;
            this.property = property;
        }
    }

    public SimpleExpression(final String expr) {
        this.expr = expr;
        this.chunks = parseExpression(expr);
    }

    @Override
    public Object evaluate(final ComponentContext ctx) throws Exception {
        if (chunks==null)
            throw new ParseException("Illegal expression: "+expr,0);
        for (final Chunk chunk : chunks) {
            Object result = ctx.getAttribute(chunk.attr, chunk.scope);
            if (chunk.property!=null)
                result = BeanUtil.getProperty(result, chunk.property);
            if (result!=null)
                return result;
        }
        return null;
    }

    @Override
    public String getExpressionString() {
        return expr;
    }


    private static Chunk[] parseExpression(final String expr) {
        try {
            final ArrayList<Chunk> result = new ArrayList<Chunk>(4);
            int s = 0, e=expr.length()-1;
            Chunk chunk;
            for (int d=expr.indexOf("|",s);  d>=s && d<e;  d=expr.indexOf('|',s)) {
                if (d>s) {
                    chunk = parseChunk(expr.substring(s,d));
                    if (chunk!=null)
                        result.add( chunk );
                }
                s = d+1;
            }
            if (s<e) {
                chunk = parseChunk( expr.substring(s,e) );
                if (chunk!=null)
                    result.add( chunk );
            }
            return result.toArray(new Chunk[result.size()]);
        } catch (Exception e) {
            return null;
        }
    }
    private static Chunk parseChunk(final String expr) throws Exception {
        final int ss = expr.indexOf(':');
        final Scope scope;
        if (ss>=0) {
            scope = Scope.valueOf( expr.substring(0,ss).trim() );
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
        return new Chunk(scope, name, property);
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
