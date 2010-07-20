package org.echosoft.framework.ui.core.compiler;

import java.util.HashMap;
import java.util.Map;

import org.echosoft.framework.ui.core.compiler.ast.ASTNode;
import org.echosoft.framework.ui.core.compiler.xml.Tag;
import org.echosoft.framework.ui.core.compiler.xml.TagHandler;
import org.echosoft.framework.ui.core.compiler.xml.TagLibrary;
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
    private final Map<String,TagLibrary> usedlibs;
    private ASTNode node;
    private Tag current;

    public XMLContentHandler(final TagLibrarySet taglibs, final ASTNode rootNode) {
        this.definitions = taglibs;
        this.usedlibs = new HashMap<String,TagLibrary>();
        node = rootNode;
    }

    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
        final TagLibrary lib = definitions.getLibrary(uri);
        if (lib==null)
            throw new SAXException("No library found by uri: "+uri);
        usedlibs.put(prefix, lib);
    }

    public void endPrefixMapping(final String prefix) throws SAXException {
        final TagLibrary lib = usedlibs.remove(prefix);
        if (lib==null)
            throw new SAXException("No mapped library found by local name:"+prefix);
    }

    public void startElement(final String uri, final String localName, final String qName, final Attributes attrs) throws SAXException {
        final TagHandler handler = uri.isEmpty() ? null : definitions.getTagHandler(uri,localName);
//        final ASTNode parent = nodes.peek();
//        handler.start(parent, attrs);
    }

    public void endElement(String uri, String localName, String qname) throws SAXException {
        System.err.println("[/elem]: "+uri+",  "+localName+",  "+qname);
        super.endElement(uri, localName, qname);
    }

}
