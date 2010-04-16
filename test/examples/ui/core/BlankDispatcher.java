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
import org.echosoft.framework.ui.extjs.widgets.form.DateRangeField;
import org.echosoft.framework.ui.extjs.widgets.form.DecimalField;
import org.echosoft.framework.ui.extjs.widgets.form.DecimalRangeField;
import org.echosoft.framework.ui.extjs.widgets.form.FieldSet;
import org.echosoft.framework.ui.extjs.widgets.form.FormPanel;
import org.echosoft.framework.ui.extjs.widgets.form.IntegerField;
import org.echosoft.framework.ui.extjs.widgets.form.IntegerRangeField;
import org.echosoft.framework.ui.extjs.widgets.form.LongField;
import org.echosoft.framework.ui.extjs.widgets.form.LongRangeField;
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
        final FormPanel form = central.append( new FormPanel(ctx.getChild("form")) );
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

        final FieldSet fs1 = form.append( new FieldSet(ctx.getChild("fs1")) );
        fs1.setTitle("Regular fields");
        fs1.setCollapsible(true);
        fs1.setTitleCollapse(true);
        fs1.setCollapsed(false);
        fs1.setAnchor("100%");

        final TextField txt1 = fs1.append( new TextField(ctx.getChild("txt1")) );
        txt1.setFieldLabel("Text");
        txt1.setValue("Vasya");
        txt1.setAllowBlank(false);
        txt1.setMinLength(3);
        txt1.setMaxLength(10);
        txt1.setMsgTarget(AbstractField.MsgTarget.UNDER);
        txt1.setStateful(true);

        final TextAreaField txt2 = fs1.append( new TextAreaField(ctx.getChild("txt2")) );
        txt2.setFieldLabel("Text Area");
        txt2.setEmptyText("enter something...");
        txt2.setMaxLength(1000);
        txt2.setHeight(150);
        txt2.setAnchor("100% ");
        txt2.setMsgTarget(AbstractField.MsgTarget.UNDER);
        txt2.setStateful(true);

        final IntegerField num1 = fs1.append( new IntegerField(ctx.getChild("num1")) );
        num1.setFieldLabel("Integer");
        num1.setValue(5);
        num1.setMinValue(-1);
        num1.setMaxLength(3);
        num1.setMsgTarget(AbstractField.MsgTarget.UNDER);
        num1.setStateful(true);
        ctx.getMessages().addMessage( new Message("num1", Message.Severity.ERROR, "my serverside message") );

        final LongField num2 = fs1.append( new LongField(ctx.getChild("num2")) );
        num2.setFieldLabel("Long");
        num2.setValue((long)5);
        num2.setMinValue((long)-1);
        num2.setMsgTarget(AbstractField.MsgTarget.UNDER);
        num2.setStateful(true);

        final DecimalField num3 = fs1.append( new DecimalField(ctx.getChild("num3")) );
        num3.setFieldLabel("Decimal 3");
        num3.setPrecision(3);
        num3.setMinValue( new BigDecimal(0.0009) );
        num3.setMaxValue( new BigDecimal(100.1) );
        num3.setMsgTarget(AbstractField.MsgTarget.SIDE);
        num3.setStateful(true);
        ctx.getMessages().addMessage( new Message("num3", Message.Severity.ERROR, "my serverside message 3") );

        final DateField dat1 = fs1.append( new DateField(ctx.getChild("dat1")) );
        dat1.setFieldLabel("Date");
        dat1.setDisabledDays(new int[]{0,6});
        dat1.setMaxValue( Types.DATE.decode("20.05.2010") );
        dat1.setMsgTarget(AbstractField.MsgTarget.SIDE);
        dat1.setStateful(true);

        final FieldSet fs2 = form.append( new FieldSet(ctx.getChild("fs2")) );
        fs2.setTitle("Range fields");
        fs2.setTitleCollapse(true);
        fs2.setCollapsible(true);
        fs2.setCollapsed(false);
        fs2.setAnchor("100%");

        final IntegerRangeField rng1 = fs2.append( new IntegerRangeField(ctx.getChild("rng1")) );
        rng1.setFieldLabel("Int range");
        rng1.setMinValue(0);
        rng1.setMaxValue(100);
        rng1.setFrom(1);
        rng1.setStateful(true);
        rng1.setFieldWidth(75);
        rng1.setMsgTarget(AbstractField.MsgTarget.UNDER);

        final LongRangeField rng2 = fs2.append( new LongRangeField(ctx.getChild("rng2")) );
        rng2.setFieldLabel("Long range");
        rng2.setMinValue(-10000L);
        rng2.setMaxValue(10000L);
        rng2.setFrom(-10L);
        rng2.setTo(10L);
        rng2.setStateful(true);
        rng2.setFieldWidth(75);
        rng2.setMsgTarget(AbstractField.MsgTarget.UNDER);

        final DecimalRangeField rng3 = fs2.append( new DecimalRangeField(ctx.getChild("rng3")) );
        rng3.setFieldLabel("Decimal range");
        rng3.setPrecision(3);
        rng3.setFrom( BigDecimal.ZERO );
        rng3.setTo( new BigDecimal(3.1415926) );
        rng3.setStateful(true);
        rng3.setFieldWidth(75);
        rng3.setMsgTarget(AbstractField.MsgTarget.UNDER);

        final DateRangeField rng4 = fs2.append( new DateRangeField(ctx.getChild("rng4")) );
        rng4.setFieldLabel("Date range");
        rng4.setStateful(true);
        rng4.setMsgTarget( AbstractField.MsgTarget.UNDER );

        final Toolbar fbar = form.assignFooter( new Toolbar(ctx.getChild("fbar")) );
        final Button bt2 = fbar.addButton("process");
        bt2.setHandler( new JSFunction(null,"this.ownerCt.ownerCt.getForm().submit()") );
        final Button bt3 = fbar.addButton("cancel");
        bt3.setHandler( new JSFunction(null,"this.ownerCt.ownerCt.getForm().reset()") );

        page.invokePage();
    }

}
