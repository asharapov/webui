package org.echosoft.framework.ui.core.compiler.xml;

import org.echosoft.common.model.Predicate;
import org.echosoft.framework.ui.core.compiler.ast.Variable;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTBlockStmt;
import org.xml.sax.Attributes;

/**
 * Содержит всю информацию используемую при обработке отдельного xml тега из исходного .wui файла.
 * @author Anton Sharapov
 */
public class BaseTag implements Tag {

    private final Tag parent;
    private final String uri;
    private final String qname;
    private final String name;
    private final Attributes attrs;
    private final TagHandler handler;

    private ASTBlockStmt container;
    private Variable bean;
    private Variable context;
    private Object data;

    public BaseTag(final Tag parent, final String uri, final String qname, final String name, final Attributes attrs, final TagHandler handler) {
        this.parent = parent;
        this.uri = uri;
        this.qname = qname;
        this.name = name;
        this.attrs = attrs;
        this.handler = handler;
        this.container = parent.getContainer();
        this.bean = null;
        this.context = parent.getContext();
    }

    public BaseTag(final ASTBlockStmt container, final Variable context) {
        this.parent = null;
        this.uri = null;
        this.qname = null;
        this.name = null;
        this.attrs = null;
        this.handler = null;
        this.container = container;
        this.bean = null;
        this.context = context;
    }

    @Override
    public Tag getParent() {
        return parent;
    }

    @Override
    public String getURI() {
        return uri;
    }

    @Override
    public String getQName() {
        return qname;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Attributes getAttrs() {
        return attrs;
    }

    @Override
    public TagHandler getHandler() {
        return handler;
    }


    @Override
    public ASTBlockStmt getContainer() {
        return container;
    }
    @Override
    public void setContainer(final ASTBlockStmt container) {
        this.container = container;
    }

    @Override
    public Variable getBean() {
        return bean;
    }
    @Override
    public void setBean(final Variable bean) {
        this.bean = bean;
    }

    @Override
    public Variable getContext() {
        return context;
    }
    @Override
    public void setContext(final Variable context) {
        this.context = context;
    }

    @Override
    public Object getData() {
        return data;
    }
    @Override
    public void setData(final Object data) {
        this.data = data;
    }

    @Override
    public Tag findParent(final Predicate<Tag> predicate) {
        for (Tag t=this; t!=null; t=t.getParent()) {
            if (predicate.accept(t))
                return t;
        }
        return null;
    }

}
