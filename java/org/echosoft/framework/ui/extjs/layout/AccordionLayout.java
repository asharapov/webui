package org.echosoft.framework.ui.extjs.layout;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.echosoft.common.json.JsonWriter;

/**
 * Описывает характеристики менеджера раскладки <code>Ext.layout.AccordionLayout</code>.<br/>
 * <strong>Работает только с панелями!</strong>
 * @author Anton Sharapov
 */
public class AccordionLayout extends FitLayout {

    private boolean activeOnTop;            // каждый раз перемещает активную панель в вверх списка.
    private boolean animate;                // использует или нет анимацию при открытии/закрытии панелей.
    private boolean autoWidth;              // если опция включена, то всем элементам контейнера будет установлено width:'auto'
    private boolean fill;                   // если опция включена, то компоновщик будет стараться использовать все доступное контейнеру пространство по высоте.
    private boolean collapseFirst;          // если опция включена, то в панели инструментов (если таковая будет показываться) кнопка 'collapse/expand' будет отображаться на первом месте.
    private boolean hideCollapseTool;       // если опция включена, то в панели инструментов (если таковая будет показываться) кнопка 'collapse/expand' не будет отображаться.
    private boolean titleCollapse;          // если опция включена, то по клику на заголовке каждой панели в контейнере та будет сворачиваться.

    public AccordionLayout() {
        super();
        autoWidth = true;
        fill = true;
        titleCollapse = true;
    }

    public LayoutItem makeItem() {
        return new AccordionLayoutItem();
    }

    public LayoutItem makeItem(final LayoutItem item) {
        return new AccordionLayoutItem(item);
    }

    public String getLayout() {
        return "accordion";
    }


    /**
     * Если опция включена, то при активации элемента контейнера, он будет перемещаться на первую позицию списка.
     * @return  <code>true</code> если при активации элемента контейнера он должен будет перемещаться на первое место в контейнере.
     *   По умолчанию возвращает <code>false</code>.
     */
    public boolean isActiveOnTop() {
        return activeOnTop;
    }

    /**
     * Устанавливает опцию, указывающую что при активации элемента контейнера, он будет перемещаться на первую позицию списка.
     * @param activeOnTop <code>true</code> если при активации элемента контейнера он должен будет перемещаться на первое место в контейнере.
     */
    public void setActiveOnTop(final boolean activeOnTop) {
        this.activeOnTop = activeOnTop;
        if (activeOnTop) {
            this.animate = false;
        }
    }

    /**
     * Если данная опция включена то операции сворачивания и разворачивания панелей в контейнере будут анимированными.
     * @return <code>true</code> если включена опция анимации сворачивания/разворачивания панелей в контейнере.
     *      По умолчанию возвращает <code>false</code>
     */
    public boolean isAnimate() {
        return animate;
    }
    /**
     * Устаналивает опцию определяющую следует ли анимировать операции сворачивания и разворачивания панелей в контейнере.
     * @param animate если <code>true</code> то опция анимации сворачивания/разворачивания панелей в контейнере будет включена.
     */
    public void setAnimate(final boolean animate) {
        this.animate = animate;
        if (animate) {
            this.activeOnTop = false;
        }
    }

    /**
     * Если данная опция включена то всем элементам контейнера ExtJS будет устанавливать
     * стиль <code>width:'auto'</code>.
     * @return если <code>true</code> то ширина компонент в контейнере будет контролироваться браузером а не ExtJS.
     *      По умолчанию возвращает <code>true</code>.
     */
    public boolean isAutoWidth() {
        return autoWidth;
    }
    /**
     * Опция определяет кто будет контролировать ширину всех компонент в данном контейнере..
     * @param autoWidth  если <code>true</code> то ExtJS будет устанавливать всем компонентам в контейнере стиль <code>width:'auto'</code>.
     */
    public void setAutoWidth(final boolean autoWidth) {
        this.autoWidth = autoWidth;
    }

