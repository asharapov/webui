package org.echosoft.framework.ui.extjs.widgets;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.AbstractBoxComponent;
import org.echosoft.framework.ui.extjs.model.DomElement;
import org.echosoft.framework.ui.extjs.model.Template;

/**
 * @author Anton Sharapov
 */
public class Box extends AbstractBoxComponent {

    private DomElement element;
    private Template template;
    private Object data;

    public Box() {
        this(null);
    }
    public Box(final ComponentContext ctx) {
        super(ctx);
    }

    public DomElement getElement() {
        return element;
    }
    public void setElement(final DomElement element) {
        this.element = element;
    }

    public String getHtml() {
        return element!=null ? element.getHtml() : null;
    }
    public void setHtml(final String html) {
        if (element==null) {
            element = new DomElement();
        }
        element.setHtml(html);
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
        out.writeProperty("xtype", "box");
        renderAttrs(out);
        if (element!=null)
            out.writeProperty("autoEl", element);
        if (template!=null)
            out.writeProperty("tpl", template);
        if (data!=null)
            out.writeProperty("data", data);
        out.endObject();
    }

}
