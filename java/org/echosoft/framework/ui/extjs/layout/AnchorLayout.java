package org.echosoft.framework.ui.extjs.layout;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.extjs.model.Size;

/**
 * Описывает характеристики менеджера раскладки <code>Ext.layout.AnchorLayout</code>.
 * @author Anton Sharapov
 */
public class AnchorLayout extends Layout {

    private Size anchorSize;        // виртуальный размер контейнера.

    public AnchorLayout() {
        anchorSize = new Size();
    }
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
     * Использование данного свойства дает возможность переопределить виртуальный размер контейнера компонент.
     * Если в возвращаемом объекте будет явно задана ширина или высота то все размеры компонент в этом контейнере
     * будут расчитываться исходя не из реальных размеров контейнера а исходя из указанных.
     * @return  виртуальные размеры контейнера.
     */
    public Size getAnchorSize() {
        return anchorSize;
    }

    @Override
    public void serialize(final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException {
        super.serialize(out);
        if (!anchorSize.isEmpty())
            out.writeProperty("anchorSize", anchorSize);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        final AnchorLayout result = (AnchorLayout)super.clone();
        result.anchorSize = (Size)anchorSize.clone();
        return result;
    }
}