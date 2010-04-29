package org.echosoft.framework.ui.extjs.widgets.form;

import java.util.ArrayList;
import java.util.List;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.Scope;
import org.echosoft.framework.ui.extjs.model.ChoiceItem;

/**
 * Группа элементов выбора, в которой выбранной в единицу времени может быть только одна опция.
 * @author Anton Sharapov
 */
public class RadioGroup extends AbstractField {

    private String name;                    // имя параметра в котором будет на сервер отправлено значение поля.
    private boolean allowBlank;             // может ли поле быть пустым.
    private boolean vertical;               // определяет порядок следования элементов выбора в группе.
    private Integer columnsCount;           // количество колонок на которые разбито все содержимое группы.
    private Number[] columnsWidth;          // кол-во колонок равно кол-ву элементов массива, каждый из которых - ширина соотв. колонки.
    private List<ChoiceItem> items;         // описания всех элементов выбора в группе.
    private String value;                   // идентификатор выбранного элемента в группе.

    public RadioGroup() {
        this(null);
    }
    public RadioGroup(final ComponentContext ctx) {
        super(ctx);
        allowBlank = true;
        items = new ArrayList<ChoiceItem>();
    }

    /**
     * Возвращает имя параметра в запросе где будет сохраняться значение данного компонента.
     * По умолчанию вычисляется как:
     * <pre>
     *      <code>name = getContext().getClientId() + ".value";</code>
     * </pre>
     * @return имя параметра в запросе где будет сохраняться значение данного компонента.
     */
    public String getName() {
        return name;
    }
    /**
     * Указывает имя параметра в запросе где будет сохраняться значение данного компонента.
     * По умолчанию вычисляется как:
     * <pre>
     *      <code>name = getContext().getClientId() + ".value";</code>
     * </pre>
     * @param name имя параметра в запросе где будет сохраняться значение данного компонента.
     */
    public void setName(final String name) {
        this.name = StringUtil.trim(name);
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
    public List<ChoiceItem> getItems() {
        return items;
    }

    /**
     * Добавляет в группу новый элемент выбора.
     * @param value  идентификатор нового элемента выбора (именно эта строка отправится на сервер в случае выбора данного элемента пользователем)
     * @param label отображаемая пользователю информация по данному элементу.
     */
    public void addItem(final String value, final String label) {
        items.add( new ChoiceItem(value,label) );
    }

    /**
     * Возвращает идентификатор выбранного элемента.
     * @return идентификатор отмеченного элемента выбора или <code>null</code>.
     */
    public String getValue() {
        return value;
    }
    /**
     * Указывает идентификатор выбранного элемента.
     * @param value идентификатор выбранного элемента.
     */
    public void setValue(final String value) {
        this.value = value!=null ? value : "";
    }


    @Override
    public void invoke(final JsonWriter out) throws Exception {
        final ComponentContext ctx = getContext();
        if (getName()==null)
            setName( ctx.getClientId() + ".value" );
        if (isStateful()) {
            final String svalue = ctx.getAttribute("value", Scope.PR_ST);
            if (svalue!=null) {
                value = svalue;
            }
            ctx.setAttribute("value", svalue, Scope.STATE);
        }
        out.beginObject();
        out.writeProperty("xtype", "radiogroup");
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
        out.writeComplexProperty("defaults");
        out.beginObject();
        out.writeProperty("name", getName());
        out.endObject();
        out.writeComplexProperty("items");
        out.beginArray();
        for (ChoiceItem item : items) {
            out.beginObject();
            out.writeProperty("inputValue", item.getValue());
            out.writeProperty("boxLabel", item.getLabel());
            if (value!=null && value.equals(item.getValue()))
                out.writeProperty("checked", true);
            out.endObject();
        }
        out.endArray();
    }
}