package org.echosoft.framework.ui.extjs.layout;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.extjs.model.Margins;

/**
 * Данный бин описывает характеристики компонент специфичных при работе в контейнере вида <code>Ext.layout.BoxLayout</code>.
 * @author Anton Sharapov
 */
public class BoxLayoutItem extends LayoutItem {

    private int flex;               // TODO: хз что это такое...
    private Margins margins;        // отступы от границ контейнера.

    public BoxLayoutItem() {
        super();
        this.margins = new Margins();
    }

    public BoxLayoutItem(final LayoutItem item) {
        super(item);
        if (item instanceof BoxLayoutItem) {
            flex = ((BoxLayoutItem) item).flex;
            margins = new Margins(((BoxLayoutItem)item).margins);
        } else {
            margins = new Margins();
        }
    }


    public int getFlex() {
        return flex;
    }
    public void setFlex(int flex) {
        this.flex = flex;
    }

    /**
     * Определяет отступы данного компонента относительно границ контейнера.
     * @return  отступы относительно границ контейнера. Никогда не возвращает <code>null</code>.
     */
    public Margins getMargins() {
        return margins;
    }
    /**
     * Определяет отступы данного компонента относительно границ контейнера.
     * @param margins отступы относительно границ контейнера.
     */
    public void setMargins(final Margins margins) {
        this.margins = margins!=null ? margins : new Margins();
    }


    @Override
    public void serialize(final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException {
        super.serialize(out);
        if (flex!=0)
            out.writeProperty("flex", flex);
        if (!margins.isEmpty())
            out.writeProperty("margins", margins.encode());
    }
}