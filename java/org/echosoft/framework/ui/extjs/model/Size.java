package org.echosoft.framework.ui.extjs.model;

import java.io.Serializable;

/**
 * Описывает размеры некоторой прямоугольной области.
 * 
 * @author Anton Sharapov
 */
public class Size implements Serializable, Cloneable {

    private int width;
    private int height;

    public Size(final int size) {
        this(size, size);
    }

    public Size(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }
    public void setWidth(final int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(final int height) {
        this.height = height;
    }


    @Override
    public int hashCode() {
        return width + height;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj==null || !getClass().equals(obj.getClass()))
            return false;
        final Size other = (Size)obj;
        return width==other.width && height==other.height;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "[Size{width:"+width+", height:"+height+"}]";
    }
}
