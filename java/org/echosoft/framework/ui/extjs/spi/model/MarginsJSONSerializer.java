package org.echosoft.framework.ui.extjs.spi.model;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.echosoft.common.json.JsonSerializer;
import org.echosoft.common.json.JsonUtil;
import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.extjs.model.Margins;

/**
 * @author Anton Sharapov
 */
public class MarginsJSONSerializer implements JsonSerializer<Margins> {

    @Override
    public void serialize(final Margins src, final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException {
        JsonUtil.encodeString(src.encode(), out.getOutputWriter());
    }
}
