package org.echosoft.framework.ui.extjs.layout;

import java.util.ArrayList;
import java.util.List;

import org.echosoft.framework.ui.core.UIComponent;
import org.echosoft.framework.ui.extjs.widgets.Button;
import org.echosoft.framework.ui.extjs.widgets.ButtonGroup;
import org.echosoft.framework.ui.extjs.widgets.ToolbarFill;
import org.echosoft.framework.ui.extjs.widgets.ToolbarSeparator;
import org.echosoft.framework.ui.extjs.widgets.ToolbarSpacer;

/**
 * Описывает характеристики менеджера компоновки <code>Ext.layout.ToolbarLayout</code>.
 * @author Anton Sharapov
 */
public class ToolbarLayout extends Layout {

    private final List<UIComponent> items;

    public ToolbarLayout() {
        super();
        items = new ArrayList<UIComponent>();
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
    @Override
    public Iterable<UIComponent> getItems() {
        return items;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends UIComponent> T append(final T item) {
        if (item==null)
            throw new IllegalArgumentException("Component must be specified");
        items.add(item);
        return item;
    }

    /**
     * Регистрирует новый вспомогательный элемент-разделитель на панели инструментов.
     * @return новый элемент типа {@link ToolbarSpacer}
     */
    public ToolbarSpacer addSpacer() {
        final ToolbarSpacer cmp = new ToolbarSpacer();
        items.add( cmp );
        return cmp;
    }

    /**
     * Регистрирует новый вспомогательный элемент-разделитель на панели инструментов.
     * @return новый элемент типа {@link ToolbarSeparator}
     */
    public ToolbarSeparator addSeparator() {
        final ToolbarSeparator cmp = new ToolbarSeparator();
        items.add( cmp );
        return cmp;
    }

    /**
     * Регистрирует новый вспомогательный элемент-разделитель на панели инструментов.
     * @return новый элемент типа {@link ToolbarFill}
     */
    public ToolbarFill addFill() {
        final ToolbarFill cmp = new ToolbarFill();
        items.add( cmp );
        return cmp;
    }

    /**
     * Регистрирует новый компонент {@link Button} на панели инструментов.
     * @param text  текст на кнопке.
     * @return новый экземпляр компонента {@link Button} который был помещен на панель инструментов.<br/>
     *  <strong>Внимание!</strong> в возвращаемом экземпляре не установлен контекст выполнения.
     */
    public Button addButton(final String text) {
        final Button button = new Button();
        button.setText(text);
        items.add( button );
        return button;
    }

    /**
     * Создает и регистрирует новый компонент {@link ButtonGroup} на панели инструментов.
     * @param title  текст заголовка.
     * @param columns  максимальное кол-во дочерних компонент которые будут расположены на одной строке.
     * @return новый экземпляр компонента {@link Button} который был помещен на панель инструментов.<br/>
     *  <strong>Внимание!</strong> в возвращаемом экземпляре не установлен контекст выполнения.
     */
    public ButtonGroup addButtonGroup(final String title, final Integer columns) {
        final ButtonGroup bg = new ButtonGroup();
        bg.setTitle( title );
        bg.setColumns( columns );
        items.add( bg );
        return bg;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getLayout() {
        return "toolbar";
    }

}
