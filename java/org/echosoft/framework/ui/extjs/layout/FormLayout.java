package org.echosoft.framework.ui.extjs.layout;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.json.annotate.JsonUseSeriazer;
import org.echosoft.framework.ui.extjs.model.Template;
import org.echosoft.framework.ui.extjs.spi.model.EnumLCJSONSerializer;

/**
 * Описывает характеристики менеджера компоновки <code>Ext.layout.FormLayout</code>.
 * @author Anton Sharapov
 */
public class FormLayout extends AnchorLayout {

    /**
     * Определяет способы выравнивания меток компонент по отношению к собственно контенту компонент на формах.
     */
    @JsonUseSeriazer(EnumLCJSONSerializer.class)
    public static enum LabelAlign {
        LEFT, TOP, RIGHT
    }

    public static final int DEFAULT_LABEL_WIDTH = 100;
    public static final int DEFAULT_LABEL_PAD = 5;

    // конфигурация компоновщика ExtJS
    private Template fieldTpl;      // шаблон используемый для адаптации компонента к отображению в форме.
    private String labelSeparator;  // фрагмент html, используемый в качестве разделителя между меткой к компоненту и самим компонентом.
    private boolean trackLabels;    // требуется ли скрывать метку компонента когда последний становится невидимым.
    // конфигурация контейнера ExtJS (не компоновщика)
    private boolean hideLabels;     // показывать или нет по умолчанию метки к компонентам на форме.
    private LabelAlign labelAlign;  // способ выравнивания меток компонент.
    private int labelWidth;         // Ширина региона отведенного для отрисовки меток компонент.
    private int labelPad;           // Определяет отступы слева для компонент относительно их меток (имеет смысл только при установленном labelWidth и labelAlign=RIGHT)

    public FormLayout() {
        super();
        labelAlign = LabelAlign.LEFT;
        labelWidth = DEFAULT_LABEL_WIDTH;
        labelPad = DEFAULT_LABEL_PAD;
    }

    /**
     * <p>Возвращает шаблон html кода используемого для адаптации компонента к отображению в составе формы.</p>
     * <p>По умолчанию имеет значение вида:<br/>
     * <pre>
     * &lt;div class="x-form-item {itemCls}" tabIndex="-1">
     *   &lt;label for="{id}" style="{labelStyle}" class="x-form-item-label">{label}{labelSeparator}&lt;/label>
     *   &lt;div class="x-form-element" id="x-form-el-{id}" style="{elementStyle}">
     *   &lt;/div>
     *   &lt;div class="{clearCls}">&lt;/div>
     * &lt;/div>
     * </pre>
     * </p>
     * @return шаблон кода-разметки используемой для адаптации компонента к отображению его в форме ввода.
     *  или <code>null</code> если в компоновщике используется версия шаблона ExtJS по умолчанию.
     */
    public Template getFieldTpl() {
        return fieldTpl;
    }
    /**
     * Определяет шаблон используемый для адаптации содержимого компонента к отображению в форме ввода.
     * @param fieldTpl  новый шаблон или <code>null</code> для указания что следует использовать шаблон ExtJS по умолчанию.
     */
    public void setFieldTpl(final Template fieldTpl) {
        this.fieldTpl = fieldTpl;
    }

    /**
     * Возвращает строку html текста используемую по умолчанию в качестве разделителя между меткой компонента и самим компонентом.
     * Данное свойство может быть переопределено для каждого компонента на форме.
     * По умолчанию ExtJS использует символ ':' в качестве такого разделителя.
     * @return  html текст используемый по умолчанию в качестве разделителя между меткой к компоненту и самим компонентом
     *  или <code>null</code> если следует использовать значение используемое по умолчанию в ExtJS.
     */
    public String getLabelSeparator() {
        return labelSeparator;
    }
    /**
     * Указывает строку html текста используемую по умолчанию в качестве разделителя между меткой компонента и самим компонентом.
     * По умолчанию ExtJS использует символ ':' в качестве такого разделителя.
     * @param labelSeparator  текст используемый по умолчанию в качестве разделителя между меткой к компоненту и самим компонентом
     *  или <code>null</code> если следует использовать значение используемое по умолчанию в ExtJS.
     */
    public void setLabelSeparator(final String labelSeparator) {
        this.labelSeparator = labelSeparator;
    }

    /**
     * Данное свойство определяет поведение компонент на форме при их скрытии. Если свойство установлено в <code>true</code> то
     * при скрытии компонент управляемых данным компоновщиком будет также скрыта и его метка.
     * @return <code>true</code> если при скрытии компонент на форме следует также и скрывать метки к ним.
     *      По умолчанию метод возвращает <code>false</code>.
     */
    public boolean isTrackLabels() {
        return trackLabels;
    }
    /**
     * Если данное свойство установлено в <code>true</code> то при скрытии компонент управляемых данным компоновщиком будет также скрыты их метки.
     * @param trackLabels <code>true</code> если при скрытии компонент на форме следует также и скрывать метки к ним.
     *      По умолчанию метод возвращает <code>false</code>.
     */
    public void setTrackLabels(final boolean trackLabels) {
        this.trackLabels = trackLabels;
    }


