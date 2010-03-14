package org.echosoft.framework.ui.extjs.model;

import java.io.Serializable;

import org.echosoft.common.json.annotate.JsonUseSeriazer;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.extjs.spi.model.TemplateJSONSerializer;

/**
 * Описывает простой шаблон некоторого фрагмента HTML.
 * 
 * @author Anton Sharapov
 */
@JsonUseSeriazer(TemplateJSONSerializer.class)
public class Template implements Serializable, Cloneable {

    private String html;
    private boolean compiled;
    private boolean disableFormats;

    public Template(final String html) {
        this.html = html;
    }

    public Template(final String html, final boolean compiled, final boolean disableFormats) {
        this.html = html;
        this.compiled = compiled;
        this.disableFormats = disableFormats;
    }

    /**
     * Возвращает шаблон фрагмента HTML.
     * @return  шаблон фрагмента HTML.
     */
    public String getHtml() {
        return html;
    }
    public void setHtml(final String html) {
        this.html = html;
    }

    /**
     * Возвращает <code>true</code> если данный шаблон фрагмента требуется скомпилировать немедленно при конструировании соответствующего JS объекта.
     * В противном случае компиляция будет отложена на более поздний момент.
     * @return true для немедленной компиляции шаблона. По умолчанию false.
     */
    public boolean isCompiled() {
        return compiled;
    }
    public void setCompiled(boolean compiled) {
        this.compiled = compiled;
    }

    public boolean isDisableFormats() {
        return disableFormats;
    }
    public void setDisableFormats(boolean disableFormats) {
        this.disableFormats = disableFormats;
    }


    @Override
    public int hashCode() {
        return html!=null ? html.hashCode() : 0;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj==null || !getClass().equals(obj.getClass()))
            return false;
        final Template other = (Template)obj;
        return (html!=null ? html.equals(other.html) : other.html==null) &&
                compiled==other.compiled && 
                disableFormats==other.disableFormats;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "["+ StringUtil.extractClass(getClass().getName())+"{html:"+html+"}]";
    }
}
