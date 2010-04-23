package org.echosoft.framework.ui.extjs.widgets.form;

import java.util.Set;

import org.echosoft.common.json.JSExpression;
import org.echosoft.common.json.JSFunction;
import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;

/**
 * Абстрактный класс от которого наследуются те компоненты которые подразумевают ввод пользователем значения
 * в соответствующее поле ввода (строки, числа, ...)
 * @author Anton Sharapov
 */
public abstract class AbstractTextField extends AbstractField {

    public static final Set<String> EVENTS =
            StringUtil.asUnmodifiableSet(AbstractField.EVENTS, "autosize", "keydown", "keypress", "keyup");

    private String name;                    // имя под которым значение данного компонента будет сохраняться на сервер.
    private Integer tabIndex;               //
    private boolean allowBlank;             // может ли поле быть пустым.
    private String emptyText;               // текст отображаемый в пустом поле ввода.
    private Integer minLength;              // минимально допустимая длина текста.
    private Integer maxLength;              // максимально допустимая длина текста.
    private String vtype;                   // идентификатор валидатора, используемого для данного поля.
    private JSFunction validator;           // функция используемая для валидации значения данного поля.
    private JSExpression regex;             // RegExp используемое для валидации значения данного поля.
    private JSExpression maskRe;            // RegExp определяющий символы которые допустимо использовать в данном поле.
    private JSExpression stripCharsRe;      // RegExp отвечающий за удаление ненужных символов из значения поля перед его валидацией.

    public AbstractTextField(final ComponentContext ctx) {
        super(ctx);
        allowBlank = true;
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
        this.name = name;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }
    public void setTabIndex(final Integer tabIndex) {
        this.tabIndex = tabIndex;
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
     * Возвращает текст который будет отображаться в пустом поле ввода. Этот же текст будет отправляться на сервер если
     * пользователь в этом поле так ничего и не напишет перед отправкой формы на сервер.
     * @return текст который будет отображаться в пустом поле ввода.
     */
    public String getEmptyText() {
        return emptyText;
    }
    /**
     * Указывает текст который будет отображаться в пустом поле ввода. Этот же текст будет отправляться на сервер если
     * пользователь в этом поле так ничего и не напишет перед отправкой формы на сервер.
     * @param emptyText текст который будет отображаться в пустом поле ввода.
     */
    public void setEmptyText(final String emptyText) {
        this.emptyText = emptyText;
    }

    /**
     * Возвращает минимально допустимую длину значения поля ввода.
     * @return минимально допустимая длина значения поля ввода или <code>null</code> в случае отсутствия данного ограничения.
     */
    public Integer getMinLength() {
        return minLength;
    }
    /**
     * Задает минимально допустимую длину значения поля ввода.
     * @param minLength минимально допустимая длина значения поля ввода или <code>null</code> в случае отсутствия данного ограничения.
     */
    public void setMinLength(final Integer minLength) {
        this.minLength = minLength;
    }

    /**
     * Возвращает максимально допустимую длину значения поля ввода.
     * @return минимально допустимая длина значения поля ввода или <code>null</code> в случае отсутствия данного ограничения.
     */
    public Integer getMaxLength() {
        return maxLength;
    }
    /**
     * Задает максимально допустимую длину значения поля ввода.
     * @param maxLength минимально допустимая длина значения поля ввода или <code>null</code> в случае отсутствия данного ограничения.
     */
    public void setMaxLength(final Integer maxLength) {
        this.maxLength = maxLength;
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
     * Возвращает регулярное выражение используемое для валидации значения данного поля.
     * @return регулярное выражение используемое для валидации значений данного поля ввода.
     */
    public JSExpression getRegex() {
        return regex;
    }
    /**
     * Указывает регулярное выражение используемое для валидации значения данного поля.
     * @param regex регулярное выражение используемое для валидации значений данного поля ввода.
     */
    public void setRegex(final JSExpression regex) {
        this.regex = regex;
    }

    /**
     * Возвращает регулярное выражение используемое для фильтрации допустимых символов при вводе пользователем значения.
     * @return регулярное выражение используемое для фильтрации допустимых символов при вводе пользователем значения.
     */
    public JSExpression getMaskRe() {
        return maskRe;
    }
    /**
     * Указывает регулярное выражение используемое для фильтрации допустимых символов при вводе пользователем значения в поле.
     * @param maskRe регулярное выражение используемое для фильтрации допустимых символов при вводе пользователем значения в поле.
     */
    public void setMaskRe(final JSExpression maskRe) {
        this.maskRe = maskRe;
    }

    /**
     * Возвращает регулярное выражение используемое перед валидацией значения данного поля для удаления лишних символов
     * присутствие которых хоть и является формально допустимым но совершенно излишне.
     * @return регулярное выражение используемое для удаления из значения поля всех лишних символов.
     */
    public JSExpression getStripCharsRe() {
        return stripCharsRe;
    }
    /**
     * Указывает регулярное выражение используемое перед валидацией значения данного поля для удаления лишних символов
     * присутствие которых хоть и является формально допустимым но совершенно излишне.
     * @param stripCharsRe регулярное выражение используемое для удаления из значения поля всех лишних символов.
     */
    public void setStripCharsRe(final JSExpression stripCharsRe) {
        this.stripCharsRe = stripCharsRe;
    }

    @Override
    protected void renderContent(final JsonWriter out) throws Exception {
        super.renderContent(out);
        out.writeProperty("name", name);
        if (tabIndex != null)
            out.writeProperty("tabIndex", tabIndex);
        if (!allowBlank)
            out.writeProperty("allowBlank", false);
        if (emptyText != null)
            out.writeProperty("emptyText", emptyText);
        if (minLength != null)
            out.writeProperty("minLength", minLength);
        if (maxLength != null)
            out.writeProperty("maxLength", maxLength);
        if (vtype != null)
            out.writeProperty("vtype", vtype);
        if (validator != null)
            out.writeProperty("validator", validator);
        if (regex != null)
            out.writeProperty("regex", regex);
        if (maskRe != null)
            out.writeProperty("maskRe", maskRe);
        if (stripCharsRe != null)
            out.writeProperty("stripCharsRe", stripCharsRe);
    }

    @Override
    protected Set<String> getSupportedEvents() {
        return AbstractTextField.EVENTS;
    }

}