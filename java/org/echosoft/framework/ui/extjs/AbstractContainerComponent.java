package org.echosoft.framework.ui.extjs;

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

    public static final Set<String> EVENTS =
            StringUtil.asUnmodifiableSet(AbstractBoxComponent.EVENTS,
                    "add", "afterlayout", "beforeadd", "beforeremove", "remove");

    private Layout layout;                      // используемый менеджер упаковки дочерних компонент в контейнере.
    private String activeItem;                  // идентификатор активного компонента в контейнере.
    private boolean forceLayout;                // выполняет компоновку компонент в контейнере даже тогда когда этого можно было бы на первый взгляд и не делать (пр: скрытые/свернутые панели).

    public AbstractContainerComponent(final ComponentContext ctx) {
        super(ctx);
    }

    /**
     * Возвращает менеджер упаковки компонент в контейнере.
     * @return используемый в настоящий момент упаковщик компонент. Никогда не возвращает <code>null</code>.
     */
    public Layout getLayout() {
        if (layout==null)
            layout = new AutoLayout();
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
    /**
     * Устанавливает менеджер упаковки компонент в контейнере и возвращает ссылку на него.<br/>
     * При смене типа упаковщика метод для каждого компонента в контейнере заменяет информацию о его расположении в контейнере.
     * @param layout  упаковщик компонент или <code>null</code> если требуется использовать упаковщик по умолчанию.
     * @return используемый в настоящий момент упаковщик компонент. Никогда не возвращает <code>null</code>.
     * @see #getLayout() 
     * @see #setLayout(Layout)
     */
    @SuppressWarnings("unchecked")
    public <T extends Layout> T assignLayout(final T layout) {
        setLayout(layout);
        return (T)getLayout();
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
     * Регистрирует новый компонент в данном регионе.
     * @param  item  новый компонент который должен быть добавлен в данный регион.
     * @return  Добавленный в регион компонент.
     */
    public <T extends UIComponent> T append(final T item) {
        return getLayout().append(item);
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


    @Override
    protected void renderContent(final JsonWriter out) throws Exception {
        super.renderContent(out);
        if (activeItem!=null)
            out.writeProperty("activeItem", activeItem);
        if (forceLayout)
            out.writeProperty("forceLayout", true);
        if (layout!=null)
            layout.serialize(out);
    }

    @Override
    protected Set<String> getSupportedEvents() {
        return AbstractContainerComponent.EVENTS;
    }

}
