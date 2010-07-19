package org.echosoft.framework.ui.core.compiler.ast;

import javax.servlet.Servlet;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.SortedSet;
import java.util.TreeSet;

import org.echosoft.common.io.FastStringWriter;
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
    private final SortedSet<Class> imports;

    /**
     * Узел дерева, соответствующий основному классу - сервлету.
     */
    private final ClassNode mainClsNode;

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
        imports = new TreeSet<Class>(ASTNode.CLS_COMPARATOR);
        mainClsNode = new ClassNode(clsName, Servlet.class);
        append(mainClsNode);
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
    public ClassNode getMainClassNode() {
        return mainClsNode;
    }

    @Override
    public ASTNode append(final ASTNode node) {
        if (!(node instanceof ClassNode))
            throw new IllegalStateException("ClassNode required");
        return super.append(node);
    }

    @Override
    public void translate(final Writer out) throws IOException {
        final FastStringWriter buf = new FastStringWriter(4096);
        for (ASTNode node : children) {
            out.write('\n');
            node.translate(buf);
        }

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
        buf.writeOut( out );
    }

    /**
     * Регистрирует очередной класс в секции "import".
     * @param cls класс чьи объекты будут использоваться в генерируемом классе.
     * @return  краткая форма записи если класс успешно занесен в секцию "import", в противном случае возвращает строку,
     *      соответствующую полной форме записи имени класса.
     */
    public String ensureClassImported(final Class cls) {
        final Class normcls = cls.isArray() ? cls.getComponentType() : cls;
        if (imports.contains(normcls))
            return cls.getSimpleName();
        final String name = normcls.getSimpleName();
        for (Class cn : imports) {
            if (cn.getSimpleName().equals(name)) {
                return cls.getCanonicalName();
            }
        }
        imports.add( normcls );
        return cls.getSimpleName();
    }


    public static void main(String args[]) {
//        final TreeSet<String> ss = new TreeSet<String>();
//        for (TypeVariable tp : ss.getClass().getTypeParameters()) {
//        }
        final Class[] classes = {TreeSet.class, TreeSet[].class};
        for (Class cls : classes) {
            System.out.println("short:  "+cls.getSimpleName());
            System.out.println("full:  "+cls.getCanonicalName());
            System.out.println();
        }
    }
}
