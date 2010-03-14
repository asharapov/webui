package org.echosoft.framework.ui.extjs.layout;

/**
 * Описывает характеристики менеджера раскладки <code>Ext.layout.AccordionLayout</code>.<br/>
 * <strong>Работает только с панелями!</strong>
 * @author Anton Sharapov
 */
public class AccordionLayout extends FitLayout {

    private boolean activeOnTop;            // каждый раз перемещает активную панель в вверх списка.
    private boolean animate;                // использует или нет анимацию при открытии/закрытии панелей.
    private boolean autoWidth;              // 
    private boolean collapseFirst;
    private boolean fill;
    private boolean hideCollapseTool;
    private boolean titleCollapse;

    public AccordionLayout() {
        super();
        setAutoWidth(true);
        setFill(true);
        setTitleCollapse(true);
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


    public boolean isActiveOnTop() {
        return activeOnTop;
    }
    public void setActiveOnTop(final boolean activeOnTop) {
        this.activeOnTop = activeOnTop;
        if (activeOnTop) {
            this.animate = false;
        }
    }

    public boolean isAnimate() {
        return animate;
    }
    public void setAnimate(final boolean animate) {
        this.animate = animate;
        if (animate) {
            this.activeOnTop = false;
        }
    }

    public boolean isAutoWidth() {
        return autoWidth;
    }
    public void setAutoWidth(final boolean autoWidth) {
        this.autoWidth = autoWidth;
    }

    public boolean isCollapseFirst() {
        return collapseFirst;
    }
    public void setCollapseFirst(final boolean collapseFirst) {
        this.collapseFirst = collapseFirst;
    }

    public boolean isFill() {
        return fill;
    }
    public void setFill(final boolean fill) {
        this.fill = fill;
    }

    public boolean isHideCollapseTool() {
        return hideCollapseTool;
    }
    public void setHideCollapseTool(final boolean hideCollapseTool) {
        this.hideCollapseTool = hideCollapseTool;
        if (hideCollapseTool) {
            this.titleCollapse = true;
        }
    }

    public boolean isTitleCollapse() {
        return titleCollapse;
    }
    public void setTitleCollapse(final boolean titleCollapse) {
        this.titleCollapse = titleCollapse;
        if (!titleCollapse && hideCollapseTool) {
            hideCollapseTool = false;
        }
    }
}