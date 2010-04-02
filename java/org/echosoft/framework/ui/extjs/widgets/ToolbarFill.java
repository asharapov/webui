package org.echosoft.framework.ui.extjs.widgets;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.AbstractBoxComponent;

/**
 * Данный компонент является вспомогательным и предназначен для отделения элементов панели инструментов
 * выравниваемых по левому краю панели от элементов, выравниваемых по правому краю панели.
 * @see Toolbar
 * @author Anton Sharapov
 */
public class ToolbarFill extends AbstractBoxComponent {

    public ToolbarFill() {
        this(null);
    }
    public ToolbarFill(final ComponentContext ctx) {
        super(ctx);
    }


    public void invoke(final JsonWriter out) throws Exception {
        out.writeObject("->");
    }
}