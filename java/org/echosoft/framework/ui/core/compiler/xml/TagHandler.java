package org.echosoft.framework.ui.core.compiler.xml;

import org.echosoft.framework.ui.core.compiler.codegen.TranslationContext;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author Anton Sharapov
 */
public interface TagHandler {

    /**
     * Вызывается при обработке открывающего тега.
     * @param ctx  контекст трансляции.
     * @param attrs  информация о всех аттрибутах для данного тега.
     * @throws SAXException  в случае каких-либо проблем.
     */
    public void start(TranslationContext ctx, Attributes attrs) throws SAXException;

    /**
     * Вызывается при обработке закрывающего тега.
     * @param ctx  контекст трансляции.
     * @throws SAXException  в случае каких-либо проблем.
     */
    public void end(TranslationContext ctx) throws SAXException;

    /**
     * Вызывается при обработке фрагмента текста внутри в теле тега.
     * @param ctx  контекст трансляции.
     * @param ch  массив символов.
     * @param start  индекс первого символа в массиве.
     * @param length  кол-во символов.
     * @throws SAXException  в случае каких-либо проблем.
     */
    public void appendText(TranslationContext ctx, char[] ch, int start, int length) throws SAXException;

}
