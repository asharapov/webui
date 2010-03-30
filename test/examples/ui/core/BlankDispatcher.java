package examples.ui.core;

import org.echosoft.common.json.JSFunction;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.UIContext;
import org.echosoft.framework.ui.extjs.ExtJSPage;
import org.echosoft.framework.ui.extjs.layout.BorderLayout;
import org.echosoft.framework.ui.extjs.layout.BorderLayoutRegion;
import org.echosoft.framework.ui.extjs.layout.FitLayout;
import org.echosoft.framework.ui.extjs.layout.FormLayout;
import org.echosoft.framework.ui.extjs.model.Template;
import org.echosoft.framework.ui.extjs.widgets.Box;
import org.echosoft.framework.ui.extjs.widgets.Panel;

import examples.ui.Dispatcher;

/**
 * @author Anton Sharapov
 */
public class BlankDispatcher implements Dispatcher {

    public void dispatch(final UIContext uctx) throws Exception {
        final ExtJSPage page = new ExtJSPage(uctx);
        page.setTitle("My first page");
        page.setIconRef("/img/favicon.ico");

        page.addListener("render", new JSFunction(null, "alert('onrender...');"));

        final ComponentContext ctx = page.getContext();
        final FitLayout fit = page.assignLayout( new FitLayout() );
        final Panel root = fit.append( new Panel(null) );
        final BorderLayout rtl = root.assignLayout( new BorderLayout() );

        final BorderLayoutRegion north = rtl.getNorth(true);
        north.setHeight(60);
        final Box b1 = north.append( new Box(ctx.getChild("b1")) );
        b1.setHtml("This is a north region");

        final BorderLayoutRegion south = rtl.getSouth(true);
        south.setHeight(30);
        final Box b2 = south.append( new Box(ctx.getChild("b2")) );
        b2.setHtml("This is a south region");

        final BorderLayoutRegion central = rtl.getCenter();
        final Panel p1 = central.append( new Panel(ctx.getChild("p1")) );
        p1.setTitle("Enter you data");
        p1.setFrame(true);
        p1.setWidth(400);
        final FormLayout fl = p1.assignLayout( new FormLayout() );
        final Box b3 = fl.append( new Box() );
        b3.setTemplate( new Template("Hello, {0}!") );
        b3.setData( new String[]{"Master"} );
        b3.setFieldLabel("Greeting");
        final Box b4 = fl.append( new Box() );
        b4.setHtml("some text");
        b4.setFieldLabel("test");

        page.invokePage();
    }

}
