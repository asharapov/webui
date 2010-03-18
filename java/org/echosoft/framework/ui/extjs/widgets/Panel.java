package org.echosoft.framework.ui.extjs.widgets;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.AbstractContainerComponent;
import org.echosoft.framework.ui.extjs.AbstractExtJSComponent;

/**
 * @author Anton Sharapov
 */
public class Panel extends AbstractContainerComponent {

    private Boolean animCollapse;       // Следует ли использовать анимацию при сворачивании/разворачивании панели.
    private String baseCls;             // Составная часть имени CSS класса, применяемая к различным элементам панели.
    private boolean bodyBorder;

    public Panel(final ComponentContext ctx) {
        super(ctx);
    }

    public Boolean isAnimCollapse() {
        return animCollapse;
    }
    public void setAnimCollapse(final Boolean animCollapse) {
        this.animCollapse = animCollapse;
    }

    public String getBaseCssClass() {
        return baseCls;
    }
    public void setBaseCssClass(final String baseCls) {
        this.baseCls = baseCls;
    }

    @Override
    public void invoke(final JsonWriter out) throws Exception {
        out.beginObject();
        out.writeProperty("xtype", "panel");
        if (animCollapse !=null)
            out.writeProperty("animCollapse", animCollapse);
        if (baseCls!=null)
            out.writeProperty("baseCls", baseCls);
        renderAttrs(out);

        out.writeComplexProperty("items");
        out.beginArray();
        for (AbstractExtJSComponent item : getItems()) {
            item.invoke(out);
        }
        out.endArray();
        out.endObject();
    }
}
