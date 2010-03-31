package org.echosoft.framework.ui.extjs.layout;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.echosoft.common.json.JsonWriter;

/**
 * Описывает характеристики менеджера компоновки <code>Ext.layout.HBoxLayout</code>.
 * @author Anton Sharapov
 */
public class HBoxLayout extends BoxLayout {

    public static enum Align {
        TOP, MIDDLE, STRETCH, STRETCHMAX
    }

    private Align align;        // определяет способ выравнивания компонент в контейнере.
    private Pack pack;          // определяет способ упаковки компонент в контейнере.
    private Integer flex;       // свойство должно применяться ко всем компонентам в контейнере.

    public HBoxLayout() {
        super();
        align = Align.TOP;
        pack = Pack.START;
    }


    /**
     * Возвращает используемый менеджером компоновки способ выравнивания компонент в контейнере.
     * @return используемый способ выравнивания компонент.
     *      По умолчанию возвращает {@link Align#TOP}
     */
    public Align getAlign() {
        return align;
    }
    /**
     * Задает используемый менеджером компоновки способ выравнивания компонент в контейнере.
     * @param align используемый способ выравнивания компонент. Если <code>null</code> то
     *      будет использовано значение по умолчанию - {@link Align#TOP}.
     */
    public void setAlign(final Align align) {
        this.align = align!=null ? align : Align.TOP;
    }

    /**
     * Возвращает используемый менеджером компоновки способ упаковки компонент в контейнере.
     * @return используемый способ упаковки компонент в контейнере.
     *  По умолчанию возвращает {@link Pack#START}.
     */
    public Pack getPack() {
        return pack;
    }
    /**
     * Задает используемый менеджером компоновки способ упаковки компонент в контейнере.
     * @param pack используемый способ упаковки компонент в контейнере. Если <code>null</code> то
     *  будет использовано значение по умолчанию - {@link Pack#START}.
     */
    public void setPack(final Pack pack) {
        this.pack = pack!=null ? pack : Pack.START;
    }

    public Integer getFlex() {
        return flex;
    }
    public void setFlex(final Integer flex) {
        this.flex = flex;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getLayout() {
        return "hbox";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isCustomized() {
        return super.isCustomized() || align!=Align.TOP || pack!=Pack.START || flex!=null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void serializeConfigAttrs(final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException {
        super.serializeConfigAttrs(out);
        if (align!=Align.TOP)
            out.writeProperty("align", align.name().toLowerCase());
        if (pack!=Pack.START)
            out.writeProperty("pack", pack.name().toLowerCase());
        if (flex!=null)
            out.writeProperty("flex", flex);
    }

}
