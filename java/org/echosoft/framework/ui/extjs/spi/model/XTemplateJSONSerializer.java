package org.echosoft.framework.ui.extjs.spi.model;

import java.io.IOException;
import java.io.Writer;

import org.echosoft.common.json.JsonSerializer;
import org.echosoft.common.json.JsonUtil;
import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.extjs.model.XTemplate;

/**
 * @author Anton Sharapov
 */
public class XTemplateJSONSerializer implements JsonSerializer<XTemplate> {

    @Override
    public void serialize(final XTemplate src, final JsonWriter jw) throws IOException {
        final Writer out = jw.getOutputWriter();
        final boolean compiled = src.isCompiled(),
              disableFormats = src.isDisableFormats();
        out.write("new Ext.XTemplate(");
        JsonUtil.encodeString(src.getHtml(), out);
        if (compiled || disableFormats) {
            out.write(",{");
            if (compiled) {
                out.write("compiled:true");
                if (disableFormats)
                    out.write(',');
            }
            if (disableFormats)
                out.write("disableFormats:true");
            out.write('}');
        }
        out.write(')');
    }
}