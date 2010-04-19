package org.echosoft.framework.ui.extjs.widgets;

import java.util.Set;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.json.annotate.JsonUseSeriazer;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.AbstractContainerComponent;
import org.echosoft.framework.ui.extjs.layout.CardLayout;
import org.echosoft.framework.ui.extjs.layout.Layout;
import org.echosoft.framework.ui.extjs.spi.model.EnumLCJSONSerializer;

/**
 * Панель закладок.
 * @author Anton Sharapov
 */
public class TabPanel extends AbstractContainerComponent {

    public static Set<String> EVENTS =
            StringUtil.asUnmodifiableSet(AbstractContainerComponent.EVENTS,
                    "activate", "deactivate", "beforeclose", "close",
                    "beforetabchange", "tabchange", "contextmenu");
    /**
     * Определяет расположение ярлыков к панелям.
     */
    @JsonUseSeriazer(EnumLCJSONSerializer.class)
    public static enum Position {
        TOP, BOTTOM
    }

    private static final int DEFAULT_TAB_WIDTH = 120;
    private static final int DEFAULT_MIN_TAB_WIDTH = 30;

    private boolean enableTabScroll;        // разрешает скроллировать закладки если они не умещаются в контейнере.
    private boolean layoutOnTabChange;      // дает возм-ть обновлять компоновку компонент на активной панели при установке фокуса на ней.
    private boolean plain;                  // отрисовывает панели без фоновой окантовки.
    private boolean resizeTabs;             // разрешает изменять размеры закладок.
    private int tabWidth;                   // ширина закладок по умолчанию (работает только при resizeTabs=true).
    private int minTabWidth;                // минимальная ширина закладок (работает только при resizeTabs=true).
    private Position tabPosition;           // определяет расположение закладок.
    private Toolbar tbar;                   // Верхняя панель инструментов.
    private Toolbar bbar;                   // Нижняя панель инструментов.
    private Toolbar fbar;                   // Панель инструментов в самом низу панели.

    public TabPanel() {
        this(null);
    }
    public TabPanel(final ComponentContext ctx) {
        super(ctx);
        tabWidth = DEFAULT_TAB_WIDTH;
        minTabWidth = DEFAULT_MIN_TAB_WIDTH;
        tabPosition = Position.TOP;
        setLayout( new CardLayout() );
    }

    @Override
    public void setLayout(final Layout layout) {
        if (layout instanceof CardLayout) {
            super.setLayout(layout);
        } else
            throw new IllegalArgumentException("Given component supports card layout only");
    }

    /**
     * Возвращает итератор по всем панелям управляемым данным компонентом.
     * @return итератор по всем панелям управляемым данным компонентом.
     */
    public Iterable<Panel> getPanels() {
        return ((CardLayout)getLayout()).getPanels();
    }

    /**
     * Возвращает <code>true</code> если разрешен режим скроллирования закладок в случае нехватки пространства.
     * @return <code>true</code> если разрешен режим скроллирования закладок в случае нехватки пространства.
     * По умолчанию возвращает <code>false</code>.
     */
    public boolean isEnableTabScroll() {
        return enableTabScroll;
    }
    /**
     * Разрешает режим скроллирования закладок в случае нехватки пространства.
     * @param enableTabScroll <code>true</code> если разрешен режим скроллирования закладок в случае нехватки пространства.
     */
    public void setEnableTabScroll(final boolean enableTabScroll) {
        this.enableTabScroll = enableTabScroll;
    }

    /**
     * Требуется ли перекомпоновывать содержимое панели при ее активации.
     * @return По умолчанию возвращает <code>false</code>.
     */
    public boolean isLayoutOnTabChange() {
        return layoutOnTabChange;
    }
    /**
     * Требуется ли перекомпоновывать содержимое панели при ее активации.
     * @param layoutOnTabChange <code>true</code> если требуется компоновка содержимого панели при каждой ее активации.
     */
    public void setLayoutOnTabChange(final boolean layoutOnTabChange) {
        this.layoutOnTabChange = layoutOnTabChange;
    }

    /**
     * Возвращает <code>true</code> если под закладками панелей не требуется отображать фоновое изображение.
     * @return <code>true</code> если под закладками панелей не требуется отображать фоновое изображение.
     * По умолчанию возвращает <code>false</code>.
     */
    public boolean isPlain() {
        return plain;
    }
    /**
     * Определяет требуется ли под закладками панелей отображать фоновое изображение.
     * @param plain <code>true</code> если под закладками панелей не требуется отображать фоновое изображение.
     */
    public void setPlain(final boolean plain) {
        this.plain = plain;
    }

    /**
     * Возвращает <code>true</code> если допускается изменение размеров закладок панелей в соответствии с доступным
     * для них пространством контейнера.
     * @return <code>true</code> если размеры закладок панелей могут изменяться в зависимости от наличия доступного
     * для них пространства.
     *  По умолчанию возвращает <code>false</code>.
     */
    public boolean isResizeTabs() {
        return resizeTabs;
    }
    public void setResizeTabs(final boolean resizeTabs) {
        this.resizeTabs = resizeTabs;
    }

    /**
     * Возвращает ширину закладок по умолчанию. 
     * Работает только когда свойство <code>resizeTabs</code> = <code>true</code>.
     * @return ширина закладок по умолчанию.
     */
    public int getTabWidth() {
        return tabWidth;
    }
    /**
     * Устанавливает ширину закладок по умолчанию.
     * Работает только когда свойство <code>resizeTabs</code> = <code>true</code>.
     * @param tabWidth ширина закладок по умолчанию.
     */
    public void setTabWidth(final int tabWidth) {
        this.tabWidth = tabWidth>=0 ? tabWidth : DEFAULT_TAB_WIDTH;
    }

