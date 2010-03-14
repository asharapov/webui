package org.echosoft.framework.ui.extjs.model;

import java.io.Serializable;

import org.echosoft.common.io.FastStringTokenizer;

/**
 * Описывает величину отступов по краям относительно компонента унаследованного от {@link org.echosoft.framework.ui.extjs.AbstractBoxComponent}.
 * @author Anton Sharapov
 */
public class Margins implements Serializable, Cloneable {

    /**
     * Формирует объект типа {@link Margins} из строк вида:
     * <ul>
     *  <li> "1"  => трансформируется в new Margins(1);
     *  <li> "1 2"  => трансформируется в new Margins(1, 2);
     *  <li> "1 2 3"  => трансформируется в new Margins(1, 2, 2, 3);
     *  <li> "1 2 3 4"  => трансформируется в new Margins(1, 2, 3, 4);
     * </ul>
     * @param str  строка, которую требуется разобрать.
     * @return возвращает соответствующий экземпляр класса {@link Margins} или <code>null</code> для пустой строки.
     * @throws NumberFormatException  в случае некорректной строки.
     */
    public static Margins decode(final String str) {
        if (str==null || str.length()==0)
            return null;
        final int[] d = new int[4];
        int pos = 0;
        final FastStringTokenizer tokeniser = new FastStringTokenizer(str, ' ', (char)0);
        while (tokeniser.hasMoreTokens() && pos<4) {
            d[pos++] = Integer.parseInt( tokeniser.nextToken() );
        }
        switch (d.length) {
            case 0 : return null;
            case 1 : return new Margins(d[0]);
            case 2 : return new Margins(d[0], d[1]);
            case 3 : return new Margins(d[0], d[1], d[1], d[2]);
            default : return new Margins(d[0], d[1], d[2], d[3]);
        }
    }

    private int top;
    private int right;
    private int bottom;
    private int left;

    public Margins() {
        super();
    }

    public Margins(final int margin) {
        this(margin, margin, margin, margin);
    }

    public Margins(final int height, final int width) {
        this(height, width, height, width);
    }

    public Margins(final int top, final int right, final int bottom, final int left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public Margins(final Margins other) {
        this.top = other.top;
        this.right = other.right;
        this.bottom = other.bottom;
        this.left = other.left;
    }

    /**
     * Возвращает величину отступа сверху.
     * @return  отступ сверху (в пикселах).
     */
    public int getTop() {
        return top;
    }
    public void setTop(int top) {
        this.top = top;
    }

    /**
     * Возвращает величину отступа справа.
     * @return  отступ справа (в пикселах).
     */
    public int getRight() {
        return right;
    }
    public void setRight(int right) {
        this.right = right;
    }

    /**
     * Возвращает величину отступа снизу.
     * @return  отступ снизу (в пикселах).
     */
    public int getBottom() {
        return bottom;
    }
    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    /**
     * Возвращает величину отступа слева.
     * @return  отступ слева (в пикселах).
     */
    public int getLeft() {
        return left;
    }
    public void setLeft(int left) {
        this.left = left;
    }

    /**
     * Возвращает <code>true</code> если величины отступов со всех сторон равны <code>0</code>.
     * @return  <code>true</code> если величины отступов со всех сторон равны <code>0</code>.
     */
    public boolean isEmpty() {
        return top==0 && right==0 && bottom==0 && left==0;
    }

    /**
     * Преобразует величины отступов в строку в соответствии с правилами принятыми в CSS.
     * @return  строковое представление отступов.
     */
    public String encode() {
        if (top==bottom && right==left) {
            return top==right
                    ? Integer.toString(top,10)
                    : Integer.toString(top,10)+' '+Integer.toString(right,10);
        } else {
            final StringBuilder buf = new StringBuilder();
            buf.append(top).append(' ').append(right).append(' ').append(bottom).append(' ').append(left);
            return buf.toString();
        }
    }

    @Override
    public int hashCode() {
        return right + top + left + bottom;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj==null || !getClass().equals(obj.getClass()))
            return false;
        final Margins other = (Margins)obj;
        return top==other.top && right==other.right && bottom==other.bottom && left==other.left;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "[Margins{top:"+top+", right:"+right+", bottom:"+bottom+", left:"+left+"}]";
    }
}