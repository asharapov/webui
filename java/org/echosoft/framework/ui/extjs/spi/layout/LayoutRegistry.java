package org.echosoft.framework.ui.extjs.spi.layout;

import org.echosoft.framework.ui.extjs.layout.Layout;
import org.echosoft.framework.ui.extjs.layout.LayoutItem;

/**
 * Точка доступа для алгоритмов сериализации в JSON информации об используемых в компонентах менеджеров упаковки 
 * @author Anton Sharapov
 */
public class LayoutRegistry {

    public static void registerLayout(final Class<Layout> cfgCls, final Class<LayoutItem> itmCls, final LayoutRenderer renderer) {

    }

    public static LayoutRenderer getRenderer(final Layout config) {
        return null;
    }

    public static LayoutRenderer getRenderer(final LayoutItem item) {
        return null;
    }
}
