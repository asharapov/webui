package org.echosoft.framework.ui.extjs.layout;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.UIComponent;

/**
 * <p>Данный класс описывает модель менеджера компоновки компонент <code>Ext.layout.ContainerLayout</code>.</p>
 * <p>Если какое-либо свойство данного класса имеет значение отличное от значения по умолчанию то оно будет
 * явно включено в начальную конфигурацию ExtJS компонента.</p>
 * @see org.echosoft.framework.ui.extjs.AbstractContainerComponent
 * @author Anton Sharapov
 */
public abstract class Layout implements Serializable {

    private String extraCls;            // имя CSS класса которое будет дополнительно назначено контейнеру.
    private boolean renderHidden;       // все компоненты кроме невидимого делает как невидимые
    private boolean skipLayout;         // если мы должны пропустить рендеринг свойства "layout". Хак применяемый в некоторых компонентах с намертво назначенным компоновщиком.

    /**
     * Опциональное имя CSS класса которое быть назначено контейнеру.
     * @return имя CSS класса которое должно быть назначено контейнеру компонент или <code>null</code> в случае использования значений по умолчанию.
     */
    public String getExtraCls() {
        return extraCls;
    }
    /**
     * Устанавливает имя CSS класса которое должно быть назначено контейнеру компонент.
     * @param extraCls имя CSS класса или <code>null</code> если требуется использовать значение по умолчанию.
     */
    public void setExtraCls(final String extraCls) {
        this.extraCls = extraCls;
    }

    /**
     * Если свойство равно <code>true</code> то все компоненты в контейнере кроме активного будут рендериться по умолчанию невидимыми.
     * @return <code>true</code> если все компоненты в контейнере кроме активного будут рендериться по умолчанию невидимыми.
     *      Значение свойства по умолчанию: <code>false</code>.
     */
    public boolean isRenderHidden() {
        return renderHidden;
    }
    /**
     * Устанавливает флаг, указывающий что все компоненты в контейнеры должны отрисовываться в невидимом режиме. По умолчанию этот режим выключен.
     * @param renderHidden <code>true</code> если все компоненты в контейнере кроме активного должны будут рендериться по умолчанию невидимыми.
     */
    public void setRenderHidden(final boolean renderHidden) {
        this.renderHidden = renderHidden;
    }

    /**
     * Данное свойство представляет собой способ обойти багу в ExtJS которая проявляется для того набора контейнерных компонент
     * в которых тип используемого компоновщика жестко определен на стадии дизайна. Типичным примером такого является компонент
     * <code>TabPanel</code> жестко сцепленный с компоновщиком <code>CardLayout</code>. В подобных случаях указание в конфиге
     * компонента свойства <code>layout</code> как минимум избыточно, а в ряде случаев еще и приводит к явным ошибкам
     * (<code>TabPanel</code>, версия 3.2.0 ExtJS).<br/>
     * Установка данного свойства приводит к тому что свойство <code>layout</code> не будет включаться в конфигурацию компонента.
     * @return <code>true</code> если свойство <code>layout</code> не должно сериализоваться в выходной JSON поток.
     */
    public boolean isSkipLayout() {
        return skipLayout;
    }

    /**
     * @param skipLayout <code>true</code> если свойство <code>layout</code> не должно сериализоваться в выходной JSON поток.
     * @see #isSkipLayout() 
     */
    public void setSkipLayout(final boolean skipLayout) {
        this.skipLayout = skipLayout;
    }

    /**
     * Возвращает количество компонент управляемых данным менеджером компоновки.
     * @return количество компонент управляемых данным менеджером компоновки.
     */
    public abstract int getItemsCount();

    /**
     * Возвращает итератор по всем компонентам управляемым данным менеджером компоновки.
     * @return итератор по всем компонентам управляемым данным менеджером компоновки.
     */
    public abstract Iterable<UIComponent> getItems();

