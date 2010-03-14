package org.echosoft.framework.ui.extjs.layout;

import org.echosoft.framework.ui.extjs.model.Margins;

/**
 * Описывает характеристики менеджера раскладки <code>Ext.layout.BoxLayout</code>.
 * @author Anton Sharapov
 */
public class BoxLayout extends Layout {

    private Margins defaultMargins;     // применяется к каждому компоненту в контейнере у которого не указано соответствующее свойство.
    private Margins padding;            // указывает отступы применяемые ко всем компонетам в контейнере.

    public BoxLayout() {
        super();
        defaultMargins = new Margins();
        padding = new Margins();
    }
    
    public LayoutItem makeItem() {
        return new BoxLayoutItem();
    }

    public LayoutItem makeItem(final LayoutItem item) {
        return new BoxLayoutItem(item);
    }

    public String getLayout() {
        return "box";
    }

    /**
     * Возвращает величину отступов которые будут применяться к каждому компоненту в контейнере у которого не было явно указан соответствующий параметр.
     * @return величина отступов по умолчанию для всех компонент в контейнере. Метод никогда не возвращает <code>null</code>.
     */
    public Margins getDefaultMargins() {
        return defaultMargins;
    }
    /**
     * Указывает величину отступов которые будут применяться к каждому компоненту в контейнере у которого не было явно указан соответствующий параметр.
     * @param defaultMargins величина отступов по умолчанию для всех компонент в контейнере.
     */
    public void setDefaultMargins(final Margins defaultMargins) {
        this.defaultMargins = defaultMargins!=null ? defaultMargins : new Margins();
    }

    /**
     * Определяет величину отступов от границ контейнера для всех компонентов в нем. 
     * @return  отступы определенные для всех компонент контейнера или <code>null</code>.
     */
    public Margins getPadding() {
        return padding;
    }
    /**
     * Определяет величину отступов от границ контейнера для всех компонентов в нем.
     * @param padding отступы определенные для всех компонент контейнера или <code>null</code>. 
     */
    public void setPadding(final Margins padding) {
        this.padding = padding;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        final BoxLayout result = (BoxLayout)super.clone();
        result.defaultMargins = (Margins)defaultMargins.clone();
        result.padding = (Margins)padding.clone();
        return result;
    }
}