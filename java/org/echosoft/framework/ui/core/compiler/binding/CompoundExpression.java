package org.echosoft.framework.ui.core.compiler.binding;

import java.util.ArrayList;

import org.echosoft.common.io.FastStringWriter;
import org.echosoft.framework.ui.core.RequestContext;

/**
 * @author Anton Sharapov
 */
public class CompoundExpression implements ValueExpression {

    /**
     * Оригинальный текст вычисляемого выражения.
     */
    private final String compoundExpr;
    private final ValueExpression[] expressions;

    public CompoundExpression(final String text) {
        this.compoundExpr = text;
        final ArrayList<ValueExpression> items = new ArrayList<ValueExpression>();
        if (text!=null) {
            final int len = text.length();
            int start = 0, ts = -1;
            char cc = (char)0;
            for (int i=0; i<len; i++) {
                final char c = text.charAt(i);
                if (c=='{' && cc=='@') {
                    ts = i-1;
                } else
                if (c=='}' && ts>=start) {
                    if (ts>start) {
                        items.add( new StaticExpression(text.substring(start,ts)) );
                    }
                    items.add( new SimpleExpression(text.substring(ts+2,i)) );
                    start = i+1;
                    ts = i;
                }
                cc = c;
            }
            if (start<len) {
                items.add( new StaticExpression(text.substring(start)) );
            }
        }
        this.expressions = items.toArray(new ValueExpression[items.size()]);
    }

    @Override
    public Object evaluate(final RequestContext ctx) throws Exception {
        switch (expressions.length) {
            case 0 : return null;
            case 1 : return expressions[0].evaluate(ctx);
            default: {
                final FastStringWriter buf = new FastStringWriter();
                for (ValueExpression expr : expressions) {
                    final Object result = expr.evaluate(ctx);
                    if (result!=null)
                        buf.write( result.toString() );
                }
                return buf.toString();
            }
        }
    }

    @Override
    public String getExpressionString() {
        return compoundExpr;
    }

    @Override
    public int hashCode() {
        return compoundExpr !=null ? compoundExpr.hashCode() : 0;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!getClass().equals(obj.getClass()))
            return false;
        final CompoundExpression other = (CompoundExpression)obj;
        return compoundExpr !=null ? compoundExpr.equals(other.compoundExpr) : other.compoundExpr ==null;
    }

    @Override
    public String toString() {
        return "[CompoundExpression{"+ compoundExpr +"}]";
    }

}
