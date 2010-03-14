package org.echosoft.framework.ui.core.spi;

import java.util.Map;

import org.echosoft.framework.ui.core.BaseMockUIContext;
import org.echosoft.framework.ui.core.StateHolder;
import org.echosoft.framework.ui.core.ViewStateDescriptor;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Anton Sharapov
 */
public class GZIPStateSerializerTest {

    private static final GZIPStateSerializer serializer = new GZIPStateSerializer();

    @Test
    public void testEmptyStates() throws Exception {
        final StateHolder states1 = new StateHolder();
        final String key = serializer.encodeState(new BaseMockUIContext(states1));
        Assert.assertNotNull(key);
        final StateHolder states2 = new StateHolder();
        serializer.decodeState(new BaseMockUIContext(states2), key);
        Assert.assertEquals(states1, states2);
    }

    @Test
    public void testOneStates() throws Exception {
        final StateHolder states1 = new StateHolder();
        final Map<String,Object> state = states1.ensureStateExists( new ViewStateDescriptor("mypkg", "view1", 1));
        state.put("param1", 1);
        state.put("param2", 2.0);
        final String key = serializer.encodeState(new BaseMockUIContext(states1));
        Assert.assertNotNull(key);
        final StateHolder states2 = new StateHolder();
        serializer.decodeState(new BaseMockUIContext(states2), key);
        Assert.assertEquals(states1, states2);
    }

    @Test
    public void testTwoStates() throws Exception {
        final StateHolder states1 = new StateHolder();
        final ViewStateDescriptor v1 = new ViewStateDescriptor("mypkg", "view1", 1);
        Map<String,Object> state = states1.ensureStateExists(v1);
        state.put("param1", 1);
        state.put("param2", 2.0);
        final ViewStateDescriptor v2 = new ViewStateDescriptor("mypkg", "view2", 2);
        state = states1.ensureStateExists(v2);
        state.put("p1", "aaa");
        state.put("p2", "bbb");
        state.put("p3", "ccc");
        states1.setCurrentDescriptor(v2);
        final String key = serializer.encodeState(new BaseMockUIContext(states1));
        System.out.println(key.length()+"\n"+key);
        Assert.assertNotNull(key);
        final StateHolder states2 = new StateHolder();
        serializer.decodeState(new BaseMockUIContext(states2), key);
        Assert.assertEquals(states1, states2);
        System.out.println(states2);
    }


}
