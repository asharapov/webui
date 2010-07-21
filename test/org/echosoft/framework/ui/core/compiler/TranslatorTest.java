package org.echosoft.framework.ui.core.compiler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.mock.MockHttpServletRequest;
import org.echosoft.framework.ui.core.mock.MockHttpServletResponse;
import org.echosoft.framework.ui.core.web.wui.Options;
import org.echosoft.framework.ui.core.web.wui.Resource;
import org.echosoft.framework.ui.core.web.wui.RuntimeContext;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Anton Sharapov
 */
public class TranslatorTest {

    @Test
    public void testTranslate() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Map<String,String> env = new HashMap<String,String>();
        env.put("src-dir", "w:/webui/test");
        env.put("dst-dir", "w:/webui/build");
        env.put("target-package", "");
        env.put("classpath", "");
        env.put("mode", "development");
        final Options opts = new Options(env);
        final RuntimeContext rctx = new RuntimeContext(opts, null);
        final Resource resource = rctx.getResource("/wuifiles/page1.wui");
        resource.service(request, response);
        System.out.println("*** servlet response: ***");
        System.err.println(response.getEnclosingWriter().toString());
    }

    @Test
    public void testParse() throws Exception {
        final File file = new File("w:/webui/test/wuifiles/page1.wui");
        final DefaultHandler handler =
                new DefaultHandler() {
                    Locator locator = null;
                    public void setDocumentLocator(Locator locator) {
                        this.locator = locator;
                        System.err.println("[locator]: "+locator);
                    }
                    public void notationDecl(String name, String publicId, String systemId) throws SAXException {
                        System.err.println("[decl]: "+name+",  "+publicId+",  "+systemId);
                        super.notationDecl(name, publicId, systemId);
                    }
                    public void unparsedEntityDecl(String name,String publicId,String systemId, String notationName) throws SAXException {
                        System.err.println("[unpdecl]: "+name+",  "+publicId+",  "+systemId+",  "+notationName);
                        super.unparsedEntityDecl(name, publicId, systemId, notationName);
                    }
                    public void processingInstruction(String target, String data) throws SAXException {
                        System.err.println("[pi]: "+target+",  "+data);
                        super.processingInstruction(target, data);
                    }
                    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                        System.err.println("[resolve]:  "+publicId+",  "+systemId);
                        return super.resolveEntity(publicId, systemId);
                    }
                    public void skippedEntity(String name) throws SAXException {
                        System.err.println("[skippedEntity: "+name);
                        super.skippedEntity(name);
                    }
                    public void startPrefixMapping(String prefix, String uri) throws SAXException {
                        System.err.println("[prefmap]: "+prefix+",  "+uri);
                        super.startPrefixMapping(prefix, uri);
                    }
                    public void endPrefixMapping(String prefix) throws SAXException {
                        System.err.println("[/prefmap]: "+prefix);
                        super.endPrefixMapping(prefix);
                    }
                    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
                        System.err.println("[elem]: "+uri+",  "+localName+",  "+qName+",  "+attrs.getLength());
                        super.startElement(uri, localName, qName, attrs);
                    }
                    public void endElement(String uri, String localName, String qname) throws SAXException {
                        System.err.println("[/elem]: "+uri+",  "+localName+",  "+qname);
                        super.endElement(uri, localName, qname);
                    }
                    public void characters(char[] ch, int start, int length) {
                        final String text = new String(ch, start, length);
                        if (StringUtil.trim(text)==null)
                            return;
                        System.err.println("[text]: "+text);
                    }
                    public void error(SAXParseException e) throws SAXException {
                        System.err.println("[error]:");
                        e.printStackTrace(System.err);
                    }
                    public void warning(SAXParseException e) throws SAXException {
                        System.err.println("[warn]:");
                        e.printStackTrace(System.err);
                    }
                };

        final SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            factory.setNamespaceAware(true);
            factory.setXIncludeAware(true);
//            factory.setValidating(true);
            final SAXParser saxParser = factory.newSAXParser();
            saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            saxParser.parse(file, handler);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
}
