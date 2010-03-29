package org.echosoft.framework.ui.extjs.layout;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.UIComponent;
import org.echosoft.framework.ui.extjs.widgets.Panel;

/**
 * <p>Описывает характеристики менеджера компоновки <code>Ext.layout.CardLayout</code>.</p>
 * <p><strong>Важно!</strong> Данный тип менеджера компоновки работает только с экземплярами {@link Panel} и его потомками.</p>
 * @author Anton Sharapov
 */
public class CardLayout extends Layout {

    private final List<Panel> items;
    private boolean deferredRender;         // определяет когда будут рендериться компоненты в этом контейнере: сразу (по умолчанию) или при их активации.
    private boolean layoutOnCardChange;     // дает возм-ть обновлять компоновку компонент когда контейнер изменяется. По умолчанию false.

    public CardLayout() {
        super();
        items = new ArrayList<Panel>();
    }

    /**
     * При включенной опции компоновщик будет осуществлять рендеринг компонент только в момент их активации.
     * (в единицу времени пользователю отображается только один компонент, активный компонент).
     * В противном случае (т.е. по умолчанию) рендеринг всех компонент будет происходить сразу после загрузки страницы.
     * @return <code>true</code> если требуется обеспечить т.н. ленивый рендеринг компонент в контейнере.
     *  По умолчанию возвращает <code>false</code>. 
     */
    public boolean isDeferredRender() {
        return deferredRender;
    }
    /**
     * При включенной опции компоновщик будет осуществлять рендеринг компонент только в момент их активации.
     * (в единицу времени пользователю отображается только один компонент, активный компонент).
     * В противном случае (т.е. по умолчанию) рендеринг всех компонент будет происходить сразу после загрузки страницы.
     * @param deferredRender <code>true</code> если требуется обеспечить т.н. ленивый рендеринг компонент в контейнере.
     */
    public void setDeferredRender(final boolean deferredRender) {
        this.deferredRender = deferredRender;
    }

    /**
     * Требуется ли перекомпоновывать в контейнере компоненты при изменении последних.
     * @return По умолчанию возвращает <code>false</code>.
     */
    public boolean isLayoutOnCardChange() {
        return layoutOnCardChange;
    }
    /**
     * Требуется ли перекомпоновывать в контейнере компоненты при изменении последних.
     * @param layoutOnCardChange <code>true</code> если требуется перекомпоновка при изменении компонент.
     */
    public void setLayoutOnCardChange(final boolean layoutOnCardChange) {
        this.layoutOnCardChange = layoutOnCardChange;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemsCount() {
        return items.size();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Iterable<UIComponent> getItems() {
        return (Iterable)items;
    }

    /**
     * Возвращает итератор по всем компонентам управляемым данным менеджером компоновки.
     * Так как данный менеджер компоновки может управлять только компонентами реализующими {@link Panel}
     * то данный вызов использовать уместнее чем стандартный {@link #getItems}.
     * @return итератор по всем компонентам управляемым данным менеджером компоновки.
     */
    public Iterable<Panel> getPanels() {
        return items;
    }

    /**
     * {@inheritDoc}
     * @param item  компонент который требуется добавить в контейнер. Должен относиться к классу {@link Panel} или одному из его наследников.
     */
    @Override
    public <T extends UIComponent> T append(final T item) {
        if (item==null)
            throw new IllegalArgumentException("Component must be specified");
        if (!(item instanceof Panel))
            throw new IllegalArgumentException("This layout manager can manage only Panel instances");
        items.add( (Panel)item );
        return item;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getLayout() {
        return "card";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isCustomized() {
        return super.isCustomized() ||
                deferredRender || layoutOnCardChange;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void serializeConfigAttrs(final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException {
        super.serializeConfigAttrs(out);
        if (deferredRender)
            out.writeProperty("deferredRender", true);
        if (layoutOnCardChange)
            out.writeProperty("layoutOnCardChange", true);
    }

}
