package org.echosoft.framework.ui.extjs.widgets.form;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.layout.FormLayout;
import org.echosoft.framework.ui.extjs.widgets.Panel;

/**
 * Описывает группу полей расположенных под единым заголовком.
 * @author Anton Sharapov
 */
public class FieldSet extends Panel {

    private boolean checkboxToggle;         // возможность сворачивать панель по клику на чекбоксе, расположенному рядом с заголовком.

    public FieldSet() {
        this(null);
    }
    public FieldSet(final ComponentContext ctx) {
        super(ctx);
        setLayout( new FormLayout() );
    }

    /**
     * Свойство дает возможность сворачивать/разворачивать панель по клику на чекбоксе,
     * расположенному в заголовке панели.
     * @return <code>true</code> если такая возможность есть.
     */
    public boolean isCheckboxToggle() {
        return checkboxToggle;
    }
    public void setCheckboxToggle(final boolean checkboxToggle) {
        this.checkboxToggle = checkboxToggle;
    }


    @Override
    public void invoke(final JsonWriter out) throws Exception {
        out.beginObject();
        out.writeProperty("xtype", "fieldset");
        renderContent(out);
        out.endObject();
    }

    @Override
    protected void renderContent(final JsonWriter out) throws Exception {
        super.renderContent(out);
        if (checkboxToggle)
            out.writeProperty("checkboxToggle", true);

        final ComponentContext ctx = getContext();
        if (ctx!=null) {
            ctx.getResources().attachScript(ctx.encodeThemeURL("/pkgs/pkg-forms.js", false));
            ctx.getResources().attachScript(ctx.encodeThemeURL("/ux/form-plugins.js", false));
        }
    }

}
