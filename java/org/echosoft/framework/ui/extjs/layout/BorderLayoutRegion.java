package org.echosoft.framework.ui.extjs.layout;

import java.util.ArrayList;
import java.util.List;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.UIComponent;
import org.echosoft.framework.ui.extjs.model.Margins;

/**
 * Данный является частью менеджера упаковки {@link BorderLayout}.
 * @author Anton Sharapov
 */
public class BorderLayoutRegion implements UIComponent {

    public static enum Region {
        NORTH, EAST, SOUTH, WEST, CENTER
    }

    public static enum CollapseMode {
        STANDARD, MINI
    }

    private final List<UIComponent> items;
    private final Region region;        // к какому региону относится данный компонент.
    private boolean animFloat;          // использовать или нет анимацию при открытии и закрытии "плавающей" панели.
    private boolean autoHide;           // следует ли автоматически закрывать "плавающую" панель при потере фокуса.
    private boolean floatable;          // определяет возможность показа региона в "плавающем" виде.
    private Margins cmargins;           // отступы по краям региона когда последний находится в свернутом виде. Никогда не равен null.
    private Margins margins;            // отступы по краям региона. Никогда не равен null.
    private boolean collapsible;        // определяет возможность сворачивания региона.
    private CollapseMode collapseMode;  // определяет способ сворачивания региона.
    private boolean split;              // включение данной опции дает возможность динамически изменять размеры региона (ширину или высоту).
    private String splitTip;            // всплывающая подсказка отображаемая над полоской-разделителем между регионами (используется при split=true).
    private int minSize;                // минимальная ширина/высота региона (зависит от региона).
    private int maxSize;                // максимальная ширина/высота региона (зависит от региона).
    private int width;                  // ширина региона по умолчанию (обязательно к использованию в регионах WEST и EAST).
    private int height;                 // высота региона по умолчанию (обязательно к использованию в регионах NORTH и SOUTH).

    public BorderLayoutRegion(final Region region) {
        if (region==null)
            throw new IllegalArgumentException("No valid region specified");
        this.region = region;
        items = new ArrayList<UIComponent>();
        animFloat = true;
        autoHide = true;
        floatable = true;
        collapseMode = CollapseMode.STANDARD;
        minSize = 50;
        maxSize = 500;
        width = 50;
        height = 50;
    }

    /**
     * {@inheritDoc}
     * <p><strong>Важно!</strong> Данный тип компонент не требует никакого взаимодействия с внешним миром и поэтому данный метод всегда возвращает <code>null</code>.</p>
     * @return  Всегда возвращает <code>null</code>.
     */
    public ComponentContext getContext() {
        return null;
    }

    /**
     * Возвращает регион в котором данный компонент будет размещен. <br/>
     * В контейнере не может находиться более одного компонента с одинаковым значением данного свойства.
     * @return  регион в котором расположен данный компонент.
     */
    public Region getRegion() {
        return region;
    }

    /**
     * Определяет используются ли анимационные эффекты при открытии и закрытии "всплывающей" панели.<br/>
     * Значение по умолчанию: <code>true</code>.
     * @return <code>true</code> если анимационные эффекты используются.
     */
    public boolean isAnimFloat() {
        return animFloat;
    }
    /**
     * Определяет используются ли анимационные эффекты при открытии и закрытии "всплывающей" панели.<br/>
     * Значение по умолчанию: <code>true</code>.
     * @param animFloat следует указывать <code>true</code> если анимационные эффекты должны используются.
     */
    public void setAnimFloat(final boolean animFloat) {
        this.animFloat = animFloat;
    }

    /**
     * Возвращает <code>true</code> если "всплывающая" панель должна автоматически скрываться при потере фокуса.<br/>
     * Значение по умолчанию: <code>true</code>.
     * @return <code>true</code> если "всплывающая" панель должна автоматически скрываться при потере фокуса.
     */
    public boolean isAutoHide() {
        return autoHide;
    }
    /**
     * Определяет должна ли всплывающая панель автоматически скрываться при потере фокуса.<br/>
     * Значение по умолчанию: <code>true</code>.
     * @param autoHide <code>true</code> если "всплывающая" панель должна автоматически скрываться при потере фокуса.
     */
    public void setAutoHide(final boolean autoHide) {
        this.autoHide = autoHide;
    }

