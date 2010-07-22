package org.echosoft.framework.ui.core.compiler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.echosoft.common.io.FastStringWriter;
import org.echosoft.framework.ui.core.compiler.ast.ASTNode;
import org.echosoft.framework.ui.core.compiler.ast.FileNode;
import org.echosoft.framework.ui.core.compiler.xml.Tag;
import org.echosoft.framework.ui.core.compiler.xml.TagHandler;
import org.echosoft.framework.ui.core.compiler.xml.TagLibrarySet;
import org.echosoft.framework.ui.core.web.wui.Options;
import org.xml.sax.SAXException;

/**
 * Отвечает за трансляцию .wui файлов в соответствуюшие .java сервлеты.
 * @author Anton Sharapov
 */
public class Translator {

    private static final TagHandler DEFAULT_HTML_TAG_HANDLER = new TagHandler() {
        @Override
        public ASTNode doStartTag(final Tag tag) throws SAXException {
            final FastStringWriter buf = new FastStringWriter(64);
            buf.write('<');
            buf.write(tag.qname);
            for (int i=0,l=tag.attrs.getLength(); i<l; i++) {
                buf.write(' ');
                buf.write(tag.attrs.getQName(i));
                buf.write("=\"");
                buf.write(tag.attrs.getValue(i));
                buf.write('\"');
            }
            buf.write('>');
            final char[] c = buf.toString().toCharArray();
            tag.parent.handler.doBodyText(tag.parent, c, 0, c.length);
            return null;
        }
        @Override
        public void doEndTag(final Tag tag) throws SAXException {
            final FastStringWriter buf = new FastStringWriter(10);
            buf.write("</");
            buf.write(tag.qname);
            buf.write('>');
            final char[] c = buf.toString().toCharArray();
            tag.parent.handler.doBodyText(tag.parent, c, 0, c.length);
        }
        @Override
        public void doBodyText(final Tag tag, final char[] ch, final int start, final int length) throws SAXException {
            tag.parent.handler.doBodyText(tag.parent, ch, start, length);
        }
    };
    private static TagLibrarySet instance;
    private static TagLibrarySet getTagLibraries() throws IOException {
        if (instance==null) {
            final TagLibrarySet set = TagLibrarySet.findLibariesInClasspath();
            set.ensureLibraryExists("http://www.w3.org/1999/xhtml")
                    .setDefaultHandler(DEFAULT_HTML_TAG_HANDLER);
            set.ensureLibraryExists("http://www.w3.org/2001/XMLSchema-instance");
            set.ensureLibraryExists("http://www.w3.org/2001/XInclude");
            instance = set;
        }
        return instance;
    }

    /**
     * Выполняет трансляцию .wui Файла в соответствующий ему .java файл.
     * @param uri  относительный путь до данного ресурса на сервере. Декодируется в имя класса и пакет java.
     * @param options  конфигурационные параметры.
     * @return  путь к сгенерированному .java классу.
     * @throws IOException  в случае каких-либо проблем с сохранением генерируемого файла на диск.
     */
    public static File translate(final String uri, final Options options) throws IOException {
        final File srcFile = new File(options.rootSrcDir, uri);
        final TagLibrarySet taglibs = Translator.getTagLibraries();
        final FileNode rootNode = new FileNode(uri, options);
        final XMLContentHandler handler = new XMLContentHandler(taglibs, rootNode.getServletService());

        final SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            factory.setNamespaceAware(true);
            factory.setXIncludeAware(true);
//            factory.setValidating(true);
            final SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(srcFile, handler);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        final File file = rootNode.getDstFile();
        final FileWriter out = new FileWriter( file );
        try {
            rootNode.translate(out);
            out.flush();
        } finally {
            out.close();
        }
        return file;
    }
}
