package org.echosoft.framework.ui.core.compiler.xml;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.echosoft.common.utils.ObjectUtil;
import org.echosoft.common.utils.StringUtil;

/**
 * Описывает библиотеку тегов - элементов описания web страниц на xml.  
 * @author Anton Sharapov
 */
public class TagLibrary {

    private final String uri;
    private final Map<String,TagHandler> tags;
    private TagHandler defaultHandler;

    public TagLibrary(final String uri) {
        this.uri = StringUtil.trim(uri);
        this.tags = new HashMap<String,TagHandler>();
    }

    /**
     * Возвращает уникальный идентификатор библиотеки тегов (включая версию).
     * @return  уникальный идентификатор данной библиотеки тегов.
     */
    public String getUri() {
        return uri;
    }

    /**
     * Регистрирует в библиотеке новый тег.
     * @param tagName  имя тега (чуствительно к регистру).
     * @param className  имя класса - обработчика. Должен реализовывать интерфейс {@link TagHandler}.
     * @throws InvocationTargetException  поднимается в случае ошибки конструирования экземпляра класса {@link TagHandler} по имени его класса.
     * @throws ClassNotFoundException  поднимается в случае ошибки конструирования экземпляра класса {@link TagHandler} по имени его класса.
     * @throws NoSuchMethodException  поднимается в случае ошибки конструирования экземпляра класса {@link TagHandler} по имени его класса.
     * @throws IllegalAccessException  поднимается в случае ошибки конструирования экземпляра класса {@link TagHandler} по имени его класса.
     * @throws InstantiationException  поднимается в случае ошибки конструирования экземпляра класса {@link TagHandler} по имени его класса.
     */
    public void addTagHandler(final String tagName, final String className) throws InvocationTargetException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        final TagHandler handler = ObjectUtil.makeInstance(className, TagHandler.class);
        tags.put(tagName, handler);
    }

    /**
     * Возвращает обработчик тегов по умолчанию.
     * Он используется в случаях когда не представляется возможным иными средствами определить обработчик по имени тега.
     * @return обработчик тегов по умолчанию или <code>null</code>.
     */
    public TagHandler getDefaultHandler() {
        return defaultHandler;
    }

    /**
     * Указывает обработчик тегов по умолчанию.
     * @param defaultHandler обработчик тегов по умолчанию.
     */
    public void setDefaultHandler(final TagHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    /**
     * Возвращает обработчик тега по его имени.
     * @param tagName  имя тега (чуствительно к регистру).
     * @return  экземпляр класса {@link TagHandler} или
     * <code>null</code> если указанный тег не зарегистрирован в библиотеке и ранее не был указан обработчик тегов по умолчанию.
     */
    public TagHandler getTagHandler(final String tagName) {
        final TagHandler result = tags.get(tagName);
        return result!=null ? result : defaultHandler;
    }
}