    /**
     * <p>Регистрирует новый компонент в менеджере компоновки.</p>
     * <p>Данный метод реализован во всех типах менеджеров компоновки но в ряде случаев его реализация имеет свои особенности,
     * так как отдельные компоновщики налагают различные требования к размещаемым в них компонентам. В таких случаях
     * можно только порекомендовать использовать специальные методы предоставляемые этими компоновщиками.</p>
     * @param item  компонент который требуется добавить в контейнер, управляемый данным менеджером компоновки.
     * @return  добавленный в контейнер компонент. В абсолютном большинстве случаев это тот же экземпляр что был передан в аргументе данного метода.
     * @throws IllegalArgumentException  данное исключение может инициироваться отдельными компоновщиками в случае если
     *      переданный им компонент не удовлетворяет каким-либо требованиям специфичным для данного компоновщика.  
     * @throws IllegalStateException  данное исключение может инициироваться отдельными компоновщиками в случае если
     *      переданный им компонент не удовлетворяет каким-либо требованиям специфичным для данного компоновщика.
     */
    public abstract <T extends UIComponent> T append(final T item);

    /**
     * Сериализует свойства данного менеджера компоновки компонент в JSON формат согласно нотации принятой в ExtJS.<br/>
     * Сериализуются как минимум следующие свойства:
     * <ol>
     *  <li> <code>layout</code>
     *  <li> <code>layoutConfig</code>
     *  <li> <code>items</code>.
     * </ol>
     * @param out  выходной поток.
     * @throws Exception  в случае каких-либо ошибок в процессе сериализации или отправки данных.
     */
    public void serialize(final JsonWriter out) throws Exception {
        if (!skipLayout)
            out.writeProperty("layout", getLayout());
        if (isCustomized()) {
            out.writeComplexProperty("layoutConfig");
            out.beginObject();
            serializeConfigAttrs(out);
            out.endObject();
        }
        final int cnt = getItemsCount();
        if (cnt==1) {
            final UIComponent item = getItems().iterator().next();
            out.writeComplexProperty("items");
            item.invoke( out );
        } else
        if (cnt>1) {
            out.writeComplexProperty("items");
            out.beginArray();
            for (UIComponent item : getItems()) {
                item.invoke( out );
            }
            out.endArray();
        }
    }

    /**
     * Возвращает кодовое обозначение данного менеджера компоновки компонент.
     * @return  строка с обозначением типа менеджера компоновки компонент.
     */
    protected abstract String getLayout();

    /**
     * <p>Указывает было ли переопределено хотя бы одно из свойств менеджера. Влияет на способ сериализации свойств данного объекта в JSON формат.</p>
     * <p>Должен быть переопределен в классах-потомках если в них добавлена поддержка новых конфигурационных атрибутов.</p>
     * @return  <code>true</code> если было переопределено значение хотя бы одного из свойств менеджера.
     * @see Layout#serialize(org.echosoft.common.json.JsonWriter) 
     */
    protected boolean isCustomized() {
        return extraCls!=null || renderHidden;
    }

    /**
     * <p>Сериализует конфигурационные аттрибуты данного менеджера компоновки.</p>
     * <p>Должен быть переопределен в классах-потомках если в них добавлена поддержка новых конфигурационных атрибутов.</p>
     * @param out  выходной поток.
     * @throws IOException  в случае каких-либо ошибок связанных с помещением данных в поток.
     * @throws InvocationTargetException  в случае ошибок в процессе сериализации данных в JSON формат.
     * @throws IllegalAccessException  в случае ошибок в процессе сериализации данных в JSON формат.
     * @see Layout#serialize(org.echosoft.common.json.JsonWriter)
     */
    protected void serializeConfigAttrs(final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException {
        out.writeProperty("type", getLayout());
        if (extraCls!=null)
            out.writeProperty("extraCls", extraCls);
        if (renderHidden)
            out.writeProperty("renderHidden", true);
    }

}
