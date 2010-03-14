package org.echosoft.framework.ui.extjs.layout;

import java.io.Serializable;

import org.echosoft.common.utils.StringUtil;

/**
 * Данный бин представляет собой набор свойств описывающих положение компонента в контейнере.<br/>
 * Если какое-либо свойство бина имеет значение <code>null</code> то оно не будет включено в конфигурацию ExtJS компонент и соответственно
 * для него будет использоваться значение по умолчанию.
 * @see Layout
 * @author Anton Sharapov
 */
public abstract class LayoutItem implements Serializable, Cloneable {

    private String ctCls;           // CSS класс который будет дополнительно добавлен к контейнеру в котором данный компонент расположен.
    private String itemId;          // идентификатор компонента в контейнере. Должен быть уникальным в рамках всех компонентов лежащих в данном контейнере.
    private boolean hideParent;     // признак, определяющий поведение контейнера в момент когда входящий в него компонент становится невидимым.

    public LayoutItem() {
    }

    public LayoutItem(final LayoutItem item) {
        if (item!=null) {
            this.ctCls = item.ctCls;
            this.itemId = item.itemId;
            this.hideParent = item.hideParent;
        }
    }

    public String getCtCssClass() {
        return ctCls;
    }
    public void setCtCssClass(final String ctCls) {
        this.ctCls = ctCls;
    }

    /**
     * Возвращает альтернативный идентификатор компонента в контейнере, который позволяет однозначно идентифицировать компонент среди всех прочих дочерних компонент лежащих
     * в этом же контейнере компонент.
     * @return  альтернативный идентификатор дочернего компонента в контейнере. Обязан быть уникальным в рамках всех прочих дочерних компонент этого же контейнера. Может быть <code>null</code>.
     */
    public String getItemId() {
        return itemId;
    }
    /**
     * Устанавливает альтернативный идентификатор компонента в контейнере, который позволяет однозначно идентифицировать компонент среди всех прочих дочерних компонент лежащих
     * в этом же контейнере компонент.
     * @param itemId  альтернативный идентификатор дочернего компонента в контейнере. Обязан быть уникальным в рамках всех прочих дочерних компонент этого же контейнера. Может быть <code>null</code>.
     */
    public void setItemId(final String itemId) {
        this.itemId = StringUtil.trim(itemId);
    }

    /**
     * Определяет поведение контейнера в котором находится данный компонент в момент, когда последний становится видимым/невидимым.<br/>
     * Если данное свойство равно <code>true</code> то переключение компонента в невидимый режим автоматически переключает в невидимый режим
     * и весь контейнер в котором этот компонент расположен.
     * @return  <code>true</code> в случае когда при скрытии компонента должен быть скрыт и весь контейнер в котором этот компонент находится. По умолчанию возвращает <code>false</code>.  
     */
    public boolean isHideParent() {
        return hideParent;
    }
    /**
     * Определяет поведение контейнера в котором находится данный компонент в момент, когда последний становится видимым/невидимым.<br/>
     * Если данное свойство равно <code>true</code> то переключение компонента в невидимый режим автоматически переключает в невидимый режим
     * и весь контейнер в котором этот компонент расположен.
     * @param hideParent  <code>true</code> в случае когда требуется при скрытии компонента автоматически скрывать и весь контейнер в котором этот компонент находится.
     */
    public void setHideParent(final boolean hideParent) {
        this.hideParent = hideParent;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
