package examples.ui.core;

import org.echosoft.framework.ui.core.UIContext;
import org.echosoft.framework.ui.extjs.ExtJSPage;

import examples.ui.Dispatcher;

/**
 * @author Anton Sharapov
 */
public class BlankDispatcher implements Dispatcher {

    public void dispatch(final UIContext uctx) throws Exception {
        final ExtJSPage page = new ExtJSPage(uctx);
        page.setTitle("My first page");
        page.setIconRef("/img/favicon.ico");
        page.invokePage();
    }

}
