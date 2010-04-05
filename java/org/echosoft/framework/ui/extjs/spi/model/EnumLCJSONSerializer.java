package org.echosoft.framework.ui.extjs.spi.model;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;

import org.echosoft.common.json.JsonSerializer;
import org.echosoft.common.json.JsonWriter;

/**
 * Сериализатор для всех перечислимых типов чьи имена при трансляции в JSON должны быть
 * форсированно переведены в нижний регистр. 
 * @author Anton Sharapov
 */
public class EnumLCJSONSerializer implements JsonSerializer<Enum> {

    @Override
    public void serialize(final Enum src, final JsonWriter jw) throws IOException, InvocationTargetException, IllegalAccessException {
        final Writer out = jw.getOutputWriter();
        out.write('"');
        out.write( src.name().toLowerCase() );
        out.write('"');
    }

}
