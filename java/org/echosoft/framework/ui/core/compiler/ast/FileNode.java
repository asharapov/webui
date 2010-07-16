package org.echosoft.framework.ui.core.compiler.ast;

import javax.servlet.Servlet;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Comparator;
import java.util.TreeSet;

import org.echosoft.framework.ui.core.web.wui.Options;

/**
 * Содержит всю собранную информацию о транслируемом файле (включая все содержащиеся в нем классы).
 * @author Anton Sharapov
 */
public class FileNode extends ASTNode {
    /**
     * Конфигурация модуля трансляции .wui файлов в сервлеты.
     */
    public final Options options;
    /**
     * Путь до целевого .java файла заданный в канонической форме.
     */
    private final File dstFile;
    /**
     * Имя пакета в котором находится генерируемый .java класс.
     */
    private final String pkgName;
    /**
     * Множество классов, которые используются в генерируемом классе.
     */
    private final TreeSet<Class> imports;

    /**
     * Узел дерева, соответствующий основному классу - сервлету.
     */
    private final ClassNode clsnode;

    private static final Comparator<Class> CLS_COMPARATOR =
            new Comparator<Class>() {
                public int compare(final Class o1, final Class o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            };

    /**
     * @param uri  относительный путь до данного ресурса на сервере. Декодируется в имя класса и пакет java.
     * @param options  конфигурационные параметры.
     * @throws IOException  в случае проблем с получением канонической версии пути к требуемому файлу.
     */
    public FileNode(final String uri, final Options options) throws IOException {
        this.options = options;
        int p;
        String path = (p=uri.lastIndexOf('.')) >= 0 ? uri.substring(0,p) : uri; // убрали расширение исходного файла
        if (options.basePkgName!=null)
            path = '/' + options.basePkgName.replace('.','/') + path;
        dstFile = new File( options.rootDstDir, path+".java" );
        p = path.lastIndexOf('/');
        final String clsName;
        if (p>0) {
            pkgName = path.substring(1,p).replace('/','.');
            clsName = path.substring(p+1);
        } else {
            pkgName = "";
            clsName = path.substring(1);
        }
        imports = new TreeSet<Class>(CLS_COMPARATOR);
        clsnode = new ClassNode(clsName, Servlet.class);
        append(clsnode);
    }

    /**
     * Возвращает указание в каком месте файловой системы должен быть расположен транслируемый файл.
     * @return файл в который должно быть сохранено содержимое данного дерева узлов.
     */
    public File getDstFile() {
        return dstFile;
    }

    /**
     * Ссылка на узел, соответствующий транслируемому классу - сервлету.
     * @return узел, соответствующий основному классу в файле (сервлету).
     */
    public ClassNode getClassNode() {
        return clsnode;
    }

    @Override
    public ASTNode append(final ASTNode node) {
        if (!(node instanceof ClassNode))
            throw new IllegalStateException("ClassNode required");
        return super.append(node);
    }

    @Override
    public void translate(final Writer out) throws IOException {
        if (!pkgName.isEmpty()) {
            out.write("package ");
            out.write(pkgName);
            out.write(";\n\n");
        }
        for (Class cls : imports) {
            out.write("import ");
            out.write(cls.getCanonicalName());
            out.write(";\n");
        }
        for (ASTNode node : children) {
            out.write('\n');
            node.translate(out);
        }
    }

    /**
     * Регистрирует очередной класс в секции "import".
     * @param cls класс чьи объекты будут использоваться в генерируемом классе.
     * @return  <code>true</code> если класс успешно занесен в секцию "import".
     *   <code>false</code> если в секции "import" уже имеется класс с таким именем но находящийся в другом пакете.
     *   В этом случае все переменные указанного класса требуется при объявлении указывать вместе с пакетом.
     */
    public boolean ensureClassImported(Class cls) {
        if (cls.isArray())
            cls = cls.getComponentType();
        if (imports.contains(cls))
            return true;
        final String name = cls.getSimpleName();
        for (Class cn : imports) {
            if (cn.getSimpleName().equals(name)) {
                return false;
            }
        }
        imports.add( cls );
        return true;
    }
    
}
