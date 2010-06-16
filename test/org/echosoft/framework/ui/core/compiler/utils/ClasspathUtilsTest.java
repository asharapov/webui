package org.echosoft.framework.ui.core.compiler.utils;

import java.net.URL;
import java.util.Set;

import org.echosoft.framework.ui.core.compiler.xml.TagLibrarySet;
import org.junit.Test;

/**
 * @author Anton Sharapov
 */
public class ClasspathUtilsTest {

    @Test
    public void testResolveClassPath() throws Exception {
        System.out.println( ClasspathUtils.resolveClassPath(null) );
    }

    @Test
    public void testSearch() throws Exception {
        Set<URL> set = ClasspathUtils.search("META-INF/", ".taglib.xml");
        for (URL url : set) {
            System.out.println(url);
        }
    }


    @Test
    public void testTLS() throws Exception {
        TagLibrarySet set = TagLibrarySet.findLibariesInClasspath();
        System.out.println(set);
    }
}