    /**
     * Возвращает минимально допустимую ширину закладок.
     * Работает только когда свойство <code>resizeTabs</code> = <code>true</code>.
     * @return минимально допустимая ширина закладок. 
     */
    public int getMinTabWidth() {
        return minTabWidth;
    }
    /**
     * Устанавливает минимально допустимую ширину закладок.
     * Работает только когда свойство <code>resizeTabs</code> = <code>true</code>.
     * @param minTabWidth минимально допустимая ширина закладок.
     */
    public void setMinTabWidth(final int minTabWidth) {
        this.minTabWidth = minTabWidth>=0 ? minTabWidth : DEFAULT_MIN_TAB_WIDTH;
    }

    /**
     * Возвращает положение ярлыков панелей относительно самих панелей.
     * @return {@link Position#TOP} если ярлыки должны быть расположены выше и
     *      {@link Position#BOTTOM} если ярлыки должны быть расположены ниже панелей.
     *      Значение свойства по умолчанию: {@link Position#TOP}.
     */
    public Position getTabPosition() {
        return tabPosition;
    }
    /**
     * Указывает положение ярлыков панелей относительно самих панелей.
     * @param tabPosition {@link Position#TOP} если ярлыки должны быть расположены выше и
     *      {@link Position#BOTTOM} если ярлыки должны быть расположены ниже панелей.
     */
    public void setTabPosition(final Position tabPosition) {
        this.tabPosition = tabPosition!=null ? tabPosition : Position.TOP;
    }

    /**
     * Возвращает ссылку на линейку инструментов расположенную в верхней части панели.
     * @return ссылку на линейку инструментов расположенную в верхней части панели или <code>null</code>.
     */
    public Toolbar getTopToolbar() {
        return tbar;
    }
    /**
     * Указывает ссылку на линейку инструментов которая должна располагаться в верхней части панели.
     * @param tbar ссылку на линейку инструментов которая должна располагаться в верхней части панели или <code>null</code>.
     */
    public void setTopToolbar(final Toolbar tbar) {
        this.tbar = tbar;
    }
    public Toolbar assignTopToolbar(final Toolbar tbar) {
        this.tbar = tbar;
        return tbar;
    }

    /**
     * Возвращает ссылку на линейку инструментов расположенную в нижней части панели.
     * @return ссылку на линейку инструментов расположенную в нижней части панели или <code>null</code>.
     */
    public Toolbar getBottomToolbar() {
        return bbar;
    }
    /**
     * Указывает ссылку на линейку инструментов которая должна располагаться в нижней части панели.
     * @param bbar ссылку на линейку инструментов которая должна располагаться в нижней части панели или <code>null</code>.
     */
    public void setBottomToolbar(final Toolbar bbar) {
        this.bbar = bbar;
    }
    public Toolbar assignBottomToolBar(final Toolbar bbar) {
        this.bbar = bbar;
        return bbar;
    }

    /**
     * Возвращает ссылку на линейку инструментов расположенную в самой нижней части панели.
     * @return ссылку на линейку инструментов расположенную в самой нижней части панели или <code>null</code>.
     */
    public Toolbar getFooter() {
        return fbar;
    }
    /**
     * Указывает ссылку на линейку инструментов расположенную в самой нижней части панели.
     * @param fbar ссылка на линейку инструментов расположенную в самой нижней части панели или <code>null</code>.
     */
    public void setFooter(final Toolbar fbar) {
        this.fbar = fbar;
    }
    public Toolbar assignFooter(final Toolbar fbar) {
        this.fbar = fbar;
        return fbar;
    }


    public void invoke(final JsonWriter out) throws Exception {
        out.beginObject();
        out.writeProperty("xtype", "tabpanel");
        renderContent(out);
        out.endObject();
    }

    @Override
    protected void renderContent(final JsonWriter out) throws Exception {
        super.renderContent(out);
        if (enableTabScroll)
            out.writeProperty("enableTabScroll", true);
        if (layoutOnTabChange)
            out.writeProperty("layoutOnTabChange", true);
        if (plain)
            out.writeProperty("plain", true);
        if (resizeTabs)
            out.writeProperty("resizeTabs", true);
        if (tabWidth != DEFAULT_TAB_WIDTH)
            out.writeProperty("tabWidth", tabWidth);
        if (minTabWidth != DEFAULT_MIN_TAB_WIDTH)
            out.writeProperty("minTabWidth", minTabWidth);
        if (tabPosition != Position.TOP)
            out.writeProperty("tabPosition", tabPosition);
        if (tbar != null) {
            out.writeComplexProperty("tbar");
            tbar.invoke(out);
        }
        if (bbar != null) {
            out.writeComplexProperty("bbar");
            bbar.invoke(out);
        }
        if (fbar != null) {
            out.writeComplexProperty("fbar");
            fbar.invoke(out);
        }

        final ComponentContext ctx = getContext();
        if (ctx!=null) {
            ctx.getResources().attachScript(ctx.encodeThemeURL("/pkgs/pkg-tabs.js", false));
        }
    }

    @Override
    protected Set<String> getSupportedEvents() {
        return TabPanel.EVENTS;
    }

}
