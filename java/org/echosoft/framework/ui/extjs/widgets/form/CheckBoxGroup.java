package org.echosoft.framework.ui.extjs.widgets.form;

import java.util.ArrayList;
import java.util.List;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;

/**
 * @author Anton Sharapov
 */
public class CheckBoxGroup extends AbstractField {

    private boolean allowBlank;             // может ли поле быть пустым.
    private boolean vertical;               // определяет порядок следования элементов выбора в группе.
    private Integer columnsCount;           // количество колонок на которые разбито все содержимое группы.
    private Number[] columnsWidth;          // кол-во колонок равно кол-ву элементов массива, каждый из которых - ширина соотв. колонки.
    private List<CheckBox> items;           // элементы выбора.

    public CheckBoxGroup() {
        this(null);
    }
    public CheckBoxGroup(final ComponentContext ctx) {
        super(ctx);
        allowBlank = true;
        items = new ArrayList<CheckBox>();
    }

    /**
     * Возвращает <code>true</code> если поле ввода является валидным даже при отсутствии значения в нем.
     * @return <code>true</code> если поле валидно и при отсутствии значения в нем.
     *      По умолчанию возвращает <code>true</code>.
     */
    public boolean isAllowBlank() {
        return allowBlank;
    }
    /**
     * Определяет, является ли поле ввода валидным при отсутствии значения в нем.
     * @param allowBlank <code>true</code> если поле валидно и при отсутствии значения в нем.
     */
    public void setAllowBlank(final boolean allowBlank) {
        this.allowBlank = allowBlank;
    }

    /**
     * Возвращает <code>true</code> если порядок следования элементов выбора будет таков что заполняться сначала
     * будет первая колонка, потом вторая колонка и т.д. В противно случае порядок заполнения будет построчным.
     * @return флаг определяющий порядок следования элементов выбора: поколоночный или построчный.
     */
    public boolean isVertical() {
        return vertical;
    }
    /**
     * Указывает порядок следования элементов выбора в группе: по колонкам или по строкам.
     * @param vertical  определяет порядок следования элементов в группе.
     */
    public void setVertical(final boolean vertical) {
        this.vertical = vertical;
    }

    /**
     * Возвращает количество колонок равной ширины на которые будет разбит вывод элементов выбора.<br/>
     * Для разбиения элементов выбора на колонки разной ширины следует использовать свойство {@link #getColumnsWidth()} ()}.
     * @return количество колонок равной ширины на которые будет разбит вывод элементов выбора.
     *  По умолчанию возвращает <code>null</code>.
     */
    public Integer getColumnsCount() {
        return columnsCount!=null
                    ? columnsCount
                    : (columnsWidth!=null ? columnsWidth.length : null);
    }
    /**
     * Указывает количество колонок на которые следует разбить вывод элементов выбора.
     * При использовании данного способа указания кол-ва колонок все колонки будут созданы равной ширины.
     * Для разбиения элементов выбора на колонки разной ширины следует использовать свойство {@link #getColumnsWidth()} ()}.
     * @param columnsCount  количество колонок равного размера на которые следует разбить вывод элементов выбора.
     */
    public void setColumnsCount(final Integer columnsCount) {
        this.columnsCount = columnsCount;
        if (columnsCount!=null) {
            this.columnsWidth = null;
        }
    }

    /**
     * Возвращает информацию о ширине всех колонов на которые следует разбить вывод элементов выбора.
     * Количество элементов массива однозначно указывает на количество колонок на которые следует разбить вывод элементов выбора.
     * Установка этого свойства используется в случае когда надо указать для каждой колонки свою специфичную ширину.
     * Целочисленные значения в качестве элеметов массива обозначают абсолютные размеры в пикселях.
     * Вещественные значения - ширину соответствующей колонки в процентах от общего размера контейнера.
     * @return информация о ширине всех колонок.
     */
    public Number[] getColumnsWidth() {
        return columnsWidth;
    }
    /**
     * Установка этого свойства используется в случае когда надо указать для каждой колонки свою специфичную ширину.
     * Количество элементов массива однозначно указывает на количество колонок на которые следует разбить вывод элементов выбора.
     * Целочисленные значения в качестве элеметов массива обозначают абсолютные размеры в пикселях.
     * Вещественные значения - ширину соответствующей колонки в процентах от общего размера контейнера.
     * @param columnsWidth информация о ширине всех колонок.
     */
    public void setColumnsWidth(final Number[] columnsWidth) {
        this.columnsWidth = columnsWidth;
    }

    /**
     * Возвращает список всех элементов выбора для данной группы.
     * @return список всех элементов выбора для данной группы. Метод никогда не возвращает <code>null</code>.
     */
    public List<CheckBox> getItems() {
        return items;
    }

    /**
     * Добавляет в группу новый элемент выбора.
     * @param item новый элемент выбора для данной группы. Не может быть <code>null</code>.
     * @return добавленный элемент выбора для данной группы.
     */
    public CheckBox append(final CheckBox item) {
        if (item==null)
            throw new IllegalArgumentException("Valid CheckBox item must be specified");
        items.add( item );
        return item;
    }


    @Override
    public void invoke(final JsonWriter out) throws Exception {
        final ComponentContext ctx = getContext();
        final boolean stateful = isStateful();
        for (CheckBox item : items) {
            item.setStateful( stateful );
        }
        out.beginObject();
        out.writeProperty("xtype", "checkboxgroup");
        renderContent(out);
        out.endObject();
    }

    @Override
    public void renderContent(final JsonWriter out) throws Exception {
        super.renderContent(out);
        if (!allowBlank)
            out.writeProperty("allowBlank", false);
        if (vertical)
            out.writeProperty("vertical", true);
        if (columnsCount!=null) {
            out.writeProperty("columns", columnsCount);
        } else
        if (columnsWidth!=null) {
            out.writeProperty("columns", columnsWidth);
        }
        out.writeComplexProperty("items");
        out.beginArray();
        for (CheckBox item : items) {
            item.invoke(out);
        }
        out.endArray();
    }
}
