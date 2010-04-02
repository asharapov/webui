package org.echosoft.framework.ui.extjs;

import java.util.Set;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.model.Margins;
import org.echosoft.framework.ui.extjs.model.Point;

/**
 * Абстрактный класс от которого наследуются все компоненты ExtJS имеющие определенные размеры как по ширине так и по высоте.
 * @author Anton Sharapov
 */
public abstract class AbstractBoxComponent extends AbstractComponent {

    public static final Set<String> EVENTS = StringUtil.asUnmodifiableSet(AbstractComponent.EVENTS, "move", "resize");

    private boolean autoScroll;     // дает возможность скроллировать содержимое компонента если оно не влезает в установленные рамки.
    private boolean autoWidth;      // Если true то ширина будет полностью контролироваться браузером (путем установки стиля width:'auto'), в противном случае ширина будет контролироваться ExtJS
    private boolean autoHeight;     // Если true то высота будет полностью контролироваться браузером (путем установки стиля height:'auto'), в противном случае высота будет контролироваться ExtJS
    private Integer width;          // ширина компонента (в пикселях).
    private Integer height;         // высота компонента (в пикселях).
    private Integer maxWidth;       // максимально допустимая ширина (в пикселях).
    private Integer minWidth;       // минимально допустимая ширина (в пикселях).
    private Integer maxHeight;      // максимально допустимая высота (в пикселях).
    private Integer minHeight;      // минимально допустимая высота (в пикселях).
    // свойства, определяющие положение данного компонента в родительском контейнере
    //   под управлением <code>Ext.layout.AbsoluteLayout</code>:
    private Point point;            // координаты левого верхнего угла компонента.
    //   под управлением <code>Ext.layout.AnchorLayout</code>:
    private String anchor;          // строка определяющая ширину и (опционально) высоту  относительно размеров контейнера в целом.
    //   под управлением <code>Ext.layout.BoxLayout</code>:
    private Margins margins;        // отступы от границ контейнера.
    private int flex;               // к-т, определяющий ширину или высоту компонента относительно размеров других компонент в контейнере.
    //   под управлением <code>Ext.layout.FormLayout</code>:
    private String clearCls;        // CSS класс, дополнительно применяемый к области-разделителю между данным и нижележашим компонентами на форме.
    private String itemCls;         // CSS класс, дополнительно применяемый к области включающей в себя и сам компонент и метку к нему.
    private String labelStyle;      // CSS стиль, дополнительно применяемый к метке компонента.
    private String labelSeparator;  // текст (допускается html), используемый в разделителя между меткой и самим компонентом.
    private String fieldLabel;      // метка компонента на форме.
    private boolean hideLabel;      // позволяет скрыть метку к компоненту на форме.
    //   под управлением <code>Ext.layout.ColumnLayout</code>:
    private Double columnWidth;     // ширина компонента в процентах. Число в диапазоне 0.0 (соответствует 0%) - 1.0 ( соответствует 100%)
    //   под управлением <code>Ext.layout.TableLayout</code>:
    private Integer rowspan;        // сколько строк в таблице занимает компонент.
    private Integer colspan;        // сколько колонок в таблице занимает компонент.
    private String cellId;          // идентификатор ячейки таблицы которую занимает компонент.
    private String cellCls;         // CSS класс, применяемый к ячейке таблицы которую занимает данный компонент.

    public AbstractBoxComponent(final ComponentContext ctx) {
        super(ctx);
    }


    /**
     * Возвращает <code>true</code> если требуется возможность скроллировать содержимое компонента в случае когда оно не помещается в установленные границы.
     * @return <code>true</code> если требуется обеспечить скроллирование содержимого компонента. По умолчанию возвращает <code>false</code>.
     */
    public boolean isAutoScroll() {
        return autoScroll;
    }
    /**
     * Указывает требуется ли возможность скроллирования содержимого компонента в случае когда оно не помещается в установленные границы.<br/>
     * На практике приводит к тому что для корневого элемента компонента добавляется стиль <code>overflow:'auto'</code>
     * @param autoScroll <code>true</code> если требуется обеспечить скроллирование содержимого компонента. По умолчанию возвращает <code>false</code>.
     */
    public void setAutoScroll(final boolean autoScroll) {
        this.autoScroll = autoScroll;
    }

