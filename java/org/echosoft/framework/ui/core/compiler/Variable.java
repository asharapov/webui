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
    /**
     * Была ли данная переменная уже объявлена в коде (при повторном использовании всегда true).
     */
    public boolean alreadyDeclared;

    public Variable(final MethodContext mc, final Class cls, final String name) {
        this.cls = cls;
        this.name = name;
        this.shouldBeQualified = !mc.tc.ensureClassImported(cls);
        this.declareLevel = mc.getLevel();
        this.alreadyDeclared = false;
    }

}
