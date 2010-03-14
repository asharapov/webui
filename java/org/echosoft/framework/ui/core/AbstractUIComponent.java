package org.echosoft.framework.ui.core;

import org.echosoft.common.json.JsonWriter;

/**
 * Содержит реализацию некоторых низкоуровневых методов действие которых одинаково для всех компонент.
 * @author Anton Sharapov
 */
public abstract class AbstractUIComponent implements UIComponent {

    private ComponentContext ctx;

    public AbstractUIComponent(final ComponentContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    public ComponentContext getContext() {
        return ctx;
    }

    /**
     * Позволяет установить контекст компонента в том случае если он еще не был установлен ранее.
     * @param ctx  идентификатор данного компонента.
     * @throws IllegalStateException  в случае если у данного компонента уже установлен контекст.
     */
    public void setContext(final ComponentContext ctx) {
        if (this.ctx!=null)
            throw new IllegalStateException("Context already specified for component");
        this.ctx = ctx;
    }


    /**
     * {@inheritDoc}
     */
    public abstract void invoke(JsonWriter out) throws Exception;
}
