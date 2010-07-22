package org.echosoft.framework.ui.core.compiler;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.echosoft.framework.ui.core.compiler.ast.ASTNode;
import org.echosoft.framework.ui.core.compiler.xml.Tag;
import org.echosoft.framework.ui.core.compiler.xml.TagHandler;
import org.echosoft.framework.ui.core.compiler.xml.TagLibrarySet;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Отвечает за потоковый разбор xml документов.
 * @author Anton Sharapov
 */
public class XMLContentHandler extends DefaultHandler {

    private final TagLibrarySet definitions;
    private final Map<String, Deque<String>> aliasses;  // mapping:  alias -> stack of uri
    private ASTNode rootNode;
    private Tag current;
    private Locator locator;

    public XMLContentHandler(final TagLibrarySet taglibs, final ASTNode rootNode) {
        this.definitions = taglibs;
        this.aliasses = new HashMap<String, Deque<String>>();
        this.rootNode = rootNode;
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }
    @Override
    public void startPrefixMapping(final String alias, final String uri) throws SAXException {
        Deque<String> stack = aliasses.get(alias);
        if (stack==null) {
            stack = new LinkedList<String>();
            aliasses.put(alias, stack);
        }
        stack.push(uri);
        if (!definitions.containsLibrary(uri))
            throw new SAXException("No library found by uri: "+uri+"  at line "+locator.getLineNumber());
    }

    @Override
    public void endPrefixMapping(final String alias) throws SAXException {
        final Deque<String> stack = aliasses.get(alias);
        if (stack==null || stack.isEmpty()) {
            throw new SAXException("No mapped library found by alias: "+alias+"  at line "+locator.getLineNumber());
        } else {
            stack.pop();
        }
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attrs) throws SAXException {
        final TagHandler handler = definitions.getTagHandler(uri,localName);
        if (handler==null) {
            // если не были указаны обработчики тегов по умолчанию, то мы ничего более не в состоянии сделать...
            throw new SAXException("Unknown tag: "+qName);
        }
        current = current==null
                ? new Tag(rootNode, uri, qName, localName, attrs, handler)
                : new Tag(current, uri, qName, localName, attrs, handler);
        final ASTNode node = current.handler.doStartTag(current);
        current.childrenContainer = node!=null ? node : current.container;
    }

    @Override
    public void endElement(String uri, String localName, String qname) throws SAXException {
        current.handler.doEndTag(current);
        current = current.parent;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        current.handler.doBodyText(current, ch, start, length);
    }
}
