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
    private String bodyCssClass;        // CSS класс применяемый к основному телу панели.
    private String bodyStyle;           // CSS стиль применяемый к основному телу панели.
    private String title;               // Заголовок панели.
    private boolean collapsible;        // Может ли содержимое панели схлопываться.
    private boolean collapsed;          // По умолчанию содержимое панели схлопнуто ?
    private boolean titleCollapse;      // Позволять ли схлопывание панели по клику на любое место заголовка ?.
    private boolean frame;              // Использовать или нет продвинутое оформление панели.
    private boolean border;             // Выделять или нет границы панели.
    private boolean preventBodyReset;   // Сброс ExtJS стилей в основном теле панели (имеет смысл если мы там отображаем HTML текст)
    private Toolbar tbar;               // Панель инструментов сверху панели.
    private Toolbar bbar;               // Панель инструментов внизу панели.
    private Toolbar fbar;               // Панель инструментов в самом низу панели.
    // и еще PanelTool's надо указать.
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


    public String getTitle() {
        return title;
    }
    public void setTitle(final String title) {
        this.title = title;
    }

    public boolean isCollapsible() {
        return collapsible;
    }
    public void setCollapsible(final boolean collapsible) {
        this.collapsible = collapsible;
    }

    public boolean getFrame() {
        return frame;
    }
    public void setFrame(final boolean frame) {
        this.frame = frame;
    }

    public boolean getBorder() {
        return border;
    }
    public void setBorder(final boolean border) {
        this.border = border;
    }

    public Toolbar getTopToolBar() {
        return tbar;
    }
    public void setTobToolbar(final Toolbar tbar) {
        this.tbar = tbar;
    }

    public Toolbar getBottomToolbar(final Toolbar bbar) {
        return bbar;
    }
    public void setBottomToolbar(final Toolbar bbar) {
        this.bbar = bbar;
    }

    public Toolbar getFooter() {
        return fbar;
    }
    public void setFooter(final Toolbar fbar) {
        this.fbar = fbar;
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
        out.endObject();
    }

    @Override
    public void renderAttrs(final JsonWriter out) throws Exception {
        super.renderAttrs(out);
        if (!animCollapse)
            out.writeProperty("animCollapse", false);
        if (title!=null)
            out.writeProperty("title", title);
        if (collapsible)
            out.writeProperty("collapsible", true);
        if (frame)
            out.writeProperty("frame", true);
        if (!border)
            out.writeProperty("border", false);
        if (tbar!=null) {
            out.writeComplexProperty("tbar");
            tbar.invoke(out);
        }
        if (bbar!=null) {
            out.writeComplexProperty("bbar");
            bbar.invoke(out);
        }
        if (fbar!=null) {
            out.writeComplexProperty("fbar");
            fbar.invoke(out);
        }

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
    }
}
