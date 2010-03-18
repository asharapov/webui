package org.echosoft.framework.ui.extjs;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.layout.AutoLayout;
import org.echosoft.framework.ui.extjs.layout.Layout;
import org.echosoft.framework.ui.extjs.layout.LayoutItem;

/**
 * Базовый класс для всех компонент, которые служат контейнерами для других компонент.
 * @author Anton Sharapov
 */
public abstract class AbstractContainerComponent extends AbstractBoxComponent implements Iterable<AbstractExtJSComponent> {

    public static final Set<String> EVENTS = StringUtil.asUnmodifiableSet(AbstractBoxComponent.EVENTS, "add", "afterlayout", "beforeadd", "beforeremove", "remove");

    private Layout layout;                      // используемый менеджер упаковки дочерних компонент в контейнере.
    private List<AbstractExtJSComponent> items; // список компонент, расположенных в данном контейнере.
    private LayoutItem defaults;                // описывает характеристики расположения компонент в контейнере по умолчанию. Не может быть <code>null</code>.
    private String activeItem;                  // идентификатор активного компонента в контейнере.
    private boolean autoDestroy;                // уничтожает компоненты в контейнере автоматически (по умолчанию) или делегирует это бизнес-программисту (событие 'remove').
    private boolean forceLayout;                // выполняет компоновку компонент в контейнере даже тогда когда этого можно было бы на первый взгляд и не делать (пр: скрытые/свернутые панели).
    private boolean hideBorders;                // следует ли скрывать рамки вокруг каждого компонента в контейнере (false по умолчанию)
    private transient List<AbstractExtJSComponent> _items;

    public AbstractContainerComponent(final ComponentContext ctx) {
        super(ctx);
        layout = new AutoLayout();
        defaults = layout.makeItem();
        items = new ArrayList<AbstractExtJSComponent>();
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
     * @return упаковщик компонент или <code>null</code> если требуется использовать упаковщик по умолчанию.
     */
    public Layout getLayout() {
        return layout;
    }
    /**
     * Устанавливает менеджер упаковки компонент в контейнере.<br/>
     * При смене типа упаковщика метод для каждого компонента в контейнере заменяет информацию о его расположении в контейнере.
     * @param layout  упаковщик компонент или <code>null</code> если требуется использовать упаковщик по умолчанию.
     */
    public void setLayout(Layout layout) {
        if (layout==null) {
            layout = new AutoLayout();
        }
        if (!this.layout.getClass().equals(layout.getClass())) {
            defaults = layout.makeItem(defaults);
            for (AbstractExtJSComponent cmp : items) {
                cmp.setLayoutItem( layout.makeItem(cmp.getLayoutItem()) );
            }
        }
        this.layout = layout;
    }

    /**
     * Возвращает бин со значениями по умолчанию свойств, влияющих на расположение добавляемых компонент в контейнер.
     * @return  характеристики по умолчанию используемые при помещении новых компонент в данный контейнер. Метод никогда не возвращает <code>null</code>.
     */
    public LayoutItem getDefaults() {
        return defaults;
    }

    /**
     * Возвращает количество компонент помещенных в данный контейнер.
     * @return  кол-во компонент в данном контейнере.
     */
    public int getItemsCount() {
        return items.size();
    }

    /**
     * Возвращает неизменяемый список с компонентами расположенным в данном контейнере.
     * @return  неизменяемый список компонент в данном контейнере>
     */
    public List<AbstractExtJSComponent> getItems() {
        if (_items==null) {
            _items = Collections.unmodifiableList(items);
        }
        return _items;
    }

    /**
     * Итератор по всем компонентам расположенным в данном контейнере.
     * Возможно удаление компонента из контейнера при вызове метода <code>remove()</code> итератора.
     * @return  итератор по компонентам в контейнере.
     */
    public Iterator<AbstractExtJSComponent> iterator() {
        return items.iterator();
    }

    /**
     * Добавляет в контейнер в конец списка еще один компонент.<br/>
     * При помещении в контейнер нового компонента у последнего инициализируется свойство {@link AbstractExtJSComponent#getLayoutItem()}
     * @param component  компонент который должен быть добавлен в данный контейнер.
     * @return  ссылка на добавленный в контейнер компонент.
     */
    public AbstractExtJSComponent append(final AbstractExtJSComponent component) {
        component.setLayoutItem( layout.makeItem(defaults) );
        items.add(component);
        return component;
    }

    /**
     * Добавляет в контейнер в указанную позицию списка еще один компонент.<br/>
     * При помещении в контейнер нового компонента у последнего инициализируется свойство {@link AbstractExtJSComponent#getLayoutItem()}
     * @param component  компонент который должен быть добавлен в данный контейнер.
     * @param index  позиция в списке дочерних компонент на которую должен быть помещен добавляемый компонент.
     * @return  ссылка на добавленный в контейнер компонент.
     */
    public AbstractExtJSComponent insert(final AbstractExtJSComponent component, final int index) {
        component.setLayoutItem( layout.makeItem(defaults) );
        items.add(index, component);
        return component;
    }

    /**
     * Удаляет из контейнера компонент по указанному индексу.
     * @param index  описывает положение удаляемого компонента в контейнере.
     * @return  удаленнный из контейнера компонент.
     */
    public AbstractExtJSComponent remove(final int index) {
        return items.remove(index);
    }


    /**
     * Возвращает идентификатор (или альтернативный идентификатор) дочернего компонента в контейнере, который должен быть
     * автоматически помечен как "активный" после загрузки страницы. 
     * @return идентификатор дочернего компонента который должен быть сразу после загрузки страницы помечен как активный. Может быть <code>null</code>.
     * @see AbstractExtJSComponent#getContext()
     * @see LayoutItem#getItemId() 
     */
    public String getActiveItem() {
        return activeItem;
    }
    /**
     * Указывает идентификатор (или альтернативный идентификатор) дочернего компонента в контейнере, который должен быть
     * автоматически помечен как "активный" после загрузки страницы.
     * @param activeItem идентификатор дочернего компонента который должен быть сразу после загрузки страницы помечен как активный. Может быть <code>null</code>.
     * @see AbstractExtJSComponent#getContext()
     * @see LayoutItem#getItemId()
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
        if (defaults!=null) {
            out.writeComplexProperty("defaults");
            out.beginObject();
            defaults.serialize(out);
            out.endObject();
        }

//        TODO: определиться с тем кто и как будет инициировать рендеринг элементов контейнера ...
//        out.writeComplexProperty("items");
//        out.beginArray();
//        for (AbstractExtJSComponent item : items) {
//
//        }
//        out.endArray();
    }

}
