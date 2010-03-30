package org.echosoft.framework.ui.extjs.widgets;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.AbstractContainerComponent;
import org.echosoft.framework.ui.extjs.model.DomElement;
import org.echosoft.framework.ui.extjs.model.Template;

/**
 * Простейший контейнер компонент. 
 * @author Anton Sharapov
 */
public class Container extends AbstractContainerComponent {

    private DomElement autoEl;
    private Template template;
    private Object data;

    public Container() {
        this(null);
    }
    public Container(final ComponentContext ctx) {
        super(ctx);
    }

    public DomElement getAutoEl() {
        return autoEl;
    }
    public void setAutoEl(final DomElement autoEl) {
        this.autoEl = autoEl;
    }

    public String getHtml() {
        return autoEl!=null ? autoEl.getHtml() : null;
    }
    public void setHtml(final String html) {
        if (autoEl==null) {
            autoEl = new DomElement();
        }
        autoEl.setHtml(html);
    }

    public Template getTemplate() {
        return template;
    }
    public void setTemplate(final Template template) {
        this.template = template;
    }

    public Object getData() {
        return data;
    }
    public void setData(final Object data) {
        this.data = data;
    }


    @Override
    public void invoke(final JsonWriter out) throws Exception {
        out.beginObject();
        out.writeProperty("xtype", "container");
        renderAttrs(out);
        if (autoEl !=null) {
            if (autoEl.hasContentOnly()) {
                out.writeProperty("html", autoEl.getHtml());
            } else {
                out.writeProperty("autoEl", autoEl);
            }
        }
        if (template!=null)
            out.writeProperty("tpl", template);
        if (data!=null)
            out.writeProperty("data", data);
        out.endObject();
    }

}
