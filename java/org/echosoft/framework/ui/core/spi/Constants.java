package org.echosoft.framework.ui.core.spi;

/**
 * @author Anton Sharapov
 */
public class Constants {

    /**
     * По этому пути в атрибутах запроса находится объект типа Map<String,Object> доступный для всех компонент участвующих в обработки пользовательского запроса.
     * @see org.echosoft.framework.ui.core.Scope#SHARED
     */
    public static final String SHARED_ATTRS_MAP = "topsbi.framework.ui.shared";

    /**
     * Имя атрибута в пользовательской сессии, который хранит логическое имя темы используемой при обработке всех запросов в рамках
     * данной сессии. Если указанный атрибут в сессии отсутствует то будет использоваться тема по умолчанию.
     */
    public static final String CURRENT_THEME = "topsbi.framework.ui.theme";

    /**
     * Параметр запроса, содержащий либо ключ к хранимым состояниям форм либо сами эти состояния (зависит от реализации).
     */
    public static final String VIEWSTATE_PARAMNAME = "__VIEWSTATE";
}
