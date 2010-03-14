package examples.ui;

import org.echosoft.framework.ui.core.UIContext;

/**
 * @author Anton Sharapov
 */
public interface Dispatcher {

    public void dispatch(final UIContext uctx) throws Exception;
}
