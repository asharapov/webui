package org.echosoft.framework.ui.core.compiler;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.echosoft.framework.ui.core.Application;

/**
 * @author Anton Sharapov
 */
public class Utils {

    /**
     * На основании информации предоставляемой текущим загрузчиком классов восстанавливаем значение переменной CLASSPATH.
     * @param sctx  контекст сервлета
     * @return строка состоящая из путей ко всем .jar файлам которые доступны в рантайме нашего java процесса. 
     */
    public static String resolveClassPath(final ServletContext sctx) {
        final Set<String> paths = new LinkedHashSet<String>();
        for (ClassLoader loader=Thread.currentThread().getContextClassLoader(); loader!=null; loader=loader.getParent()) {
            if (loader instanceof URLClassLoader) {
                for (URL url : ((URLClassLoader)loader).getURLs()) {
                    final String repository = url.toString();
                    if (repository.startsWith("file://")) {
                        paths.add( repository.substring(7) );
                    } else
                    if (repository.startsWith("file:")) {
                        paths.add( repository.substring(5) );
                    } else
                    if (repository.startsWith("jndi:") && sctx!=null) {
                        paths.add( sctx.getRealPath(repository.substring(5)) );
                    } else
                        Application.log.warn("skipped unknown repository format: "+repository);
                }
            } else {
                Application.log.warn("Unresolved class loader: "+loader);
                // у некоторых таких непонятных загрузчиков есть метод getClasspath попробуем до него добраться ...
                try {
                    final Method method = loader.getClass().getMethod("getClasspath", new Class[]{});
                    if (method!=null) {
                        final Object obj = method.invoke(loader);
                        if (obj instanceof String) {
                            paths.add( (String)obj );
                        }
                    }
                } catch (Exception e) {/**/}
            }
        }
        final StringBuilder out = new StringBuilder(256);
        for (String path : paths) {
            if (out.length()>0)
                out.append(File.pathSeparator);
            out.append(path);
        }
        return out.toString();
    }

    public static Set<URL> search(final String prefix, final String suffix) throws IOException {
        final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        final Set<URL> result = new HashSet<URL>();
        for (Enumeration<URL> e = cl.getResources(prefix); e.hasMoreElements(); )  {
            final URL url = e.nextElement();
            System.out.println(url);
        }
        return result;
    }

    public static void main(String args[]) throws Exception {
        Set<URL> set = search("META-INF/", ".taglib.xml");

    }
}
