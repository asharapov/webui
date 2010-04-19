package examples.ui.core;

import org.echosoft.common.json.JSFunction;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.UIContext;
import org.echosoft.framework.ui.extjs.ExtJSPage;

import examples.ui.Dispatcher;

/**
 * @author Anton Sharapov
 */
public class BlankDispatcher implements Dispatcher {

    public void dispatch(final UIContext uctx) throws Exception {
        final ExtJSPage page = new ExtJSPage(uctx);
        final ComponentContext ctx = page.getContext();     // or  ctx = new ComponentContext(uctx)
        page.setTitle("My first page");
        page.setIcon("/img/favicon.ico");
        page.addListener("render", new JSFunction(null, "alert('onrender...');"));
        page.invokePage();
    }

}
