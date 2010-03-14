package org.echosoft.framework.ui.core.theme;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Различные сервера приложений в некоторых деталях работают по разному со ссылками на ресурсы приложения.
 * Данный класс призван решать все подобные проблемы для серверов приложений на котором данная библиотека тестировалась:
 * <ul>
 *  <li> Tomcat
 *  <li> JBoss
 *  <li> IBM WAS
 *  <li> Oracle iAS
 * </ul>
 *
 * @author Anton Sharapov
 */
final class UrlUtil {

    // известные протоколы
    private static final String CODE_SOURCE_PROTOCOL = "code-source";  // используется в Oracle iAS (не работает с абсолютными ссылками на ресурсы).
    private static final String VFS_PROTOCOL = "vfszip";               // используется в JBoss5 (не работает с абсолютными ссылками на ресурсы).
    private static final String WSJAR_PROTOCOL = "wsjar";              // используется в IBM WAS (не работает с относительными ссылками на ресурсы).
    private static final String JAR_PROTOCOL = "jar";                  // используется в Tomcat и JBoss4 (корректно работает со всеми видами ссылок на ресурсы).


    /**
     * Возвращает ссылку на корневой каталог в jar файле.
     * @param url  ссылка на ресурс /META-INF/MANIFEST.MF в заданном jar Файле.
     * @return  ссылка на корневой каталог в jar файле.
     * @throws MalformedURLException  В случае некорректной ссылки на jar файл.
     */
    public static URL normalize(final URL url) throws MalformedURLException {
        final String protocol = url.getProtocol();
        if (CODE_SOURCE_PROTOCOL.equals(protocol) || VFS_PROTOCOL.equals(protocol)) {
            return new URL(url, "..");
        } else {
            return new URL(url, "/");
        }
    }

    /**
     * Возвращает ссылку на указанный ресурс расположенный в заданном ссылкой jar файле.
     * @param url  ссылка на корневой каталог jar файла.
     * @param resource  абсолютный путь (относительно корневого каталога jar файла) на требуемый ресурс в jar файле.
     * @return  ссылка на требуемый ресурс.
     * @throws MalformedURLException  в случае некорректной ссылки на jar файл.
     */
    public static URL makeResourceURL(final URL url, final String resource) throws MalformedURLException {
        final String protocol = url.getProtocol();
        if (CODE_SOURCE_PROTOCOL.equals(protocol) || VFS_PROTOCOL.equals(protocol)) {
            return new URL(url, "."+resource);
        } else {
            return new URL(url, resource);
        }
    }
}
