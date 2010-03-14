package org.echosoft.framework.ui.extjs.widgets;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.AbstractContainerComponent;

/**
 * @author Anton Sharapov
 */
public class Panel extends AbstractContainerComponent {

    private boolean animToCollapse;
    private String baseCls;             // CSS класс, применяемый к каждому дочернему элементу на панели.
    private boolean bodyBorder;

    public Panel(final ComponentContext ctx) {
        super(ctx);
    }

    @Override
    public void invoke(final JsonWriter out) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
