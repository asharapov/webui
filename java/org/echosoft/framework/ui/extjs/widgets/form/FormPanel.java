package org.echosoft.framework.ui.extjs.widgets.form;

import java.util.Map;
import java.util.Set;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.widgets.Panel;

/**
 * @author Anton Sharapov
 */
public class FormPanel extends Panel {

    public static enum Method {
        GET, POST
    }
    public static Set<String> EVENTS =
            StringUtil.asUnmodifiableSet(Panel.EVENTS, "actioncomplete", "actionfailed", "beforeaction");

    private Map<String,Object> baseParams;  // дополнительные параметры, отправляемые на сервер.
    private String url;                     // URL ресурса на который будет делаться запрос.
    private Method method;                  // метод отправки запроса (либо GET либо POST).
    private boolean fileUpload;             // признак что надо аплоадить файл - т.е. отдельный алгоритм отправки данных на сервер.
    private boolean standardSubmit;         // использовать классическую форму отправки данных на сервер или посредством AJAX.
    private boolean trackResetOnLoad;       // form.reset() откатывает до тех данных что были получены при загрузке страницы или до данных полученных последним AJAX запросом.
    private int timeout;                    // время ожидания ответа (в секундах)
    private String waitTitle;               // используется при AJAX - сообщение что идет ожидание данных от сервера.
    private String formId;                  // id формы.
    private boolean monitorValid;           // использовать фоновую валидацию полей формы
    private int monitorPoll;                // частота проверок полей формы (мсек).

    public FormPanel() {
        this(null);
    }
    public FormPanel(final ComponentContext ctx) {
        super(ctx);
        timeout = 30;
        monitorPoll = 200;
    }

    /**
     * Возвращает перечень дополнительных параметров которые должны отправляться на сервер вместе с основным содержимым формы.
     * @return перечень дополнительных параметров которые будут отправляться на сервер вместе с основным содержимым формы.
     */
    public Map<String,Object> getBaseParams() {
        return baseParams;
    }
    public void setBaseParams(final Map<String,Object> baseParams) {
        this.baseParams = baseParams;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(final String url) {
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }
    public void setMethod(final Method method) {
        this.method = method;
    }

    public boolean isFileUpload() {
        return fileUpload;
    }
    public void setFileUpload(final boolean fileUpload) {
        this.fileUpload = fileUpload;
    }

    public boolean isStandardSubmit() {
        return standardSubmit;
    }
    public void setStandardSubmit(final boolean standardSubmit) {
        this.standardSubmit = standardSubmit;
    }

    public boolean isTrackResetOnLoad() {
        return trackResetOnLoad;
    }
    public void setTrackResetOnLoad(final boolean trackResetOnLoad) {
        this.trackResetOnLoad = trackResetOnLoad;
    }

    public int getTimeout() {
        return timeout;
    }
    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }

    public String getWaitTitle() {
        return waitTitle;
    }
    public void setWaitTitle(String waitTitle) {
        this.waitTitle = waitTitle;
    }

    public String getFormId() {
        return formId;
    }
    public void setFormId(String formId) {
        this.formId = formId;
    }

    public boolean isMonitorValid() {
        return monitorValid;
    }
    public void setMonitorValid(final boolean monitorValid) {
        this.monitorValid = monitorValid;
    }

    public int getMonitorPoll() {
        return monitorPoll;
    }
    public void setMonitorPoll(final int monitorPoll) {
        this.monitorPoll = monitorPoll;
    }



    @Override
    public void invoke(final JsonWriter out) throws Exception {
        out.beginObject();
        out.writeProperty("xtype", "form");
        renderAttrs(out);
        out.endObject();
    }

    @Override
    protected void renderAttrs(final JsonWriter out) throws Exception {
        super.renderAttrs(out);
        if (baseParams!=null)
            out.writeProperty("baseParams", baseParams);
        if (url!=null)
            out.writeProperty("url", url);
        if (method!=null)
            out.writeProperty("method", method);
        if (fileUpload)
            out.writeProperty("fileUpload", true);
        if (standardSubmit)
            out.writeProperty("standardSubmit", true);
        if (trackResetOnLoad)
            out.writeProperty("trackResetOnLoad", true);
        if (timeout!=30)
            out.writeProperty("timeout", timeout);
        if (waitTitle!=null)
            out.writeProperty("waitTitle", waitTitle);
        if (formId!=null)
            out.writeProperty("formId", formId);
        if (monitorValid)
            out.writeProperty("monitorValid", true);
        if (monitorPoll!=200)
            out.writeProperty("monitorPoll", monitorPoll);
    }

    @Override
    protected Set<String> getSupportedEvents() {
        return FormPanel.EVENTS;
    }
}
