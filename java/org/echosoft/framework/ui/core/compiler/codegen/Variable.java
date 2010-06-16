package org.echosoft.framework.ui.core.compiler.codegen;

/**
 * Описывает информацию по отдельной переменной в методе транслируемого класса.
 * @author Anton Sharapov
 */
public class Variable {

    /**
     * Контекст метода в котором объявлена данная переменная.
     */
    public final MethodContext mc;
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
     * Может ли данная переменная получить другое значение (а не константа ли она?)
     */
    public final boolean reusable;
    /**
     * Определяет возможно ли уже повторное использование данной переменной или она еще используется в коде.
     */
    public Integer useLevel;
    /**
     * Была ли данная переменная уже объявлена в коде (при повторном использовании всегда true).
     */
    public boolean alreadyDeclared;


    public Variable(final MethodContext mc, final Class cls, final String name) {
        this(mc, cls, name, true);
    }
    public Variable(final MethodContext mc, final Class cls, final String name, final boolean reusable) {
        this.mc = mc;
        this.cls = cls;
        this.name = name;
        this.shouldBeQualified = !mc.tc.ensureClassImported(cls);
        this.declareLevel = mc.getLevel();
        this.reusable = reusable;
        this.alreadyDeclared = false;
    }

    /**
     * Помечаем что переменная более не требуется для тех задач для которых она аллоцировалась ранее.
     */
    public void release() {
        useLevel = null;
    }


    public void writeDeclaration() {
        if (!alreadyDeclared) {
            if (!reusable)
                mc.content.write("final ");
            mc.content.write( shouldBeQualified ? cls.getCanonicalName() : cls.getSimpleName() );
            mc.content.write(' ');
            alreadyDeclared = true;
        }
        mc.content.write(name);
        mc.content.write(' ');
    }

    public void writeClass() {
        mc.content.write( shouldBeQualified ? cls.getCanonicalName() : cls.getSimpleName() );
    }
}
