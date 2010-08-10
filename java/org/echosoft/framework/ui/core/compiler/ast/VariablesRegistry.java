package org.echosoft.framework.ui.core.compiler.ast;

import org.echosoft.framework.ui.core.compiler.ast.type.RefType;

/**
 * Внутри любого блока кода могут быть объявлены переменные с соответствующей областью видимости.
 * Данный интерфейс содержит набор методов, с помощью которых происходит учет использования
 * локальных переменных в генерируемом коде.
 * @author Anton Sharapov
 */
public interface VariablesRegistry extends VariablesContainer {

    /**
     * Регистрирует в данном блоке кода новую переменную.
     * По умолчанию, регистрируется переменная с указанным в аргументе именем. Если по каким-либо причинам данное имя не
     * может быть использовано при регистрации новой переменной (некорректное имя или переменная с таким именем уже есть)
     * то метод сам сгенерирует имя переменной (по возможности, путем добавления некоторого числового префикса).
     * @param type  класс объявляемой переменной.
     * @param expectedName  ожидаемое имя переменной. Может не совпадать с тем именем которое вернет данный метод.
     * @param reusable  если <code>true</code> то переменная может повторно использоваться в дальнейшем.
     *          (повторно использоваться могут только переменные у которых стоит отметка что они в данный момент не используются).
     * @return  информация о зарегистрированной в блоке переменной.
     * @throws IllegalArgumentException  в случае если переменная с указанным именем уже доступна в блоке.
     */
    public Variable defineVariable(RefType type, String expectedName, boolean reusable);

    /**
     * Находит неиспользуемую локальную переменную требуемого типа в данном блоке.
     * Переменные в родительских блоках не рассматриваются.
     * @param type  класс искомой переменной.
     * @return  информация о найденной переменной или <code>null</code>.
     */
    public Variable findUnusedVariable(RefType type);

    /**
     * Подбирает имя для новой локальной переменной в данном блоке кода.
     * @param expectedName  ожидаемое имя переменной. Может не совпадать с тем именем которое вернет данный метод.
     * @return  подобранное имя для новой переменной. Как правило либо совпадает с <code>expectedName</code> либо является
     *    конкатенацией expectedName и некоторого автоинкрементного числа.
     */
    public String findUnusedVariableName(String expectedName);

}
