package org.echosoft.framework.ui.core.compiler;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;

import org.junit.Test;

/**
 * @author Anton Sharapov
 */
public class TranslatorTest {

    private static final File base = new File("w:/webui/web");

    @Test
    public void testTranslate() throws Exception {
        // транслируем ...
        final File file = Translator.translate(base, "/wui/page1.wui");
        // компилируем ...
        final JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
        final StandardJavaFileManager sjfm = jc.getStandardFileManager(null, null, null);
        final Iterable<? extends JavaFileObject> files = sjfm.getJavaFileObjects(file);
        final JavaCompiler.CompilationTask task = jc.getTask(null, sjfm, null, null, null, files);
        boolean result = task.call();
        System.out.println("result = " + result);

        // Add more compilation tasks
        sjfm.close();
    }
}
