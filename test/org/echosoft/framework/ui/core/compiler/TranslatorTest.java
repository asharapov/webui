package org.echosoft.framework.ui.core.compiler;

import java.io.File;

import org.junit.Test;

/**
 * @author Anton Sharapov
 */
public class TranslatorTest {

    private static final File base = new File("w:/web");

    @Test
    public void testTranslate() throws Exception {
        Translator.translate(base, "/wui/page1.wui");
    }
}
