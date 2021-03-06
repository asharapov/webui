package org.echosoft.framework.ui.extjs.layout;

import java.util.ArrayList;
import java.util.List;

import org.echosoft.framework.ui.core.UIComponent;

/**
 * Описывает характеристики менеджера компоновки <code>Ext.layout.AutoLayout</code>.
 * @author Anton Sharapov
 */
public class AutoLayout extends Layout {

    private final List<UIComponent> items;

    public AutoLayout() {
        super();
        items = new ArrayList<UIComponent>();
    }

    @Override
    public int getItemsCount() {
        return items.size();
    }

    @Override
    public Iterable<UIComponent> getItems() {
        return items;
    }

    @Override
    public <T extends UIComponent> T append(final T item) {
        if (item==null)
            throw new IllegalArgumentException("Component must be specified");
        items.add(item);
        return item;
    }

    @Override
    protected String getLayout() {
        return "auto";
    }

}
