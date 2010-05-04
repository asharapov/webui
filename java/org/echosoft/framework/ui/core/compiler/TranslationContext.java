package org.echosoft.framework.ui.core.compiler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
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
     * Множество классов, которые используются в генерируемом классе.
     */
    public final TreeSet<Class> imports;
    /**
     * Информация о транслируемых в настоящий момент методах данного класса.
     */
    public final ArrayList<MethodContext> methods;

    public MethodContext current;

    private static final Comparator<Class> CLS_COMPARATOR =
            new Comparator<Class>() {
                public int compare(final Class o1, final Class o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            };

    /**
     * Выполняет трансляцию .wui Файла в соответствующий ему .java файл.
     * @param baseDir  каталог под которым расположена вся иерархия проектных .wui файлов
     * и соответствующих им сервлетов. Практически, это корневой каталог веб приложения на сервере.
     * @param filePath  путь к транслируемому .wui файлу относительно базового каталога.
     * @throws IOException  в случае проблем с получением канонической версии пути к требуемому файлу.
     */
    public TranslationContext(File baseDir, String filePath) throws IOException {
        baseDir = baseDir.getCanonicalFile();
        int p;
        srcFile = new File(baseDir, filePath). getCanonicalFile();
        if (!srcFile.isFile())
            throw new IllegalArgumentException("Source file not finded");
        if (!srcFile.getPath().startsWith(baseDir.getPath()))
            throw new IllegalArgumentException("Source path doesn't belongs to base directory");
        p = srcFile.getPath().lastIndexOf('.');
        dstFile = new File( srcFile.getPath().substring(0,p)+".java" );
        p = srcFile.getName().lastIndexOf('.');
        clsName = srcFile.getName().substring(0,p);
        filePath = srcFile.getPath().substring(baseDir.getPath().length());
        pkgName = Utils.getPackageNameByPath(filePath);
        imports = new TreeSet<Class>(CLS_COMPARATOR);
        methods = new ArrayList<MethodContext>();
    }

    /**
     * Регистрирует очередной класс в секции "import".
     * @param cls класс чьи объекты будут использоваться в генерируемом классе.
     * @return  <code>true</code> если класс успешно занесен в секцию "import".
     *   <code>false</code> если в секции "import" уже имеется класс с таким именем но находящийся в другом пакете.
     *   В этом случае все переменные указанного класса требуется при объявлении указывать вместе с пакетом.
     */
    public boolean ensureClassImported(final Class cls) {
        final String name = cls.isArray() ? cls.getClass().getName() : cls.getName();
        if (imports.contains(cls))
            return true;
        for (Class cn : imports) {
            if (cn.getName().equals(name)) {
                return false;
            }
        }
        imports.add( cls );
        return true;
    }
    
}
