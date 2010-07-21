package org.echosoft.framework.ui.core.compiler.xml;

import org.echosoft.framework.ui.core.compiler.ast.ASTNode;
import org.xml.sax.SAXException;

/**
 * @author Anton Sharapov
 */
public interface TagHandler {

    /**
     * Вызывается при обработке открывающего тега.
     * @param tag  описание текущего тега.
     * @return  узел синтаксического дерева, под которым будут создаваться узлы, генерируемые на основе дочених тегов исходного файла.
     * @throws SAXException  в случае каких-либо проблем.
     */
    public ASTNode start(Tag tag) throws SAXException;

    /**
     * Вызывается при обработке закрывающего тега.
     * @param tag  описание текущего тега.
     * @throws SAXException  в случае каких-либо проблем.
     */
    public void end(Tag tag) throws SAXException;

    /**
     * Вызывается при обработке фрагмента текста внутри в теле тега.
     * @param tag  описание текущего тега.
     * @param ch  массив символов.
     * @param start  индекс первого символа в массиве.
     * @param length  кол-во символов.
     * @throws SAXException  в случае каких-либо проблем.
     */
    public void appendText(Tag tag, char[] ch, int start, int length) throws SAXException;

}
