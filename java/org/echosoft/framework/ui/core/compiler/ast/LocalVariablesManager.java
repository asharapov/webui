package org.echosoft.framework.ui.core.compiler.ast;

import java.util.Map;

import org.echosoft.framework.ui.core.compiler.Variable;

/**
 * Внутри любого блока кода могут быть объявлены переменные с соответствующей областью видимости.
 * Данный интерфейс регламентирует набор методов, с помощью которых происходит учет использования
 * локальных переменных в генерируемом коде.
 * @see MethodNode
 * @see StatementListNode
 * @author Anton Sharapov
 */
public interface LocalVariablesManager {

    /**
     * Возвращает информацию по переменным доступным из данного блока кода.
     * Сюда включаются как переменные объявленные в этом блоке, так и переменные (включая поля класса) объявленные в
     * вышележащих блоках кода.
     * @return  экземпляр класса Map, где ключом является имя переменной а значением - подробная информация о ней.
     */
    public Map<String, Variable> getAccessibleVariables();

    /**
     * Регистрирует в данном блоке кода новую переменную. Если переменная с таким именем уже доступна в данном блоке
     * (т.е. уже была объявлена либо в данном блоке либо в одном из вышележащих) то операция вернет исключение.
     * @param cls  класс объявляемой переменной.
     * @param expectedName  ожидаемое имя переменной. Может не совпадать с тем именем которое вернет данный метод.
     * @param modifiable  если <code>false</code> то переменная объявляется как <code>final</code>.
     * @param reusable  если <code>true</code> то переменная может повторно использоваться в дальнейшем.
     *          (повторно использоваться могут только переменные у которых стоит отметка что они в данный момент не используются).
     * @return  информация о зарегистрированной в блоке переменной.
     * @throws IllegalArgumentException  в случае если переменная с указанным именем уже доступна в блоке.
     */
    public Variable defineVariable(Class cls, String expectedName, boolean modifiable, boolean reusable);

    /**
     * Находит неиспользуемую локальную переменную требуемого типа.
     * @param cls  класс искомой переменной.
     * @return  информация о найденной переменной или <code>null</code>.
     */
    public Variable findUnusedVariable(Class cls);

    /**
     * Подбирает имя для новой локальной переменной в данном блоке кода.
     * @param expectedName  ожидаемое имя переменной. Может не совпадать с тем именем которое вернет данный метод.
     * @return  подобранное имя для новой переменной. Как правило либо совпадает с expectedName либо является
     *  конкатенацией expectedName и некоторого числа.
     */
    public String findUnusedVariableName(String expectedName);

}
