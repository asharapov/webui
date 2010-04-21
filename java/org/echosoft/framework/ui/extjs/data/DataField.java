package org.echosoft.framework.ui.extjs.data;

import java.io.Serializable;

import org.echosoft.common.json.JSExpression;

/**
 * Содержит информацию об отдельном поле записи в источнике данных.
 * 
 * @author Anton Sharapov
 */
public class DataField implements Serializable, Cloneable {

    private final String name;              // имя поля. Обязательное поле!
    private String type;                    // тип данных.
    private String mapping;                 // дает возможность маппинга
    private Object defaultValue;            // значение по умолчанию (когда в источнике данных нет этого поля)ю\.
    private JSExpression convert;           // функция или ссылка на функцию используемую для нормализации значения поля в записи.
    private String dateFormat;              // используется для конвертации полей типа "дата".
    private SortDirection sortDir;          // начальная направление сортировки данного поля.
    private JSExpression sortType;          // функция или ссылка на функцию возвращающую версию значения пригодную для сравения.
    private boolean allowBlank;             // считать ли валидным пустое значение.

    public DataField(final String name) {
        if (name==null)
            throw new IllegalArgumentException("Name must be specified");
        this.name = name;
        this.allowBlank = true;
    }

    /**
     * Возвращает <code>true</code> если о данном поле известна только минимальная информация (его название).
     * В таком случае ExtJS допускает упрощенную форму описания этого поля.
     * @return <code>true</code> если о данном поле известна только минимальная информация (его название).
     */
    public boolean hasShortForm() {
        return type==null && mapping==null && defaultValue==null &&
                convert==null && dateFormat==null && sortDir==null && sortType==null && allowBlank;
    }

    /**
     * Возвращает логическое название поля в записи.
     * @return название поля в записи.
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает строковой идентификатор под которым соответствующий тип данных был зарегистрирован
     * в ExtJS: <code>Ext.data.Types</code>.
     * @return информация о типе данных находящихся в данном поле.
     */
    public String getType() {
        return type;
    }
    /**
     * Указывает строковой идентификатор типа данных.
     * @param type информация о типе данных находящихся в данном поле.
     * @see  <code>ExtJS:  Ext.data.Types</code>
     */
    public void setType(final String type) {
        this.type = type;
    }

    public String getMapping() {
        return mapping;
    }
    public void setMapping(final String mapping) {
        this.mapping = mapping;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
    public void setDefaultValue(final Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public JSExpression getConvert() {
        return convert;
    }
    public void setConvert(final JSExpression convert) {
        this.convert = convert;
    }

    public String getDateFormat() {
        return dateFormat;
    }
    public void setDateFormat(final String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public SortDirection getSortDir() {
        return sortDir;
    }
    public void setSortDir(final SortDirection sortDir) {
        this.sortDir = sortDir;
    }

    public JSExpression getSortType() {
        return sortType;
    }
    public void setSortType(final JSExpression sortType) {
        this.sortType = sortType;
    }

    public boolean isAllowBlank() {
        return allowBlank;
    }
    public void setAllowBlank(final boolean allowBlank) {
        this.allowBlank = allowBlank;
    }


    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj==null || !getClass().equals(obj.getClass()))
            return false;
        final DataField other = (DataField)obj;
        return name.equals(other.name);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "[DataField{name:"+name+"}]";
    }
}
