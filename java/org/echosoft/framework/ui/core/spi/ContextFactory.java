package org.echosoft.framework.ui.core.spi;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.echosoft.framework.ui.core.UIContext;

/**
 * Определяет алгоритм конструирования экземпляров {@link UIContext} на основе информации о запросе пользователя.
 * Используется главным образом для более четкой поддержки различных портальных движков.
 * @author Anton Sharapov
 */
public interface ContextFactory {

    /**
     * Создает новый экземпляр определенного класса, реализующего интерфейс {@link UIContext}.
     * @param request  запрос пользователя.
     * @param response  ответ пользователю.
     * @param config  конфигурация сервлета на который пришел пользовательский запрос.
     * @return  полностью настроенный и готовый к применению экземпляр {@link UIContext}.
     * @throws Exception в случае каких-либо проблем с созданием или настройкой созданного экземпляра {@link UIContext}.
     */
    public UIContext makeContext(HttpServletRequest request, HttpServletResponse response, ServletConfig config) throws Exception;

}