    /**
     * <p>Определяет способ расчета ширины компонента.</p>
     * <p>Если свойство установлено в <code>true</code> то ширина будет расчитываться полностью браузером (путем установки стиля width:'auto').
     * В противном случае ширина задается либо путем непосредственного ее задания либо вычисляется по тем или иным правилам менеджером компоновки родительского контейнера.
     * @return <code>true</code> если ширина должна вычисляться браузером, <code>false</code> если ширина должна вычисляться средствами ExtJS.
     */
    public boolean isAutoWidth() {
        return autoWidth;
    }
    /**
     * <p>Определяет способ расчета ширины компонента.</p>
     * <p>Если свойство установлено в <code>true</code> то ширина будет расчитываться полностью браузером (путем установки стиля width:'auto').
     * В противном случае ширина задается либо путем непосредственного ее задания либо вычисляется по тем или иным правилам менеджером компоновки родительского контейнера.
     * @param autoWidth <code>true</code> если ширина должна вычисляться браузером, <code>false</code> если ширина должна вычисляться средствами ExtJS.
     */
    public void setAutoWidth(final boolean autoWidth) {
        this.autoWidth = autoWidth;
    }

    /**
     * <p>Определяет способ расчета высоты компонента.</p>
     * <p>Если свойство установлено в <code>true</code> то высота будет расчитываться полностью браузером (путем установки стиля height:'auto').
     * В противном случае высота задается либо путем непосредственного ее задания либо вычисляется по тем или иным правилам менеджером компоновки родительского контейнера.
     * @return <code>true</code> если высота должна вычисляться браузером, <code>false</code> если высота должна вычисляться средствами ExtJS.
     */
    public boolean isAutoHeight() {
        return autoHeight;
    }
    /**
     * <p>Определяет способ расчета высоты компонента.</p>
     * <p>Если свойство установлено в <code>true</code> то высота будет расчитываться полностью браузером (путем установки стиля height:'auto').
     * В противном случае высота задается либо путем непосредственного ее задания либо вычисляется по тем или иным правилам менеджером компоновки родительского контейнера.
     * @param autoHeight <code>true</code> если высота должна вычисляться браузером, <code>false</code> если высота должна вычисляться средствами ExtJS.
     */
    public void setAutoHeight(final boolean autoHeight) {
        this.autoHeight = autoHeight;
    }

    /**
     * Возвращает ширину компонента в пикселах.
     * Если значение свойства не указано то определение ширины компонента будет возложено на браузер. В этом случае будет также установлено autoWidth
     * соответствующего объекта ExtJS фреймворка. 
     * @return ширина компонента в пикселах.
     */
    public Integer getWidth() {
        return width;
    }
    /**
     * Устанавливает ширину компонента в пикселах.
     * Если значение свойства равно <code>null</code> то определение ширины компонента будет возложено на браузер. В этом случае будет также установлено autoWidth
     * соответствующего объекта ExtJS фреймворка.
     * @param width ширина компонента в пикселах.
     */
    public void setWidth(final Integer width) {
        this.width = width;
    }

    /**
     * Возвращает высоту компонента в пикселах.
     * Если значение свойства не указано то определение высоты компонента будет возложено на браузер. В этом случае будет также установлено autoHeight
     * соответствующего объекта ExtJS фреймворка.
     * @return высота компонента в пикселах.
     */
    public Integer getHeight() {
        return height;
    }
    /**
     * Устанавливает высоту компонента в пикселах.
     * Если значение свойства равно <code>null</code> то определение высоты компонента будет возложено на браузер. В этом случае будет также установлено autoHeight
     * соответствующего объекта ExtJS фреймворка.
     * @param height ширина компонента в пикселах.
     */
    public void setHeight(final Integer height) {
        this.height = height;
    }