    /**
     * Если опция включена то компоновщик будет стараться использовать все доступное контейнеру пространство по высоте.<br/>
     * @return <code>true</code> если требуется чтобы компоновщик использовал все доступное контейнеру пространство.
     *      По умолчанию возвращает <code>true</code>.
     */
    public boolean isFill() {
        return fill;
    }
    /**
     * Опция определяет должен ли компоновщик стараться использовать все доступное контейнеру пространство по высоте.<br/>
     * @param fill если <code>true</code> то компоновщик будет использовть все доступное контейнеру пространство.
     */
    public void setFill(final boolean fill) {
        this.fill = fill;
    }

    /**
     * Опция определяет положение кнопки "скрыть/раскрыть" на панели инструментов.
     * @return <code>true</code> если эта кнопка будет крайней слева, <code>false</code> если она будет крайней справа.
     *      По умолчанию возвращает <code>false</code>.
     */
    public boolean isCollapseFirst() {
        return collapseFirst;
    }
    /**
     * Устанавливает опцию определяещую положение кнопки "скрыть/раскрыть" на панели инструментов.
     * @param collapseFirst <code>true</code> если эта кнопка должна быть крайней слева, <code>false</code> если она должна быть крайней справа.
     *      По умолчанию возвращает <code>false</code>.
     */
    public void setCollapseFirst(final boolean collapseFirst) {
        this.collapseFirst = collapseFirst;
    }

    /**
     * Опция определяет наличие кнопки "скрыть/раскрыть" на панели инструментов.
     * @return <code>true</code> если эта кнопка будет скрыта от пользователя; если <code>false</code> (по умолчанию) то кнопка будет отображаться пользователю.
     */
    public boolean isHideCollapseTool() {
        return hideCollapseTool;
    }
    /**
     * Устанавливает опцию определяещую доступность кнопки "скрыть/раскрыть" на панели инструментов для пользователя.
     * @param hideCollapseTool  <code>true</code> если эта кнопка должна быть скрыта от пользователя.
     */
    public void setHideCollapseTool(final boolean hideCollapseTool) {
        this.hideCollapseTool = hideCollapseTool;
        if (hideCollapseTool) {
            this.titleCollapse = true;
        }
    }

    /**
     * Опция определяет возможность сворачивания/разворачивания панели по клику на ее строку заголовка.
     * @return <code>true</code> если по клику на заголовке панели она будет сворачиваться или разворачиваться.
     *      По умолчанию метод возвращает <code>true</code>.
     */
    public boolean isTitleCollapse() {
        return titleCollapse;
    }
    /**
     * Устанавливает опцию определяющую возможность сворачивания/разворачивания панели по клику на ее строку заголовка.
     * @param titleCollapse <code>true</code> если по клику на заголовке панели она будет сворачиваться или разворачиваться.
     *      По умолчанию метод возвращает <code>true</code>.
     */
    public void setTitleCollapse(final boolean titleCollapse) {
        this.titleCollapse = titleCollapse;
        if (!titleCollapse && hideCollapseTool) {
            hideCollapseTool = false;
        }
    }

    @Override
    protected boolean isCustomized() {
        return super.isCustomized() ||
                activeOnTop || animate || !autoWidth || collapseFirst || !fill || hideCollapseTool || !titleCollapse;
    }

    @Override
    protected void serializeConfigAttrs(final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException {
        super.serializeConfigAttrs(out);
        if (activeOnTop)
            out.writeProperty("activeOnTop", true);
        if (animate)
            out.writeProperty("animate", true);
        if (!autoWidth)
            out.writeProperty("autoWidth", false);
        if (collapseFirst)
            out.writeProperty("collapseFirst", true);
        if (!fill)
            out.writeProperty("fill", false);
        if (hideCollapseTool)
            out.writeProperty("hideCollapseTool", true);
        if (!titleCollapse)
            out.writeProperty("titleCollapse", false);
    }


}