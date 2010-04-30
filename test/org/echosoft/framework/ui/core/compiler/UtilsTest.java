package org.echosoft.framework.ui.core.compiler;

import java.io.File;

import org.echosoft.common.utils.StringUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Anton Sharapov
 */
public class UtilsTest {

    @Test
    public void testGetPackageNameByPath() throws Exception {
        final Object[][] cases = new Object[][]{
                {null,""},
                {"",""},
                {"/",""},
                {"a", ""},
                {"/a", ""},
                {"a.wui", ""},
                {"/a.wui", ""},
                {"a/b.wui", "a"},
                {"/a/b.wui", "a"},
                {"a/b/c.wui", "a.b"},
                {"/a/b/c.wui", "a.b"}
        };
        for (Object[] test : cases) {
            final String src = StringUtil.replace( (String)test[0], "/", File.separator);
            final String result = Utils.getPackageNameByPath( src );
            Assert.assertEquals(test[1], result);
        }
    }
}
