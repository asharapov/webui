package org.echosoft.framework.ui.extjs;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;

/**
 * Абстрактный класс от которого наследуются все компоненты ExtJS имеющие определенные размеры как по ширине так и по высоте.
 * @author Anton Sharapov
 */
public abstract class AbstractBoxComponent extends AbstractExtJSComponent {

    public static final Set<String> EVENTS = StringUtil.asUnmodifiableSet(AbstractExtJSComponent.EVENTS, "move", "resize");

    private boolean autoScroll; // дает возможность скроллировать содержимое компонента если оно не влезает в установленные рамки.
    private Integer width;      // ширина компонента (в пикселях). Если null то ширина будет полностью контролироваться браузером (autoWidth=true).
    private Integer height;     // высота компонента (в пикселях). Если null то высота будет полностью контролироваться браузером (autoHeight=true).
    private Integer maxWidth;   // максимально допустимая ширина (в пикселях).
    private Integer minWidth;   // минимально допустимая ширина (в пикселях).
    private Integer maxHeight;  // максимально допустимая высота (в пикселях).
    private Integer minHeight;  // минимально допустимая высота (в пикселях).

    public AbstractBoxComponent(final ComponentContext ctx) {
        super(ctx);
    }

    /**
     * Возвращает перечень всех поддерживаемых компонентом событий.<br/>
     * Если компонент, являющийся наследником данного класса поддерживает иной перечень событий то он должен переопределить данный метод.
     * @return  неизменяемый перечень событий, поддерживаемых данным компонентом. Метод никогда не возвращает <code>null</code>.
     */
    @Override
    protected Set<String> getSupportedEvents() {
        return EVENTS;
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



    @Override
    protected void renderAttrs(final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException {
        super.renderAttrs(out);
        if (width==null) {
            out.writeProperty("autoWidth", true);
        } else {
            out.writeProperty("width", width);
        }
        if (height==null) {
            out.writeProperty("autoHeight", true);
        } else {
            out.writeProperty("height", height);
        }
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
    }

}
