package org.echosoft.framework.ui.extjs.widgets.form;

import org.echosoft.common.json.JSFunction;
import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.Scope;
import org.echosoft.framework.ui.core.theme.Theme;

/**
 * Компонент для ввода диапазона целых чисел.
 * @author Anton Sharapov
 */
public class IntegerRangeField extends AbstractField {

    private static final String THEME_MSGRES_FROM = "field.numrange.from";
    private static final String THEME_MSGRES_TO = "field.numrange.to";

    private Integer from;                   // нижняя граница диапазона.
    private Integer to;                     // верхняя граница диапазона.
    private Integer minValue;               // минимально-допустимое значение диапазона.
    private Integer maxValue;               // максимально-допустимое значение диапазона.
    private boolean allowBlank;             // могут ли поля ввода быть пустыми.
    private String vtype;                   // идентификатор валидатора, используемого для валидации обоих полей ввода.
    private JSFunction validator;           // функция используемая для валидации обоих полей ввода.
    private Integer fieldWidth;             // ширина каждого поля ввода.

    public IntegerRangeField() {
        this(null);
    }
    public IntegerRangeField(final ComponentContext ctx) {
        super(ctx);
        allowBlank = true;
    }

    /**
     * Возвращает нижнюю границу вводимого диапазона.
     * @return нижняя граница вводимого диапазона.
     */
    public Integer getFrom() {
        return from;
    }
    /**
     * Устанавливает нижнюю границу вводимого диапазона.
     * @param from нижняя граница вводимого диапазона.
     */
    public void setFrom(final Integer from) {
        this.from = from;
    }

    /**
     * Возвращает верхнюю границу вводимого диапазона.
     * @return верхняя граница вводимого диапазона.
     */
    public Integer getTo() {
        return to;
    }
    /**
     * Устанавливает верхнюю границу вводимого диапазона.
     * @param to верхняя граница вводимого диапазона.
     */
    public void setTo(final Integer to) {
        this.to = to;
    }

    /**
     * Возвращает минимально допустимое значение для обоих полей.
     * @return минимально допустимое значение.
     */
    public Integer getMinValue() {
        return minValue;
    }
    /**
     * Устанавливает минимально допустимое значение для обоих полей.
     * @param minValue минимально допустимое значение.
     */
    public void setMinValue(final Integer minValue) {
        this.minValue = minValue;
    }

    /**
     * Возвращает максимально допустимое значение для обоих полей.
     * @return максимально допустимое значение.
     */
    public Integer getMaxValue() {
        return maxValue;
    }
    /**
     * Устанавливает максимально допустимое значение для обоих полей.
     * @param maxValue максимально допустимое значение.
     */
    public void setMaxValue(final Integer maxValue) {
        this.maxValue = maxValue;
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
            String svalue = StringUtil.trim( (String)ctx.getAttribute("from", Scope.PR_ST) );
            if (svalue!=null) {
                from = Integer.parseInt(svalue,10);
            }
            ctx.setAttribute("from", svalue, Scope.STATE);
            svalue = StringUtil.trim( (String)ctx.getAttribute("to", Scope.PR_ST) );
            if (svalue!=null) {
                to = Integer.parseInt(svalue,10);
            }
            ctx.setAttribute("to", svalue, Scope.STATE);
        }
        if (from!=null && to!=null && from>to) {
            final Integer swapped = from;
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
        final Integer tabIndex = getTabIndex();

        out.writeProperty("combineErrors", false);
        out.writeComplexProperty("defaults");
        out.beginObject();
        out.writeProperty("allowDecimals", false);
        out.writeProperty("minValue", minValue!=null ? minValue : Integer.MIN_VALUE);
        out.writeProperty("maxValue", maxValue!=null ? maxValue : Integer.MAX_VALUE);
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
