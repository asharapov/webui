package org.echosoft.framework.ui.extjs.layout;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.UIComponent;
import org.echosoft.framework.ui.extjs.model.Size;

/**
 * Описывает характеристики менеджера компоновки <code>Ext.layout.AnchorLayout</code>.
 * @author Anton Sharapov
 */
public class AnchorLayout extends Layout {

    private final List<UIComponent> items;
    private Size anchorSize;                    // виртуальный размер контейнера.

    public AnchorLayout() {
        super();
        items = new ArrayList<UIComponent>();
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
    /**
     * Использование данного свойства дает возможность переопределить виртуальный размер контейнера компонент.
     * Если в возвращаемом объекте будет явно задана ширина или высота то все размеры компонент в этом контейнере
     * будут расчитываться исходя не из реальных размеров контейнера а исходя из указанных.
     * @param anchorSize  Виртуальные размеры контейнера. Может быть <code>null</code>.
     */
    public void setAnchorSize(final Size anchorSize) {
        this.anchorSize = anchorSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemsCount() {
        return items.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<UIComponent> getItems() {
        return items;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends UIComponent> T append(final T item) {
        if (item==null)
            throw new IllegalArgumentException("Component must be specified");
        items.add(item);
        return item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException {
        super.serialize(out);
        if (anchorSize!=null && !anchorSize.isEmpty())
            out.writeProperty("anchorSize", anchorSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getLayout() {
        return "anchor";
    }

}