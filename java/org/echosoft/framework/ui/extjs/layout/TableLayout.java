package org.echosoft.framework.ui.extjs.layout;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.UIComponent;

/**
 * Описывает характеристики менеджера компоновки <code>Ext.layout.TableLayout</code>.
 * @author Anton Sharapov
 */
public class TableLayout extends Layout {

    private final List<UIComponent> items;
    private Integer columns;            // количество колонок на которые следует раскладывать компоненты в контейнере.

    public TableLayout() {
        super();
        items = new ArrayList<UIComponent>();
    }

    /**
     * Возвращает количество колонок на которые компоновщик будет разбивать все компоненты в контейнере.
     * Если свойство возвращает <code>null</code> то все компоненты будут отображаться в одну линию (как если бы кол-во колонок было бесконечным).
     * @return количество колонок на которые будут биться все компоненты в контейнере.
     *  По умолчанию возвращает <code>null</code>.
     */
    public Integer getColumns() {
        return columns;
    }
    /**
     * Задает количество колонок на которые компоновщик будет разбивать все компоненты в контейнере.
     * Если свойство возвращает <code>null</code> то все компоненты будут отображаться в одну линию (как если бы кол-во колонок было бесконечным).
     * @param columns количество колонок на которые будут биться все компоненты в контейнере.
     */
    public void setColumns(final Integer columns) {
        this.columns = columns;
    }

    @Override
    public int getItemsCount() {
        return items.size();
    }

    @Override
    public Iterable<UIComponent> getItems() {
        return items;
    }

    @Override
    public <T extends UIComponent> T append(final T item) {
        if (item==null)
            throw new IllegalArgumentException("Component must be specified");
        items.add(item);
        return item;
    }

    @Override
    protected String getLayout() {
        return "table";
    }

    @Override
    protected boolean isCustomized() {
        return super.isCustomized() || columns!=null;
    }

    @Override
    protected void serializeConfigAttrs(final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException {
        super.serializeConfigAttrs(out);
        if (columns!=null)
            out.writeProperty("columns", columns);
    }

}
