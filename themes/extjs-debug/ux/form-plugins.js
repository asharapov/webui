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
        var form = formPanel.getForm();
        form.applyExternalParams = this.applyExternalParams.createCallback(formPanel);
        form.submit = form.submit.createInterceptor( this.onSubmit );
    },
    onSubmit : function(options) {
        if(this.standardSubmit){
            options = options || {};
            var v = options.clientValidation === false || this.isValid();
            if(v){
                this.applyExternalParams();
                var el = this.el.dom;
                if(this.url && Ext.isEmpty(el.action)){
                    el.action = this.url;
                }
                el.submit();
            }
            return false;
        }
        return true;
    },
    applyExternalParams: function(fp) {
        var p = Ext.apply({}, fp.baseParams, WUI.params),
            n;
        for (n in p) {
            if (!fp.get(n)) {
                fp.add({xtype:"hidden", itemId:n, name:n, value:p[n]});
            }
        }
        fp.doLayout();
    }
});
Ext.preg("Ext.ux.wui.plugins.FormPanel", Ext.ux.wui.plugins.FormPanel);

/**
 * Данный плагин добавляет поддержку конфигурационного свойства <code>activeError</code> в полях формы.
 * TODO: не работает если в компоненте изначально установлено непустое значение.
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

/**
 * Обрабатывает ситуацию с комбобоксом возникающую после загрузки страницы при комбинации следующих условий:
 * <ol>
 *   <li> Компонент использует удаленный источник данных (с установленной опцией autoLoad=true).
 *   <li> Реальное и отображаемое поля в компоненте отличаются.
 *   <li> Компонент имеет значение не равное <code>null</code>.
 * </ol>
 * В описанном случае если не принимать специальных мер то после загрузки страницы в поле ввода будет
 * отображаться реальное значение компонента а не то что должно отображаться пользователю.
 */
Ext.ux.wui.plugins.ComboBox = Ext.extend(Ext.util.Observable, {
    init: function(field) {
        var val = field.getValue(),
            handler = function() {
              field.setValue(val);
              this.un("load", handler);
            };
        if (field.store!=null && field.store.autoLoad && !field.store.lastOptions &&
            field.valueField!==field.displayField && val!==undefined) {
          //field.clearValue();
          field.store.on("load", handler);
        }
    }
});
Ext.preg("Ext.ux.wui.plugins.ComboBox", Ext.ux.wui.plugins.ComboBox);
