package examples.ui.core;

import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.UIContext;
import org.echosoft.framework.ui.extjs.ExtJSPage;
import org.echosoft.framework.ui.extjs.layout.BorderLayout;
import org.echosoft.framework.ui.extjs.layout.BorderLayoutItem;
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
        final ComponentContext ctx = page.getContext();

        final Panel root = new Panel(ctx.getChild("root"));
        root.setLayout( new BorderLayout() );

        final Panel north = (Panel)root.append( new Panel(ctx.getChild("north")) );
        final BorderLayoutItem nli = (BorderLayoutItem)north.getLayoutItem();
        nli.setRegion(BorderLayoutItem.Region.NORTH);

        final Panel south = (Panel)root.append( new Panel(ctx.getChild("south")) );
        final BorderLayoutItem sli = (BorderLayoutItem)south.getLayoutItem();
        sli.setRegion(BorderLayoutItem.Region.SOUTH);

        page.invokePage();
    }

}
