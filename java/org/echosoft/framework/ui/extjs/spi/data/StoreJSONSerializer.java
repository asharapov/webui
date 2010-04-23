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
    public void serialize(final Store src, final JsonWriter jw) throws IOException, InvocationTargetException, IllegalAccessException {
        jw.beginObject();
        jw.writeProperty("xtype", src.getXType());
        jw.writeProperty("storeId", src.getStoreId());
        if (src.isAutoDestroy())
            jw.writeProperty("autoDestroy", true);
        if (src.isAutoLoad())
            jw.writeProperty("autoLoad", src.getAutoLoadOptions()!=null ? src.getAutoLoadOptions() : true);
        if (src.isAutoSave())
            jw.writeProperty("autoSave", true);
        if (!src.isBatch())
            jw.writeProperty("batch", false);
        if (src.isRestful())
            jw.writeProperty("restful", true);
        if (src.isRemoteSort())
            jw.writeProperty("remoteSort", true);
        if (src.getSortInfo()!=null)
            jw.writeProperty("sortInfo", src.getSortInfo());
        if (src.getUrl()!=null)
            jw.writeProperty("url", src.getUrl());
        if (src.getData()!=null)
            jw.writeProperty("data", src.getData());
        if (src.getBaseParams()!=null)
            jw.writeProperty("baseParams", src.getBaseParams());
        if (src.getParamNames()!=null)
            jw.writeProperty("paramNames", src.getParamNames());
        if (src.getReader()!=null)
            jw.writeProperty("reader", src.getReader());
        if (src.getListeners()!=null)
            jw.writeProperty("listeners", src.getListeners());
        jw.endObject();
    }

}
