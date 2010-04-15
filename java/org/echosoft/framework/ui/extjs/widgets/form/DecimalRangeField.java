package org.echosoft.framework.ui.extjs.widgets.form;

import java.math.BigDecimal;

import org.echosoft.common.json.JSFunction;
import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.Scope;
import org.echosoft.framework.ui.core.theme.Theme;

/**
 * Компонент для ввода диапазона вещественных чисел.
 * @author Anton Sharapov
 */
public class DecimalRangeField extends AbstractField {

    private static final String THEME_MSGRES_FROM = "field.numrange.from";
    private static final String THEME_MSGRES_TO = "field.numrange.to";

    private BigDecimal from;                // нижняя граница диапазона.
    private BigDecimal to;                  // верхняя граница диапазона.
    private BigDecimal minValue;            // минимально-допустимое значение диапазона.
    private BigDecimal maxValue;            // максимально-допустимое значение диапазона.
    private int precision;                  // количество знаков после запятой.
    private boolean allowBlank;             // могут ли поля ввода быть пустыми.
    private String vtype;                   // идентификатор валидатора, используемого для валидации обоих полей ввода.
    private JSFunction validator;           // функция используемая для валидации обоих полей ввода.
    private Integer tabIndex;               //
    private Integer fieldWidth;             // ширина каждого поля ввода.

    public DecimalRangeField() {
        this(null);
    }
    public DecimalRangeField(final ComponentContext ctx) {
        super(ctx);
        allowBlank = true;
        precision = 2;
    }

    /**
     * Возвращает нижнюю границу вводимого диапазона.
     * @return нижняя граница вводимого диапазона.
     */
    public BigDecimal getFrom() {
        return from;
    }
    /**
     * Устанавливает нижнюю границу вводимого диапазона.
     * @param from нижняя граница вводимого диапазона.
     */
    public void setFrom(final BigDecimal from) {
        this.from = from;
    }

    /**
     * Возвращает верхнюю границу вводимого диапазона.
     * @return верхняя граница вводимого диапазона.
     */
    public BigDecimal getTo() {
        return to;
    }
    /**
     * Устанавливает верхнюю границу вводимого диапазона.
     * @param to верхняя граница вводимого диапазона.
     */
    public void setTo(final BigDecimal to) {
        this.to = to;
    }

    /**
     * Возвращает минимально допустимое значение для обоих полей.
     * @return минимально допустимое значение.
     */
    public BigDecimal getMinValue() {
        return minValue;
    }
    /**
     * Устанавливает минимально допустимое значение для обоих полей.
     * @param minValue минимально допустимое значение.
     */
    public void setMinValue(final BigDecimal minValue) {
        this.minValue = minValue;
    }

    /**
     * Возвращает максимально допустимое значение для обоих полей.
     * @return максимально допустимое значение.
     */
    public BigDecimal getMaxValue() {
        return maxValue;
    }
    /**
     * Устанавливает максимально допустимое значение для обоих полей.
     * @param maxValue максимально допустимое значение.
     */
    public void setMaxValue(final BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Возвращает максимально допустимое количество знаков в дробной части числа.
     * @return максимально допустимое количество знаков в дробной части числа.
     *  Значение свойства по умолчанию: <code>2</code>.
     */
    public int getPrecision() {
        return precision;
    }
    /**
     * Указывает максимально допустимое количество знаков в дробной части числа.
     * @param precision максимально допустимое количество знаков в дробной части числа.
     */
    public void setPrecision(final int precision) {
        this.precision = precision<0 ? 0 : precision;
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

    /**
     * Возвращает ширину обоих полей ввода.
     * @return ширина каждого поля ввода.
     */
    public Integer getFieldWidth() {
        return fieldWidth;
    }
    /**
     * Устанавливает ширину обоих полей ввода.
     * @param fieldWidth ширина каждого поля ввода.
     */
    public void setFieldWidth(final Integer fieldWidth) {
        this.fieldWidth = fieldWidth;
    }


    public void invoke(final JsonWriter out) throws Exception {
        final ComponentContext ctx = getContext();
        if (isStateful()) {
            String svalue = ctx.getAttribute("from", Scope.PR_ST);
            if (svalue!=null && !svalue.isEmpty()) {
                ctx.setAttribute("from", svalue, Scope.STATE);
                from = new BigDecimal(svalue);
            }
            svalue = ctx.getAttribute("to", Scope.PR_ST);
            if (svalue!=null && !svalue.isEmpty()) {
                ctx.setAttribute("to", svalue, Scope.STATE);
                to = new BigDecimal(svalue);
            }
        }
        if (from!=null && to!=null && from.compareTo(to)==1) {
            final BigDecimal swapped = from;
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
        if (minValue!=null)
            out.writeProperty("minValue", minValue);
        if (maxValue!=null)
            out.writeProperty("maxValue", maxValue);
        if (precision!=2)
            out.writeProperty("decimalPrecision", precision);
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
        out.writeProperty("xtype", "numberfield");
        out.writeProperty("itemId", "from");
        out.writeProperty("ref", "from");
        out.writeProperty("name", ctx.getClientId()+".from");
        if (from!=null)
            out.writeProperty("value", from);
        if (tabIndex!=null)
            out.writeProperty("tabIndex", tabIndex);
        if (fieldWidth!=null)
            out.writeProperty("width", fieldWidth);
        out.endObject();

        out.beginObject();
        out.writeProperty("xtype", "displayfield");
        out.writeProperty("value", theme.getMessage(THEME_MSGRES_TO));
        out.endObject();
        out.beginObject();
        out.writeProperty("xtype", "numberfield");
        out.writeProperty("itemId", "to");
        out.writeProperty("ref", "to");
        out.writeProperty("name", ctx.getClientId()+".to");
        if (to!=null)
            out.writeProperty("value", to);
        if (tabIndex!=null)
            out.writeProperty("tabIndex", tabIndex);
        if (fieldWidth!=null)
            out.writeProperty("width", fieldWidth);
        out.endObject();

        out.endArray();
    }
}