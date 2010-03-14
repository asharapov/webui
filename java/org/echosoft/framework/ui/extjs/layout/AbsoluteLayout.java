package org.echosoft.framework.ui.extjs.layout;

/**
 * Описывает характеристики менеджера раскладки <code>Ext.layout.AbsoluteLayout</code>.
 * @author Anton Sharapov
 */
public class AbsoluteLayout extends AnchorLayout {

    public LayoutItem makeItem() {
        return new AbsoluteLayoutItem();
    }

    public LayoutItem makeItem(final LayoutItem item) {
        return new AbsoluteLayoutItem(item);
    }

    public String getLayout() {
        return "absolute";
    }

}