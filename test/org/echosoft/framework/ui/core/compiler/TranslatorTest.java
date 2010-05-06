package org.echosoft.framework.ui.core.compiler;

import java.util.HashMap;
import java.util.Map;

import org.echosoft.framework.ui.core.web.wui.Options;
import org.echosoft.framework.ui.core.web.wui.Resource;
import org.echosoft.framework.ui.core.web.wui.RuntimeContext;
import org.junit.Test;

/**
 * @author Anton Sharapov
 */
public class TranslatorTest {

    @Test
    public void testTranslate() throws Exception {
        final Map<String,String> env = new HashMap<String,String>();
        env.put("src-dir", "w:/webui/web");
        env.put("dst-dir", "w:/webui/build");
        env.put("target-package", "wuifiles");
        env.put("classpath", "");
        env.put("mode", "development");
        final Options opts = new Options(env);
        final RuntimeContext rctx = new RuntimeContext(opts, null);
        final Resource resource = rctx.getResource("/wui/page1.wui");
        resource.service(null,null);
    }
}
