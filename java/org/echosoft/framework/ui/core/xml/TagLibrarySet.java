package org.echosoft.framework.ui.core.xml;

import java.util.HashMap;
import java.util.Map;

/**
 * Содержит ссылки на все загруженные библиотеки тегов.
 * @author Anton Sharapov
 */
public class TagLibrarySet {

    private final Map<String,TagLibrary> libraries;

    public static TagLibrarySet findLibariesInClasspath() {
        final TagLibrarySet result = new TagLibrarySet();
        return result;
    }

    public TagLibrarySet() {
        this.libraries = new HashMap<String,TagLibrary>();
    }

    /**
     * Осуществляет поиск библиотеки тегов в списке уже зарегистрированых библиотек.
     * Если таковая в списке еще не значится то регистрирует ее.
     * @param uri  URI используемый в качестве уникального идентификатора библиотеки тегов.
     * @return  описание указаной библиотеки. Никогда не возвращает <code>null</code>.
     */
    public TagLibrary ensureLibraryExists(final String uri) {
        TagLibrary library = libraries.get(uri);
        if (library==null) {
            library = new TagLibrary(uri);
            libraries.put(uri, library);
        }
        return library;
    }

    /**
     * Осуществляет поиск библиотеки тегов в списке уже зарегистрированых библиотек.
     * Если таковая в списке не значится то метод возвращает <code>null</code>.
     * @param uri  URI используемый в качестве уникального идентификатора библиотеки тегов.
     * @return  описание указаной библиотеки или <code>null</code> если таковая еще не зарегистрирована.
     */
    public TagLibrary getLibrary(final String uri) {
        return libraries.get(uri);
    }

    /**
     * В библиотеке тегов обозначенной под соответствующем URI ищется обработчик указанного в аргументе тега.
     * @param uri  URI используемый в качестве уникального идентификатора библиотеки тегов.
     * @param tagName  локальное имя тега.
     * @return  обработчик указанного тега или <code>null</code> в случае отсутствия информации по указанной библиотеке или отсутствия в этой
     * библиотеке информации по указанному тегу.
     */
    public TagHandler getTagHandler(final String uri, final String tagName) {
        final TagLibrary library = libraries.get(uri);
        return library!=null ? library.getTagHandler(tagName) : null;
    }
}
