package org.echosoft.framework.ui.extjs.widgets;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.AbstractContainerComponent;
import org.echosoft.framework.ui.extjs.model.DomElement;

/**
 * Простейший контейнер компонент.
 * Используется исключительно в целях отображения дочерних компонент в определенной раскладке.
 * @author Anton Sharapov
 */
public class Container extends AbstractContainerComponent {

    private DomElement autoEl;

    public Container() {
        this(null);
    }
    public Container(final ComponentContext ctx) {
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

    @Override
    public void invoke(final JsonWriter out) throws Exception {
        out.beginObject();
        out.writeProperty("xtype", "container");
        renderAttrs(out);
        if (autoEl!=null) {
            out.writeProperty("autoEl", autoEl);
        }
        out.endObject();
    }

}
