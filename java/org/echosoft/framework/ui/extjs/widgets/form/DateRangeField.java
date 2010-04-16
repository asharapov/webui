package org.echosoft.framework.ui.extjs.widgets.form;

import java.util.Date;

import org.echosoft.common.json.JSFunction;
import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.Scope;
import org.echosoft.framework.ui.core.theme.Theme;

/**
 * Компонент для ввода диапазона дат.
 * @author Anton Sharapov
 */
public class DateRangeField extends AbstractField {

    private static final String THEME_MSGRES_FROM = "field.daterange.from";
    private static final String THEME_MSGRES_TO = "field.daterange.to";

    private Date from;                      // нижняя граница диапазона.
    private Date to;                        // верхняя граница диапазона.
    private Date minValue;                  // минимально-допустимое значение диапазона.
    private Date maxValue;                  // максимально-допустимое значение диапазона.
    private String[] disabledDates;         // перечень дат (диапазонов дат) запрещенных для ввода в компоненте.
    private int[] disabledDays;             // перечень дней недели(0..6) запрещенных для ввода в компоненте.
    private boolean allowBlank;             // могут ли поля ввода быть пустыми.
    private String vtype;                   // идентификатор валидатора, используемого для валидации обоих полей ввода.
    private JSFunction validator;           // функция используемая для валидации обоих полей ввода.
    private Integer tabIndex;               //

    public DateRangeField() {
        this(null);
    }
    public DateRangeField(final ComponentContext ctx) {
        super(ctx);
        allowBlank = true;
    }

    /**
     * Возвращает нижнюю границу вводимого диапазона.
     * @return нижняя граница вводимого диапазона.
     */
    public Date getFrom() {
        return from;
    }
    /**
     * Устанавливает нижнюю границу вводимого диапазона.
     * @param from нижняя граница вводимого диапазона.
     */
    public void setFrom(final Date from) {
        this.from = from;
    }

    /**
     * Возвращает верхнюю границу вводимого диапазона.
     * @return верхняя граница вводимого диапазона.
     */
    public Date getTo() {
        return to;
    }
    /**
     * Устанавливает верхнюю границу вводимого диапазона.
     * @param to верхняя граница вводимого диапазона.
     */
    public void setTo(final Date to) {
        this.to = to;
    }

    /**
     * Возвращает минимально допустимое значение для обоих полей.
     * @return минимально допустимое значение.
     */
    public Date getMinValue() {
        return minValue;
    }
    /**
     * Устанавливает минимально допустимое значение для обоих полей.
     * @param minValue минимально допустимое значение.
     */
    public void setMinValue(final Date minValue) {
        this.minValue = minValue;
    }

    /**
     * Возвращает максимально допустимое значение для обоих полей.
     * @return максимально допустимое значение.
     */
    public Date getMaxValue() {
        return maxValue;
    }
    /**
     * Устанавливает максимально допустимое значение для обоих полей.
     * @param maxValue максимально допустимое значение.
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
     * Возвращает идентификатор валидатора в ExtJS который будет использоваться для проверки значения данного поля ввода.
     * Используется в типовых случаях (например валидация e-mail)
     * @return идентификатор валидатора в ExtJS который будет использоваться для проверки значения данного поля ввода.
     * @see <code>Ext.form.VTypes</code>
     */
    public String getVType() {
        return vtype;
    }
    /**
     * Указывает идентификатор типового валидатора в ExtJS который будет использоваться для проверки значения данного поля ввода.
     * @param vtype идентификатор валидатора в ExtJS который будет использоваться для проверки значения данного поля ввода.
     * @see <code>Ext.form.VTypes</code>
     */
    public void setVType(final String vtype) {
        this.vtype = vtype;
    }

    /**
     * Используется для валидации значения поля ввода в нетиповых случаях.
     * @return javascript функция используемая для валидации значений данного поля.
     */
    public JSFunction getValidator() {
        return validator;
    }
    /**
     * Используется для валидации значения поля ввода в нетиповых случаях.
     * @param validator javascript функция используемая для валидации значений данного поля.
     */
    public void setValidator(final JSFunction validator) {
        this.validator = validator;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }
    public void setTabIndex(final Integer tabIndex) {
        this.tabIndex = tabIndex;
    }



    public void invoke(final JsonWriter out) throws Exception {
        final ComponentContext ctx = getContext();
        if (isStateful()) {
            String svalue = ctx.getAttribute("from", Scope.PR_ST);
            if (svalue!=null && !svalue.isEmpty()) {
                ctx.setAttribute("from", svalue, Scope.STATE);
                from = StringUtil.parseDate(svalue);
            }
            svalue = ctx.getAttribute("to", Scope.PR_ST);
            if (svalue!=null && !svalue.isEmpty()) {
                ctx.setAttribute("to", svalue, Scope.STATE);
                to = StringUtil.parseDate(svalue);
            }
        }
        if (from!=null && to!=null && from.getTime()>to.getTime()) {
            final Date swapped = from;
            from = to;
            to = swapped;
        }

        out.beginObject();
        out.writeProperty("xtype", "compositefield");
        renderContent(out);
        out.endObject();
    }

    @Override
    protected void renderContent(final JsonWriter out) throws Exception {
        super.renderContent(out);
        final ComponentContext ctx = getContext();
        final Theme theme = ctx.getTheme();

        out.writeProperty("combineErrors", false);
        out.writeComplexProperty("defaults");
        out.beginObject();
        out.writeProperty("format", "d.m.Y");
        if (minValue!=null)
            out.writeProperty("minValue", minValue);
        if (maxValue!=null)
            out.writeProperty("maxValue", maxValue);
        if (disabledDates!=null)
            out.writeProperty("disabledDates", disabledDates);
        if (disabledDays!=null)
            out.writeProperty("disabledDays", disabledDays);
        if (!allowBlank)
            out.writeProperty("allowBlank", false);
        if (vtype!=null)
            out.writeProperty("vtype", vtype);
        if (validator!=null)
            out.writeProperty("validator", validator);
        out.endObject();
        out.writeComplexProperty("items");
        out.beginArray();

        out.beginObject();
        out.writeProperty("xtype", "displayfield");
        out.writeProperty("value", theme.getMessage(THEME_MSGRES_FROM));
        out.endObject();
        out.beginObject();
        out.writeProperty("xtype", "datefield");
        out.writeProperty("itemId", "from");
        out.writeProperty("ref", "from");
        out.writeProperty("name", ctx.getClientId()+".from");
        if (from!=null)
            out.writeProperty("value", from);
        if (tabIndex!=null)
            out.writeProperty("tabIndex", tabIndex);
        out.endObject();

        out.beginObject();
        out.writeProperty("xtype", "displayfield");
        out.writeProperty("value", theme.getMessage(THEME_MSGRES_TO));
        out.endObject();
        out.beginObject();
        out.writeProperty("xtype", "datefield");
        out.writeProperty("itemId", "to");
        out.writeProperty("ref", "to");
        out.writeProperty("name", ctx.getClientId()+".to");
        if (to!=null)
            out.writeProperty("value", to);
        if (tabIndex!=null)
            out.writeProperty("tabIndex", tabIndex);
        out.endObject();

        out.endArray();
    }
}