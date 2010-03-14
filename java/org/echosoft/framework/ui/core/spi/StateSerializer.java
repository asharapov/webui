package org.echosoft.framework.ui.core.spi;

import org.echosoft.framework.ui.core.UIContext;

/**
 * Данный интерфейс описывает алгоритм, отвечающий за сохранение и последующее восстановление состояний посещенных страниц между запросами к системе определенного пользователя.
 * Данные состояния, в отличие от сессии пользователя на сервере жестко привязаны к окну пользовательского браузера.
 * 
 * @author Anton Sharapov
 */
public interface StateSerializer {

    /**
     * Сериализует состояния посещенных страниц и возвращает строковый ключ по которому в методе {@link #decodeState(org.echosoft.framework.ui.core.UIContext , String)} возможно будет восстановить эти состояния.
     * @param uctx контекст текущего пользовательского запроса.
     * @return  ключ по которому возможно восстановление состояний страниц.
     * @throws Exception  в случае каких-либо проблем.
     */
    public String encodeState(UIContext uctx) throws Exception;

    /**
     * Восстанавливает состояния посещенных ранее страниц на основе переданного в аргументе ключа.
     * @param uctx  контекст пользовательского запроса.
     * @param encodedState  ключ, сгенерированный ранее в методе {@link #encodeState(UIContext)}, по которому можно будет восстановить состояние посещенных ранее страниц.
     * @throws Exception  в случае каких-либо проблем.
     */
    public void decodeState(UIContext uctx, String encodedState) throws Exception;
}
