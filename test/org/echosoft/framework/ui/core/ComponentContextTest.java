package org.echosoft.framework.ui.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Anton Sharapov
 */
public class ComponentContextTest {

    @Test
    public void testWithoutPrefix() throws Exception {
        final UIContext uctx = new BaseMockUIContext();

        final ComponentContext ctx0 = new ComponentContext(uctx);
        Assert.assertEquals("", ctx0.getId());
        Assert.assertEquals("", ctx0.getClientId());

        final ComponentContext ctx1 = ctx0.getChild("a");
        Assert.assertEquals("a", ctx1.getId());
        Assert.assertEquals("a", ctx1.getClientId());

        final ComponentContext ctx2 = ctx1.getChild("b");
        Assert.assertEquals("b", ctx2.getId());
        Assert.assertEquals("a.b", ctx2.getClientId());

        final ComponentContext ctx3 = ctx2.getChild("c");
        Assert.assertEquals("c", ctx3.getId());
        Assert.assertEquals("a.b.c", ctx3.getClientId());
    }

    @Test
    public void testWithPrefix() throws Exception {
        final BaseMockUIContext uctx = new BaseMockUIContext();
        uctx.setParamsPrefix("xxx");

        final ComponentContext ctx0 = new ComponentContext(uctx);
        Assert.assertEquals("", ctx0.getId());
        Assert.assertEquals("xxx", ctx0.getClientId());

        final ComponentContext ctx1 = ctx0.getChild("a");
        Assert.assertEquals("a", ctx1.getId());
        Assert.assertEquals("xxxa", ctx1.getClientId());

        final ComponentContext ctx2 = ctx1.getChild("b");
        Assert.assertEquals("b", ctx2.getId());
        Assert.assertEquals("xxxa.b", ctx2.getClientId());

        final ComponentContext ctx3 = ctx2.getChild("c");
        Assert.assertEquals("c", ctx3.getId());
        Assert.assertEquals("xxxa.b.c", ctx3.getClientId());
    }
}
