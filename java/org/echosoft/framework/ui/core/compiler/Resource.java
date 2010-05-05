package org.echosoft.framework.ui.core.compiler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;

import org.echosoft.common.utils.StringUtil;

/**
 * Описывает отдельно взятый .wui ресурс.
 * @author Anton Sharapov
 */
public class Resource {
    /**
     * Относительный путь до ресурса.
     */
    private final String uri;
    /**
     * Глобальные конфигурационные параметры модуля.
     */
    private final Options options;
    /**
     * Исходный .wui Файл который требуется транслировать в .java а затем скомпилировать в .class
     */
    private final File wuiFile;
    /**
     * Результат трансляции исходного .wui файла в соответствующий .java файл.
     */
    private final File javaFile;
    /**
     * Результат компиляции оттранслированного .java файла в соответствующий .class файл.
     */
    private final File classFile;
    /**
     * Если <code>true</code> то ресурс должен быть в принудительном порядке перекомпилирован.
     */
    private boolean reload;

    public static Resource getResource(final String uri, final Options options) {
        final File file = new File(options.rootSrcDir, uri);
        return file.isFile()
                ? new Resource(uri, options)
                : null;
    }

    private Resource(final String uri, final Options options) {
        this.uri = uri;
        this.options = options;
        this.wuiFile = new File(options.rootSrcDir, uri);
        final String path = '/' + StringUtil.replace(options.basePkgName,".","/") + uri;
        final int p = path.lastIndexOf('.');
        this.javaFile = new File( options.rootDstDir, path.substring(0,p)+".java" );
        this.classFile = new File( options.rootDstDir, path.substring(0,p)+".class" );
        this.reload = options.development || classFile.lastModified()<wuiFile.lastModified();
    }

    /**
     * Возвращает идентификатор ресурса в качестве которого выступает относительный путь
     * до соответствующего .wui файла на сервере.
     * @return идентификатор ресурса.
     */
    public String getUri() {
        return uri;
    }

    /**
     * Проверяет данный ресурс на наличие его на сервере. Когда сервер находится в режиме отладки
     * на нем могут динамически добавляться, изменяться и удаляться практически любые ресурсы.
     * @return <code>true</code> если лежащий в основе данного ресурса .wui файл присутствует на сервере.
     */
    public boolean exists() {
        return wuiFile.isFile();
    }

    /**
     * <p>Проверяет ресурс на доступность, определяет требуется ли компиляция сервлета генерируемого на
     * основе исходного .wui Файла (определяется сравниванием дат последнего изменения соответствующих .wui и .class файлов).</p>
     * <p>В случае успешного завершения данного метода для данного ресурса уже имеется актуальная версия соответствующего .class файла
     * (сам класс еще может быть не не загружен в JVM).</p> 
     * @throws ServletException в случае каких-либо проблем при компиляции сервлета.
     * @throws IOException в случае каких-либо проблем с вводом-выводом (ресурс не найден).
     */
    public void invalidate() throws ServletException, IOException {
        if (options.development) {
            // цель данной стратегии - как можно более своевременная перекомпиляция устаревших классов.
            final long tstamp = wuiFile.lastModified();
            if (reload || classFile.lastModified()<tstamp)  // время изменения отсутствующего файла всегда 0.
                compile(tstamp);
        } else {
            // цель данной стратегии - как можно меньше операций с io.
            if (reload)
                compile(wuiFile.lastModified());
        }
        reload = false;
    }

    /**
     * Выполняет вызов к сервлету являющемуся результатом компиляции соответствующего .wui файла.
     * @param request  пользовательский запрос
     * @param response ответ системы на пользовательский запрос.
     * @throws ServletException в случае каких-либо проблем при вызове сервлета.
     * @throws IOException в случае каких-либо проблем с вводом-выводом.
     */
    public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        // TODO: при необходимости загружаем класс в JVM и создаем новый экземпдяр сервлета.
        // TODO: выполняем метод service(request,response) у этого сервлета.
    }


    /**
     * Данный метод вызывается когда требуется перекомпилировать оттранслированный java класc
     * (а заодно и проверить его на актуальность)
     * @param tstamp время последнего изменения .wui файла.
     * @throws ServletException в случае каких-либо проблем при компиляции сервлета.
     * @throws IOException в случае каких-либо проблем с вводом-выводом (ресурс не найден).
     */
    protected void compile(final long tstamp) throws ServletException, IOException {
        if (classFile.isFile() && !classFile.delete())
            throw new IOException("Unable to delete file: "+classFile.getAbsolutePath());
        if (options.development) {
            if (reload || javaFile.lastModified()<tstamp)   // время изменения отсутствующего файла всегда 0.
                translate();
        } else {
            if (reload)
                translate();
        }
        // собственно компиляция .java -> .class
        final JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
        final StandardJavaFileManager sjfm = jc.getStandardFileManager(null, null, null);
        final Iterable<? extends JavaFileObject> files = sjfm.getJavaFileObjects(javaFile);
        final JavaCompiler.CompilationTask task = jc.getTask(null, sjfm, null, null, null, files);
        if (!task.call())
            throw new ServletException("Unable to compile resource");
        // TODO: загрузка класса и инициализация сервлета ...
    }

    /**
     * Вызывается когда надо преобразовать .wui файл в соответствующий ему .java класс.
     * @throws ServletException в случае каких-либо проблем при трансляции сервлета.
     * @throws IOException в случае каких-либо проблем с вводом-выводом (ресурс не найден).
     */
    protected void translate() throws ServletException, IOException {
        if (javaFile.isFile()) {
            if (!javaFile.delete())
                throw new IOException("Unable to delete file: "+javaFile.getAbsolutePath());
        } else {
            final File dir = javaFile.getParentFile();
            if (!dir.exists() && !dir.mkdirs())
                throw new IOException("Unable to create directory: "+dir.getAbsolutePath());
        }
        // TODO: выполняем трансляцию .wui -> .java здесь ...
        // пока заглушка ...
        Translator.translate(uri, options);
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj==null || !getClass().equals(obj.getClass()))
            return false;
        final Resource other = (Resource)obj;
        return uri.equals( other.uri );
    }

    @Override
    public String toString() {
        return "[Resource{uri:"+uri+"}]";
    }
}
