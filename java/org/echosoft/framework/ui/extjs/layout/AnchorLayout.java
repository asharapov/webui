package org.echosoft.framework.ui.extjs.layout;

import org.echosoft.framework.ui.extjs.model.Size;

/**
 * Описывает характеристики менеджера раскладки <code>Ext.layout.AnchorLayout</code>.
 * @author Anton Sharapov
 */
public class AnchorLayout extends Layout {

    private Size anchorSize;        // виртуальный размер контейнера или null.  //TODO ExtJS: свойство должно быть в контексте контейнера, не упаковщика!!!

    public LayoutItem makeItem() {
        return new AnchorLayoutItem();
    }

    public LayoutItem makeItem(final LayoutItem item) {
        return new AnchorLayoutItem(item);
    }

    public String getLayout() {
        return "anchor";
    }

    /**
     * Использование данного свойства дает возможность переопределить виртуальный размер контейнера компонент,
     * то есть все размеры компонент в этом контейнере расчитываются исходя не из реальных размеров контейнера а исходя из указанных в этом свойстве.
     * @return  виртуальные размеры контейнера или <code>null</code> если расчет размеров компонент должен вестись основываясь на реальных размерах контейнера.
     */
    public Size getAnchorSize() {
        return anchorSize;
    }
    /**
     * Дает возможность переопределить виртуальный размер контейнера компонент,
     * то есть все размеры компонент в этом контейнере расчитываются исходя не из реальных размеров контейнера а исходя из указанных в этом свойстве.
     * @param anchorSize  виртуальные размеры контейнера или <code>null</code> если расчет размеров компонент должен вестись основываясь на реальных размерах контейнера.
     */
    public void setAnchorSize(final Size anchorSize) {
        this.anchorSize = anchorSize;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        final AnchorLayout result = (AnchorLayout)super.clone();
        if (anchorSize!=null)
            result.anchorSize = (Size)anchorSize.clone();
        return result;
    }
}