package examples.ui.dispatchers;

import org.echosoft.common.json.JSFunction;
import org.echosoft.common.model.LongReference;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.UIContext;
import org.echosoft.framework.ui.extjs.ExtJSPage;
import org.echosoft.framework.ui.extjs.data.JsonReader;
import org.echosoft.framework.ui.extjs.data.Store;
import org.echosoft.framework.ui.extjs.layout.FitLayout;
import org.echosoft.framework.ui.extjs.widgets.Button;
import org.echosoft.framework.ui.extjs.widgets.TabPanel;
import org.echosoft.framework.ui.extjs.widgets.Toolbar;
import org.echosoft.framework.ui.extjs.widgets.form.ComboBox;
import org.echosoft.framework.ui.extjs.widgets.form.FormPanel;

import examples.ui.Dispatcher;

/**
 * @author Anton Sharapov
 */
public class ComboBoxDispatcher implements Dispatcher {
    @Override
    public void dispatch(UIContext uctx) throws Exception {
        final ExtJSPage page = new ExtJSPage(uctx);
        final ComponentContext ctx = page.getContext();     // or  ctx = new ComponentContext(uctx)
        page.setTitle("My first page");
        page.setIcon("/img/favicon.ico");
        page.setLayout( new FitLayout() );

        final TabPanel tabs = page.append( new TabPanel(ctx.getChild("tp")) );
        tabs.setResizeTabs(true);
        tabs.setActiveItem("fp2");

        final FormPanel fp1 = tabs.append( new FormPanel(ctx.getChild("fp1")) );
        fp1.setTitle("Local data");
        fp1.setWidth(400);
        fp1.setFrame(true);
        fp1.setUrl("/examples/combobox");
        fp1.setStandardSubmit(true);
        fp1.setMethod(FormPanel.Method.POST);

        final ComboBox cb1 = fp1.append( new ComboBox(ctx.getChild("cb1")) );
        cb1.setFieldLabel("Combo 1");
        cb1.setStateful(true);
        cb1.assignStore(new LongReference(1,"Moscow"),
                        new LongReference(2,"Minsk"),
                        new LongReference(3,"Kiev"),
                        new LongReference(4,"Astana"),
                        new LongReference(5,"Bishkek") );
        cb1.getStore().setStoreId("store1");
        cb1.setForceSelection(false);

        final ComboBox cb2 = fp1.append( new ComboBox(ctx.getChild("cb2")) );
        cb2.setFieldLabel("Combo 2");
        cb2.setStateful(true);
        cb2.setStoreRef("store1");
        cb2.setValueField("id");
        cb2.setDisplayField("title");
        cb2.setMode(ComboBox.Mode.LOCAL);
        cb2.setTriggerAction(ComboBox.TriggerAction.ALL);
        cb2.setForceSelection(true);
        cb2.setEditable(false);
        cb2.setResizable(true);
        cb2.setPageSize(3);
        cb2.setValue("2");

        final Toolbar tb1 = fp1.assignFooter( new Toolbar(ctx.getChild("tb1")) );
        final Button bt11 = tb1.addButton("submit 1");
        bt11.setHandler( new JSFunction(null,"this.ownerCt.ownerCt.getForm().submit()") );
        final Button bt12 = tb1.addButton("cancel 1");
        bt12.setHandler( new JSFunction(null,"this.ownerCt.ownerCt.getForm().reset()") );



        final FormPanel fp2 = tabs.append( new FormPanel(ctx.getChild("fp2")) );
        fp2.setTitle("Remote data");
        fp2.setTabTip("testing remote data");
        fp2.setWidth(400);
        fp2.setFrame(true);
        fp2.setUrl("/examples/combobox");
        fp2.setStandardSubmit(true);
        fp2.setMethod(FormPanel.Method.POST);

        final ComboBox cb3 = fp2.append( new ComboBox(ctx.getChild("cb3")) );
        cb3.setFieldLabel("Combo 3");
        cb3.setStateful(true);
        final Store store = new Store("store3");
        store.setUrl(ctx.encodeURL("/ajax/simple/application/services.environmentServices/getEnvironment"));
        store.setAutoLoad(true);
        store.setReader( new JsonReader("id", "title"));
        store.addListener("load", new JSFunction(null, "console.log('store.onload: ',arguments);"));
        store.addListener("clear", new JSFunction(null, "console.log('store.onclear: ',arguments);"));
        store.addListener("exception", new JSFunction(null, "console.log('store.onexception: ',arguments);"));
        cb3.setStore( store );
        cb3.setValueField("id");
        cb3.setDisplayField("title");
        cb3.setMode(ComboBox.Mode.REMOTE);
        cb3.setTriggerAction(ComboBox.TriggerAction.ALL);
        cb3.setResizable(true);
        cb3.setPageSize(5);

        final Toolbar tb2 = fp2.assignFooter( new Toolbar(ctx.getChild("tb2")) );
        final Button bt21 = tb2.addButton("submit 2");
        bt21.setHandler( new JSFunction(null,"this.ownerCt.ownerCt.getForm().submit()") );
        final Button bt22 = tb2.addButton("cancel 2");
        bt22.setHandler( new JSFunction(null,"this.ownerCt.ownerCt.getForm().reset()") );

        page.invokePage();
    }
}
