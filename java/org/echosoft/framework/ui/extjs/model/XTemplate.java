package org.echosoft.framework.ui.extjs.model;

import org.echosoft.common.json.annotate.JsonUseSeriazer;
import org.echosoft.framework.ui.extjs.spi.model.XTemplateJSONSerializer;

/**
 * Шаблон некоторого фрагмента HTML с расширенными возможностями.
 * @author Anton Sharapov
 */
@JsonUseSeriazer(XTemplateJSONSerializer.class)
public class XTemplate extends Template {

    public XTemplate(final String html) {
        super(html);
    }

    public XTemplate(final String html, final boolean compiled, final boolean disableFormats) {
        super(html, compiled, disableFormats);
    }
}
