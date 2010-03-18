package org.echosoft.framework.ui.core.spi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.echosoft.common.utils.Base64Util;
import org.echosoft.framework.ui.core.StateHolder;
import org.echosoft.framework.ui.core.UIContext;
import org.echosoft.framework.ui.core.ViewStateDescriptor;

/**
 * @author Anton Sharapov
 */
public class GZIPStateSerializer implements StateSerializer {

    /**
     * {@inheritDoc}
     */
    public String encodeState(final UIContext uctx) throws Exception {
        final StateHolder states = uctx.getStates();
        final ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
        final GZIPOutputStream zout = new GZIPOutputStream(bout);
        final ObjectOutputStream out = new ObjectOutputStream(zout);
        try {
            out.writeInt(states.getViewStates().size());
            for (ViewStateDescriptor desc : states.getViewStates()) {
                final Map<String,Object> state = states.getViewState(desc);
                out.writeObject(desc);
                out.writeObject(state);
            }
            out.writeObject(states.getCurrentDescriptor());
        } finally {
            out.close();
            zout.close();
        }
        return Base64Util.encode(bout.toByteArray());
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void decodeState(final UIContext uctx, final String encodedState) throws IOException, ClassNotFoundException {
        if (encodedState==null || encodedState.length()==0)
            return;
        final StateHolder states = uctx.getStates();
        final byte[] decodedData = Base64Util.decode(encodedState);
        final ByteArrayInputStream bis = new ByteArrayInputStream(decodedData);
        final GZIPInputStream zis = new GZIPInputStream(bis);
        final ObjectInputStream ois = new ObjectInputStream(zis);
        try {
            final int cnt = ois.readInt();
            for (int i=0; i<cnt; i++) {
                final ViewStateDescriptor desc = (ViewStateDescriptor)ois.readObject();
                final Map<String,Object> state = states.ensureStateExists(desc);
                state.putAll( (Map<String,Object>)ois.readObject() );
            }
            states.setCurrentDescriptor( (ViewStateDescriptor)ois.readObject() );
        } finally {
            ois.close();
            zis.close();
        }
    }
}
