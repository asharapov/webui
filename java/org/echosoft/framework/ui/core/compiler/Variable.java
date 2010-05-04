package org.echosoft.framework.ui.core.compiler;

/**
 * Описывает информацию по отдельной переменной в методе транслируемого класса.
 * @author Anton Sharapov
 */
public class Variable {

    /**
     * Имя переменной в методе.
     */
    public final String name;
    /**
     * Имя класса переменной.
     */
    public final Class cls;
    /**
     * Требуется ли указание полного имени класса (вместе с пакетом) при объявлении переменной.
     * Ситуация возникает когда в числе импортируемых пэкеджей уже есть такой что содержит класс с подобным именем.
     */
    public final boolean shouldBeQualified;
    /**
     * Указывает область определения данной переменной в методе.
     */
    public final int declareLevel;
    /**
     * Определяет возможно ли уже повторное использование данной переменной или она еще используется в коде.
     */
    public Integer useLevel;

    public Variable(final String name, final Class cls, final boolean shouldBeQualified, final int declareLevel) {
        this.name = name;
        this.cls = cls;
        this.shouldBeQualified = shouldBeQualified;
        this.declareLevel = declareLevel;
    }
}
