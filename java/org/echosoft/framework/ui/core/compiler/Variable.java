package org.echosoft.framework.ui.core.compiler;

import org.echosoft.common.utils.StringUtil;

/**
 * Внутренняя структура данных, описывающая информацию по отдельной переменной в методе транслируемого класса.
 * @author Anton Sharapov
 */
public class Variable {

    /**
     * Имя класса переменной.
     */
    public final Class cls;
    /**
     * Имя переменной в методе.
     */
    public final String name;
    /**
     * Может ли переменная изменять свое значение.
     */
    public final boolean modifiable;
    /**
     * Имеет смысл только при modifiable=true.
     * Определяет может ли быть данная переменная повторно использована при обработке другого тега.
     */
    public final boolean reusable;
    /**
     * Используется ли данная переменная в коде или ее можно использовать для других целей.
     */
    public boolean used;


    public Variable(final Class cls, final String name) {
        this(cls, name, true, true);
    }
    public Variable(final Class cls, final String name, final boolean modifiable, final boolean reusable) {
        if (cls==null || !StringUtil.isJavaIdentifier(name))
            throw new IllegalArgumentException("Invalid class or name of variable");
        this.cls = cls;
        this.name = name;
        this.modifiable = modifiable;
        this.reusable = modifiable && reusable;
        this.used = false;
    }

    /**
     * Помечаем что переменная используется в коде. 
     */
    public void markAsUsed() {
        used = true;
    }

    /**
     * Помечаем что переменная более не требуется для тех задач для которых она аллоцировалась ранее.
     */
    public void release() {
        used = false;
    }

}
