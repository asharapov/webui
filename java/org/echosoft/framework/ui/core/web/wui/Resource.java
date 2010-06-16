package org.echosoft.framework.ui.core.web.wui;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.echosoft.framework.ui.core.compiler.codegen.Translator;

/**
 * Описывает отдельно взятый .wui ресурс.
 * @author Anton Sharapov
 */
public class Resource {
    /**
     * Глобальный контекст
     */
    private final RuntimeContext rctx;
    /**
     * Относительный путь до .wui ресурса.
     */
    private final String uri;
    /**
     * Полное имя класса сервлета который должен быть сгенерирован на основе данного .wui файла.
     */
    private final String className;
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
     * Экземпляр сервлета скомпилированного на основе .wui файла.
     */
    private HttpServlet servlet;


    public static Resource getResource(final RuntimeContext rctx, final String uri) {
        final File file = new File(rctx.getOptions().rootSrcDir, uri);
        return file.isFile()
                ? new Resource(rctx, uri)
                : null;
    }

    private Resource(final RuntimeContext rctx, final String uri) {
        this.rctx = rctx;
        this.uri = uri;
        final Options options = rctx.getOptions();
        String path = uri.substring(0, uri.lastIndexOf('.'));   // "/p1/p2/myclass.wui"  =>  "/p1/p2/myclass"
        if (options.basePkgName!=null) {
            this.className = options.basePkgName + path.replace('/','.');
            path = '/' + options.basePkgName.replace('.','/') + path;
        } else {
            this.className = path.substring(1).replace('/','.');
        }
        this.wuiFile = new File(options.rootSrcDir, uri);
        this.javaFile = new File( options.rootDstDir, path+".java" );
        this.classFile = new File( options.rootDstDir, path+".class" );

        if (classFile.lastModified()>=wuiFile.lastModified()) {
            // вроде бы файл с сервлетом есть и он валиден. Попробуем сразу создать экземпляр сервлета...
            try {
                final Class cls = rctx.getWUIClassLoader().loadClass(className);
                this.servlet = (HttpServlet)cls.newInstance();
                this.servlet.init(rctx.getServletConfig());
            } catch (Exception e) {
                // это был какой-то неправильный класс...
            }
        }
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
     * Валидирует состояние данного ресурса, при необходимости компилирует соответствующий сервлет и
     * выполняет вызов к сервлету являющемуся результатом компиляции соответствующего .wui файла.
     * @param request  пользовательский запрос
     * @param response ответ системы на пользовательский запрос.
     * @throws ServletException в случае каких-либо проблем при вызове сервлета.
     * @throws IOException в случае каких-либо проблем с вводом-выводом.
     * @throws java.io.FileNotFoundException в случае когда соответствующий .wui файл отсутствует (был удален).
     */
    public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            invalidate();
        } catch (IOException e) {
            throw new FileNotFoundException("Resource not found: "+uri);
        }
        servlet.service(request, response);
    }


    /**
     * <p>Проверяет ресурс на доступность, определяет требуется ли компиляция сервлета генерируемого на
     * основе исходного .wui Файла (определяется сравниванием дат последнего изменения соответствующих .wui и .class файлов).</p>
     * <p>В случае успешного завершения данного метода для данного ресурса уже имеется актуальная версия соответствующего .class файла
     * (сам класс еще может быть не не загружен в JVM).</p>
     * @throws ServletException в случае каких-либо проблем при компиляции сервлета.
     * @throws IOException в случае каких-либо проблем с вводом-выводом (ресурс не найден).
     */
    protected void invalidate() throws ServletException, IOException {
        if (rctx.getOptions().development) {
            // цель данной стратегии - как можно более своевременная перекомпиляция устаревших классов.
            final long tstamp = wuiFile.lastModified();
            if (tstamp==0) {
                throw new FileNotFoundException("Resource not found");
            }
            if (servlet==null || classFile.lastModified()<tstamp) {
                synchronized (this) {
                    if (servlet==null || classFile.lastModified()<tstamp)
                        compile();
                }
            }
        } else {
            // цель данной стратегии - как можно меньше операций с io.
            if (servlet==null) {
                synchronized (this) {
                    if (servlet==null)
                        compile();
                }
            }
        }
    }

    /**
     * Данный метод вызывается когда требуется перекомпилировать оттранслированный java класc
     * (а заодно и проверить его на актуальность)
     * @throws ServletException в случае каких-либо проблем при компиляции сервлета.
     * @throws IOException в случае каких-либо проблем с вводом-выводом (ресурс не найден).
     */
    protected void compile() throws ServletException, IOException {
        // зачистка старых результатов компиляции ...
        if (javaFile.isFile() && !javaFile.delete())
            throw new IOException("Unable to delete file: "+classFile.getAbsolutePath());
        if (classFile.isFile() && !classFile.delete())
            throw new IOException("Unable to delete file: "+classFile.getAbsolutePath());
        if (servlet!=null) {
            servlet.destroy();
            servlet = null;
        }
        // проверим существование каталога ... todo: перенести в транслятор!
        final File dir = javaFile.getParentFile();
        if (!dir.exists() && !dir.mkdirs())
            throw new IOException("Unable to create directory: "+dir.getAbsolutePath());
        // трансляция .wui -> .java ...
        Translator.translate(uri, rctx.getOptions());
        // собственно компиляция и загрузка сервлета...
        rctx.compile(javaFile);
        try {
            final Class cls = rctx.getWUIClassLoader().loadClass(className);
            servlet = (HttpServlet)cls.newInstance();
            servlet.init(rctx.getServletConfig());
        } catch (Exception e) {
            servlet = null;
            throw new ServletException(e.getMessage(), e);
        }
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