    /**
     * Возвращает максимально допустимую ширину компонента в пикселях.
     * @return максимально допустимая ширина или <code>null</code> если ограничения на максимальную ширину для компонента нет.
     */
    public Integer getMaxWidth() {
        return maxWidth;
    }
    /**
     * Устанавливает максимально допустимую ширину компонента в пикселях.
     * @param maxWidth максимально допустимая ширина или <code>null</code> если ограничения на максимальную ширину для компонента нет.
     */
    public void setMaxWidth(final Integer maxWidth) {
        this.maxWidth = maxWidth;
    }

    /**
     * Возвращает минимально допустимую ширину компонента в пикселях.
     * @return минимально допустимая ширина или <code>null</code> если ограничения на минимальную ширину для компонента нет.
     */
    public Integer getMinWidth() {
        return minWidth;
    }
    /**
     * Устанавливает минимально допустимую ширину компонента в пикселях.
     * @param minWidth минимально допустимая ширина или <code>null</code> если ограничения на минимальную ширину для компонента нет.
     */
    public void setMinWidth(final Integer minWidth) {
        this.minWidth = minWidth;
    }

    /**
     * Возвращает максимально допустимую высоту компонента в пикселях.
     * @return максимально допустимая высота или <code>null</code> если ограничения на максимальную высоту для компонента нет.
     */
    public Integer getMaxHeight() {
        return maxHeight;
    }
    /**
     * Устанавливает максимально допустимую высоту компонента в пикселях.
     * @param maxHeight максимально допустимая высота или <code>null</code> если ограничения на максимальную высоту для компонента нет.
     */
    public void setMaxHeight(final Integer maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     * Возвращает минимально допустимую высоту компонента в пикселях.
     * @return минимально допустимая высота или <code>null</code> если ограничения на минимальную высоту для компонента нет.
     */
    public Integer getMinHeight() {
        return minHeight;
    }
    /**
     * Устанавливает минимально допустимую высоту компонента в пикселях.
     * @param minHeight минимально допустимая высота или <code>null</code> если ограничения на минимальную высоту для компонента нет.
     */
    public void setMinHeight(final Integer minHeight) {
        this.minHeight = minHeight;
    }


    //
    // Группа свойств, определяющих положение компонента в родительском контйнере ...
    //

    /**
     * <strong>[AbsoluteLayout]: </strong> Возвращает координаты по которым будет располагаться верхний левый угол компонента.
     * Координаты могут быть указаны как относительно родительского контейнера так и относительно страницы в целом.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки допускающих абсолютное позициронирование компонент.</p>
     * @return координаты по которым будет располагаться верхний левый угол компонента или <code>null</code>.
     * @see <code> <code>Ext.layout.AbsoluteLayout</code>
     */
    public Point getPoint() {
        return point;
    }
    /**
     * <strong>[AbsoluteLayout]: </strong> Указывает координаты по которым будет располагаться верхний левый угол компонента.
     * Координаты могут быть указаны как относительно родительского контейнера так и относительно страницы в целом.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки допускающих абсолютное позициронирование компонент.</p>
     * @param point координаты по которым будет располагаться верхний левый угол компонента.
     * @see <code> <code>Ext.layout.AbsoluteLayout</code>
     */
    public void setPoint(final Point point) {
        this.point = point;
    }


    /**
     * <strong>[AnchorLayout]: </strong> Определяет ширину и высоту компонента относительно размеров контейнера в целом.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки допускающих указание размеров компонент относительно размеров контейенера в целом.</p>
     * @return  строка вида "30% 50%" или "-100 -50" или <code>null</code>.
     * @see <code> <code>Ext.layout.AnchorLayout</code>
     */
    public String getAnchor() {
        return anchor;
    }
    /**
     * <strong>[AnchorLayout]: </strong> Определяет ширину и высоту компонента относительно размеров контейнера в целом.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки допускающих указание размеров компонент относительно размеров контейенера в целом.</p>
     * @param anchor  строка вида "30% 50%" или "-100 -50".
     * @see <code> <code>Ext.layout.AnchorLayout</code>
     */
    public void setAnchor(final String anchor) {
        this.anchor = StringUtil.trim(anchor);
    }


    /**
     * <strong>[BoxLayout]: </strong> Определяет отступы данного компонента относительно границ контейнера.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.BoxLayout</code> или его потомков.</p>
     * @return  отступы относительно границ контейнера или <code>null</code>.
     * @see <code>Ext.layout.BoxLayout</code>
     */
    public Margins getMargins() {
        return margins;
    }
    /**
     * <strong>[BoxLayout]: </strong> Определяет отступы данного компонента относительно границ контейнера.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.BoxLayout</code> или его потомков.</p>
     * @param margins отступы относительно границ контейнера.
     * @see <code>Ext.layout.BoxLayout</code>
     */
    public void setMargins(final Margins margins) {
        this.margins = margins;
    }

    /**
     * <strong>[BoxLayout]: </strong>
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.BoxLayout</code> или его потомков.</p>
     * @see <code>Ext.layout.BoxLayout</code>
     * @return коэффициент исходя из значений которого расчитывается ширина или высота компонента.
     */
    public int getFlex() {
        return flex;
    }
    /**
     * <strong>[BoxLayout]: </strong>
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.BoxLayout</code> или его потомков.</p>
     * @see <code>Ext.layout.BoxLayout</code>
     * @param flex коэффициент исходя из значений которого расчитывается ширина или высота компонента.
     */
    public void setFlex(final int flex) {
        this.flex = flex;
    }


    /**
     * <strong>[FormLayout]: </strong> Возвращает класс CSS который требуется применить к области экрана, разделяющему данный и последующий компоненты на форме.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.FormLayout</code></p>
     * @return CSS класс дополнительно применяемый к области экрана между данным и последующим компонентами на форме.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public String getClearCls() {
        return clearCls;
    }
    /**
     * <strong>[FormLayout]: </strong> Задает класс CSS который требуется применить к области экрана, разделяющему данный и последующий компоненты на форме.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.FormLayout</code></p>
     * @param clearCls  CSS класс дополнительно применяемый к области экрана между данным и последующим компонентами на форме.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public void setClearCls(final String clearCls) {
        this.clearCls = StringUtil.trim(clearCls);
    }

    /**
     * <strong>[FormLayout]: </strong> Возвращает класс CSS который будет применен к области экрана включающей в себя и сам компонент и метку к нему.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.FormLayout</code></p>
     * @return класс CSS который будет применен к области экрана включающей в себя данный компонент и метку к нему.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public String getItemCls() {
        return itemCls;
    }
    /**
     * <strong>[FormLayout]: </strong> Задает класс CSS который требуется применить к области экрана включающей в себя и сам компонент и метку к нему.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.FormLayout</code></p>
     * @param itemCls класс CSS который будет применен к области экрана включающей в себя данный компонент и метку к нему.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public void setItemCls(final String itemCls) {
        this.itemCls = StringUtil.trim(itemCls);
    }

    /**
     * <strong>[FormLayout]: </strong> Возвращает стиль CSS который будет применен к метке для данного компонента.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.FormLayout</code></p>
     * @return стиль CSS который будет применен к метке для данного компонента.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public String getLabelStyle() {
        return labelStyle;
    }
    /**
     * <strong>[FormLayout]: </strong> Задает стиль CSS который будет применен к метке для данного компонента.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.FormLayout</code></p>
     * @param labelStyle стиль CSS который будет применен к метке для данного компонента.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public void setLabelStyle(final String labelStyle) {
        this.labelStyle = StringUtil.trim(labelStyle);
    }

    /**
     * <strong>[FormLayout]: </strong> Возвращает текст (допускается html) который будет помещен между меткой и компонентом.
     * Если данное свойство не указано то ExtJS будет использовать символ ':'.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.FormLayout</code></p>
     * @return текст, используемый в качестве разделителя между меткой и компонентом.
     *  Если свойство равно <code>null</code> то будет использоваться значение по умолчанию.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public String getLabelSeparator() {
        return labelSeparator;
    }
    /**
     * <strong>[FormLayout]: </strong> Задает текст (допускается html) который будет помещен между меткой и компонентом.
     * Если данное свойство не указано то ExtJS будет использовать символ ':'.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.FormLayout</code></p>
     * @param labelSeparator текст, используемый в качестве разделителя между меткой и компонентом.
     *  Если свойство равно <code>null</code> то будет использоваться значение по умолчанию.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public void setLabelSeparator(final String labelSeparator) {
        this.labelSeparator = labelSeparator;
    }

    /**
     * <strong>[FormLayout]: </strong> Возвращает текст (допускается html) используемый в качестве метки к данному компоненту на форме.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.FormLayout</code></p>
     * @return текст используемый в качестве метки к данному компоненту на форме.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public String getFieldLabel() {
        return fieldLabel;
    }
    /**
     * <strong>[FormLayout]: </strong> Задает текст (допускается html) используемый в качестве метки к данному компоненту на форме.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.FormLayout</code></p>
     * @param fieldLabel текст используемый в качестве метки к данному компоненту на форме.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public void setFieldLabel(final String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    /**
     * <strong>[FormLayout]: </strong> Данное свойство определяет будет ли отображаться для данного компонента метка на форме.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.FormLayout</code></p>
     * @return <code>true</code> если метка должна отсутствовать.
     *      По умолчанию возвращает <code>false</code>.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public boolean isHideLabel() {
        return hideLabel;
    }
    /**
     * <strong>[FormLayout]: </strong> Определяет будет ли отображаться для данного компонента метка на форме.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.FormLayout</code></p>
     * @param hideLabel <code>true</code> если метка должна отсутствовать.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public void setHideLabel(final boolean hideLabel) {
        this.hideLabel = hideLabel;
    }


    /**
     * <strong>[ColumnLayout]: </strong> Возвращает число в диапазоне 0..1 соответствующее ширине компонента в процентах (здесь 1.0 соответствует 100%).
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.ColumnLayout</code></p>
     * @return  Ширина компонента в процентах.
     * @see <code>Ext.layout.ColumnLayout</code>
     */
    public Double getColumnWidth() {
        return columnWidth;
    }
    /**
     * <strong>[ColumnLayout]: </strong> Устанавливает ширину компонента в процентах.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.ColumnLayout</code></p>
     * @param columnWidth  Число в диапазоне 0..1 соответствующее ширине компонента в процентах (здесь 1.0 соответствует 100%).
     * @see <code>Ext.layout.ColumnLayout</code>
     */
    public void setColumnWidth(final Double columnWidth) {
        if (columnWidth==null || columnWidth<0) {
            this.columnWidth = null;
        } else
        if (columnWidth>1) {
            this.columnWidth = 1.0;
        } else {
            this.columnWidth = columnWidth;
        }
    }


    /**
     * <strong>[TableLayout]: </strong> Возвращает количество строк которые будет занимать данный компонент в таблице.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.TableLayout</code></p>
     * @return  Количество строк которые будет занимать данный компонент в таблице.
     * @see <code>Ext.layout.TableLayout</code>
     */
    public Integer getRowspan() {
        return rowspan;
    }
    /**
     * <strong>[TableLayout]: </strong> Задает количество строк которые будет занимать данный компонент в таблице.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.TableLayout</code></p>
     * @param rowspan  Количество строк которые будет занимать данный компонент в таблице.
     * @see <code>Ext.layout.TableLayout</code>
     */
    public void setRowspan(final Integer rowspan) {
        this.rowspan = rowspan;
    }

    /**
     * <strong>[TableLayout]: </strong> Возвращает количество колонок которые будет занимать данный компонент в таблице.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.TableLayout</code></p>
     * @return  Количество колонок которые будет занимать данный компонент в таблице.
     * @see <code>Ext.layout.TableLayout</code>
     */
    public Integer getColspan() {
        return colspan;
    }
    /**
     * <strong>[TableLayout]: </strong> Задает количество колонок которые будет занимать данный компонент в таблице.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.TableLayout</code></p>
     * @param colspan  Количество колонок которые будет занимать данный компонент в таблице.
     * @see <code>Ext.layout.TableLayout</code>
     */
    public void setColspan(final Integer colspan) {
        this.colspan = colspan;
    }

    /**
     * <strong>[TableLayout]: </strong> Возвращает идентификатор который будет назначен ячейке таблицы в которой будет размещаться данный компонент.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.TableLayout</code></p>
     * @return Идентификатор ячейки таблицы которую будет занимать данный компонент.
     */
    public String getCellId() {
        return cellId;
    }
    /**
     * <strong>[TableLayout]: </strong> Задает идентификатор который будет назначен ячейке таблицы в которой будет размещаться данный компонент.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.TableLayout</code></p>
     * @param cellId Идентификатор ячейки таблицы которую будет занимать данный компонент.
     */
    public void setCellId(final String cellId) {
        this.cellId = StringUtil.trim(cellId);
    }

    /**
     * <strong>[TableLayout]: </strong> Возвращает CSS класс который будет назначен ячейке таблицы в которой будет размещен данный компонент.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.TableLayout</code></p>
     * @return CSS класс для ячейки таблицы в которой будет размещен данный компонент.
     */
    public String getCellCls() {
        return cellCls;
    }
    /**
     * <strong>[TableLayout]: </strong> Задает CSS класс который будет назначен ячейке таблицы в которой будет размещен данный компонент.
     * <p><strong>Внимание!</strong> Данное свойство используется только в контейнерах работающих под управлением менеджеров компоновки <code>Ext.layout.TableLayout</code></p>
     * @param cellCls CSS класс для ячейки таблицы в которой будет размещен данный компонент.
     */
    public void setCellCls(final String cellCls) {
        this.cellCls = StringUtil.trim(cellCls);
    }


    @Override
    protected void renderAttrs(final JsonWriter out) throws Exception {
        super.renderAttrs(out);
        if (autoWidth)
            out.writeProperty("autoWidth", true);
        if (autoHeight)
            out.writeProperty("autoHeight", true);
        if (width!=null)
            out.writeProperty("width", width);
        if (height!=null)
            out.writeProperty("height", height);
        if (maxWidth!=null)
            out.writeProperty("boxMaxWidth", maxWidth);
        if (minWidth!=null)
            out.writeProperty("boxMinWidth", minWidth);
        if (maxHeight!=null)
            out.writeProperty("boxMaxHeight", maxHeight);
        if (minHeight!=null)
            out.writeProperty("boxMinHeight", minHeight);
        if (autoScroll)
            out.writeProperty("autoScroll", true);

        if (point!=null) {
            if (point.isAbsolute()) {
                out.writeProperty("pageX", point.getX());
                out.writeProperty("pageY", point.getY());
            } else {
                out.writeProperty("x", point.getX());
                out.writeProperty("y", point.getY());
            }
        }
        if (anchor!=null)
            out.writeProperty("anchor", anchor);
        if (margins!=null)
            out.writeProperty("margins", margins.encode());
        if (flex!=0)
            out.writeProperty("flex", flex);
        if (clearCls!=null)
            out.writeProperty("clearCls", clearCls);
        if (itemCls!=null)
            out.writeProperty("itemCls", itemCls);
        if (labelStyle!=null)
            out.writeProperty("labelStyle", labelStyle);
        if (labelSeparator!=null)
            out.writeProperty("labelSeparator", labelSeparator);
        if (fieldLabel!=null)
            out.writeProperty("fieldLabel", fieldLabel);
        if (hideLabel)
            out.writeProperty("hideLabel", true);
        if (columnWidth!=null)
            out.writeProperty("columnWidth", columnWidth);
        if (rowspan!=null)
            out.writeProperty("rowspan", rowspan);
        if (colspan!=null)
            out.writeProperty("colspan", colspan);
        if (cellId!=null)
            out.writeProperty("cellId", cellId);
        if (cellCls!=null)
            out.writeProperty("cellCls", cellCls);
    }

    @Override
    protected Set<String> getSupportedEvents() {
        return EVENTS;
    }

}
