package org.echosoft.framework.ui.core.compiler;

import java.util.HashMap;
import java.util.Map;

import org.echosoft.framework.ui.core.compiler.ast.ASTNode;
import org.echosoft.framework.ui.core.compiler.xml.Tag;
import org.echosoft.framework.ui.core.compiler.xml.TagHandler;
import org.echosoft.framework.ui.core.compiler.xml.TagLibrarySet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Отвечает за потоковый разбор xml документов.
 * @author Anton Sharapov
 */
public class XMLContentHandler extends DefaultHandler {

    private final TagLibrarySet definitions;
    private final Map<String,String> aliasses;  // mapping:  prefix ->  uri
    private String defaultURI;                  // если uri="" то будет использоваться значение этого поля.
    private ASTNode rootNode;
    private Tag current;

    public XMLContentHandler(final TagLibrarySet taglibs, final ASTNode rootNode) {
        this.definitions = taglibs;
        this.aliasses = new HashMap<String,String>();
        this.defaultURI = "";
        this.rootNode = rootNode;
    }

    @Override
    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
        if (!definitions.containsLibrary(uri))
            throw new SAXException("No library found by uri: "+uri);
        aliasses.put(prefix, uri);
    }

    @Override
    public void endPrefixMapping(final String prefix) throws SAXException {
        final String uri = aliasses.remove(prefix);
        if (uri==null)
            throw new SAXException("No mapped library found by local name:"+prefix);
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attrs) throws SAXException {
        final String effectiveURI = uri.isEmpty() ? defaultURI : uri;
        final TagHandler handler = definitions.getTagHandler(uri,localName);
        if (handler==null) {
            // если не были указаны обработчики тегов по умолчанию, то мы ничего более не в состоянии сделать...
            throw new SAXException("Unknown tag: "+qName);
        }
        current = current==null
                ? new Tag(rootNode, effectiveURI, qName, localName, attrs, handler)
                : new Tag(current, effectiveURI, qName, localName, attrs, handler);
        final ASTNode node = current.handler.start(current);
        current.childrenContainer = node!=null ? node : current.container;
    }

    @Override
    public void endElement(String uri, String localName, String qname) throws SAXException {
        current.handler.end(current);
        current = current.parent;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        current.handler.appendText(current, ch, start, length);
    }
}
