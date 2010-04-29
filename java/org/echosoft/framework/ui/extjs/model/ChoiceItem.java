package org.echosoft.framework.ui.extjs.model;

import java.io.Serializable;

/**
 * Описывает отдельный элемент выбора используемый для таких компонент как <code>RadioGroup</code> или <code>CheckboxGroup</code>.
 * @author Anton Sharapov
 */
public class ChoiceItem implements Serializable, Cloneable {

    private final String value;             // значение отправляемое на сервер.
    private final String label;             // отображаемый пользователю текст описывающий данный элемент выбора.
    private final boolean disabled;         // определяет может ли данный элемент быть выбран.

    public ChoiceItem(final String value) {
        this.value = this.label = value!=null ? value : "";
        this.disabled = false;
    }
    public ChoiceItem(final String value, final String label) {
        this.value = value!=null ? value : "";
        this.label = label!=null ? label : this.value;
        this.disabled = false;
    }
    public ChoiceItem(final String value, final String label, final boolean disabled) {
        this.value = value!=null ? value : "";
        this.label = label!=null ? label : this.value;
        this.disabled = disabled;
    }

    /**
     * Возвращает уникальный идентификатор данного элемента.
     * Может быть пустой строкой но не <code>null</code>.
     * @return уникальный идентификатор данного элемента.
     */
    public String getValue() {
        return value;
    }

    /**
     * Возвращает видимый пользователю текст с описанием данного элемента выбора.
     * @return текст, описывающий данную опцию. 
     */
    public String getLabel() {
        return label;
    }

    /**
     * Возвращает признак, определяющий может ли пользователь выбрать данный элемент или нет.
     * @return <code>true</code> в случае если пользователь не может выбрать данный элемент.
     */
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public int hashCode() {
        return value!=null ? value.hashCode() : 0;
    }
    @Override
    public boolean equals(final Object obj) {
        if (obj==null || !getClass().equals(obj.getClass()))
            return false;
        final ChoiceItem other = (ChoiceItem)obj;
        return value!=null ? value.equals(other.value) : other.value==null;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    @Override
    public String toString() {
        return "[ChoiceItem{value:"+value+", label:"+label+(disabled ? " disabled" : "")+"}]";
    }
}
