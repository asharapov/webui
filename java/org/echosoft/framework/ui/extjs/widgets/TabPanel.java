package org.echosoft.framework.ui.extjs.widgets;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.AbstractContainerComponent;

/**
 * @author Anton Sharapov
 */
public class TabPanel extends AbstractContainerComponent {

    private boolean enableTabScroll;
    private boolean layoutOnTabChange;
    private boolean plain;
    private boolean resizeTabs;
    private int tabWidth;
    private int minTabWidth;
    private Object tabPosition; // "top", "bottom"
    // todo: tbar, bbar, fbar
    // todo: items must be Panel instances.

    public TabPanel() {
        this(null);
    }
    public TabPanel(final ComponentContext ctx) {
        super(ctx);
    }


    public void invoke(final JsonWriter out) throws Exception {
        out.beginObject();
        out.writeProperty("xtype", "tabpanel");
        renderContent(out);
        out.endObject();
    }

    @Override
    protected void renderContent(final JsonWriter out) throws Exception {
        super.renderContent(out);
    }

}
