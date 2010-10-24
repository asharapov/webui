package org.echosoft.framework.ui.core.compiler.ast.type;

import org.echosoft.framework.ui.core.compiler.ast.visitors.GenericVisitor;
import org.echosoft.framework.ui.core.compiler.ast.visitors.VoidVisitor;

/**
 * 
 * @author Anton Sharapov
 */
public final class SimpleTypeArgument extends TypeArgument {

    private Type type;

    public SimpleTypeArgument(final Type type) {
        if (type!=null) {
            this.type = type;
            this.type.setParent(this);
        }
    }

    public SimpleTypeArgument(final Class type) {
        if (type!=null) {
            this.type = new Type(type);
            this.type.setParent(this);
        }
    }


    public Type getType() {
        return type;
    }
    public void setType(final Type type) {
        this.type = type;
        if (this.type!=null)
            this.type.setParent(this);
    }
    public void setType(final Class type) {
        if (type!=null) {
            this.type = new Type(type);
            this.type.setParent(this);
        } else {
            this.type = null;
        }
    }


    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }
}
