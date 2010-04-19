Ext.ns("Ext.ux.wui.plugins");

/**
 * Данный плагин отвечает за добавление ряда невидимый полей ввода в форму перед ее отправкой на сервер.
 * В форму добавляются следующие поля:
 * <ul>
 *  <li> параметры из свойства baseParams формы.
 *  <li> параметры из глобального объекта WUI.params.
 * </ul>
 */
Ext.ux.wui.plugins.FormPanel = Ext.extend(Ext.util.Observable, {
    init: function(formPanel) {
        formPanel.on("render", this.onRender);
    },
    onRender: function(fp) {
        var el = fp.getForm().el.dom;
        el.submit = el.submit.createInterceptor(
            function() {
                var p = Ext.apply({}, fp.baseParams, WUI.params),
                    n;
                for (n in p) {
                    if (!fp.get(n)) {
                        fp.add({xtype:"hidden", itemId:n, name:n, value:p[n]});
                    }
                }
                fp.doLayout();
                return true;
            });
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
