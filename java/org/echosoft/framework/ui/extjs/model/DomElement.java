package org.echosoft.framework.ui.extjs.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.echosoft.common.json.annotate.JsonUseSeriazer;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.extjs.spi.model.DomElementJSONSerializer;

/**
 * Содержит описание отдельного DOM элемента. Соответствует классу ExtJS <code>Ext.DomHelper</code>.
 * @author Anton Sharapov
 */
@JsonUseSeriazer(DomElementJSONSerializer.class)
public class DomElement implements Serializable {

    private static final String DEFAULT_TAG = "div";

    private String tag;
    private Map<String,String> attrs;
    private String html;

    public DomElement() {
        this.tag = DEFAULT_TAG;
    }
    public DomElement(String tag) {
        this.tag = StringUtil.getNonEmpty(tag, DEFAULT_TAG);
    }

    public DomElement(String tag, String html) {
        this.tag = StringUtil.getNonEmpty(tag, DEFAULT_TAG);
        this.html = StringUtil.trim(html);
    }

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = StringUtil.getNonEmpty(tag, DEFAULT_TAG);
    }

    public Map<String,String> getAttrs() {
        return attrs;
    }
    public String getAttr(String attrName) {
        return attrs!=null ? attrs.get(attrName) : null;
    }
    public void setAttr(String name, String value) {
        if (attrs==null)
            attrs = new HashMap<String,String>();
        attrs.put(name, value);
    }

    public String getHtml() {
        return html;
    }
    public void setHtml(String html) {
        this.html = StringUtil.trim(html);
    }

    /**
     * Возвращает <code>true</code> только при выполнении следующих условий:
     * <ol>
     *  <li> Свойство {@link #getTag()} возвращает  "<code>div</code>" (значение по умолчанию).
     *  <li> Свойство {@link #getAttrs()} возвращает <code>null</code>, т.е. не указан ни один дополнительный атрибут.
     *  <li> Свойство {@link #getHtml()} возвращает значение отличное от <code>null</code>.
     * </ol>
     * В этом случае возможна более краткая форма записи объекта в  JSON формат согласно нотации, принятой в ExtJS.
     * @return <code>true</code> только когда:
     *      имя тега равно <code>div</code> (значение по умолчанию);
     *      не указан ни один атрибут тега;
     *      имеется не пустое содержимое тега.
     */
    public boolean hasContentOnly() {
        return DEFAULT_TAG.equals(tag) && attrs==null && html!=null;
    }
}
