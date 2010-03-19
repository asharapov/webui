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

    private String tag;
    private Map<String,String> attrs;
    private String html;

    public DomElement(final String tag) {
        this.tag = StringUtil.getNonEmpty(tag, "div");
    }

    public DomElement(final String tag, final String html) {
        this.tag = StringUtil.getNonEmpty(tag, "div");
        this.html = StringUtil.trim(html);
    }

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = StringUtil.getNonEmpty(tag, "div");
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
}
