package org.echosoft.framework.ui.extjs.layout;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.extjs.model.Margins;

/**
 * Данный бин описывает характеристики компонент специфичных при работе в контейнере вида <code>Ext.layout.BorderLayout</code>.
 * @author Anton Sharapov
 */
public class BorderLayoutItem extends LayoutItem {

    public static enum Region {
        NORTH, EAST, SOUTH, WEST, CENTER
    }

    public static enum CollapseMode {
        STANDARD, MINI
    }

    private Region region;              // к какому региону относится данный компонент.
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

    public BorderLayoutItem() {
        super();
        region = Region.CENTER;
        animFloat = true;
        autoHide = true;
        floatable = true;
        cmargins = new Margins();
        margins = new Margins();
        collapseMode = CollapseMode.STANDARD;
        minSize = 50;
        maxSize = 500;
    }

    public BorderLayoutItem(final LayoutItem item) {
        super(item);
        if (item instanceof BorderLayoutItem) {
            final BorderLayoutItem other = (BorderLayoutItem)item;
            region = other.region;
            animFloat = other.animFloat;
            autoHide = other.autoHide;
            floatable = other.floatable;
            cmargins = new Margins(other.cmargins);
            margins = new Margins(other.margins);
            collapsible = other.collapsible;
            collapseMode = other.collapseMode;
            split = other.split;
            splitTip = other.splitTip;
            minSize = other.minSize;
            maxSize = other.maxSize;
        } else {
            region = Region.CENTER;
            animFloat = true;
            autoHide = true;
            floatable = true;
            cmargins = new Margins();
            margins = new Margins();
            collapseMode = CollapseMode.STANDARD;
            minSize = 50;
            maxSize = 500;
        }
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
     * Определяет регион в котором данный компонент будет размещен. <br/>
     * В контейнере не может находиться более одного компонента с одинаковым значением данного свойства.
     * @param region  регион в котором будет расположен данный компонент. Не может быть <code>null</code>.
     */
    public void setRegion(final Region region) {
        this.region = region!=null ? region : Region.CENTER;
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
        this.cmargins = cmargins!=null ? cmargins : new Margins();
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
        this.margins = margins!=null ? margins : new Margins();
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
     * @param maxSize  максисмально допустимый размер региона в пикселях.
     */
    public void setMaxSize(final int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public void serialize(final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException {
        super.serialize(out);
        out.writeProperty("region", region.name().toLowerCase());
        if (!animFloat)
            out.writeProperty("animFloat", false);
        if (!autoHide)
            out.writeProperty("autoHide", false);
        if (!floatable)
            out.writeProperty("floatable", false);
        if (!cmargins.isEmpty())
            out.writeProperty("cmargins", cmargins.encode());
        if (!margins.isEmpty())
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
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        final BorderLayoutItem result = (BorderLayoutItem)super.clone();
        result.cmargins = (Margins)cmargins.clone();
        result.margins = (Margins)margins.clone();
        return result;
    }
}
