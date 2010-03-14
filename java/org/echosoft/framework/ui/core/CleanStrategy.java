package org.echosoft.framework.ui.core;

/**
 * Определяет алгоритм, используемый для удаления информации о неактуальных в настоящее время страницах.
 *
 * @author Anton Sharapov
 */
public interface CleanStrategy {

    /**
     * @return идентификатор алгоритма.
     */
    public String getId();

    /**
     * Метод возвращает <code>true</code> если второй аргумент указывает на дескриптор страницы информацию о которой уже можно удалить.
     * @param current дескриптор текущей страницы. Может быть <code>null</code>.
     * @param candidate дескриптор страницы, для которой надо вынести решение - стоит ли хранить ее в менеджере состояний.
     * @return  <code>true</code> если дескриптор страницы указанный во втором аргументе можно удалить из менеджера состояний.
     */
    public boolean needToClean(final ViewStateDescriptor current, final ViewStateDescriptor candidate);
}
