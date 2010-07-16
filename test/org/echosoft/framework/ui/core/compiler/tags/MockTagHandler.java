package org.echosoft.framework.ui.core.compiler.tags;

import org.echosoft.framework.ui.core.compiler.ast.ASTNode;
import org.echosoft.framework.ui.core.compiler.xml.TagHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author Anton Sharapov
 */
public class MockTagHandler implements TagHandler {

    @Override
    public void start(ASTNode parent, Attributes attrs) throws SAXException {
    }

    @Override
    public void end(ASTNode parent) throws SAXException {
    }

    @Override
    public void appendText(ASTNode parent, char[] ch, int start, int length) throws SAXException {
    }

}
