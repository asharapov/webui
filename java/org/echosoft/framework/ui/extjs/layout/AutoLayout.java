package org.echosoft.framework.ui.extjs.layout;

/**
 * Описывает характеристики менеджера раскладки <code>Ext.layout.AutoLayout</code>.
 * @author Anton Sharapov
 */
public class AutoLayout extends Layout {

    public LayoutItem makeItem() {
        return new AutoLayoutItem();
    }

    public LayoutItem makeItem(final LayoutItem item) {
        return new AutoLayoutItem(item);
    }

    public String getLayout() {
        return "auto";
    }

}
