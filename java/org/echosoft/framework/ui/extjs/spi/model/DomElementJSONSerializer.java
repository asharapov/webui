package org.echosoft.framework.ui.extjs.spi.model;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.echosoft.common.json.JsonSerializer;
import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.extjs.model.DomElement;

/**
 * @author Anton Sharapov
 */
public class DomElementJSONSerializer implements JsonSerializer<DomElement> {

    @Override
    public void serialize(final DomElement src, final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException {
        final Map<String,String> attrs = src.getAttrs();
        final String html = src.getHtml();
        if ( (attrs==null || attrs.isEmpty()) && html==null ) {
            out.writeObject(src.getTag());
        } else {
            out.beginObject();
            out.writeProperty("tag", src.getTag());
            if (attrs!=null)
            for (Map.Entry<String,String> attr : attrs.entrySet()) {
                out.writeProperty(attr.getKey(), attr.getValue());
            }
            if (html!=null)
                out.writeProperty("html", html);
            out.endObject();
        }
    }
}
