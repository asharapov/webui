package org.echosoft.framework.ui.extjs.widgets;

import java.util.Set;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.AbstractContainerComponent;
import org.echosoft.framework.ui.extjs.layout.ToolbarLayout;

/**
 * Панель инструментов.
 * @author Anton Sharapov
 */
public class Toolbar extends AbstractContainerComponent {

    public static enum Align {
        LEFT, CENTER, RIGHT
    }
    public static final Set<String> EVENTS = StringUtil.asUnmodifiableSet(AbstractContainerComponent.EVENTS, "overflowchange");

    private Align buttonAlign;              // способ выравнивания кнопок в тулбаре
    private boolean enableOverflow;         // требуется ли обработка выхода кнопок за пределы тулбара

    public Toolbar() {
        this(null);
    }
    public Toolbar(final ComponentContext ctx) {
        super(ctx);
        buttonAlign = Align.LEFT;
        setLayout( new ToolbarLayout() );
    }

    public Align getButtonAlign() {
        return buttonAlign;
    }
    public void setButtonAlign(final Align buttonAlign) {
        this.buttonAlign = buttonAlign!=null ? buttonAlign : Align.LEFT;
    }

    public boolean isEnableOverflow() {
        return enableOverflow;
    }
    public void setEnableOverflow(final boolean enableOverflow) {
        this.enableOverflow = enableOverflow;
    }

    @Override
    public void invoke(final JsonWriter out) throws Exception {
        final ComponentContext ctx = getContext();
        ctx.getResources().attachScript( ctx.encodeThemeURL("/pkgs/pkg-toolbars.js",false) );
        out.beginObject();
        out.writeProperty("xtype", "toolbar");
        renderAttrs(out);
        out.endObject();
    }

    @Override
    protected Set<String> getSupportedEvents() {
        return EVENTS;
    }

    @Override
    protected void renderAttrs(final JsonWriter out) throws Exception {
        super.renderAttrs(out);
        if (buttonAlign!=Align.LEFT)
            out.writeProperty("buttonAlign", buttonAlign);
        if (enableOverflow)
            out.writeProperty("enableOverflow", true);
    }
}
