package org.echosoft.framework.ui.core.compiler;

import java.io.File;
import java.io.IOException;

/**
 * Отвечает за трансляцию .wui файлов в соответствуюшие .java сервлеты.
 * @author Anton Sharapov
 */
public class Translator {

    /**
     * Выполняет трансляцию .wui Файла в соответствующий ему .java файл.
     * @param baseDir  каталог под которым расположена вся иерархия проектных .wui файлов
     * и соответствующих им сервлетов. Практически, это корневой каталог веб приложения на сервере.
     * @param filePath  путь к транслируемому .wui файлу относительно базового каталога.
     * @return  путь к сгенерированному .java классу.
     * @throws IOException  в случае каких-либо проблем с сохранением генерируемого файла на диск.
     */
    public static File translate(File baseDir, String filePath) throws IOException {
        final TranslationContext tc = new TranslationContext(baseDir, filePath);
        return tc.dstFile;
    }
}
