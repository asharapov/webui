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

package org.echosoft.framework.ui.core.compiler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Содержит ряд методов, используемых для поиска требуемых ресурсов  
 * @author Jacob Hookom
 * @author Roland Huss
 * @author Ales Justin (ales.justin@jboss.org)
 * @version $Id: Classpath.java,v 1.11 2009/02/02 23:04:45 rlubke Exp $
 */
public final class ClasspathUtils {

	public static Set<URL> search(final String prefix, final String suffix) throws IOException {
        return search(Thread.currentThread().getContextClassLoader(), prefix, suffix);
	}

	public static Set<URL> search(ClassLoader cl, String prefix, String suffix) throws IOException {
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
	 * Search from URL. Fall back on prefix tokens if not able to read from
	 * original url param.
	 * 
	 * @param result the result urls
	 * @param prefix the current prefix
	 * @param suffix the suffix to match
	 * @param url the current url to start search
	 * @throws IOException for any error
	 */
	private static void searchFromURL(final Set<URL> result, String prefix, final String suffix, URL url) throws IOException {
		boolean done = false;
		InputStream is = getInputStream(url);
		if (is != null) {
			try {
				final ZipInputStream zis = (is instanceof ZipInputStream) ? (ZipInputStream)is : new ZipInputStream(is);
				try {
					ZipEntry entry = zis.getNextEntry();
					// initial entry should not be null
					// if we assume this is some inner jar
					done = (entry != null);
					while (entry != null) {
						String entryName = entry.getName();
						if (entryName.endsWith(suffix)) {
							String urlString = url.toExternalForm();
							result.add(new URL(urlString + entryName));
						}
						entry = zis.getNextEntry();
					}
				} finally {
					zis.close();
				}
			} catch (Exception ignore) {
			}
		}
		if (!done && prefix.length() > 0) {
			// we add '/' at the end since join adds it as well
			String urlString = url.toExternalForm() + "/";
			String[] split = prefix.split("/");
			prefix = join(split, true);
			String end = join(split, false);
			int p = urlString.lastIndexOf(end);
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
	 * Open input stream from url. Ignore any errors.
	 * 
	 * @param url the url to open
	 * @return input stream or null if not possible
	 */
	private static InputStream getInputStream(URL url) {
		try {
			return url.openStream();
		} catch (Throwable t) {
			return null;
		}
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
