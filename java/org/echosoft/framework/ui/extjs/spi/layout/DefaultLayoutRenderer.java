package org.echosoft.framework.ui.extjs.spi.layout;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.extjs.layout.Layout;
import org.echosoft.framework.ui.extjs.layout.LayoutItem;

/**
 * @author Anton Sharapov
 */
public class DefaultLayoutRenderer implements LayoutRenderer {

    /**
     * <p>
     * Формирует фрагмент JSON следующего вида (в общем случае):
     * <pre>
     *   layout: {type:"auto", extraCls:null, renderHidden:false}
     * </pre>
     * </p>
     * {@inheritDoc}
     */
    @Override
    public void renderConfig(final Layout layout, final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException {
        final String ecl = layout.getExtraCls();
        final boolean rh = layout.isRenderHidden();
        if (ecl==null && !rh) {
            out.writeProperty("layout", "auto");
        } else {
            out.writeComplexProperty("layout");
            out.beginObject();
            out.writeProperty("type", "auto");
            if (ecl!=null)
                out.writeProperty("extraCls", ecl);
            if (rh)
                out.writeProperty("renderHidden", true);
            out.endObject();
        }
    }

    @Override
    public void renderItem(final LayoutItem item, final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException {
        //TODO: dont forget that chunk...
    }

}
