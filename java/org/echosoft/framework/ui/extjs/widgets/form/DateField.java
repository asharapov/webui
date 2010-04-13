package org.echosoft.framework.ui.extjs.widgets.form;

import java.util.Date;
import java.util.Set;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.Scope;

/**
 * @author Anton Sharapov
 */
public class DateField extends AbstractTextField {

    public static final Set<String> EVENTS =
            StringUtil.asUnmodifiableSet(AbstractTextField.EVENTS, "select");

    private Date value;                     // значение в данном поле ввода.
    private Date minValue;                  // минимально допустимая дата.
    private Date maxValue;                  // максимально допустимая дата.
    private String[] disabledDates;         // перечень дат (диапазонов дат) запрещенных для ввода в компоненте.
    private int[] disabledDays;             // перечень дней недели(0..6) запрещенных для ввода в компоненте.

    public DateField() {
        this(null);
    }
    public DateField(final ComponentContext ctx) {
        super(ctx);
    }

    /**
     * Возвращает значение данного поля.
     * @return  значение данного поля.
     */
    public Date getValue() {
        return value;
    }
    /**
     * Устанавливает значение данного поля.
     * @param value значение данного поля.
     */
    public void setValue(final Date value) {
        this.value = value;
    }

    /**
     * Возвращает минимально допустимую дату в качестве значения данного поля.
     * @return минимально допустимая дата.
     */
    public Date getMinValue() {
        return minValue;
    }
    /**
     * Устанавливает минимально допустимую дату в качестве значения данного поля.
     * @param minValue минимально допустимая дата.
     */
    public void setMinValue(final Date minValue) {
        this.minValue = minValue;
    }

    /**
     * Возвращает максимально допустимую дату в качестве значения данного поля.
     * @return максимально допустимая дата.
     */
    public Date getMaxValue() {
        return maxValue;
    }
    /**
     * Устанавливает максимально допустимую дату в качестве значения данного поля.
     * @param maxValue максимально допустимая дата.
     */
    public void setMaxValue(final Date maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Возвращает перечень дат запрещенных к указанию в данном поле. Каждый элемент в массиве
     * является либо строковым представлением конкретной даты в формате <code>dd.MM.YYYY</code> либо
     * описанием диапазона дат.
     * @return массив строк где каждый элемент описывает либо дату либо диапазон дат запрещенные к выбору в данном поле.
     *  По умолчанию возвращает <code>null</code>.
     */
    public String[] getDisabledDates() {
        return disabledDates;
    }
    /**
     * Указывает перечень дат запрещенных к указанию в данном поле. Каждый элемент в массиве
     * является либо строковым представлением конкретной даты в формате <code>dd.MM.YYYY</code> либо
     * описанием диапазона дат.
     * @param disabledDates массив строк где каждый элемент описывает либо дату либо диапазон дат запрещенные к выбору в данном поле.
     */
    public void setDisabledDates(final String[] disabledDates) {
        this.disabledDates = disabledDates;
    }

    /**
     * Возвращает перечень дней недели которые запрещены к указанию в данном поле.
     * Каждый день недели кодируется числом (0=воскресенье, 1=понедельник, ... 6=суббота).
     * @return перечень дней недели запрещенные к указанию в данном поле.
     */
    public int[] getDisabledDays() {
        return disabledDays;
    }
    /**
     * Указывает перечень дней недели которые запрещены к указанию в данном поле.
     * Каждый день недели кодируется числом (0=воскресенье, 1=понедельник, ... 6=суббота).
     * @param disabledDays перечень дней недели запрещенные к указанию в данном поле.
     */
    public void setDisabledDays(final int[] disabledDays) {
        this.disabledDays = disabledDays;
    }

    @Override
    public void invoke(final JsonWriter out) throws Exception {
        final ComponentContext ctx = getContext();

        ctx.getResources().attachScript( ctx.encodeThemeURL("/pkgs/pkg-menu.js",false) );

        setName( ctx.getClientId() + ".value" );
        if (isStateful()) {
            final String svalue = ctx.getAttribute("value", Scope.PR_ST);
            if (svalue!=null && !svalue.isEmpty()) {
                ctx.setAttribute("value", svalue, Scope.STATE);
                value = StringUtil.parseDate(svalue);
            }
        }
        out.beginObject();
        out.writeProperty("xtype", "datefield");
        renderAttrs(out);
        out.endObject();
    }

    @Override
    protected void renderAttrs(final JsonWriter out) throws Exception {
        super.renderAttrs(out);
        out.writeProperty("format", "d.m.Y");
        if (value!=null)
            out.writeProperty("value", value);
        if (minValue!=null)
            out.writeProperty("minValue", minValue);
        if (maxValue!=null)
            out.writeProperty("maxValue", maxValue);
        if (disabledDates!=null)
            out.writeProperty("disabledDates", disabledDates);
        if (disabledDays!=null)
            out.writeProperty("disabledDays", disabledDays);
    }

    @Override
    protected Set<String> getSupportedEvents() {
        return DateField.EVENTS;
    }

}
