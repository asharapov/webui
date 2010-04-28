package org.echosoft.framework.ui.extjs.widgets.form;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.AbstractContainerComponent;
import org.echosoft.framework.ui.extjs.layout.HBoxLayout;
import org.echosoft.framework.ui.extjs.layout.Layout;

/**
 * Поле ввода в форме, состоящее из множества других полей, упакованных в контейнер при помощи менеджера компоновки <code>Ext.layout.HBox</code>.
 * @author Anton Sharapov
 */
public class CompositeField extends AbstractContainerComponent {

    private boolean combineErrors;      // определяет способ отображения сообщений об ошибках в компонентах лежащих в этом контейнере.

    public CompositeField() {
        this(null);
    }
    public CompositeField(final ComponentContext ctx) {
        super(ctx);
        combineErrors = true;
    }

    @Override
    public HBoxLayout getLayout() {
        return (HBoxLayout)super.getLayout();
    }
    @Override
    public void setLayout(final Layout layout) {
        if (layout instanceof HBoxLayout) {
            layout.setSkipLayout(true);
            super.setLayout(layout);
        } else
            throw new IllegalArgumentException("CompositeField supports 'Ext.layout.HBoxLayout' only");
    }

    /**
     * Возвращает <code>true</code> если компонент должен самостоятельно отображать все сообщения об ошибках относящиеся к дочерним компонентам.
     * @return <code>true</code> если компонент должен самостоятельно отображать все сообщения об ошибках относящиеся к дочерним компонентам.
     * Значение свойства по умолчанию: <code>true</code>.
     */
    public boolean isCombineErrors() {
        return combineErrors;
    }
    /**
     * Указывает должен ли компонент самостоятельно отображать все сообщения об ошибках относящиеся к дочерним компонентам.
     * @param combineErrors <code>true</code> если компонент должен самостоятельно отображать все сообщения об ошибках относящиеся к дочерним компонентам.
     */
    public void setCombineErrors(final boolean combineErrors) {
        this.combineErrors = combineErrors;
    }

    @Override
    public void invoke(final JsonWriter out) throws Exception {
        out.beginObject();
        out.writeProperty("xtype", "compositefield");
        renderContent(out);
        out.endObject();
    }

    @Override
    public void renderContent(final JsonWriter out) throws Exception {
        super.renderContent(out);
        if (!combineErrors)
            out.writeProperty("combineErrors", false);
    }

    @Override
    protected Layout makeDefaultLayout() {
        final HBoxLayout layout = new HBoxLayout();
        layout.setSkipLayout(true);
        return layout;
    }
}
