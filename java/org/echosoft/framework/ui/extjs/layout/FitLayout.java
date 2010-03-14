package org.echosoft.framework.ui.extjs.layout;

/**
 * Описывает характеристики менеджера раскладки <code>Ext.layout.FitLayout</code>.
 * @author Anton Sharapov
 */
public class FitLayout extends Layout {

    public LayoutItem makeItem() {
        return new FitLayoutItem();
    }

    public LayoutItem makeItem(final LayoutItem item) {
        return new FitLayoutItem(item);
    }

    public String getLayout() {
        return "fit";
    }

}