package org.echosoft.framework.ui.core.compiler.ast;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
     * Узел дерева, соответствующий методу service(...) основного класса - сервлета.
     */
    private final MethodNode serviceNode;

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
        final ClassNode clsnode = new ClassNode(clsName, HttpServlet.class);
        append(clsnode);
        serviceNode = new MethodNode(null, "service", Visibility.PUBLIC, false)
                .setOverrided(true)
                .addArgument(HttpServletRequest.class, "request", true)
                .addArgument(HttpServletResponse.class, "response", true)
                .addException(ServletException.class)
                .addException(IOException.class);
        clsnode.append(serviceNode);
    }

    /**
     * Возвращает указание в каком месте файловой системы должен быть расположен транслируемый файл.
     * @return файл в который должно быть сохранено содержимое данного дерева узлов.
     */
    public File getDstFile() {
        return dstFile;
    }

    /**
     * Ссылка на узел, соответствующий методу service(...) транслируемого основного класса - сервлета.
     * @return узел, соответствующий методу service(...) транслируемого класса - сервлета.
     */
    public MethodNode getServletService() {
        return serviceNode;
    }

    @Override
    public FileNode getRoot() {
        return this;
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
            buf.write('\n');
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

}
