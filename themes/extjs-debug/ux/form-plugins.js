Ext.ns("Ext.ux.wui.plugins");

/**
 * Данный плагин отвечает за добавление ряда невидимый полей ввода в форму перед ее отправкой на сервер.
 * В форму добавляются следующие поля:
 * <ul>
 *  <li> __VIEWSTATE  - копируется из WUI.env.state
 *  <li> параметры из свойства baseParams формы.
 * </ul>
 */
Ext.ux.wui.plugins.FormPanel = Ext.extend(Ext.util.Observable, {
    init: function(formPanel) {
        formPanel.on("render", this.onRender);
    },
    onRender: function(fp) {
        var formEl = fp.getForm().el.dom;
        var funct = formEl.submit;
        formEl.submit = function() {
            for (var n in fp.baseParams) {
                if (!fp.get(n)) {
                    fp.add({xtype:"hidden", itemId:n, name:n, value:fp.baseParams[n]});
                }
            }
            if (!fp.get("__VIEWSTATE")) {
                fp.add({xtype:"hidden", itemId:"__VIEWSTATE", name:"__VIEWSTATE", value:WUI.env.state});
            }
            fp.doLayout();
            funct.apply(formEl);
        };
    }
});
Ext.preg("Ext.ux.wui.plugins.FormPanel", Ext.ux.wui.plugins.FormPanel);

/**
 * Данный плагин добавляет поддержку конфигурационного свойства <code>activeError</code> в полях формы.
 */
Ext.ux.wui.plugins.Field = Ext.extend(Ext.util.Observable, {
    init: function(field) {
        if (field.initialConfig.activeError) {
            field.on("render", function() {
                this.markInvalid(this.initialConfig.activeError);
            })
        }
    }
});
Ext.preg("Ext.ux.wui.plugins.Field", Ext.ux.wui.plugins.Field);