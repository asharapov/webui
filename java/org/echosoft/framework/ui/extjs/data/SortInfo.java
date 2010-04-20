package org.echosoft.framework.ui.extjs.data;

import java.io.Serializable;

/**
 * Задает направление сортировки по определенному полю из источника данных.
 * @author Anton Sharapov
 */
public class SortInfo implements Serializable {

    private final String field;             // логическое имя поля.
    private final SortDirection direction;  // направление сортировки.

    public SortInfo(final String field, final SortDirection direction) {
        if (field==null)
            throw new IllegalArgumentException("Field must be specified");
        this.field = field;
        this.direction = direction!=null ? direction : SortDirection.ASC;
    }

    /**
     * Возвращает имя поля по которому идет сортировка.
     * @return имя поля по которому идет сортировка.
     */
    public String getField() {
        return field;
    }

    /**
     * Возвращает направление сортировки.
     * @return направление сортировки.
     */
    public SortDirection getDirection() {
        return direction;
    }

    @Override
    public int hashCode() {
        return field.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj==null || !getClass().equals(obj.getClass()))
            return false;
        final SortInfo other = (SortInfo)obj;
        return field.equals(other.field) && direction.equals(other.direction);
    }

    @Override
    public String toString() {
        return "[SortInfo{field:"+field+", dir:"+direction+"}]";
    }
}
