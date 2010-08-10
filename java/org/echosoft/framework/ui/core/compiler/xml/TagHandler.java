package org.echosoft.framework.ui.core.compiler.xml;

import org.echosoft.framework.ui.core.compiler.ast.stmt.BlockStmt;
import org.xml.sax.SAXException;

/**
 * Обработчик отдельно взятого тега из исходного .wui файла.
 * @author Anton Sharapov
 */
public interface TagHandler {

    /**
     * Вызывается при обработке открывающего тега.
     * @param tag  описание текущего тега.
     * @return  узел синтаксического дерева, под которым будут создаваться узлы, генерируемые на основе дочених тегов исходного файла.
     * @throws SAXException  в случае каких-либо проблем.
     */
    public BlockStmt doStartTag(Tag tag) throws SAXException;

    /**
     * Вызывается при обработке закрывающего тега.
     * @param tag  описание текущего тега.
     * @throws SAXException  в случае каких-либо проблем.
     */
    public void doEndTag(Tag tag) throws SAXException;

    /**
     * Вызывается при обработке фрагмента текста внутри в теле тега.
     * @param tag  описание текущего тега.
     * @param ch  массив символов.
     * @param start  индекс первого символа в массиве.
     * @param length  кол-во символов.
     * @throws SAXException  в случае каких-либо проблем.
     */
    public void doBodyText(Tag tag, char[] ch, int start, int length) throws SAXException;

}
