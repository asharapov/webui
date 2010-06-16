/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.echosoft.framework.ui.core.compiler.utils;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.echosoft.framework.ui.core.Application;

/**
 * Содержит методы для работы с ресурсами, доступными загрузчикам классов в JVM.
 * В основе данного класса лежат фрагменты кода, заимствованного из проектов:
 * <ul>
 *  <li> <a href="http://tomcat.apache.org">apache tomcat</a>
 *  <li> <a href="https://facelets.dev.java.net">JSF facelets</a>
 * </ul>
 * @author Jacob Hookom
 * @author Roland Huss
 * @author Ales Justin (ales.justin@jboss.org)
 */
public final class ClasspathUtils {

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

    /**
     * Возвращает ссылки на ресурсы, доступные текущему загрузчику, URL которых начинается и заканчивается
     * определенными строковыми значениями.
     * @param prefix  строка, с которой начинается путь до ресурса (в jar файле).
     * @param suffix  строка, которой заканчивается путь до ресурса.
     * @return  множество ссылок на ресурсы, доступных в JVM, удовлетворяющих указанным в аргументах метода ограничениям.
     * @throws IOException  в случае каких-либо проблем.
     */
	public static Set<URL> search(final String prefix, final String suffix) throws IOException {
        return search(Thread.currentThread().getContextClassLoader(), prefix, suffix);
	}

    /**
     * Возвращает ссылки на ресурсы, доступные указанному загрузчику, URL которых начинается и заканчивается
     * определенными строковыми значениями.
     * @param cl  загрузчик классов, используемый для поиска ресурсов.
     * @param prefix  строка, с которой начинается путь до ресурса (в jar файле).
     * @param suffix  строка, которой заканчивается путь до ресурса.
     * @return  множество ссылок на ресурсы, доступных в JVM, удовлетворяющих указанным в аргументах метода ограничениям.
     * @throws IOException  в случае каких-либо проблем.
     */
	public static Set<URL> search(final ClassLoader cl, final String prefix, final String suffix) throws IOException {
		final Set<URL> result = new LinkedHashSet<URL>();
        for (Enumeration<URL> e = cl.getResources(prefix); e.hasMoreElements(); ) {
            final URL url = e.nextElement();
            final URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);
            final JarFile jarFile;
            if (conn instanceof JarURLConnection) {
                jarFile = ((JarURLConnection) conn).getJarFile();
            } else {
                jarFile = getAlternativeJarFile(url);
            }
            if (jarFile != null) {
                searchJar(cl, result, jarFile, prefix, suffix);
            } else {
                boolean searchDone = searchDir(result, new File(URLDecoder.decode(url.getFile(), "UTF-8")), suffix);
                if (!searchDone) {
                    searchFromURL(result, prefix, suffix, url);
                }
            }
        }
		return result;
	}


    private static boolean searchDir(final Set<URL> result, final File file, final String suffix) throws IOException {
        if (!file.isDirectory())
            return false;
        for (File fc : file.listFiles()) {
            final String path = fc.getAbsolutePath();
            if (fc.isDirectory()) {
                searchDir(result, fc, suffix);
            } else
            if (path.endsWith(suffix)) {
                result.add( fc.toURI().toURL() );
            }
        }
        return true;
	}

    /**
	 * Search from URL. Fall back on prefix tokens if not able to read from original url param.
	 * @param result the result urls
	 * @param prefix the current prefix
	 * @param suffix the suffix to match
	 * @param url the current url to start search
	 * @throws IOException for any error
	 */
	private static void searchFromURL(final Set<URL> result, String prefix, final String suffix, URL url) throws IOException {
		boolean done = false;
        try {
            final InputStream is = url.openStream();
            if (is!=null) {
                final ZipInputStream zis = (is instanceof ZipInputStream) ? (ZipInputStream)is : new ZipInputStream(is);
                try {
                    ZipEntry entry = zis.getNextEntry();
                    // initial entry should not be null
                    // if we assume this is some inner jar
                    done = (entry != null);
                    while (entry != null) {
                        final String entryName = entry.getName();
                        if (entryName.endsWith(suffix)) {
                            final String urlString = url.toExternalForm();
                            result.add(new URL(urlString + entryName));
                        }
                        entry = zis.getNextEntry();
                    }
                } finally {
                    zis.close();
                }
            }
        } catch (Exception e) { /* ignore exception */ }
		if (!done && prefix.length() > 0) {
			// we add '/' at the end since join adds it as well
			final String urlString = url.toExternalForm() + "/";
			final String[] split = prefix.split("/");
			prefix = join(split, true);
			final String end = join(split, false);
			final int p = urlString.lastIndexOf(end);
			url = new URL(urlString.substring(0, p));
			searchFromURL(result, prefix, suffix, url);
		}
	}

	private static String join(final String[] tokens, final boolean excludeLast) {
		final StringBuilder join = new StringBuilder();
        final int length = tokens.length - (excludeLast ? 1 : 0);
		for (int i = 0; i < length; i++)
			join.append(tokens[i]).append('/');
		return join.toString();
	}

    /**
	 * For URLs to JARs that do not use JarURLConnection - allowed by the
	 * servlet spec - attempt to produce a JarFile object all the same. Known
	 * servlet engines that function like this include Weblogic and OC4J. This
	 * is not a full solution, since an unpacked WAR or EAR will not have JAR
	 * "files" as such.
     * @param url   URL to resource in jar file.
     * @return a JarFile instance.
     * @throws IOException if any error occurs
	 */
	private static JarFile getAlternativeJarFile(final URL url) throws IOException {
		final String urlFile = url.getFile();
		// Trim off any suffix - which is prefixed by "!/" on Weblogic
		int separatorIndex = urlFile.indexOf("!/");

		// OK, didn't find that. Try the less safe "!", used on OC4J
		if (separatorIndex == -1) {
			separatorIndex = urlFile.indexOf('!');
		}

		if (separatorIndex != -1) {
			String jarFileUrl = urlFile.substring(0, separatorIndex);
			// And trim off any "file:" prefix.
			if (jarFileUrl.startsWith("file:")) {
				jarFileUrl = jarFileUrl.substring("file:".length());
                                jarFileUrl = URLDecoder.decode(jarFileUrl, "UTF-8");
			}
			return new JarFile(jarFileUrl);
		}
		return null;
	}

    private static void searchJar(final ClassLoader cl, final Set<URL> result, final JarFile file, final String prefix, final String suffix) throws IOException {
        for (Enumeration<JarEntry> e = file.entries(); e.hasMoreElements(); ) {
            final JarEntry entry;
            try {
                entry = e.nextElement();
            } catch (Throwable t) {
                continue;
            }
            final String name = entry.getName();
            if (name.startsWith(prefix) && name.endsWith(suffix)) {
                for (Enumeration<URL> e2 = cl.getResources(name); e2.hasMoreElements(); ) {
                    result.add( e2.nextElement() );
                }
            }
        }
	}

}
