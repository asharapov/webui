/*!
 * Ext JS Library 3.2.0
 * Copyright(c) 2006-2010 Ext JS, Inc.
 * licensing@extjs.com
 * http://www.extjs.com/license
 */

Ext.ns("Ext.ux.wui.form");

/**
 * @class Ext.ux.wui.form.Checkbox
 * @extends Ext.form.Field
 * Single checkbox field with tristate support. Can be used as a direct replacement for traditional checkbox fields.
 * @constructor
 * Creates a new Checkbox
 * @param {Object} config Configuration options
 * @xtype tri-checkbox
 */
Ext.ux.wui.form.Checkbox = Ext.extend(Ext.form.Field, {
    /**
     * @cfg {String} focusClass The CSS class to use when the checkbox receives focus (defaults to undefined)
     */
    focusClass : undefined,
    /**
     * @cfg {String} fieldClass The default CSS class for the checkbox (defaults to 'x-form-field')
     */
    fieldClass : 'x-form-field',
    /**
     * @cfg {Boolean} tristate <tt>true</tt> if the checkbox support <tt>indeterminated</tt> state (defaults to <tt>false</tt>)
     */
    tristate : false,
    /**
     * @cfg {Boolean} checked <tt>true</tt> if the checkbox should render initially checked (defaults to <tt>false</tt>)
     */
    checked : false,
    /**
     * @cfg {String} boxLabel The text that appears beside the checkbox
     */
    boxLabel: '&#160;',
    /**
     * @cfg {String/Object} autoCreate A DomHelper element spec, or true for a default element spec (defaults to
     * {tag: 'input', type: 'checkbox', autocomplete: 'off'})
     */
    defaultAutoCreate : {tag: 'input', type: 'checkbox', autocomplete: 'off'},
    /**
     * @cfg {String} boxLabel The text that appears beside the checkbox
     */
    /**
     * @cfg {Function} handler A function called when the {@link #checked} value changes (can be used instead of
     * handling the check event). The handler is passed the following parameters:
     * <div class="mdetail-params"><ul>
     * <li><b>checkbox</b> : Ext.ux.wui.form.Checkbox<div class="sub-desc">The Checkbox being toggled.</div></li>
     * <li><b>checked</b> : Boolean<div class="sub-desc">The new checked state of the checkbox.</div></li>
     * </ul></div>
     */
    /**
     * @cfg {Object} scope An object to use as the scope ('this' reference) of the {@link #handler} function
     * (defaults to this Checkbox).
     */

    // private
    actionMode : 'wrap',

	// private
    initComponent : function(){
        Ext.ux.wui.form.Checkbox.superclass.initComponent.call(this);
        this.addEvents(
            /**
             * @event check
             * Fires when the checkbox is checked or unchecked.
             * @param {Ext.form.Checkbox} this This checkbox
             * @param {Boolean} checked The new checked value
             */
            'check'
        );
    },

    // private
    onResize : function(){
        Ext.ux.wui.form.Checkbox.superclass.onResize.apply(this, arguments);
        if(!this.boxLabel && !this.fieldLabel){
            this.el.alignTo(this.wrap, 'c-c');
        }
    },

    // private
    initEvents : function(){
        Ext.ux.wui.form.Checkbox.superclass.initEvents.call(this);
        this.mon(this.el, {
            scope: this,
            click: this.onClick
        });
    },

    /**
     * @hide
     * Overridden and disabled. The editor element does not support standard valid/invalid marking.
     * @method
     */
    markInvalid : Ext.emptyFn,
    /**
     * @hide
     * Overridden and disabled. The editor element does not support standard valid/invalid marking.
     * @method
     */
    clearInvalid : Ext.emptyFn,

    // private
    onRender : function(ct, position){
        Ext.ux.wui.form.Checkbox.superclass.onRender.call(this, ct, position);
        this.el.dom.removeAttribute('name');
        this.wrap = this.el.wrap({cls: 'x-form-check-wrap'});
        if(this.boxLabel){
            this.wrap.createChild({tag: 'label', htmlFor: this.el.id, cls: 'x-form-cb-label', html: this.boxLabel});
        }
        this.valueEl = this.el.insertSibling({tag:'input', type:'hidden', autocomplete:'off'}, 'after', false);
        if (this.submitValue !== false)
            this.valueEl.dom.setAttribute('name', this.name || this.id);

        this.setValue(this.checked);

        // Need to repaint for IE, otherwise positioning is broken
        if(Ext.isIE){
            this.wrap.repaint();
        }
        this.resizeEl = this.positionEl = this.wrap;
    },

    // private
    onDestroy : function(){
        Ext.destroy(this.wrap);
        Ext.ux.wui.form.Checkbox.superclass.onDestroy.call(this);
    },

    // private
    initValue : function() {
        this.originalValue = this.getValue();
    },

    getRawValue : function() {
        return this.rendered ? this.valueEl.value : this.checked!==null ? ""+this.checked : "";
    },

    /**
     * Returns the checked state of the checkbox.
     * @return {Boolean} True if checked, else false
     */
    getValue : function(){
        if(this.rendered){
            return this.el.dom.indeterminate ? null : this.el.dom.checked;
        }
        return this.checked;
    },

	// private
    onClick : function(){
        if (this.readOnly) {
            this.setValue( this.checked );
        } else
        if (this.tristate) {
            var v = this.checked==null ? false : (this.checked ? null : true);
            this.setValue(v);
        } else {
            if(this.el.dom.checked != this.checked){
                this.setValue(this.el.dom.checked);
            }
        }
    },

    /**
     * Sets the checked state of the checkbox, fires the 'check' event, and calls a
     * <code>{@link #handler}</code> (if configured).
     * @param {Boolean/String} checked The following values will check the checkbox:
     * <code>true, 'true', '1', or 'on'</code>. Any other value will uncheck the checkbox.
     * @return {Ext.form.Field} this
     */
    setValue : function(v){
        var checked = this.checked ;
        this.checked = this.tristate && v===null
                ? null
                : (v === true || v === 'true' || v == '1' || String(v).toLowerCase() == 'on');
        if(this.rendered){
            if (this.checked==null) {
                this.el.dom.checked = false;
                this.el.dom.indeterminate = true;
                this.valueEl.dom.value = "";
            } else {
                this.el.dom.indeterminate = false;
                this.el.dom.checked = this.checked;
                this.valueEl.dom.value = this.checked;
            }
        }
        if(checked !== this.checked){
            this.fireEvent('check', this, this.checked);
            if(this.handler){
                this.handler.call(this.scope || this, this, this.checked);
            }
        }
        return this;
    }
});
Ext.reg('tri-checkbox', Ext.ux.wui.form.Checkbox);
