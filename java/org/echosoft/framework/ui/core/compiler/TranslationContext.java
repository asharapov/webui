package org.echosoft.framework.ui.core.compiler;

import java.io.File;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Содержит всю собранную информацию о транслируемом классе, его структуре и зависимостях.
 * @author Anton Sharapov
 */
public class TranslationContext {
    /**
     * Путь до исходного .wui файла заданный в канонической форме.
     */
    public final File srcFile;
    /**
     * Путь до целевого .java файла заданный в канонической форме.
     */
    public final File dstFile;
    /**
     * Имя транслируемого файла (без расширения)
     * Производная от него используется в качестве имени генерируемого .java класса.
     */
    public final String clsName;
    /**
     * Имя пакета в котором находится генерируемый .java класс.
     */
    public final String pkgName;
    /**
     * Множество пакетов, которые должны быть импортированы в транслируемый класс.
     */
    public final SortedSet<Class> imports;

    /**
     * Выполняет трансляцию .wui Файла в соответствующий ему .java файл.
     * @param baseDir  каталог под которым расположена вся иерархия проектных .wui файлов
     * и соответствующих им сервлетов. Практически, это корневой каталог веб приложения на сервере.
     * @param filePath  путь к транслируемому .wui файлу относительно базового каталога.
     */
    public TranslationContext(File baseDir, String filePath) throws IOException {
        baseDir = baseDir.getCanonicalFile();
        int p;
        srcFile = new File(baseDir, filePath). getCanonicalFile();
        if (!srcFile.getPath().startsWith(baseDir.getPath()))
            throw new IllegalArgumentException("Source path doesn't belong to base directory");
        p = srcFile.getPath().lastIndexOf('.');
        dstFile = new File( srcFile.getPath().substring(0,p)+".java" );
        p = srcFile.getName().lastIndexOf('.');
        clsName = srcFile.getName().substring(0,p);
        filePath = srcFile.getPath().substring(baseDir.getPath().length());
        pkgName = Utils.getPackageNameByPath(filePath);
        imports = new TreeSet<Class>();
    }

    public boolean ensureClassImported(final Class cls) {
        if (imports.contains(cls))
            return true;
        final String name = cls.getName();
        for (Class c : imports) {
            if (c.getName().equals(name)) {
                return false;
            }
        }
        imports.add( cls );
        return true;
    }
}
