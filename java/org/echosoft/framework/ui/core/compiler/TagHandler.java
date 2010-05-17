package org.echosoft.framework.ui.core.compiler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author Anton Sharapov
 */
public interface TagHandler {

    /**
     * Вызывается при обработке открывающего тега.
     * @param uri  URI определяющее пространство имен для данного тега или пустая строка.
     * @param name имя тега в указанном пространстве имен
     * @param attrs  информация о всех аттрибутах для данного тега.
     * @param ctx  контекст трансляции.
     * @throws SAXException  в случае каких-либо проблем.
     */
    public void open(String uri, String name, Attributes attrs, TranslationContext ctx) throws SAXException;

    /**
     * Вызывается при обработке закрывающего тега.
     * @param uri  URI определяющее пространство имен для данного тега или пустая строка.
     * @param name имя тега в указанном пространстве имен
     * @param ctx  контекст трансляции.
     * @throws SAXException  в случае каких-либо проблем.
     */
    public void close(String uri, String name, TranslationContext ctx) throws SAXException;

    /**
     * Вызывается при обработке фрагмента текста внутри в теле тега.
     * @param ch  массив символов.
     * @param start  индекс первого символа в массиве.
     * @param length  кол-во символов.
     * @param ctx  контекст трансляции.
     * @throws SAXException  в случае каких-либо проблем.
     */
    public void appendText(char[] ch, int start, int length, TranslationContext ctx) throws SAXException;

}
