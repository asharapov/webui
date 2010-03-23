package org.echosoft.framework.ui.extjs;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Set;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.UIComponent;
import org.echosoft.framework.ui.extjs.layout.AutoLayout;
import org.echosoft.framework.ui.extjs.layout.Layout;

/**
 * Базовый класс для всех компонент, которые служат контейнерами для других компонент.
 * @author Anton Sharapov
 */
public abstract class AbstractContainerComponent extends AbstractBoxComponent {

    public static final Set<String> EVENTS = StringUtil.asUnmodifiableSet(AbstractBoxComponent.EVENTS, "add", "afterlayout", "beforeadd", "beforeremove", "remove");

    private Layout layout;                      // используемый менеджер упаковки дочерних компонент в контейнере.
    private String activeItem;                  // идентификатор активного компонента в контейнере.
    private boolean autoDestroy;                // уничтожает компоненты в контейнере автоматически (по умолчанию) или делегирует это бизнес-программисту (событие 'remove').
    private boolean forceLayout;                // выполняет компоновку компонент в контейнере даже тогда когда этого можно было бы на первый взгляд и не делать (пр: скрытые/свернутые панели).
    private boolean hideBorders;                // следует ли скрывать рамки вокруг каждого компонента в контейнере (false по умолчанию)

    public AbstractContainerComponent(final ComponentContext ctx) {
        super(ctx);
        layout = new AutoLayout();
        autoDestroy = true;
    }

    /**
     * Возвращает перечень всех поддерживаемых компонентом событий.<br/>
     * Если компонент, являющийся наследником данного класса поддерживает иной перечень событий то он должен переопределить данный метод.
     * @return  неизменяемый перечень событий, поддерживаемых данным компонентом. Метод никогда не возвращает <code>null</code>.
     */
    @Override
    protected Set<String> getSupportedEvents() {
        return EVENTS;
    }

    /**
     * Возвращает менеджер упаковки компонент в контейнере.
     * @return используемый в настоящий момент упаковщик компонент. Никогда не возвращает <code>null</code>.
     */
    public Layout getLayout() {
        return layout;
    }
    /**
     * Устанавливает менеджер упаковки компонент в контейнере.<br/>
     * При смене типа упаковщика метод для каждого компонента в контейнере заменяет информацию о его расположении в контейнере.
     * @param layout  упаковщик компонент или <code>null</code> если требуется использовать упаковщик по умолчанию.
     */
    public void setLayout(final Layout layout) {
        this.layout = layout!=null ? layout : new AutoLayout();
    }

    @SuppressWarnings("unchecked")
    public <T extends Layout> T assignLayout(final T layout) {
        setLayout(layout);
        return (T)this.layout;
    }


    /**
     * Возвращает количество компонент помещенных в данный контейнер.
     * @return  кол-во компонент в данном контейнере.
     */
    public int getItemsCount() {
        return layout!=null ? layout.getItemsCount() : 0;
    }

    /**
     * Возвращает итератор по всем компонентам помещенным в данный контейнер.
     * @return итератор по всем компонентам помещенным в данный контейнер.
     */
    public Iterable<UIComponent> getItems() {
        return layout!=null ? layout.getItems() : Collections.<UIComponent>emptyList();
    }

    /**
     * Возвращает идентификатор (или альтернативный идентификатор) дочернего компонента в контейнере, который должен быть
     * автоматически помечен как "активный" после загрузки страницы. 
     * @return идентификатор дочернего компонента который должен быть сразу после загрузки страницы помечен как активный. Может быть <code>null</code>.
     * @see AbstractComponent#getContext()
     * @see AbstractComponent#getItemId()  
     */
    public String getActiveItem() {
        return activeItem;
    }
    /**
     * Указывает идентификатор (или альтернативный идентификатор) дочернего компонента в контейнере, который должен быть
     * автоматически помечен как "активный" после загрузки страницы.
     * @param activeItem идентификатор дочернего компонента который должен быть сразу после загрузки страницы помечен как активный. Может быть <code>null</code>.
     * @see AbstractComponent#getContext()
     * @see AbstractComponent#getItemId()
     */
    public void setActiveItem(final String activeItem) {
        this.activeItem = StringUtil.trim(activeItem);
    }

    /**
     * Определяет последовательность действий при исключении компонента из контейнера.
     * При установленном флаге контейнер автоматически вызовет метод <code>destroy</code> у компонента.
     * При сброшенном флаге это может сделать бизнес-программист в обработчике события <code>remove</code>. 
     * @return  <code>true</code> (по умолчанию) если контейнер автоматически уничтожает исключаемый из контейнера компонента.
     */
    public boolean isAutoDestroy() {
        return autoDestroy;
    }
    /**
     * Определяет последовательность действий при исключении компонента из контейнера.
     * При установленном флаге контейнер автоматически вызовет метод <code>destroy</code> у компонента.
     * При сброшенном флаге это может сделать бизнес-программист в обработчике события <code>remove</code>.
     * @param autoDestroy если <code>true</code> то контейнер автоматически будет уничтожать исключаемые из контейнера компоненты; если <code>false</code> то эта обязанность
     *      будет возложена на программистов.
     */
    public void setAutoDestroy(final boolean autoDestroy) {
        this.autoDestroy = autoDestroy;
    }

    /**
     * Возвращает <code>true</code> если контейнер должен будет выполнять компоновку компонент в принудительном порядке даже тогда когда этого можно было бы и не делать (пр: скрытые/свернутые панели).
     * @return <code>true</code> если контейнер должен принудительно выполнять компоновку компонент. Значение свойства по умолчанию - <code>false</code>.
     */
    public boolean isForceLayout() {
        return forceLayout;
    }
    /**
     * Указывает должен ли будет контейнер выполнять компоновку компонент в принудительном порядке даже тогда когда этого можно было бы и не делать (пр: скрытые/свернутые панели).
     * @param forceLayout <code>true</code> если контейнер должен принудительно выполнять компоновку компонент. Значение свойства по умолчанию - <code>false</code>.
     */
    public void setForceLayout(final boolean forceLayout) {
        this.forceLayout = forceLayout;
    }

    /**
     * Возвращает <code>true</code> если контейнер должен скрывать рамки вокруг каждого своего дочернего компонента.
     * @return <code>true</code> если контейнер должен скрывать рамки вокруг дочерних компонент. По умолчанию возвращает <code>false</code>.
     */
    public boolean isHideBorders() {
        return hideBorders;
    }
    /**
     * Указывает должен ли контейнер скрывать рамки вокруг каждого своего дочернего компонента.
     * @param hideBorders <code>true</code> если контейнер должен скрывать рамки вокруг дочерних компонент. Значение свойства по умолчанию = <code>false</code>.
     */
    public void setHideBorders(final boolean hideBorders) {
        this.hideBorders = hideBorders;
    }


    @Override
    protected void renderAttrs(final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException {
        super.renderAttrs(out);
        if (activeItem!=null)
            out.writeProperty("activeItem", activeItem);
        if (!autoDestroy)
            out.writeProperty("autoDestroy", false);
        if (forceLayout)
            out.writeProperty("forceLayout", true);
        if (hideBorders)
            out.writeProperty("hideBorders", true);
        if (layout!=null)
            layout.serialize(out);
    }

}
