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
     * @param uri  относительный путь до данного ресурса на сервере. Декодируется в имя класса и пакет java.
     * @param options  конфигурационные параметры.
     * @throws IOException  в случае проблем с получением канонической версии пути к требуемому файлу.
     */
    public TranslationContext(final String uri, final Options options) throws IOException {
        srcFile = new File(options.rootSrcDir, uri);
        int p;
        String path = (p=uri.lastIndexOf('.')) >= 0 ? uri.substring(0,p) : uri; // убрали расширение исходного файла
        if (options.basePkgName!=null)
            path = '/' + options.basePkgName.replace('.','/') + path;
        dstFile = new File( options.rootDstDir, path+".java" );
        p = path.lastIndexOf('/');
        if (p>0) {
            pkgName = path.substring(1,p).replace('/','.');
            clsName = path.substring(p+1);
        } else {
            pkgName = "";
            clsName = path.substring(1);
        }
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
