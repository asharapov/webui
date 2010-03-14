package org.echosoft.framework.ui.extjs.layout;

/**
 * Данный бин описывает характеристики компонент специфичных при работе в контейнере вида <code>Ext.layout.AbsoluteLayout</code>.
 * @author Anton Sharapov
 */
public class AbsoluteLayoutItem extends AnchorLayoutItem {

    private int x;              // координата левого верхнего угла компонента по оси X
    private int y;              // координата левого верхнего угла компонента по оси Y
    private boolean related;    // используются координаты относительно левого верхнего угла родительского контейнера (true) или абсолютные координаты на странице.

    public AbsoluteLayoutItem() {
        super();
        related = true;
    }

    public AbsoluteLayoutItem(final LayoutItem item) {
        super(item);
        if (item instanceof AbsoluteLayoutItem) {
            this.x = ((AbsoluteLayoutItem)item).x;
            this.y = ((AbsoluteLayoutItem)item).y;
            this.related = ((AbsoluteLayoutItem)item).related;
        } else {
            related = true;
        }
    }


    /**
     * Возвращает X координату левого верхнего угла компонента.
     * @return X координата левого верхнего угла компонента.
     */
    public int getX() {
        return x;
    }
    /**
     * Указывает X координату левого верхнего угла компонента.
     * @param x X координата левого верхнего угла компонента.
     */
    public void setX(final int x) {
        this.x = x;
    }

    /**
     * Возвращает Y координату левого верхнего угла компонента.
     * @return Y координата левого верхнего угла компонента.
     */
    public int getY() {
        return y;
    }
    /**
     * Указывает Y координату левого верхнего угла компонента. 
     * @param y Y координата левого верхнего угла компонента.
     */
    public void setY(final int y) {
        this.y = y;
    }

    /**
     * Используются координаты относительно левого верхнего угла контейнера (related=true) или абсолютные координаты страницы.
     * Значение свойства по умолчанию: <code>true</code>.
     * @return используемая система координат: относительная (true) или абсолютная (false).
     */
    public boolean isRelated() {
        return related;
    }
    /**
     * Используются координаты относительно левого верхнего угла контейнера (related=true) или абсолютные координаты страницы.
     * Значение свойства по умолчанию: <code>true</code>.
     * @param related <code>true</code> если используется относительная система координат и <code>false</code> если используется абсолютная система координат.
     */
    public void setRelated(final boolean related) {
        this.related = related;
    }
}