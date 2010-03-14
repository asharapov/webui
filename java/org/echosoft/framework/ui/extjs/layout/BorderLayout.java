package org.echosoft.framework.ui.extjs.layout;

/**
 * Описывает характеристики менеджера раскладки <code>Ext.layout.BorderLayout</code>.
 * @author Anton Sharapov
 */
public class BorderLayout extends Layout {

    public BorderLayout() {
    }

    public LayoutItem makeItem() {
        return new BorderLayoutItem();
    }

    public LayoutItem makeItem(final LayoutItem item) {
        return new BorderLayoutItem(item);
    }

    public String getLayout() {
        return "border";
    }

}