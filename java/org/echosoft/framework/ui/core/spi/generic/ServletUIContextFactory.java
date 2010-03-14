package org.echosoft.framework.ui.core.spi.generic;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.echosoft.framework.ui.core.UIContext;
import org.echosoft.framework.ui.core.spi.ContextFactory;

/**
 * Конструирует экземпляр {@link UIContext} для простого веб приложения.
 * @see ServletUIContext
 * @author Anton Sharapov
 */
public class ServletUIContextFactory implements ContextFactory {

    @Override
    public UIContext makeContext(final HttpServletRequest request, final HttpServletResponse response, final ServletConfig config) throws Exception {
        if (request.getCharacterEncoding()==null)
            request.setCharacterEncoding("UTF-8");            // если не сделать этого, то на некоторых убогих серверах приложений может полетить кодировка. С концами полететь...
        response.setContentType("text/html; charset=UTF-8");  // по умолчанию мы используем данную кодировку и тип выходного содержимого. Когда это не так, то мы вызываем подобный метод еще раз...
        return new ServletUIContext(request, response, config);
    }
}
