package org.echosoft.framework.ui.extjs.spi.data;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.echosoft.common.json.JsonSerializer;
import org.echosoft.common.json.JsonUtil;
import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.extjs.data.DataField;

/**
 * @author Anton Sharapov
 */
public class DataFieldJsonSerializer implements JsonSerializer<DataField> {

    @Override
    public void serialize(final DataField src, final JsonWriter jw) throws IOException, InvocationTargetException, IllegalAccessException {
        if (src.hasShortForm()) {
            JsonUtil.encodeString(src.getName(), jw.getOutputWriter());
        } else {
            jw.beginObject();
            jw.writeProperty("name", src.getName());
            Object o;
            if ((o=src.getType())!=null)
                jw.writeProperty("type", o);
            if ((o=src.getMapping())!=null)
                jw.writeProperty("mapping", o);
            if ((o=src.getDefaultValue())!=null)
                jw.writeProperty("defaultValue", o);
            if ((o=src.getConvert())!=null)
                jw.writeProperty("convert", o);
            if ((o=src.getDateFormat())!=null)
                jw.writeProperty("dateFormat", o);
            if ((o=src.getSortDir())!=null)
                jw.writeProperty("sortDir", o);
            if ((o=src.getSortType())!=null)
                jw.writeProperty("sortType", o);
            if (src.isAllowBlank())
                jw.writeProperty("allowBlank", true);
            jw.endObject();
        }
    }

}
