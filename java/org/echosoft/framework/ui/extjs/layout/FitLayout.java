package org.echosoft.framework.ui.extjs.layout;

import java.util.Collections;

import org.echosoft.framework.ui.core.UIComponent;

/**
 * <p>Описывает характеристики менеджера компоновки <code>Ext.layout.FitLayout</code>.</p>
 * <p><strong>Важно!</strong> Данный тип менеджера компоновки поддерживает максимум один дочерний компонент</p>
 * @author Anton Sharapov
 */
public class FitLayout extends Layout {

    private UIComponent item;

    public FitLayout() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemsCount() {
        return item ==null ? 0 : 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<UIComponent> getItems() {
        return item ==null
                ? Collections.<UIComponent>emptyList()
                : Collections.singletonList(item);
    }

    /**
     * {@inheritDoc}
     *
     * <p><strong>ВАЖНО!</strong> Особенностью компоновщика {@link FitLayout} является то, что он может управлять только одним компонентом,
     * соответственно при попытке добавить более одного компонента метод будет всегда возвращать <code>null</code>.</p>
     * @return  Зарегистрированный в компоновщике в результате данного вызова компонент или <code>null</code> если в менеджере
     * уже был зарегистрирован ранее хотя бы один компонент.
     * @see #getItem()
     * @see #setItem(UIComponent)
     */
    @Override
    public <T extends UIComponent> T append(final T item) {
        if (item==null)
            throw new IllegalArgumentException("Component must be specified");
        if (this.item!=null)
            throw new IllegalStateException("This layout manager can't manage more than one component in container");
        this.item = item;
        return item;
    }

    public UIComponent getItem() {
        return item;
    }
    public void setItem(final UIComponent item) {
        this.item = item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getLayout() {
        return "fit";
    }

}