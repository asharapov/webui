package examples.ui.core;

import java.math.BigDecimal;

import org.echosoft.common.json.JSFunction;
import org.echosoft.common.types.Types;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.Message;
import org.echosoft.framework.ui.core.UIContext;
import org.echosoft.framework.ui.extjs.ExtJSPage;
import org.echosoft.framework.ui.extjs.layout.BorderLayout;
import org.echosoft.framework.ui.extjs.layout.BorderLayoutRegion;
import org.echosoft.framework.ui.extjs.layout.FitLayout;
import org.echosoft.framework.ui.extjs.model.Template;
import org.echosoft.framework.ui.extjs.widgets.Box;
import org.echosoft.framework.ui.extjs.widgets.Button;
import org.echosoft.framework.ui.extjs.widgets.Panel;
import org.echosoft.framework.ui.extjs.widgets.Toolbar;
import org.echosoft.framework.ui.extjs.widgets.form.AbstractField;
import org.echosoft.framework.ui.extjs.widgets.form.DateField;
import org.echosoft.framework.ui.extjs.widgets.form.DecimalField;
import org.echosoft.framework.ui.extjs.widgets.form.FormPanel;
import org.echosoft.framework.ui.extjs.widgets.form.IntegerField;
import org.echosoft.framework.ui.extjs.widgets.form.LongField;
import org.echosoft.framework.ui.extjs.widgets.form.TextAreaField;
import org.echosoft.framework.ui.extjs.widgets.form.TextField;

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
        page.setLayout( new FitLayout() );
        page.addListener("render", new JSFunction(null, "alert('onrender...');"));
        final Panel root = page.append( new Panel() );
        final BorderLayout rootLayout = root.assignLayout( new BorderLayout() );

        final BorderLayoutRegion north = rootLayout.getNorth(true);
        north.setHeight(60);
        final Box box1 = north.append( new Box() );
        box1.setHtml("North region");

        final BorderLayoutRegion south = rootLayout.getSouth(true);
        south.setHeight(30);
        final Box box2 = south.append( new Box() );
        box2.setHtml("South region");

        final BorderLayoutRegion central = rootLayout.getCenter();
        final FormPanel form = central.append( new FormPanel(ctx.getChild("p1")) );
        form.setTitle("Enter you data");
        form.setFrame(true);
        form.setWidth(400);
        form.setCollapsible(true);
        form.setStandardSubmit(true);
        form.setUrl(".");
        form.setMethod(FormPanel.Method.POST);
        final Panel.ToolButton tb1 = form.appendToolButton( Panel.ToolButtonType.REFRESH );
        tb1.setHandler( new JSFunction(null, "alert('refresh');") );
        final Box box3 = form.append( new Box() );
        box3.setFieldLabel("Greeting");
        box3.setTemplate( new Template("Hello, {0}!") );
        box3.setData( new String[]{"Master"} );

        final Button bt1 = form.append( new Button(ctx.getChild("bt1")) );
        bt1.setFieldLabel("Action");
        bt1.setText("click me");
        bt1.setHandler( new JSFunction(new String[]{"b", "e"}, "console.log(b,e);") );

        final TextField txt1 = form.append( new TextField(ctx.getChild("txt1")) );
        txt1.setFieldLabel("Text");
        txt1.setValue("Vasya Pupkinn");
        txt1.setAllowBlank(false);
        txt1.setMinLength(3);
        txt1.setMaxLength(10);
        txt1.setMsgTarget(AbstractField.MsgTarget.UNDER);
        txt1.setStateful(true);

        final TextAreaField txt2 = form.append( new TextAreaField(ctx.getChild("txt2")) );
        txt2.setFieldLabel("Text Area");
        txt2.setEmptyText("enter something...");
        txt2.setMaxLength(1000);
        txt2.setHeight(150);
        txt2.setAnchor("100% ");
        txt2.setMsgTarget(AbstractField.MsgTarget.UNDER);
        txt2.setStateful(true);

        final IntegerField num1 = form.append( new IntegerField(ctx.getChild("num1")) );
        num1.setFieldLabel("Integer");
        num1.setValue(5);
        num1.setMinValue(-1);
        num1.setMaxLength(3);
        num1.setMsgTarget(AbstractField.MsgTarget.UNDER);
        num1.setStateful(true);
        ctx.getMessages().addMessage( new Message("num1", Message.Severity.ERROR, "my serverside message") );

        final LongField num2 = form.append( new LongField(ctx.getChild("num2")) );
        num2.setFieldLabel("Long");
        num2.setValue((long)5);
        num2.setMinValue((long)-1);
        num2.setMsgTarget(AbstractField.MsgTarget.UNDER);
        num2.setStateful(true);

        final DecimalField num3 = form.append( new DecimalField(ctx.getChild("num3")) );
        num3.setFieldLabel("Decimal 3");
        num3.setPrecision(3);
        num3.setMinValue( new BigDecimal(0.0009) );
        num3.setMaxValue( new BigDecimal(100.1) );
        num3.setMsgTarget(AbstractField.MsgTarget.SIDE);
        num3.setStateful(true);
        ctx.getMessages().addMessage( new Message("num3", Message.Severity.ERROR, "my serverside message 3") );

        final DateField dat1 = form.append( new DateField(ctx.getChild("dat1")) );
        dat1.setFieldLabel("Date");
        dat1.setDisabledDays(new int[]{0,6});
        dat1.setMaxValue( Types.DATE.decode("20.05.2010") );
        dat1.setMsgTarget(AbstractField.MsgTarget.SIDE);
        dat1.setStateful(true);

        final Toolbar fbar = form.assignFooter( new Toolbar(ctx.getChild("fbar")) );
        final Button bt2 = fbar.addButton("process");
        bt2.setHandler( new JSFunction(null,"this.ownerCt.ownerCt.getForm().submit()") );
        final Button bt3 = fbar.addButton("cancel");
        bt3.setHandler( new JSFunction(null,"this.ownerCt.ownerCt.getForm().reset()") );

        page.invokePage();
    }

}
