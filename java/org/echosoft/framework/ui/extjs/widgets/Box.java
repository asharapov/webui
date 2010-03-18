package org.echosoft.framework.ui.extjs.widgets;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.AbstractBoxComponent;
import org.echosoft.framework.ui.extjs.model.Template;

/**
 * @author Anton Sharapov
 */
public class Box extends AbstractBoxComponent {

    private String tag;
    private String html;
    private Template template;
    private Object templateData;

    public Box(final ComponentContext ctx) {
        super(ctx);
    }


    @Override
    public void invoke(final JsonWriter out) throws Exception {
        out.beginObject();
        out.writeProperty("xtype", "box");
        renderAttrs(out);
        out.endObject();
    }

}
