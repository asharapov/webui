package org.echosoft.framework.ui.extjs.model;

import java.io.Serializable;

import org.echosoft.common.json.annotate.JsonWriteNulls;

/**
 * Описывает размеры некоторой прямоугольной области.
 * 
 * @author Anton Sharapov
 */
@JsonWriteNulls(false)
public class Size implements Serializable, Cloneable {

    private Integer width;
    private Integer height;

    public Size() {
        this(null, null);
    }

    public Size(final Integer size) {
        this(size, size);
    }

    public Size(final Integer width, final Integer height) {
        this.width = width;
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }
    public void setWidth(final Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }
    public void setHeight(final Integer height) {
        this.height = height;
    }

    public boolean isEmpty() {
        return width==null && height==null;
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
        return (width!=null && other.width!=null ? width.intValue()==other.width.intValue() : width==null && other.width==null) &&
               (height!=null && other.height!=null ? height.intValue()==other.height.intValue() : height==null && other.height==null);
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
