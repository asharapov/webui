package org.echosoft.framework.ui.extjs.widgets.form;

import java.util.Collection;
import java.util.Set;

import org.echosoft.common.json.JSExpression;
import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.json.annotate.JsonUseSeriazer;
import org.echosoft.common.model.Reference;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.Scope;
import org.echosoft.framework.ui.extjs.data.JsonReader;
import org.echosoft.framework.ui.extjs.data.Store;
import org.echosoft.framework.ui.extjs.spi.model.EnumLCJSONSerializer;

/**
 * @author Anton Sharapov
 */
public class ComboBox extends AbstractField {

    public static final Set<String> EVENTS =
            StringUtil.asUnmodifiableSet(AbstractField.EVENTS,
                    "autosize", "keydown", "keypress", "keyup",
                    "beforequery", "beforeselect", "collapse", "expand", "select");

    @JsonUseSeriazer(EnumLCJSONSerializer.class)
    public static enum Mode {
        LOCAL, REMOTE
    }
    @JsonUseSeriazer(EnumLCJSONSerializer.class)
    public static enum TriggerAction {
        ALL, QUERY
    }

    private Integer tabIndex;               //
    private boolean allowBlank;             // может ли поле быть пустым.
    private String emptyText;               // текст отображаемый в пустом поле ввода.
    private String name;                    // имя параметра в котором будет на сервер отправлено значение поля.
    private String value;                   // текущее значение.
    private String valueField;              // поле в источнике данных которое хранит реальное значение поля.
    private String displayField;            // поле в источнике данных которое хранит текстовое представление значения поля.
    private Store store;                    // используемый источник данных.
    private JSExpression storeRef;          // javascript ссылка на источник данных.
    private Mode mode;                      // источник данных локальный или удаленный ?
    private TriggerAction triggerAction;    // использовать или нет фильтрацию отображаемых в списке значений.
    private String queryParam;              // имя параметра в котором на сервер отправляется вводимый текст.
    private int pageSize;                   // использовать или нет постраничный вывод элементов списка.
    private boolean forceSelection;         // запрещать вводить значения, которым нет соответствия в источнике данных.
    private boolean typeAhead;              // разрешить предугадывание вводимого значения.
    private boolean editable;               // может ли пользователь руками набирать текст в поле.
    private boolean resizable;              // может ли пользователь изменять размеры выпадающего списка.
    private String title;                   // строка отображаемая в выпадающем списке над предлагаемыми значениями.
    private String listEmptyText;           // строка отображаемая в выпадающем списке если он пустой.

