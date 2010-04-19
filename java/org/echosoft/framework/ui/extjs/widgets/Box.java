package org.echosoft.framework.ui.extjs.widgets;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.AbstractBoxComponent;
import org.echosoft.framework.ui.extjs.model.DomElement;
import org.echosoft.framework.ui.extjs.model.Template;

/**
 * Простейший компонент описывающий область экрана с некоторым статическим содержимым. 
 * @author Anton Sharapov
 */
public class Box extends AbstractBoxComponent {

    private DomElement autoEl;
    private Template template;
    private Object data;

    public Box() {
        this(null);
    }
    public Box(final ComponentContext ctx) {
        super(ctx);
    }

    /**
     * Возвращает тег используемый в качестве корневого элемента компонента и его свойства (опционально).
     * Если значение свойства не указано то ExtJS будет использовать по умолчанию тег <code>div</code>.
     * @return тег используемый в качестве корневого элемента компонента.
     */
    public DomElement getAutoEl() {
        return autoEl;
    }
    /**
     * Задает тег используемый в качестве корневого элемента компонента и его свойства (опционально).
     * Если значение свойства не указано то ExtJS будет использовать по умолчанию тег <code>div</code>.
     * @param autoEl тег используемый в качестве корневого элемента компонента.
     */
    public void setAutoEl(final DomElement autoEl) {
        this.autoEl = autoEl;
    }

    /**
     * Возвращает фрагмент html кода который будет отображаться под корневым элементом компонента.
     * @return содержимое компонента.
     */
    public String getHtml() {
        return autoEl!=null ? autoEl.getHtml() : null;
    }
    /**
     * Указывает фрагмент html кода который будет отображаться под корневым элементом компонента.
     * @param html содержимое компонента.
     */
    public void setHtml(final String html) {
        if (autoEl==null) {
            autoEl = new DomElement();
        }
        autoEl.setHtml(html);
    }

    /**
     * Возвращает шаблон фрагмента html который будет использоваться в качестве содержимого данного компонента.
     * Если шаблон не задан то содержимое компонента будет вычисляться на основе свойств <code>html</code> и
     * <code>autoEl</code> компонента.
     * Используется совместно со свойством <code>data</code>.
     * @return шаблон используемый для вычисления содержимого данного компонента или <code>null</code>.
     */
    public Template getTemplate() {
        return template;
    }
    /**
     * Задает шаблон фрагмента html который будет использоваться в качестве содержимого данного компонента.
     * Если шаблон не задан то содержимое компонента будет вычисляться на основе свойств <code>html</code> и
     * <code>autoEl</code> компонента.
     * Используется совместно со свойством <code>data</code>.
     * @param template шаблон используемый для вычисления содержимого данного компонента или <code>null</code>.
     */
    public void setTemplate(final Template template) {
        this.template = template;
    }

    /**
     * Возвращает данные которые будут использоваться при вычислении содержимого компонента на основе шаблона.
     * Используется совместно со свойством <code>template</code>.
     * @return данные для шаблона.
     */
    public Object getData() {
        return data;
    }
    /**
     * Указывает данные которые будут использоваться при вычислении содержимого компонента на основе шаблона.
     * Используется совместно со свойством <code>template</code>.
     * @param data данные для шаблона.
     */
    public void setData(final Object data) {
        this.data = data;
    }


    @Override
    public void invoke(final JsonWriter out) throws Exception {
        out.beginObject();
        out.writeProperty("xtype", "box");
        renderContent(out);
        out.endObject();
    }

    @Override
    protected void renderContent(final JsonWriter out) throws Exception {
        super.renderContent(out);
        if (autoEl != null) {
            if (autoEl.hasContentOnly()) {
                out.writeProperty("html", autoEl.getHtml());
            } else {
                out.writeProperty("autoEl", autoEl);
            }
        }
        if (template != null)
            out.writeProperty("tpl", template);
        if (data != null)
            out.writeProperty("data", data);
    }
}
