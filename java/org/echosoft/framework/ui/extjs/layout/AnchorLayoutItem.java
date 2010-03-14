package org.echosoft.framework.ui.extjs.layout;

import org.echosoft.common.utils.StringUtil;

/**
 * Данный бин описывает характеристики компонент специфичных при работе в контейнере вида <code>Ext.layout.AnchorLayout</code>.
 * @author Anton Sharapov
 */
public class AnchorLayoutItem extends LayoutItem {

    private String anchor;      // строка определяющая ширину и (опционально) высоту  относительно размеров контейнера в целом.

    public AnchorLayoutItem() {
        super();
    }

    public AnchorLayoutItem(final LayoutItem item) {
        super(item);
        if (item instanceof AnchorLayoutItem) {
            anchor = ((AnchorLayoutItem)item).anchor;
        }
    }

    /**
     * Определяет ширину и высоту компонента относительно размеров контейнера в целом.
     * @return  строка вида "30% 50%" или "-100 -50".
     */
    public String getAnchor() {
        return anchor;
    }
    /**
     * Определяет ширину и высоту компонента относительно размеров контейнера в целом.
     * @param anchor  строка вида "30% 50%" или "-100 -50".
     */
    public void setAnchor(final String anchor) {
        this.anchor = StringUtil.trim(anchor);
    }
}
