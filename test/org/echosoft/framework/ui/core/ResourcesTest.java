package org.echosoft.framework.ui.core;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Anton Sharapov
 */
public class ResourcesTest {

    @Test
    public void testAttach() {
        final Resources resources = new Resources();
        resources.attachScript("/res/a.js");
        resources.attachScript("/res/b.js");
        resources.attachScript("/res/b.js");
        resources.attachScript("/res/a.js");
        resources.attachStyleSheet("/res/core.css");
        resources.embedScript("alert(self)");
        final Collection<String> js = asStrings( resources.getExternalScripts() );
        Assert.assertEquals(Arrays.asList("/res/a.js", "/res/b.js"), js);
        final Collection<String> css = asStrings( resources.getExternalStyleSheets() );
        Assert.assertEquals(Arrays.asList("/res/core.css"), css);
    }

    @Test
    public void testEmbed() throws IOException {
        final Resources resources = new Resources();
        resources.attachScript("/res/a.js");
        resources.embedScript("invokeA();");
        resources.embedScript("invokeB();");
        resources.embedScript("invokeB();");
        resources.embedScript("invokea();");
        resources.embedScript("invokeA();");
        resources.embedStyle("body {color:red}");
        resources.embedStyle("body {color:red}");
        resources.embedStyle(".classes {font-weight:bold}");
        final CharSequence js = getScripts(resources);
        Assert.assertEquals("invokeA();\ninvokeB();\ninvokea();\n", js.toString());
        final CharSequence css = getStyles(resources);
        Assert.assertEquals("body {color:red}\n.classes {font-weight:bold}\n", css.toString());
        Assert.assertTrue( getHTML(resources).length()==0);
    }

    @Test
    public void testRollback() throws IOException {
        final Resources resources = new Resources();
        final Resources.SavePoint sp0 = resources.makeSavePoint();
        Assert.assertTrue(!sp0.isAnnuled());
        sp0.rollback();
        Assert.assertTrue(sp0.isAnnuled());
        try {
            sp0.rollback();
            Assert.fail("already annulled savepoint was fired twice");
        } catch (IllegalStateException e) {
        }
        resources.attachScript("/res/a.js");
        resources.embedScript("invokeA();");
        final Resources.SavePoint sp1 = resources.makeSavePoint();
        resources.attachScript("/res/a.js");
        resources.attachScript("/res/b.js");
        Assert.assertEquals(3, resources.size());
        Assert.assertTrue(resources == sp1.getOwner());
        sp1.rollback();
        Assert.assertEquals(2, resources.size());
        Assert.assertEquals(Arrays.asList("/res/a.js"), asStrings(resources.getExternalScripts()));
        Assert.assertEquals("invokeA();\n", getScripts(resources).toString());
    }

    private static List<String> asStrings(final Collection<? extends Resources.Resource> resources) {
        final ArrayList<String> result = new ArrayList<String>(resources.size());
        for (Resources.Resource res : resources) {
            result.add( res.getUrl() );
        }
        return result;
    }
    private static CharSequence getScripts(final Resources resources) throws IOException {
        final StringWriter out = new StringWriter();
        resources.writeOutScripts(out);
        return out.toString();
    }
    private static CharSequence getStyles(final Resources resources) throws IOException {
        final StringWriter out = new StringWriter();
        resources.writeOutStyles(out);
        return out.toString();
    }
    private static CharSequence getHTML(final Resources resources) throws IOException {
        final StringWriter out = new StringWriter();
        resources.writeOutHTML(out);
        return out.toString();
    }
}
