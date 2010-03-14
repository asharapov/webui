package org.echosoft.framework.ui.core;

import java.io.Serializable;

/**
 * Дескриптор состояния (формы).
 * 
 * @author Anton Sharapov
 */
public class ViewStateDescriptor implements Serializable, Comparable<ViewStateDescriptor> {

    private final String pkg;
    private final String view;
    private final int rank;

    public ViewStateDescriptor(final String pkg, final String view, final int rank) {
        this.pkg = pkg!=null ? pkg : "";
        this.view = view!=null ? view : "";
        this.rank = rank;
    }

    /**
     * Идентифицирует группу, к которой относится данная страница.
     * @return  группа страниц. Никогда не возврашает <code>null</code>, хотя допускается пустая строка.
     */
    public String getPackage() {
        return pkg;
    }

    /**
     * Определяет идентификатор страницы в группе.
     * @return  идентификатор страницы. Никогда не возврашает <code>null</code>, хотя допускается пустая строка.
     */
    public String getView() {
        return view;
    }

    /**
     * Дополнительный параметр, определяющий своего рода "ранг" страницы в группе.
     * Используется  в некоторых алгоритма удаления ненужных более состояний из менеджера состояний.
     * @return  весовой коэффицинет, определяющий ранг страницы в группе.
     */
    public int getRank() {
        return rank;
    }

    @Override
    public int hashCode() {
        return view.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj==null || !getClass().equals(obj.getClass()))
            return false;
        final ViewStateDescriptor other = (ViewStateDescriptor)obj;
        return pkg.equals(other.pkg) &&
               view.equals(other.view) &&
               rank==other.rank;
    }

    @Override
    public int compareTo(final ViewStateDescriptor other) {
        int result = pkg.compareTo(other.pkg);
        if (result==0) {
            result = rank<other.rank ? -1 : rank>other.rank ? 1 : 0;
            if (result==0) {
                result = view.compareTo(other.view);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "[ViewState{package:"+pkg+", view:"+view+", rank:"+rank+"}]";
    }
}