    /**
     * Возвращает <code>true</code> если данный регион будучи в свернутом состоянии может "всплывать" над основным контентом контейнера.<br/>
     * Значение свойства по умолчанию: <code>true</code>.
     * @return  <code>true</code> если данный регион может отображаться в "всплывающем" виде.
     */
    public boolean isFloatable() {
        return floatable;
    }
    /**
     * Определяет может ли данный регион будучи в свернутом состоянии "всплывать" над основным контентом контейнера.<br/>
     * Значение свойства по умолчанию: <code>true</code>.
     * @param floatable  <code>true</code> если данный регион может отображаться в "всплывающем" виде.
     */
    public void setFloatable(final boolean floatable) {
        this.floatable = floatable;
    }

    /**
     * Возвращает информацию об отступах вокруг региона занимаемого компонентом в контейнере в тот момент когда компонент находится в свернутом виде.
     * @return отступы вокруг региона когда компонент находится в свернутом состоянии. Метод никогда не возвращает <code>null</code>.
     */
    public Margins getCmargins() {
        return cmargins;
    }
    /**
     * Указывает информацию об отступах вокруг региона занимаемого компонентом в контейнере в тот момент когда компонент находится в свернутом виде.
     * @param cmargins величина отступов вокруг региона когда компонент находится в свернутом состоянии. Не может быть <code>null</code>.
     */
    public void setCmargins(final Margins cmargins) {
        this.cmargins = cmargins;
    }

    /**
     * Возвращает информацию об отступах вокруг региона занимаемого компонентом в контейнере.
     * @return отступы вокруг региона занимаемого компонентом в контейнере. Метод никогда не возвращает <code>null</code>.
     */
    public Margins getMargins() {
        return margins;
    }
    /**
     * Указывает информацию об отступах вокруг региона в контейнере.
     * @param margins величина отступов вокруг региона в контейнере. Метод никогда не возвращает <code>null</code>.
     */
    public void setMargins(final Margins margins) {
        this.margins = margins;
    }

    /**
     * Возвращает <code>true</code> если регион поддерживает возможность сворачивания.<br/>
     * Значение свойства по умолчанию: <code>false</code>.
     * @return <code>true</code> если регион поддерживает возможность сворачивания.
     */
    public boolean isCollapsible() {
        return collapsible;
    }
    /**
     * Указывает поддерживает ли данный регион возможность сворачивания.<br/>
     * Значение свойства по умолчанию: <code>false</code>.
     * @param collapsible <code>true</code> если регион поддерживает возможность сворачивания.
     */
    public void setCollapsible(final boolean collapsible) {
        this.collapsible = collapsible;
    }

    /**
     * Возвращает информацию об используемом способе сворачивания региона.
     * Используется когда установлены свойства <code>collapsible</code> или <code>split</code>.
     * @return  способ сворачивания региона.
     */
    public CollapseMode getCollapseMode() {
        return collapseMode;
    }
    /**
     * Определяет способ сворачивания региона.
     * Используется когда установлены свойства <code>collapsible</code> или <code>split</code>.
     * @param collapseMode способ сворачивания региона. Не может быть <code>null</code>.
     */
    public void setCollapseMode(final CollapseMode collapseMode) {
        this.collapseMode = collapseMode!=null ? collapseMode : CollapseMode.STANDARD;
    }

    /**
     * Возвращает <code>true</code> если пользовать может изменять размер данного региона в процессе работы. Свойство не поддерживается для центрального региона.<br/>
     * Значение свойства по умолчанию: <code>false</code>.
     * @return  <code>true</code>  если размер данного региона можно динамически изменять.
     */
    public boolean isSplit() {
        return split;
    }
    /**
     * Определяет возможно ли изменение пользователем размеров данного региона в процессе работы.
     * Значение свойства по умолчанию: <code>false</code>.
     * @param split  <code>true</code>  если размер данного региона можно динамически изменять.
     */
    public void setSplit(final boolean split) {
        this.split = split;
    }

    /**
     * Возвращает всплывающую подсказку, отображаемую над полоской-разделителем между регионами. Используется только при split=true.
     * @return  всплывающая подсказка или <code>null</code>.
     */
    public String getSplitTip() {
        return splitTip;
    }
    /**
     * Указывает всплывающую подсказку, отображаемую над полоской-разделителем между регионами. Используется только при split=true.
     * @param splitTip  всплывающая подсказка или <code>null</code>.
     */
    public void setSplitTip(final String splitTip) {
        this.splitTip = StringUtil.trim(splitTip);
    }

