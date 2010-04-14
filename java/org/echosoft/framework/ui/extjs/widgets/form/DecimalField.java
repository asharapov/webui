package org.echosoft.framework.ui.extjs.widgets.form;

import java.math.BigDecimal;
import java.math.MathContext;

import org.echosoft.common.json.JSExpression;
import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.Scope;

/**
 * Компонент для ввода чисел с фиксированным количеством знаков до и после запятой.
 * @author Anton Sharapov
 */
public class DecimalField extends AbstractTextField {

    private BigDecimal value;               // значение в данном поле ввода.
    private BigDecimal minValue;            // минимально допустимое значение.
    private BigDecimal maxValue;            // максимально допустимое значение.
    private int precision;                  // количество знаков после запятой.

    public DecimalField() {
        this(null);
    }
    public DecimalField(final ComponentContext ctx) {
        super(ctx);
        precision = 2;
    }


    /**
     * Возвращает значение данного поля.
     * @return  значение данного поля.
     */
    public BigDecimal getValue() {
        return value;
    }
    /**
     * Устанавливает значение данного поля.
     * @param value значение данного поля.
     */
    public void setValue(final BigDecimal value) {
        this.value = value;
    }

    /**
     * Возвращает минимально допустимое значение в данном поле.
     * @return минимально допустимое значение.
     */
    public BigDecimal getMinValue() {
        return minValue;
    }
    /**
     * Устанавливает минимально допустимое значение в данном поле.
     * @param minValue минимально допустимое значение.
     */
    public void setMinValue(final BigDecimal minValue) {
        this.minValue = minValue;
    }

    /**
     * Возвращает максимально допустимое значение в данном поле.
     * @return максимально допустимое значение.
     */
    public BigDecimal getMaxValue() {
        return maxValue;
    }
    /**
     * Устанавливает максимально допустимое значение в данном поле.
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


    @Override
    public void invoke(final JsonWriter out) throws Exception {
        final ComponentContext ctx = getContext();
        setName( ctx.getClientId() + ".value" );
        if (isStateful()) {
            final String svalue = ctx.getAttribute("value", Scope.PR_ST);
            if (svalue!=null && !svalue.isEmpty()) {
                ctx.setAttribute("value", svalue, Scope.STATE);
                value = new BigDecimal(svalue);
            }
        }
        out.beginObject();
        out.writeProperty("xtype", "numberfield");
        renderContent(out);
        out.endObject();
    }

    @Override
    protected void renderContent(final JsonWriter out) throws Exception {
        super.renderContent(out);
        if (value!=null) {
            final MathContext mc = new MathContext(value.precision()-value.scale() + precision);
            out.writeProperty("value", new JSExpression( value.round(mc).toPlainString() ));
        }
        if (minValue!=null) {
            final MathContext mc = new MathContext(minValue.precision()-minValue.scale() + precision);
            out.writeProperty("minValue", new JSExpression( minValue.round(mc).toPlainString() ));
        }
        if (maxValue!=null) {
            final MathContext mc = new MathContext(maxValue.precision()-maxValue.scale() + precision);
            out.writeProperty("maxValue", new JSExpression( maxValue.round(mc).toPlainString() ));
        }
        if (precision!=2)
            out.writeProperty("decimalPrecision", precision);
    }

}