package org.echosoft.framework.ui.extjs.layout;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.UIComponent;
import org.echosoft.framework.ui.extjs.model.Margins;

/**
 * Описывает характеристики менеджера компоновки <code>Ext.layout.BoxLayout</code>.
 * @author Anton Sharapov
 */
public class BoxLayout extends Layout {

    public static enum Pack {
        START, CENTER, END
    }

    private final List<UIComponent> items;
    private Margins defaultMargins;             // применяется к каждому компоненту в контейнере у которого не указано соответствующее свойство.
    private Margins padding;                    // указывает отступы применяемые ко всем компонетам в контейнере.

    public BoxLayout() {
        super();
        items = new ArrayList<UIComponent>();
    }
    
    /**
     * Возвращает величину отступов которые будут применяться к каждому компоненту в контейнере
     * у которого не было явно указан соответствующий параметр.
     * @return величина отступов по умолчанию для всех компонент в контейнере или <code>null</code>.
     */
    public Margins getDefaultMargins() {
        return defaultMargins;
    }
    /**
     * Указывает величину отступов которые будут применяться к каждому компоненту в контейнере
     * у которого не было явно указан соответствующий параметр.
     * @param margins величина отступов по умолчанию для всех компонент в контейнере.
     */
    public void setDefaultMargins(final Margins margins) {
        this.defaultMargins = margins;
    }

    /**
     * Определяет величину отступов от границ контейнера для всех компонентов в нем. 
     * @return  отступы определенные для всех компонент контейнера или <code>null</code>.
     */
    public Margins getPadding() {
        return padding;
    }
    /**
     * Определяет величину отступов от границ контейнера для всех компонентов в нем.
     * @param padding отступы определенные для всех компонент контейнера или <code>null</code>. 
     */
    public void setPadding(final Margins padding) {
        this.padding = padding;
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
    protected String getLayout() {
        return "box";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isCustomized() {
        return super.isCustomized() ||
                (padding!=null && !padding.isEmpty()) ||
                (defaultMargins!=null && !defaultMargins.isEmpty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void serializeConfigAttrs(final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException {
        super.serializeConfigAttrs(out);
        if (padding!=null && !padding.isEmpty())
            out.writeProperty("padding", padding.encode());
        if (defaultMargins!=null && !defaultMargins.isEmpty())
            out.writeProperty("defaultMargins", defaultMargins.encode());
    }

}