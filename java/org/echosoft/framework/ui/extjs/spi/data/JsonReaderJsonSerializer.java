package org.echosoft.framework.ui.extjs.spi.data;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;

import org.echosoft.common.json.JsonSerializer;
import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.extjs.data.JsonReader;

/**
 * @author Anton Sharapov
 */
public class JsonReaderJsonSerializer implements JsonSerializer<JsonReader> {

    @Override
    public void serialize(final JsonReader src, final JsonWriter jw) throws IOException, InvocationTargetException, IllegalAccessException {
        final Writer out = jw.getOutputWriter();
        out.write("new Ext.data.JsonReader(");
        jw.beginObject();
        jw.writeProperty("fields", src.getFields());
        Object o;
        if ((o=src.getMessageProperty())!=null)
            jw.writeProperty("messageProperty", o);
        if ((o=src.getRoot())!=null)
            jw.writeProperty("root", o);
        if ((o=src.getSuccessProperty())!=null)
            jw.writeProperty("successProperty", o);
        if ((o=src.getTotalProperty())!=null)
            jw.writeProperty("totalProperty", o);
        if ((o=src.getIdProperty())!=null)
            jw.writeProperty("idProperty", o);
        jw.endObject();
        out.write(')');
    }

}
