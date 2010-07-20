package org.echosoft.framework.ui.core.compiler.tags;

import org.echosoft.framework.ui.core.compiler.ast.ASTNode;
import org.echosoft.framework.ui.core.compiler.xml.Tag;
import org.echosoft.framework.ui.core.compiler.xml.TagHandler;
import org.xml.sax.SAXException;

/**
 * @author Anton Sharapov
 */
public class MockTagHandler implements TagHandler {

    @Override
    public ASTNode start(Tag tag) throws SAXException {
        return null;
    }

    @Override
    public void end(Tag tag) throws SAXException {
    }

    @Override
    public void appendText(Tag tag, char[] ch, int start, int length) throws SAXException {
    }

}
