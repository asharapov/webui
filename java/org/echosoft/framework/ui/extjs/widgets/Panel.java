package org.echosoft.framework.ui.extjs.widgets;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.AbstractContainerComponent;
import org.echosoft.framework.ui.extjs.model.DomElement;
import org.echosoft.framework.ui.extjs.model.Template;

/**
 * @author Anton Sharapov
 */
public class Panel extends AbstractContainerComponent {

    private boolean animCollapse;       // Следует ли использовать анимацию при сворачивании/разворачивании панели.
    private String baseCls;             // Составная часть имени CSS класса, применяемая к различным элементам панели.
    private String title;
    private boolean frame;
    private boolean collapsible;
    private boolean border;

    private DomElement autoEl;
    private Template template;
    private Object data;

    public Panel() {
        this(null);
    }
    public Panel(final ComponentContext ctx) {
        super(ctx);
        this.animCollapse = true;
        this.border = true;
    }

    public boolean isAnimCollapse() {
        return animCollapse;
    }
    public void setAnimCollapse(final boolean animCollapse) {
        this.animCollapse = animCollapse;
    }

    public String getBaseCssClass() {
        return baseCls;
    }
    public void setBaseCssClass(final String baseCls) {
        this.baseCls = baseCls;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(final String title) {
        this.title = title;
    }

    public boolean getBorder() {
        return border;
    }
    public void setBorder(final boolean border) {
        this.border = border;
    }

    public boolean getFrame() {
        return frame;
    }
    public void setFrame(final boolean frame) {
        this.frame = frame;
    }

    public boolean isCollapsible() {
        return collapsible;
    }
    public void setCollapsible(final boolean collapsible) {
        this.collapsible = collapsible;
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
        out.writeProperty("xtype", "panel");
        renderAttrs(out);
        if (!animCollapse)
            out.writeProperty("animCollapse", false);
        if (baseCls!=null)
            out.writeProperty("baseCls", baseCls);
        if (title!=null)
            out.writeProperty("title", title);
        if (!border)
            out.writeProperty("border", false);
        if (frame)
            out.writeProperty("frame", true);
        if (collapsible)
            out.writeProperty("collapsible", true);

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
