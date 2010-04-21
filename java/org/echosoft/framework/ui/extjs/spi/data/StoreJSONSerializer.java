package org.echosoft.framework.ui.extjs.spi.data;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.echosoft.common.json.JsonSerializer;
import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.extjs.data.Store;

/**
 * @author Anton Sharapov
 */
public class StoreJSONSerializer implements JsonSerializer<Store> {

    @Override
    public void serialize(final Store src, final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException {
        out.beginObject();
        out.writeProperty("xtype", src.getXType());
        out.writeProperty("storeId", src.getStoreId());
        if (src.isAutoDestroy())
            out.writeProperty("autoDestroy", true);
        if (src.isAutoLoad())
            out.writeProperty("autoLoad", src.getAutoLoadOptions()!=null ? src.getAutoLoadOptions() : true);
        if (src.isAutoSave())
            out.writeProperty("autoSave", true);
        if (!src.isBatch())
            out.writeProperty("batch", false);
        if (src.isRestful())
            out.writeProperty("restful", true);
        if (src.isRemoteSort())
            out.writeProperty("remoteSort", true);
        if (src.getSortInfo()!=null)
            out.writeProperty("sortInfo", src.getSortInfo());
        if (src.getUrl()!=null)
            out.writeProperty("url", src.getUrl());
        if (src.getData()!=null)
            out.writeProperty("data", src.getData());
        if (src.getBaseParams()!=null)
            out.writeProperty("baseParams", src.getBaseParams());
        if (src.getParamNames()!=null)
            out.writeProperty("paramNames", src.getParamNames());
        if (src.getReader()!=null)
            out.writeProperty("reader", src.getReader());
        if (src.getListeners()!=null)
            out.writeProperty("listeners", src.getListeners());
        out.endObject();
    }

}
