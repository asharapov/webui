package org.echosoft.framework.ui.core.compiler;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Содержит ссылки на все .wui ресурсы.
 * @author Anton Sharapov
 */
public class RuntimeContext {

    private final Options options;
    private final ServletConfig servletConfig;
    private final Map<String,Resource> resources;
    private final JavaCompiler compiler;
    private final StandardJavaFileManager fileManager;
    private final ClassLoader parentClassLoader;
    private final URL[] loaderUrls;
    private URLClassLoader loader;

    public RuntimeContext(final Options options, final ServletConfig servletConfig) throws IOException {
        this.options = options;
        this.servletConfig = servletConfig;
        this.resources = new ConcurrentHashMap<String,Resource>();
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.fileManager = compiler!=null ? compiler.getStandardFileManager(null,null,null) : null;
        this.parentClassLoader = Thread.currentThread().getContextClassLoader();
        this.loaderUrls = new URL[]{options.rootDstDir.toURI().toURL()};
    }

    /**
     * @return конфигурационные параметры модуля.
     */
    public Options getOptions() {
        return options;
    }

    /**
     * Возвращает конфигурацию сервлета {@link org.echosoft.framework.ui.core.web.WUIServlet}, которая будет использоваться
     * для конфигурирования всех динамически создаваемых сервлетов.
     * @return конфигурация {@link org.echosoft.framework.ui.core.web.WUIServlet}
     */
    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    /**
     * По относительному пути до ресурса возвращает его описание.
     * Если указанный ресурс отсутствует на сервере то метод возвращает <code>null</code>.
     * @param uri  относительный путь до ресурса.
     * @return описание запрошенного ресурса или <code>null</code> если он отсутствует.
     */
    public Resource getResource(final String uri) {
        Resource resource = resources.get(uri);
        if (resource == null) {
            resource = Resource.getResource(this, uri);
            if (resource != null)
                resources.put(uri, resource);
        }
        return resource;
    }

    /**
     * <p>Возвращает загрузчик классов используемый для загрузки в JVM кода очередного сервлета.</p>
     * <p>Работа данного метода зависит от режима работы (development/production) модуля:
     * <ul>
     *  <li>В случае режима "development" при вызове данного метода будет каждый раз создаваться новый загрузчик классов.
     *  <li>В случае режима "production" при вызове данного метода будет каждый раз отдаваться один определенный загрузчик классов.
     * </ul>
     * В обоих случаях возвращаемый загрузчик классов ориентирован на поиск загружаемых классов в каталоге указанном
     * в конфигурационном параметре {@link Options#rootDstDir}.
     * @return  загрузчик классов используемый для загрузки в JVM кода очередного сервлета.
     */
    public URLClassLoader getWUIClassLoader() {
        if (options.development) {
            return new URLClassLoader(loaderUrls, parentClassLoader);
        } else {
            if (loader==null) {
                loader = new URLClassLoader(loaderUrls, parentClassLoader);
            }
            return loader;
        }
    }

    /**
     * Выполняет компиляцию указанного в аргументах набора .java файлов.
     * @param javaFiles  исходные .java файлы для компиляции.
     * @throws ServletException  в случае возникновения ошибок в процессе компиляции.
     */
    public void compile(final File... javaFiles) throws ServletException {
        final Iterable<String> opts = Arrays.asList("-cp", options.classpath);
        final Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(javaFiles);
        final JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, opts, null, files);
        if (!task.call())
            throw new ServletException("Unable to compile resource");
    }
}
