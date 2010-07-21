package org.echosoft.framework.ui.core.compiler.xml;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.echosoft.common.utils.XMLUtil;
import org.echosoft.framework.ui.core.Application;
import org.echosoft.framework.ui.core.compiler.utils.ClasspathUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Содержит ссылки на все загруженные библиотеки тегов.
 * @author Anton Sharapov
 */
public class TagLibrarySet {

    private final Map<String,TagLibrary> libraries;
    private TagLibrary defaultLibrary;

    /**
     * Осуществляет поиск и загрузку информации обо всех библиотеках тегов которые доступны
     * текущему загрузчику классов в JVM.
     * @return  коллекция найденных библиотек тегов.
     * @throws IOException  в случае каких-либо проблем.
     */
    public static TagLibrarySet findLibariesInClasspath() throws IOException {
        final TagLibrarySet result = new TagLibrarySet();
        for (URL url : ClasspathUtils.search("META-INF/", ".taglib.xml")) {
            try {
                final Document doc = XMLUtil.loadDocument(url);
                final Element root = doc.getDocumentElement();
                final String uri = root.getAttribute("uri");
                if (!root.getTagName().equals("taglib") || uri.isEmpty()) {
                    Application.log.warn("Incorrect taglib definition: "+url);
                    continue;
                }
                final TagLibrary taglib = result.ensureLibraryExists( uri );
                for (Iterator<Element> it = XMLUtil.getChildElements(root,"tag"); it.hasNext(); ) {
                    final Element elem = it.next();
                    final String tagName = elem.getAttribute("name");
                    final String className = elem.getAttribute("handler");
                    if (tagName.isEmpty() || className.isEmpty()) {
                        Application.log.warn("Illegal tag definition ("+tagName+"): "+url);
                        continue;
                    }
                    taglib.addTagHandler(tagName, className);
                }
            } catch (Exception e) {
                Application.log.warn("An error occurs while processing tags library \""+url+"\": "+e.getMessage(), e);
            }
        }
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
     * Возвращается ссылка на библиотеку тегов по умолчанию.
     * Она используется в случае когда искомая по uri библиотека не была найдена.
     * @return библиотека тегов, используемая по умолчанию для всех неизвестных uri
     *  Возвращает <code>null</code> если библиотека тегов по умолчанию не указана.
     */
    public TagLibrary getDefaultLibrary() {
        return defaultLibrary;
    }

    /**
     * Указывает библиотеку тегов, используемую всякий раз когда осуществляется запрос тегов
     * из неизвестной данному объекту библиотеки тегов.
     * @param defaultLibrary  библиотека тегов по умолчанию.
     */
    public void setDefaultLibrary(final TagLibrary defaultLibrary) {
        this.defaultLibrary = defaultLibrary;
    }

    /**
     * Возвращает <code>true</code> если объект содержит библиотеку тегов, ассоциированную с данным uri.
     * Библиотека тегов по умолчанию здесь в расчет не берется.
     * @param uri  URI используемый в качестве уникального идентификатора библиотеки тегов.
     * @return <code>true</code> если объект содержит библиотеку тегов, ассоциированную с данным uri.
     */
    public boolean containsLibrary(final String uri) {
        return libraries.containsKey(uri);
    }

    /**
     * Осуществляет поиск библиотеки тегов в списке уже зарегистрированых библиотек.
     * Если таковая в списке не значится то метод возвращает <code>null</code>.
     * @param uri  URI используемый в качестве уникального идентификатора библиотеки тегов.
     * @return  описание указаной библиотеки или <code>null</code> если таковая еще не зарегистрирована.
     */
    public TagLibrary getLibrary(final String uri) {
        final TagLibrary result = libraries.get(uri);
        return result!=null ? result : defaultLibrary;
    }

    /**
     * В библиотеке тегов обозначенной под соответствующем URI ищется обработчик указанного в аргументе тега.
     * @param uri  URI используемый в качестве уникального идентификатора библиотеки тегов.
     * @param tagName  локальное имя тега.
     * @return  обработчик указанного тега или <code>null</code> в случае отсутствия информации по указанной библиотеке или отсутствия в этой
     * библиотеке информации по указанному тегу.
     */
    public TagHandler getTagHandler(final String uri, final String tagName) {
        final TagLibrary library = getLibrary(uri);
        return library!=null ? library.getTagHandler(tagName) : null;
    }
}
