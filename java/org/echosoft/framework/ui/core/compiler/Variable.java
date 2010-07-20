package org.echosoft.framework.ui.core.compiler;

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
     * Может ли данная переменная получить другое значение (а не константа ли она?)
     */
    public final boolean reusable;
    /**
     * Используется ли данная переменная в коде или ее можно использовать для других целей.
     */
    public boolean used;


    public Variable(final Class cls, final String name) {
        this(cls, name, true);
    }
    public Variable(final Class cls, final String name, final boolean reusable) {
        this.cls = cls;
        this.name = name;
        this.reusable = reusable;
        this.used = false;
    }

    /**
     * Помечаем что переменная более не требуется для тех задач для которых она аллоцировалась ранее.
     */
    public void release() {
        used = false;
    }

}
