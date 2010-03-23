package org.echosoft.framework.ui.extjs.model;

import java.io.Serializable;

/**
 * Описывает координаты элементов страницы относительно родительского контейнера или относительно страницы в целом.
 * @author Anton Sharapov
 */
public class Point implements Serializable, Cloneable {

    private int x;              // координата по оси X
    private int y;              // координата по оси Y
    private boolean absolute;   // в какой системе координат задано положение: в координатах страницы (true) или в координатах родительского контейнера (false)

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, boolean absolute) {
        this.x = x;
        this.y = y;
        this.absolute = absolute;
    }

    /**
     * Возвращает значение координаты по оси X.
     * @return значение координаты по оси X.
     */
    public int getX() {
        return x;
    }
    /**
     * Устанавливает значение координаты по оси X.
     * @param x новое значение координаты по оси X.
     */
    public void setX(final int x) {
        this.x = x;
    }

    /**
     * Возвращает значение координаты по оси Y.
     * @return значение координаты по оси Y.
     */
    public int getY() {
        return y;
    }
    /**
     * Устанавливает значение координаты по оси Y.
     * @param y новое значение координаты по оси Y.
     */
    public void setY(final int y) {
        this.y = y;
    }

    /**
     * Определяет используемую в объекте систему координат.
     * @return <code>true</code> если координаты заданы относительно страницы в целом,
     *         <code>false</code> если координаты заданы относительно родительского контейнера.
     */
    public boolean isAbsolute() {
        return absolute;
    }
    /**
     * Определяет используемую в объекте систему координат.
     * @param absolute <code>true</code> если координаты заданы относительно страницы в целом,
     *                 <code>false</code> если координаты заданы относительно родительского контейнера.
     */
    public void setAbsolute(final boolean absolute) {
        this.absolute = absolute;
    }

    @Override
    public int hashCode() {
        return x + y;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj==null || !getClass().equals(obj.getClass()))
            return false;
        final Point other = (Point)obj;
        return x==other.x && y==other.y && absolute==other.absolute;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "[Point{x:"+x+", y:"+y+", "+(absolute ? "abs":"rel")+"}]";
    }
}
