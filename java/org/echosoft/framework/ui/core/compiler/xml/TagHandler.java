package org.echosoft.framework.ui.core.compiler.xml;

import org.echosoft.framework.ui.core.compiler.ast.ASTNode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author Anton Sharapov
 */
public interface TagHandler {

    /**
     * Вызывается при обработке открывающего тега.
     * @param parent  контекст трансляции.
     * @param attrs  информация о всех аттрибутах для данного тега.
     * @throws SAXException  в случае каких-либо проблем.
     */
    public void start(ASTNode parent, Attributes attrs) throws SAXException;

    /**
     * Вызывается при обработке закрывающего тега.
     * @param parent  контекст трансляции.
     * @throws SAXException  в случае каких-либо проблем.
     */
    public void end(ASTNode parent) throws SAXException;

    /**
     * Вызывается при обработке фрагмента текста внутри в теле тега.
     * @param parent  контекст трансляции.
     * @param ch  массив символов.
     * @param start  индекс первого символа в массиве.
     * @param length  кол-во символов.
     * @throws SAXException  в случае каких-либо проблем.
     */
    public void appendText(ASTNode parent, char[] ch, int start, int length) throws SAXException;

}
