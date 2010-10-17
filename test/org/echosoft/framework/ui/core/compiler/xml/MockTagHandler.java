package org.echosoft.framework.ui.core.compiler.xml;

import org.xml.sax.SAXException;

/**
 * @author Anton Sharapov
 */
public class MockTagHandler implements TagHandler {

    @Override
    public void doStartTag(Tag tag) throws SAXException {
    }

    @Override
    public void doEndTag(Tag tag) throws SAXException {
    }

    @Override
    public void doBodyText(Tag tag, char[] ch, int start, int length) throws SAXException {
    }

}
