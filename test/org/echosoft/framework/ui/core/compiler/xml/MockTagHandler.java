package org.echosoft.framework.ui.core.compiler.xml;

import org.echosoft.framework.ui.core.compiler.ast.stmt.BlockStmt;
import org.xml.sax.SAXException;

/**
 * @author Anton Sharapov
 */
public class MockTagHandler implements TagHandler {

    @Override
    public BlockStmt doStartTag(Tag tag) throws SAXException {
        return null;
    }

    @Override
    public void doEndTag(Tag tag) throws SAXException {
    }

    @Override
    public void doBodyText(Tag tag, char[] ch, int start, int length) throws SAXException {
    }

}
