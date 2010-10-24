package org.echosoft.framework.ui.core.compiler.ast;

import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.compiler.ast.type.Type;

/**
 * Внутренняя структура данных, описывающая информацию по отдельной переменной в методе транслируемого класса.
 * @author Anton Sharapov
 */
public class Variable {

    /**
     * Имя класса переменной.
     */
    private final Type type;
    /**
     * Имя переменной в методе.
     */
    private final String name;
    /**
     * Определяет может ли быть данная переменная повторно использована при обработке другого тега.
     */
    private final boolean reusable;
    /**
     * Используется ли в настоящий момент данная переменная в коде или ее уже можно повторно использовать для других целей.
     */
    private boolean used;


    public Variable(final Type type, final String name) {
        this(type, name, true);
    }
    public Variable(final Type type, final String name, final boolean reusable) {
        if (type==null || !StringUtil.isJavaIdentifier(name))
            throw new IllegalArgumentException("Invalid class or name of variable");
        this.type = type;
        this.name = name;
        this.reusable = reusable;
        this.used = false;
    }

    /**
     * @return  Тип данной переменной.
     */
    public Type getType() {
        return type;
    }

    /**
     * @return имя переменной
     */
    public String getName() {
        return name;
    }

    /**
     * @return  <code>true</code>  если данная переменная может быть повторно использована в коде,
     *      т.е. этой переменной может быть присвоено какое-либо другое значение.
     */
    public boolean isReusable() {
        return reusable;
    }

    /**
     * Используется ли в настоящий момент данная переменная в коде или ее уже можно повторно использовать для других целей.
     * @return <code>true</code> если переменная в настоящий момент используется.
     */
    public boolean isUsed() {
        return used;
    }
    public void setUsed(final boolean used) {
        this.used = used;
    }

}
