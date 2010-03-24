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
    private boolean renderHidden;

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
     * Определяет следует ли компоновщику отрисовывать все компоненты в контейнеры как невидимые (по умолчанию все компоненты отрисовываются как видимые).<br/>
     * Значение свойства по умолчанию: <code>false</code>.
     * @return признак как отображаются компоненты в контейнере или <code>null</code> если используется значение по умолчанию.
     */
    public boolean isRenderHidden() {
        return renderHidden;
    }
    /**
     * Устанавливает флаг, указывающий что все компоненты в контейнеры должны отрисовываться в невидимом режиме. По умолчанию этот режим выключен.
     * Значение свойства по умолчанию: <code>false</code>.
     * @param renderHidden  новое значение флага.
     */
    public void setRenderHidden(final boolean renderHidden) {
        this.renderHidden = renderHidden;
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
