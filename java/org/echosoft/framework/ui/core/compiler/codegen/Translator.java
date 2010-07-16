package org.echosoft.framework.ui.core.compiler.codegen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.echosoft.framework.ui.core.compiler.ast.FileNode;
import org.echosoft.framework.ui.core.web.wui.Options;

/**
 * Отвечает за трансляцию .wui файлов в соответствуюшие .java сервлеты.
 * @author Anton Sharapov
 */
public class Translator {

    /**
     * Выполняет трансляцию .wui Файла в соответствующий ему .java файл.
     * @param uri  относительный путь до данного ресурса на сервере. Декодируется в имя класса и пакет java.
     * @param options  конфигурационные параметры.
     * @return  путь к сгенерированному .java классу.
     * @throws IOException  в случае каких-либо проблем с сохранением генерируемого файла на диск.
     */
    public static File translate(final String uri, final Options options) throws IOException {
        final File srcFile = new File(options.rootSrcDir, uri);

        final FileNode fn = new FileNode(uri, options);
        final File file = fn.getDstFile();
        final FileWriter out = new FileWriter( file );
        try {
            fn.translate(out);
        } finally {
            out.close();
        }
        return file;
    }
}
