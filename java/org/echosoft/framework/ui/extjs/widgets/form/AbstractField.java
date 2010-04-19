package org.echosoft.framework.ui.extjs.widgets.form;

import java.util.Set;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.json.annotate.JsonUseSeriazer;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.Message;
import org.echosoft.framework.ui.extjs.AbstractBoxComponent;
import org.echosoft.framework.ui.extjs.spi.model.EnumLCJSONSerializer;

/**
 * Базовый класс от которого наследуются все компоненты, реализующие поля ввода в формах.
 * @author Anton Sharapov
 */
public abstract class AbstractField extends AbstractBoxComponent {

    public static final Set<String> EVENTS =
            StringUtil.asUnmodifiableSet(AbstractBoxComponent.EVENTS,
                    "blur", "change", "focus", "invalid", "specialKey", "valid");

    @JsonUseSeriazer(EnumLCJSONSerializer.class)
    public static enum MsgTarget {
        QTIP, TITLE, UNDER, SIDE
    }

    private String invalidText;             // Текст сообщения об ошибке по умолчанию.
    private MsgTarget msgTarget;            // Определяет место в котором будут отображаться сообщения об ошибках.
    private boolean submitValue;            // Отправлять содержимое данного поля на сервер или нет.
    private boolean validateOnBlur;         // Автоматически валидировать поле на момент потери им фокуса.
    private boolean readOnly;               // Поле доступно исключительно в режиме "только для чтения".
    private boolean stateful;               // Компонент должен сохранять свое состояние на сервере.


    public AbstractField(final ComponentContext ctx) {
        super(ctx);
        msgTarget = MsgTarget.QTIP;
        submitValue = true;
        validateOnBlur = true;
    }

    /**
     * Возвращает текст сообщения об ошибке применяемое по умолчанию для невалидного компонента.
     * @return  текст сообщения об ошибке применяемое по умолчанию для невалидного компонента.
     */
    public String getInvalidText() {
        return invalidText;
    }
    /**
     * Задает текст сообщения об ошибке применяемое по умолчанию для невалидного компонента.
     * @param invalidText  текст сообщения об ошибке применяемое по умолчанию для невалидного компонента.
     */
    public void setInvalidText(final String invalidText) {
        this.invalidText = invalidText;
    }

    /**
     * Определяет место размещения на экране сообщения об ошибке для невалидного компонента.
     * @return место размещения сообщения об ошибке для невалидного компонента.
     *  Значение свойства по умолчанию: {@link MsgTarget#QTIP}. 
     */
    public MsgTarget getMsgTarget() {
        return msgTarget;
    }
    /**
     * Задает место размещения на экране сообщения об ошибке для невалидного компонента.
     * @param msgTarget место размещения сообщения об ошибке для невалидного компонента.
     *  Значение свойства по умолчанию: {@link MsgTarget#QTIP}. 
     */
    public void setMsgTarget(final MsgTarget msgTarget) {
        this.msgTarget = msgTarget!=null ? msgTarget : MsgTarget.QTIP;
    }

    /**
     * Возвращает <code>true</code> если содержимое данного поля требуется отправлять на сервер при сабмите формы.
     * @return <code>true</code> если содержимое данного поля требуется отправлять на сервер при сабмите формы.
     *      Значение свойства по умолчанию: <code>true</code>.
     */
    public boolean isSubmitValue() {
        return submitValue;
    }
    /**
     * Задает <code>true</code> если содержимое данного поля требуется отправлять на сервер при сабмите формы.
     * @param submitValue <code>true</code> если содержимое данного поля требуется отправлять на сервер при сабмите формы.
     */
    public void setSubmitValue(final boolean submitValue) {
        this.submitValue = submitValue;
    }

    /**
     * Возвращает <code>true</code> если при потере компонентом фокуса ввода, должна выполняться валидация его состояния.
     * @return <code>true</code> если при потере компонентом фокуса ввода, должна выполняться валидация его состояния.
     *  Значение свойства по умолчанию: <code>true</code>.
     */
    public boolean isValidateOnBlur() {
        return validateOnBlur;
    }
    /**
     * Задает <code>true</code> если при потере компонентом фокуса ввода, должна выполняться валидация его состояния.
     * @param validateOnBlur <code>true</code> если при потере компонентом фокуса ввода, должна выполняться валидация его состояния.
     */
    public void setValidateOnBlur(final boolean validateOnBlur) {
        this.validateOnBlur = validateOnBlur;
    }

    /**
     * Возвращает <code>true</code> если данное поле не доступно пользователю для редактирования.
     * @return <code>true</code> если данное поле не доступно пользователю для редактирования.
     *      По умолчанию возвращает <code>false</code>.
     */
    public boolean isReadOnly() {
        return readOnly;
    }
    /**
     * Определяет доступно ли данное поле для редактирования пользователю.
     * @param readOnly <code>true</code> если данное поле не доступно пользователю для редактирования.
     */
    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * Возвращает <code>true</code> если состояние данного компонента должно быть сохранено на сервере.
     * @return <code>true</code> если состояние данного компонента должно быть сохранено на сервере.
     *      По умолчанию возвращает <code>false</code>.
     */
    public boolean isStateful() {
        return stateful;
    }
    /**
     * Задает <code>true</code> если состояние данного компонента должно быть сохранено на сервере.
     * @param stateful <code>true</code> если состояние данного компонента должно быть сохранено на сервере.
     *      По умолчанию возвращает <code>false</code>.
     */
    public void setStateful(final boolean stateful) {
        this.stateful = stateful;
    }


    @Override
    protected void renderContent(final JsonWriter out) throws Exception {
        final ComponentContext ctx = getContext();
        final Message msg = ctx.getMessages().getFirstMessage(ctx.getClientId(), Message.Severity.WARN);
        if (msg != null)
            addPlugin("Ext.ux.wui.plugins.Field");

        super.renderContent(out);

        if (invalidText != null)
            out.writeProperty("invalidText", invalidText);
        if (msgTarget != MsgTarget.QTIP)
            out.writeProperty("msgTarget", msgTarget);
        if (!submitValue)
            out.writeProperty("submitValue", false);
        if (!validateOnBlur)
            out.writeProperty("validateOnBlur", false);
        if (readOnly)
            out.writeProperty("readOnly", true);
        if (msg != null)
            out.writeProperty("activeError", msg.getSubject());

        ctx.getResources().attachScript(ctx.encodeThemeURL("/pkgs/pkg-forms.js", false));
        ctx.getResources().attachScript(ctx.encodeThemeURL("/ux/form-plugins.js", false));
    }

    @Override
    protected Set<String> getSupportedEvents() {
        return AbstractField.EVENTS;
    }

}
