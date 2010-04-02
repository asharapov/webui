package org.echosoft.framework.ui.extjs.widgets;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.AbstractBoxComponent;

/**
 * Данный компонент является вспомогательным и предназначен для вставки разделителя
 * между элементами панели инструментов.
 * @see Toolbar
 * @author Anton Sharapov
 */
public class ToolbarSeparator extends AbstractBoxComponent {

    public ToolbarSeparator() {
        this(null);
    }
    public ToolbarSeparator(final ComponentContext ctx) {
        super(ctx);
    }


    public void invoke(final JsonWriter out) throws Exception {
        out.beginObject();
        out.writeProperty("xtype", "tbseparator");
        renderAttrs(out);
        out.endObject();
    }
}