    /**
     * Возвращает минимально допустимый размер (ширина или высота) региона, используется при динамическом изменении пользователем размера региона<br/>
     * Значение свойства по умолчанию: <code>50</code> пикселей.
     * @return  минимально допустимый размер региона в пикселях.
     */
    public int getMinSize() {
        return minSize;
    }
    /**
     * Указывает минимально допустимый размер (ширина или высота) региона, используется при динамическом изменении пользователем размера региона<br/>
     * Значение свойства по умолчанию: <code>50</code> пикселей.
     * @param minSize  минимально допустимый размер региона в пикселях.
     */
    public void setMinSize(final int minSize) {
        this.minSize = minSize;
    }

    /**
     * Возвращает максимально допустимый размер (ширина или высота) региона, используется при динамическом изменении пользователем размера региона<br/>
     * Значение свойства по умолчанию: <code>500</code> пискселей.
     * @return  максимально допустимый размер региона.
     */
    public int getMaxSize() {
        return maxSize;
    }
    /**
     * Указывает максимально допустимый размер (ширина или высота) региона, используется при динамическом изменении пользователем размера региона<br/>
     * Значение свойства по умолчанию: <code>50</code> пикселей.
     * @param maxSize  максимально допустимый размер региона в пикселях.
     */
    public void setMaxSize(final int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Возвращает используемую по умолчанию ширину регионов EAST и WEST.
     * @return  ширина регионов EAST и WEST по умолчанию. 
     */
    public int getWidth() {
        return width;
    }
    /**
     * Устанавливает используемую по умолчанию ширину регионов EAST и WEST.
     * @param width  ширина регионов EAST и WEST по умолчанию. 
     */
    public void setWidth(final int width) {
        this.width = width;
    }

    /**
     * Возвращает используемую по умолчанию высоту регионов NORTH и AOUTH.
     * @return  высота регионов NORTH и SOUTH по умолчанию.
     */
    public int getHeight() {
        return height;
    }
    /**
     * Устанавливает используемую по умолчанию высоту регионов NORTH и SOUTH.
     * @param height высота регионов NORTH и SOUTH по умолчанию.
     */
    public void setHeight(final int height) {
        this.height = height;
    }

    /**
     * Возвращает количество компонент размещенных в данном регионе.
     * @return  количество компонент размещенных в данном регионе.
     */
    public int getItemsCount() {
        return items.size();
    }

    /**
     * Возвращает итератор по всем компонентам размещенным в данном регионе.
     * @return  все компонента размещенные в данном регионе.
     */
    public Iterable<UIComponent> getItems() {
        return items;
    }

    /**
     * Регистрирует новый компонент в данном регионе.
     * @param  item  новый компонент который должен быть добавлен в данный регион.
     * @return  Добавленный в регион компонент.
     */
    public <T extends UIComponent> T append(final T item) {
        if (item==null)
            throw new IllegalArgumentException("Component must be specified");
        items.add(item);
        return item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invoke(final JsonWriter out) throws Exception {
        out.beginObject();
        out.writeProperty("region", region.name().toLowerCase());
        if (!animFloat)
            out.writeProperty("animFloat", false);
        if (!autoHide)
            out.writeProperty("autoHide", false);
        if (!floatable)
            out.writeProperty("floatable", false);
        if (cmargins!=null && !cmargins.isEmpty())
            out.writeProperty("cmargins", cmargins.encode());
        if (margins!=null && !margins.isEmpty())
            out.writeProperty("margins", margins.encode());
        if (collapsible)
            out.writeProperty("collapsible", true);
        if (collapseMode!=CollapseMode.STANDARD)
            out.writeProperty("collapseMode", collapseMode.name().toLowerCase());
        if (split)
            out.writeProperty("split", true);
        if (splitTip!=null)
            out.writeProperty("splitTip", splitTip);
        if (minSize!=50 && minSize>=0)
            out.writeProperty("minSize", minSize);
        if (maxSize!=500 && maxSize>=0)
            out.writeProperty("maxSize", maxSize);
        if (region==Region.NORTH || region==Region.SOUTH) {
            out.writeProperty("height", height);
        } else {
            out.writeProperty("width", width);
        }
        if (items.size()>0) {
            out.writeComplexProperty("items");
            out.beginArray();
            for (UIComponent component : items) {
                component.invoke(out);
            }
            out.endArray();
        }
        out.endObject();
    }

}