    /**
     * Возвращает <code>true</code> если требуется по умолчанию не показывать метки к компонентам находящимся под управлением данного компоновщика.
     * Данное свойство используется в качестве значения по умолчанию для свойства <code>hideLabel</code> в компонентах.
     * @return <code>true</code> если требуется по умолчанию не отображать метки к компонентам на форме.
     *      По умолчанию свойство возвращает <code>false</code>.
     */
    public boolean isHideLabels() {
        return hideLabels;
    }
    /**
     * Определяет требуется ли по умолчанию не показывать метки к компонентам находящимся под управлением данного компоновщика.
     * Данное свойство используется в качестве значения по умолчанию для свойства <code>hideLabel</code> в компонентах.
     * @param hideLabels <code>true</code> если требуется по умолчанию не отображать метки к компонентам на форме.
     *      По умолчанию свойство равно <code>false</code>.
     */
    public void setHideLabels(final boolean hideLabels) {
        this.hideLabels = hideLabels;
    }

    /**
     * Возвращает используемый данным компоновщиком способ выравнивания меток компонент по отношению к самим компонентам.
     * @return текущий способ выравнивания меток. По умолчанию возвращает {@link LabelAlign#LEFT}.
     */
    public LabelAlign getLabelAlign() {
        return labelAlign;
    }
    /**
     * Задает способ выравнивания меток компонент по отношению к самим компонентам.
     * @param labelAlign текущий способ выравнивания меток. По умолчанию свойство равно {@link LabelAlign#LEFT}.
     */
    public void setLabelAlign(final LabelAlign labelAlign) {
        this.labelAlign = labelAlign!=null ? labelAlign : LabelAlign.LEFT;
    }

    /**
     * Возвращает ширину области экрана отведенную для отрисовки меток компонент. По умолчанию резервируется 100px.
     * @return ширина области зарезервированной для отрисовки меток компонент на форме. По умолчанию возвращает 100px.
     */
    public int getLabelWidth() {
        return labelWidth;
    }
    /**
     * Задает ширину области экрана отведенную для отрисовки меток компонент. По умолчанию резервируется 100px.
     * @param labelWidth ширина области зарезервированной для отрисовки меток компонент на форме.
     */
    public void setLabelWidth(final int labelWidth) {
        this.labelWidth = labelWidth>=0 ? labelWidth : DEFAULT_LABEL_WIDTH;
    }

    /**
     * Возвращает величину отступа левой границы компонента от метки. Имеет смысл только если задан параметр {@link #getLabelWidth()}
     * и свойство {@link #getLabelAlign()} равно {@link LabelAlign#RIGHT}.
     * @return отступ левой границы компонента от метки. По умолчанию равно <code>5</code>.
     */
    public int getLabelPad() {
        return labelPad;
    }
    /**
     * Задает величину отступа левой границы компонента от метки. Имеет смысл только если задан параметр {@link #getLabelWidth()}
     * и свойство {@link #getLabelAlign()} равно {@link LabelAlign#RIGHT}.
     * @param labelPad отступ левой границы компонента от метки. По умолчанию равно <code>5</code>.
     */
    public void setLabelPad(final int labelPad) {
        this.labelPad = labelPad>=0 ? labelPad : DEFAULT_LABEL_PAD;
    }


    @Override
    public void serialize(final JsonWriter out) throws Exception {
        if (hideLabels)
            out.writeProperty("hideLabels", true);
        if (labelAlign != LabelAlign.LEFT)
            out.writeProperty("labelAlign", labelAlign);
        if (labelWidth != DEFAULT_LABEL_WIDTH)
            out.writeProperty("labelWidth", labelWidth);
        if (labelPad != DEFAULT_LABEL_PAD)
            out.writeProperty("labelPad", labelPad);
        super.serialize(out);
    }

    @Override
    protected String getLayout() {
        return "form";
    }

    @Override
    protected boolean isCustomized() {
        return super.isCustomized() || fieldTpl!=null || labelSeparator!=null || trackLabels;
    }

    @Override
    protected void serializeConfigAttrs(final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException {
        if (fieldTpl!=null)
            out.writeProperty("fieldTpl", fieldTpl);
        if (labelSeparator!=null)
            out.writeProperty("labelSeparator", labelSeparator);
        if (trackLabels)
            out.writeProperty("trackLabels", true);
    }

}