    public ComboBox() {
        this(null);
    }
    public ComboBox(final ComponentContext ctx) {
        super(ctx);
        allowBlank = true;
        mode = Mode.REMOTE;
        triggerAction = TriggerAction.QUERY;
        editable = true;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }
    public void setTabIndex(final Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    /**
     * Возвращает <code>true</code> если поле ввода является валидным даже при отсутствии значения в нем.
     * @return <code>true</code> если поле валидно и при отсутствии значения в нем.
     *      По умолчанию возвращает <code>true</code>.
     */
    public boolean isAllowBlank() {
        return allowBlank;
    }
    /**
     * Определяет, является ли поле ввода валидным при отсутствии значения в нем.
     * @param allowBlank <code>true</code> если поле валидно и при отсутствии значения в нем.
     */
    public void setAllowBlank(final boolean allowBlank) {
        this.allowBlank = allowBlank;
    }

    /**
     * Возвращает текст который будет отображаться в пустом поле ввода. Этот же текст будет отправляться на сервер если
     * пользователь в этом поле так ничего и не напишет перед отправкой формы на сервер.
     * @return текст который будет отображаться в пустом поле ввода.
     */
    public String getEmptyText() {
        return emptyText;
    }
    /**
     * Указывает текст который будет отображаться в пустом поле ввода. Этот же текст будет отправляться на сервер если
     * пользователь в этом поле так ничего и не напишет перед отправкой формы на сервер.
     * @param emptyText текст который будет отображаться в пустом поле ввода.
     */
    public void setEmptyText(final String emptyText) {
        this.emptyText = emptyText;
    }


    /**
     * Возвращает имя параметра в запросе где будет сохраняться значение данного компонента.
     * По умолчанию вычисляется как:
     * <pre>
     *      <code>name = getContext().getClientId() + ".value";</code>
     * </pre>
     * @return имя параметра в запросе где будет сохраняться значение данного компонента.
     */
    public String getName() {
        return name;
    }
    /**
     * Указывает имя параметра в запросе где будет сохраняться значение данного компонента.
     * По умолчанию вычисляется как:
     * <pre>
     *      <code>name = getContext().getClientId() + ".value";</code>
     * </pre>
     * @param name имя параметра в запросе где будет сохраняться значение данного компонента.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Возвращает значение данного поля.
     * @return  значение данного поля.
     */
    public String getValue() {
        return value;
    }
    /**
     * Указывает текст отображаемый в данном поле.
     * @param value значение данного поля.
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * Возвращает имя поля из источника данных из которого будет выбираться
     * реальное значение данного поля (то значение, которое будет отправляться на сервер).
     * @return имя поля из источника данных значения из которого впоследствии будут отправляться на сервер.
     */
    public String getValueField() {
        return valueField;
    }
    /**
     * Указывает имя поля из источника данных из которого будет выбираться
     * реальное значение данного поля (то значение, которое будет отправляться на сервер).
     * @param valueField имя поля из источника данных значения из которого впоследствии будут отправляться на сервер.
     */
    public void setValueField(final String valueField) {
        this.valueField = valueField;
    }

    /**
     * Возвращает имя поля из источника данных значения которого отображаются в интерфейсе пользователю
     * (на сервер они по умолчанию не отправляются).
     * В большинстве случаев свойства <code>valueField</code> и <code>displayField</code> указывают на разные поля в источнике данных.
     * @return имя поля в источнике данных значения которого отображаются в пользовательском интерфейсе.
     */
    public String getDisplayField() {
        return displayField;
    }
    /**
     * Указывает имя поля из источника данных значения которого отображаются в интерфейсе пользователю
     * (на сервер они по умолчанию не отправляются).
     * В большинстве случаев свойства <code>valueField</code> и <code>displayField</code> указывают на разные поля в источнике данных.
     * @param displayField имя поля в источнике данных значения которого отображаются в пользовательском интерфейсе.
     */
    public void setDisplayField(final String displayField) {
        this.displayField = displayField;
    }


    /**
     * Возвращает источник данных используемый данным компонентом для получения списка элементов выбора.
     * @return модель источника данных используемого данным компонентом.
     */
    public Store getStore() {
        return store;
    }
    /**
     * Указывает источник данных используемый данным компонентом для получения списка элементов выбора.
     * @param store модель источника данных используемого данным компонентом.
     */
    public void setStore(final Store store) {
        this.store = store;
        this.storeRef = null;
    }

    /**
     * <p>Автоматически конфигурирует компонент для отображения локального перечня элементов выбора,
     * каждый из которых задан объектом реализующим интерфейс {@link Reference}.</p>
     * В ходе выполнения данного метода будет инициализирована модель хранилища с полями "id", "title".
     *
     * @param items  задает перечень элементов выбора для данного компонента.
     * @return ссылка на сконструированное хранилище используемое данным компонентом.
     */
    public Store assignStore(final Collection<Reference> items) {
        storeRef = null;
        store = new Store();
        store.setReader( new JsonReader("id", "title") );
        store.setData(items);
        valueField = "id";
        displayField = "title";
        mode = Mode.LOCAL;
        triggerAction = TriggerAction.ALL;
        forceSelection = true;
        return store;
    }
    public Store assignStore(final Reference... items) {
        storeRef = null;
        store = new Store();
        store.setReader( new JsonReader("id", "title") );
        store.setData(items);
        valueField = "id";
        displayField = "title";
        mode = Mode.LOCAL;
        triggerAction = TriggerAction.ALL;
        forceSelection = true;
        return store;
    }

    /**
     * Возвращает ссылку на используемый компонентом источник данных.
     * Данный вариант указания источника данных может использоваться в случае когда один источник используется
     * бОльшим количеством компонент.
     * @return ссылка на используемый компонентом источник данных.
     *      Может быть в виде строкового идентификатора хранилища под которым он зарегистрирован в
     * реестре ExtJS <code>Ext.data.StoreMgr</code> или в виде некоторого выражения на языке javascript
     * результатом вычисления которого будет <code>Ext.data.Store</code>. 
     */
    public JSExpression getStoreRef() {
        return storeRef;
    }
    /**
     * Указывает ссылку на используемый компонентом источник данных.
     * Данный вариант указания источника данных может использоваться в случае когда один источник используется
     * бОльшим количеством компонент.
     * @param storeRef ссылка на используемый компонентом источник данных, задаваемая в виде
     * выражения на языке javascript результатом вычисления которого будет экземпляр <code>Ext.data.Store</code>
     * или идентификатор под которым искомое хранилище было зарегистрировано в реестре <code>Ext.data.StoreMgr</code>.
     */
    public void setStoreRef(final JSExpression storeRef) {
        this.storeRef = storeRef;
        this.store = null;
    }
    /**
     * Указывает ссылку на используемый компонентом источник данных.
     * Данный вариант указания источника данных может использоваться в случае когда один источник используется
     * бОльшим количеством компонент.
     * @param storeId ссылка на используемый компонентом источник данных, задаваемая в виде
     * идентификатора под которым искомое хранилище было зарегистрировано в реестре <code>Ext.data.StoreMgr</code>.
     */
    public void setStoreRef(final String storeId) {
        this.storeRef = storeId!=null ? new JSExpression('"'+storeId+'"') : null;
        this.store = null;
    }

    /**
     * Свойство определяет с какими данными приходится работать в хранилище.
     * В случае работы с удаленными данными они будут подгружаться при первом запросе к ним.
     * @return {@link Mode#REMOTE} (по умолчанию) если хранилище работает с удаленными данными и
     *         {@link Mode#LOCAL} если хранилище работает с локальными данными.
     */
    public Mode getMode() {
        return mode;
    }
    /**
     * Свойство определяет с какими данными приходится работать в хранилище.
     * @param mode {@link Mode#REMOTE} (по умолчанию) если хранилище работает с удаленными данными.
     */
    public void setMode(final Mode mode) {
        this.mode = mode!=null ? mode : Mode.REMOTE;
    }

    /**
     * Влияет на то какие данные будут отображены в выпадающем списке когда пользователь кликает по переключателю.
     * Если <code>triggerAction</code> = {@link TriggerAction#ALL} то в списке отображаются все записи из хранилища
     * в противном случае, если <code>triggerAction</code> = {@link TriggerAction#QUERY} то в списке будут отображаться
     * только те записи которые удовлетворяют запросу (начинаются с тех букв что пользователь написал в поле ввода).
     * @return {@link TriggerAction#QUERY} (по умолчанию) если по клику на триггере в выпадающем списке должны
     * отображаться только отфильтрованные данные.
     */
    public TriggerAction getTriggerAction() {
        return triggerAction;
    }
    /**
     * Указывает какие данные будут отображены в выпадающем списке когда пользователь кликает по переключателю.
     * Если <code>triggerAction</code> = {@link TriggerAction#ALL} то в списке отображаются все записи из хранилища
     * в противном случае, если <code>triggerAction</code> = {@link TriggerAction#QUERY} то в списке будут отображаться
     * только те записи которые удовлетворяют запросу (начинаются с тех букв что пользователь написал в поле ввода).
     * @param triggerAction {@link TriggerAction#QUERY} если по клику на триггере в выпадающем списке должны 
     * отображаться только отфильтрованные данные.
     */
    public void setTriggerAction(final TriggerAction triggerAction) {
        this.triggerAction = triggerAction!=null ? triggerAction : TriggerAction.QUERY;
    }

    /**
     * Возвращает имя параметра запроса на получение данных из удаленного источника в
     * котором будет указываться введенный пользователем фильтр.
     * Если это свойство не указано то в ExtJS используется значение по умолчанию: <code>"query"</code>.
     * @return имя параметра в котором будет передаваться на сервер введенный пользователем 
     * фильтр на отображаемые в списке данные.
     */
    public String getQueryParam() {
        return queryParam;
    }
    /**
     * Указывает имя параметра запроса на получение данных из удаленного источника в
     * котором будет указываться введенный пользователем фильтр.
     * Если это свойство не указано то в ExtJS используется значение по умолчанию: <code>"query"</code>.
     * @param queryParam имя параметра в котором будет передаваться на сервер введенный пользователем
     * фильтр на отображаемые в списке данные.
     */
    public void setQueryParam(final String queryParam) {
        this.queryParam = queryParam;
    }

    /**
     * Используется для организации постраничного просмотра данных в списке (работает как правило с удаленными данными).
     * Возвращает максимальное количество записей отображаемых в выпадающем списке на одной странице или
     * <code>0</code> (значение по умолчанию) если организации постраничного просмотра не требуется.
     * @return максимальное кол-во записей отображаемых на странице или 0 (по умолчанию) если
     * постраничный просмотр не требуется. 
     */
    public int getPageSize() {
        return pageSize;
    }
    /**
     * Используется для организации постраничного просмотра данных в списке (работает как правило с удаленными данными).
     * Указывает максимальное количество записей отображаемых в выпадающем списке на одной странице или
     * <code>0</code> (значение по умолчанию) если организации постраничного просмотра не требуется.
     * @param pageSize максимальное кол-во записей отображаемых на странице или 0 (по умолчанию) если
     * постраничный просмотр не требуется.
     */
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Возвращает <code>false</code> если пользователь может в этом поле вводит любой текст котором нет соответствующих
     * записей в хранилище данных. В противном случае если введенному пользователем тексту не соответствует ни одна запись в списке
     * этот введенный текст автоматически сбрасывается в исходное состояние.
     * @return <code>false</code> если пользователь может вводить любой текст в поле ввода.
     * Значение свойства по умолчанию: <code>false</code>.
     */
    public boolean isForceSelection() {
        return forceSelection;
    }
    /**
     * Определяет может ли пользователь вводить в поле совершенно любой текст, включая тот который не соответствует ни одной
     * записи
     * Возвращает <code>false</code> если пользователь может в этом поле вводит любой текст котором нет соответствующих
     * записей в хранилище данных. В противном случае если введенному пользователем тексту не соответствует ни одна запись в списке
     * этот введенный текст автоматически сбрасывается в исходное состояние.
     * @param forceSelection <code>false</code> если пользователь может вводить любой текст в поле ввода.
     * Значение свойства по умолчанию: <code>false</code>.
     */
    public void setForceSelection(final boolean forceSelection) {
        this.forceSelection = forceSelection;
    }

    /**
     * Возвращает <code>true</codе> если компонент должен предугадывать искомую пользователем запись
     * по вводимому им в поле тексту.
     * @return <code>true</codе> если компонент должен предугадывать искомую пользователем запись
     * по вводимому им в поле тексту. Значение свойства по умолчанию: <code>false</code>.
     */
    public boolean isTypeAhead() {
        return typeAhead;
    }
    /**
     * Указывает должен ли компонент предугадывать искомую пользователем запись по вводимому им в поле тексту.
     * @param typeAhead <code>true</codе> если компонент должен предугадывать искомую пользователем запись
     * по вводимому им в поле тексту. Значение свойства по умолчанию: <code>false</code>.
     */
    public void setTypeAhead(final boolean typeAhead) {
        this.typeAhead = typeAhead;
    }

    /**
     * Возвращает <code>true</code> если пользователь может вводить текст в поле ввода.
     * @return <code>true</code> если пользователю разрешается вводить текст в поле ввода для
     * фильтрации отображаемых в списке записей.
     * Значение свойства по умолчанию: <code>true</code>.
     */
    public boolean isEditable() {
        return editable;
    }
    /**
     * Определяет может ли пользователь вводить текст в поле ввода.
     * @param editable <code>true</code> если пользователю разрешается вводить текст в поле ввода для
     * фильтрации отображаемых в списке записей.
     * Значение свойства по умолчанию: <code>true</code>.
     */
    public void setEditable(final boolean editable) {
        this.editable = editable;
    }

    /**
     * Возвращает <code>true</code> если пользователь может изменять размеры всплывающего списка.
     * @return <code>true</code> если пользователь может изменять размеры всплывающего списка.
     * Значение свойства по умолчанию: <code>false</code>.
     */
    public boolean isResizable() {
        return resizable;
    }
    /**
     * Указывает может ли пользователь изменять размеры всплывающего списка.
     * @param resizable <code>true</code> если пользователь может изменять размеры всплывающего списка.
     */
    public void setResizable(final boolean resizable) {
        this.resizable = resizable;
    }

    /**
     * Возвращает строку текста которая будет отображаться во всплывающем списке над предлагаемыми к выбору элементами.
     * @return строка текста которая будет отображаться во всплывающем списке над предлагаемыми к выбору элементами
     *      или <code>null</code> если над элементами ничего отображаться не должно.
     * Значение свойства по умолчанию: <code>null</code>.
     */
    public String getTitle() {
        return title;
    }
    /**
     * Указывает строку текста которая будет отображаться во всплывающем списке над предлагаемыми к выбору элементами.
     * @param title строка текста которая будет отображаться во всплывающем списке над предлагаемыми к выбору элементами
     *      или <code>null</code> если над элементами ничего отображаться не должно.
     */
    public void setTitle(final String title) {
        this.title = StringUtil.trim(title);
    }

    /**
     * Возвращает строку текста отображаемую во всплывающем списке в том случае когда в этом списке нет
     * ни одной записи предлагаемой пользователю к выбору.
     * @return текст, отображаемый во всплывающем списке в том случае когда в этом списке нет
     * ни одной записи предлагаемой пользователю к выбору.
     * Значение свойства по умолчанию: <code>null</code>.
     */
    public String getListEmptyText() {
        return listEmptyText;
    }
    /**
     * Указывает строку текста отображаемую во всплывающем списке в том случае когда в этом списке нет
     * ни одной записи предлагаемой пользователю к выбору.
     * @param listEmptyText текст, отображаемый во всплывающем списке в том случае когда в этом списке нет
     * ни одной записи предлагаемой пользователю к выбору.
     */
    public void setListEmptyText(final String listEmptyText) {
        this.listEmptyText = StringUtil.trim(listEmptyText);
    }


    @Override
    public void invoke(final JsonWriter out) throws Exception {
        final ComponentContext ctx = getContext();
        setName( ctx.getClientId() + ".value" );
        if (isStateful()) {
            final String svalue = StringUtil.trim( (String)ctx.getAttribute("value", Scope.PR_ST) );
            if (svalue!=null) {
                value = svalue;
            }
            ctx.setAttribute("value", svalue, Scope.STATE);
        }
        out.beginObject();
        out.writeProperty("xtype", "combo");
        renderContent(out);
        out.endObject();
    }

    @Override
    protected void renderContent(final JsonWriter out) throws Exception {
        super.renderContent(out);
        out.writeProperty("hiddenName", name);
        if (value!=null)
            out.writeProperty("value", value);

        if (tabIndex != null)
            out.writeProperty("tabIndex", tabIndex);
        if (!allowBlank)
            out.writeProperty("allowBlank", false);
        if (emptyText != null)
            out.writeProperty("emptyText", emptyText);

        if (valueField!=null)
            out.writeProperty("valueField", valueField);
        if (displayField!=null)
            out.writeProperty("displayField", displayField);

        if (store!=null) {
            out.writeProperty("store", store);
        } else
        if (storeRef!=null) {
            out.writeProperty("store", storeRef);
        }

        if (mode!=Mode.REMOTE)
            out.writeProperty("mode", mode);
        if (triggerAction!=TriggerAction.QUERY)
            out.writeProperty("triggerAction", triggerAction);
        if (queryParam!=null)
            out.writeProperty("queryParam", queryParam);
        if (pageSize>0)
            out.writeProperty("pageSize", pageSize);

        if (forceSelection)
            out.writeProperty("forceSelection", true);
        if (typeAhead)
            out.writeProperty("typeAhead", true);
        if (!editable)
            out.writeProperty("editable", false);
        if (resizable)
            out.writeProperty("resizable", true);
        if (title!=null)
            out.writeProperty("title", title);
        if (listEmptyText!=null)
            out.writeProperty("listEmptyText", listEmptyText);

        final ComponentContext ctx = getContext();
        ctx.getResources().attachScript(ctx.encodeThemeURL("/pkgs/data-foundation.js", false));
        ctx.getResources().attachScript(ctx.encodeThemeURL("/pkgs/data-json.js", false));
        ctx.getResources().attachScript(ctx.encodeThemeURL("/pkgs/data-list-views.js", false));
        if (resizable)
            ctx.getResources().attachScript(ctx.encodeThemeURL("/pkgs/resizable.js", false));
    }

    @Override
    protected Set<String> getSupportedEvents() {
        return ComboBox.EVENTS;
    }
}
