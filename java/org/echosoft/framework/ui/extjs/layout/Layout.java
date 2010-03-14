package org.echosoft.framework.ui.extjs.layout;

import java.io.Serializable;

import org.echosoft.common.json.annotate.JsonWriteNulls;

/**
 * Данный класс описывает настройки менеджера компоновки в контейнерах компонент.<br/>
 * Если какое-либо свойство бина имеет значение <code>null</code> то оно не будет включено в конфигурацию ExtJS компонент и соответственно
 * для него будет использоваться значение по умолчанию.<br/>
 * Детальная информация о положении отдельного компонента в этом контейнере содержится в классах, унаследованных от {@link LayoutItem}.
 * @see org.echosoft.framework.ui.extjs.AbstractContainerComponent
 * @see LayoutItem
 * @author Anton Sharapov
 */
@JsonWriteNulls(false)
public abstract class Layout implements Serializable, Cloneable {

    private String extraCls;
    private boolean renderHidden;

    /**
     * Создает новый экземпляр объекта с дополнительными характеристиками компонент расположенных в контейнерах данного типа.
     * @return  новый экземпляр объекта потомка {@link LayoutItem} чьи свойства отражают характеристики расположения компонент в контейнерах данного типа.
     */
    public abstract LayoutItem makeItem();

    /**
     * Создает новый экземпляр объекта с дополнительными характеристиками компонент расположенных в контейнерах данного типа.
     * Созданный экземпляр по возможности инициализируется значениями из объекта переданного в аргументе метода.
     * @param item  используемый шаблон. Может быть <code>null</code>.
     * @return  новый экземпляр объекта потомка {@link LayoutItem} чьи свойства отражают характеристики расположения компонент в контейнерах данного типа.
     */
    public abstract LayoutItem makeItem(final LayoutItem item);

    /**
     * Возвращает кодовое обозначение данного менеджера компоновки компонент.
     * @return  строка с обозначением типа менеджера компоновки компонент.
     */
    public abstract String getLayout();

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


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
