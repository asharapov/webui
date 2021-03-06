package org.echosoft.framework.ui.extjs.widgets;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.AbstractContainerComponent;
import org.echosoft.framework.ui.extjs.layout.Layout;
import org.echosoft.framework.ui.extjs.layout.TableLayout;

/**
 * Данный компонент как правило используется в панелях инструментов и представляет
 * собой группу кнопок и иных контролов, оформленную в стиле MS Office 2007.
 * @author Anton Sharapov
 */
public class ButtonGroup extends AbstractContainerComponent {

    private String title;

    public ButtonGroup() {
        this(null);
    }
    public ButtonGroup(final ComponentContext ctx) {
        super(ctx);
    }

    @Override
    public TableLayout getLayout() {
        return (TableLayout)super.getLayout();
    }

    @Override
    public void setLayout(final Layout layout) {
        if (layout instanceof TableLayout) {
            layout.setSkipLayout(true);
            super.setLayout(layout);
        } else
            throw new IllegalArgumentException("ButtonGroup supports 'Ext.layout.TableLayout' only");
    }

    /**
     * Возвращает текст, отображаемый в заголовке группы кнопок.
     * @return текст отображаемый в заголовке или <code>null</code> если в заголовок отображать не требуется.
     * По умолчанию возвращает <code>null</code>.
     */
    public String getTitle() {
        return title;
    }
    /**
     * Задает текст, отображаемый в заголовке группы кнопок.
     * @param title текст отображаемый в заголовке или <code>null</code> если в заголовок отображать не требуется.
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Возвращает количество колонок на которые компоновщик будет разбивать все кнопки в группе.
     * Если свойство возвращает <code>null</code> то все компоненты будут отображаться в одну линию (как если бы кол-во колонок было бесконечным).
     * @return количество колонок на которые будут биться все кнопки (и прочие компоненты) в контейнере.
     *  По умолчанию возвращает <code>null</code>.
     */
    public Integer getColumns() {
        return getLayout().getColumns();
    }
    /**
     * Задает количество колонок на которые компоновщик будет разбивать все кнопки в контейнере.
     * Если свойство возвращает <code>null</code> то все кнопки будут отображаться в одну линию (как если бы кол-во колонок было бесконечным).
     * @param columns количество колонок на которые будут биться все кнопки в контейнере.
     */
    public void setColumns(final Integer columns) {
        getLayout().setColumns(columns);
    }

    /**
     * Регистрирует новый компонент {@link Button} на панели инструментов.
     * @param text  текст на кнопке.
     * @return новый экземпляр компонента {@link Button} который был помещен на панель инструментов.<br/>
     *  <strong>Внимание!</strong> в возвращаемом экземпляре не установлен контекст выполнения.
     */
    public Button addButton(final String text) {
        final Button button = getLayout().append( new Button() );
        button.setText(text);
        return button;
    }

    @Override
    public void invoke(final JsonWriter out) throws Exception {
        out.beginObject();
        out.writeProperty("xtype", "buttongroup");
        renderContent(out);
        out.endObject();
    }

    @Override
    protected void renderContent(final JsonWriter out) throws Exception {
        super.renderContent(out);
        if (title != null)
            out.writeProperty("title", title);
    }

    @Override
    protected Layout makeDefaultLayout() {
        final TableLayout layout = new TableLayout();
        layout.setSkipLayout(true);
        return layout;
    }
}
