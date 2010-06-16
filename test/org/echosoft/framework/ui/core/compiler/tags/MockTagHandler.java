package org.echosoft.framework.ui.core.compiler.tags;

import org.echosoft.framework.ui.core.compiler.codegen.TranslationContext;
import org.echosoft.framework.ui.core.compiler.xml.TagHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author Anton Sharapov
 */
public class MockTagHandler implements TagHandler {

    @Override
    public void start(TranslationContext ctx, Attributes attrs) throws SAXException {
    }

    @Override
    public void end(TranslationContext ctx) throws SAXException {
    }

    @Override
    public void appendText(TranslationContext ctx, char[] ch, int start, int length) throws SAXException {
    }

}
