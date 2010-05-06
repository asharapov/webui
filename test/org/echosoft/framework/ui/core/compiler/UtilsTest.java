package org.echosoft.framework.ui.core.compiler;

import org.junit.Test;

/**
 * @author Anton Sharapov
 */
public class UtilsTest {

    @Test
    public void testResolveClassPath() throws Exception {
        System.out.println( Utils.resolveClassPath(null) );
    }
}
