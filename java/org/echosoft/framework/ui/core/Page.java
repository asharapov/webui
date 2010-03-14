package org.echosoft.framework.ui.core;

import java.io.Writer;

import org.echosoft.common.io.FastStringWriter;
import org.echosoft.common.json.JsonContext;
import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.spi.CleanStrategies;
import org.echosoft.framework.ui.core.theme.Theme;

/**
 * Базовая реализация корневого компонента на странице, основной задачей которого является корректное начало и окончание обработки пользовательских запросов.
 * Данный класс является стартовой точкой задачи связанной с отрисовкой содержимого данной страницы.
 * @author Anton Sharapov
 */
public abstract class Page implements UIComponent {

    private final UIContext uctx;
    private String title;
    private String iconRef;
    private String viewId;
    private int viewRank;
    private CleanStrategy cleanStrategy;
    private JsonContext jctx;
    private boolean staticPage;

    private transient ComponentContext ctx;

    public Page(final UIContext uctx) {
        this.uctx = uctx;
        this.jctx = Application.jsonContext;
        this.cleanStrategy = CleanStrategies.RANK;
        this.staticPage = false;
    }

    /**
     * Возвращает полный контекст пользовательского запроса, обработка проводится в настоящий момент в текущем потоке.
     * Результаты данного вызова могут быть использованы в дочерних классах.
     * @return  полный контекст текущего пользовательского запроса.
     */
    protected UIContext getUIContext() {
        return uctx;
    }

    /**
     * Возвращает экземпляр {@link ComponentContext} ассоциированный с текущим пользовательским запросом.<br/>
     * Данный экземпляр конструируется путем вызова: <pre> return new ComponentContext( getUIContext() ); </pre>
     * @return контекст компонента по обработке текущего запроса пользователя.
     */
    public ComponentContext getContext() {
        if (ctx==null)
            ctx = new ComponentContext(uctx);
        return ctx;
    }


    /**
     * Возвращает название страницы в заголовке браузера (оборачивается в тег &lt;title&gt;).
     * @return  название страницы которое будет отображаться в заголовке страницы.
     */
    public String getTitle() {
        return title;
    }
    /**
     * Устанавливает название страницы которое будет отображаться в заголовке страницы (внутри тега &lt;title&gt;)).
     * @param title  новое название страницы в заголовке.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Возвращает используемую на странице иконку, отображаемую рядом с навигационной строкой браузера.
     * @return  ссылку на используемую на данной странице иконку.
     */
    public String getIconRef() {
        return iconRef;
    }
    /**
     * Устанавливает ссылку на иконку, которая будет отображаться на странице рядом с навигационной строкой браузера.
     * @param iconRef  ссылка заданная либо в абсолютной форме (вида href://host:port/app/path/icon.gif) либо в относительной форме (вида /aaa/bbb/icon.gif).
     *                 В последнем случае ссылка будет преобразована к виду /app/aaa/bbb/icon.gif, где app - путь к корневой странице приложения на сервере.
     */
    public void setIconRef(final String iconRef) {
        this.iconRef = iconRef;
    }

    /**
     * Возвращает идентификатор состояния по умолчанию для данной страницы.
     * Это состояние будет указываться в контексте запроса как текущее в начале обработки компонентом пользовательских запросов.
     * @return  идентификатор состояния по умолчанию для данной страницы.
     */
    public String getViewId() {
        return viewId;
    }
    /**
     * Устанавливает дескриптор состояния который будет устанавливаться по умолчанию при начале обработки компонентом пользовательских запросов.
     * @param viewId  идентификатор состояния по умолчанию для данной страницы.
     */
    public void setViewId(final String viewId) {
        this.viewId = viewId!=null ? viewId : "";
    }

    /**
     * Возвращает ранг состояния по умолчанию для данной страницы.
     * Это состояние будет указываться в контексте запроса как текущее в начале обработки компонентом пользовательских запросов.
     * @return  ранг состояния по умолчанию для данной страницы.
     */
    public int getViewRank() {
        return viewRank;
    }
    /**
     * Устанавливает ранг состояния который будет устанавливаться по умолчанию при начале обработки компонентом пользовательских запросов.
     * @param viewRank  ранг состояния по умолчанию для данной страницы.
     */
    public void setViewRank(final int viewRank) {
        this.viewRank = viewRank;
    }

    /**
     * Устанавливает стратегию, используемую для анализа более не используемой информации в менеджере состояний форм.
     * Основываясь на определенных правилах позволяет удалять из менеджера состояния тех форм которые уже утратили свою актуальность.
     * @return  текущая стратегия удаления состояний форм которые уже не нужны нам для дальнейшей работы.
     */
    public CleanStrategy getCleanStrategy() {
        return cleanStrategy;
    }
    /**
     * Устанавливает стратегию для анализа неиспользуемых более состояний форм хранимых в контексте текущего запроса (менеджер состояний)
     * @param cleanStrategy  текущая стратегия удаления состояний форм которые уже не нужны нам для дальнейшей работы.
     */
    public void setCleanStrategy(final CleanStrategy cleanStrategy) {
        if (cleanStrategy==null)
            throw new IllegalArgumentException("clean strategy must be specified");
        this.cleanStrategy = cleanStrategy;
    }

    /**
     * Поскольку основная часть контента страницы указывается в формате JSON то нам важно иметь возможность влияния на те или иные аспекты
     * кодирования JSON выражений в выходной поток. {@link JsonContext} является стартовой точкой через которую можно поменять все возможные
     * параметры кодирования JSON выражений. Может использоваться для изменения следующих категорий параметров:
     * <ol>
     *  <li> Указания конкретной реализации {@link JsonWriter} отвечающего непосредственно за сериализацию выражений в поток.
     *         По умолчанию используется класс, реализующий компактную форму записи с включенным контролем целостности структуры данных.
     *         При желании можно использовать облегченную реализацию, без проверок структуры но чуть более быструю, или реализацию,
     *         осуществляющую форматированный вывод структуры данных в поток.
     *  <li> Указания как следует форматировать имена полей в JSON структуре: полностью в соответствии со стандартами JSON или
     *         в компактной форме (без оборачивания имен в кавычки там где это только можно, - используется по умолчанию).
     *  <li> Указания как следует сериализовать экземпляры определенных классов или интерфейсов. Дает возможность сопоставить определенному java классу свой
     *          персональный кодек, отвечающий за сериализацию его объектов в формат JSON.
     * </ol>
     * По умолчанию используется контекст по умолчанию, доступный по ссылке {@link Application#jsonContext}.
     * @see org.echosoft.common.json.JsonContext
     * @see org.echosoft.common.json.JsonWriter
     * @see org.echosoft.common.json.JsonSerializer
     * @see org.echosoft.framework.ui.core.Application#jsonContext 
     * @return  ссылка на экземпляр {@link JsonContext} используемый при формировании данной страницы.
     */
    public JsonContext getJsonContext() {
        return jctx;
    }
    /**
     * Устанавливает контекст сериализации java объектов в JSON формат. Позволяет настроить те или иные аспекты представления данных в JSON формате.  
     * @param jctx  контекст сериализации, который будет использоваться при рендеринге данной страницы.
     *              Один экземпляр контекста может одновременно использоваться разными потоками для разных задач.
     * @see org.echosoft.common.json.JsonContext
     * @see org.echosoft.common.json.JsonWriter
     * @see org.echosoft.common.json.JsonSerializer
     * @see org.echosoft.framework.ui.core.Application#jsonContext
     */
    public void setJsonContext(final JsonContext jctx) {
        if (jctx==null)
            throw new IllegalArgumentException("JSON context must be specified");
        this.jctx = jctx;
    }

    /**
     * Возвращает <code>true</code> в случае когда структура страницы является статичной и мы можем единожды сконструированный экземпляр
     * {@link Page} использовать при обработке всех последущих пользовательских запросов к данной веб странице.<br/>
     * По умолчанию возвращает <code>false</code>.
     * @return  признак того возможно ли кэширование данного экземпляра {@link Page} и последующее его использование при обрбаботке последующих запросов к этой же веб странице.
     * По умолчанию кэширование запрещено и метод возвращает <code>false</code>.
     */
    public boolean isStatic() {
        return staticPage;
    }
    /**
     * Определяет можно ли использовать единожды созданный данный экземпляр {@link Page} при обработке последующих запросов к этому веб ресурсу или
     * следует каждый раз пересоздавать соответствующий экземпляр {@link Page} (данынй режим установлен по умолчанию).
     * @param staticPage  новое значение признака.
     */
    public void setStatic(boolean staticPage) {
        this.staticPage = staticPage;
    }

    /**
     * Рендерит данную страницу в качестве ответа на текущий пользовательский запрос, ассоциированный с текущим потоком.
     * Результат рендеринга помещается в поток возвращающий данные пользователю по мере их готовности.
     * @throws Exception  в случае каких-либо проблем возникших при обработке данной страницы.
     */
    public void invokePage() throws Exception {
        final Writer out = getUIContext().getResponseWriter();
        invokePage( out);
        out.flush();
    }

    /**
     * Рендерит данную страницу в качестве ответа на текущий пользовательский запрос, ассоциированный с текущим потоком.
     * Результат рендеринга следует поместить в переданный в аргументе выходной символьный поток.
     * @param out  ссылка на выходной поток.
     * @throws Exception  в случае каких-либо проблем возникших при обработке данной страницы.
     */
    public void invokePage(final Writer out) throws Exception {
        final UIContext uctx = getUIContext();
        final Resources resources = uctx.getResources();
        final Theme theme = uctx.getTheme();
        for (String uri : theme.getGlobalJSFiles()) {
            resources.attachScript(uctx.encodeThemeURL(uri,false));
        }
        for (String uri : theme.getGlobalStylesheets()) {
            resources.attachStyleSheet(uctx.encodeThemeURL(uri,false));
        }
        uctx.switchState(getViewId(), getViewRank());
        final FastStringWriter jwout = new FastStringWriter(2048);  // промежуточный буфер используемый для сериализации JSON выражений всех компонент на данной странице.
        final JsonWriter jw = getJsonContext().makeJsonWriter(jwout);
        invoke(jw);
        uctx.getStates().clean( getCleanStrategy() );

        out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">\n");
        out.write("<html>\n");
        out.write("<head>\n");
        out.write("  <title>");
        StringUtil.encodeHTMLText(out, getTitle());
        out.write("</title>\n");
        out.write("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
        out.write("  <meta http-equiv=\"Content-Script-Type\" content=\"text/javascript\">\n");
        out.write("  <meta http-equiv=\"Content-Style-Type\" content=\"text/css\">\n");
        if (getIconRef()!=null) {
            out.write("  <link type=\"image/x-icon\" href=\"");
            out.write( uctx.encodeURL(getIconRef()) );
            out.write("\" rel=\"shortcut icon\">\n");
        }
        for (Resources.StyleSheet resource : resources.getExternalStyleSheets()) {
            out.write("  <link type=\"");
            out.write(resource.getContentType());
            out.write("\" href=\"");
            out.write(resource.getUrl());
            out.write("\" rel=\"");
            out.write(resource.getRel());
            if (resource.getMedia()!=null) {
                out.write("\" media=\"");
                out.write(resource.getMedia());
            }
            if (resource.getTitle()!=null) {
                out.write("\" title=\"");
                out.write(resource.getTitle());
            }
            out.write("\"/>\n");
        }
        for (Resources.Script resource : resources.getExternalScripts()) {
            out.write("  <script type=\"");
            out.write(resource.getContentType());
            out.write("\" src=\"");
            out.write(resource.getUrl());
            if (resource.isDefered())
                out.write("\" defer=\"defer");
            out.write("\"></script>\n");
        }
        out.write("</head>\n");
        out.write("<body>\n");
        out.write("<style type=\"text/css\">\n");
        resources.writeOutStyles(out);
        out.write("</style>\n");
        out.write("<div style=\"display:none\">\n");
        resources.writeOutHTML(out);
        out.write("</div>\n");
        out.write("<script type=\"text/javascript\">\n");
        final String fn = StringUtil.trim(getInitCallbackFunction());
        if (fn!=null) {
            out.write(fn);
        }
        out.write("(function() {\n");
        init(uctx, out);
        resources.writeOutScripts(out);
        jwout.writeOut(out);
        out.write(fn!=null ? "});\n" : "})();\n");
        out.write("</script>\n");
        out.write("</body>\n");
        out.write("</html>\n");
        out.flush();
    }

    /**
     * Метод отвечает за генерацию javascript кода, отвечающего за инициализацию фреймворка на стороне клиента.
     * @param uctx  контекст обработки текущего запроса.
     * @param out  выходной поток куда должен быть помещен сгенерированный код.
     * @throws Exception  в случае каких-либо проблем.
     */
    protected abstract void init(final UIContext uctx, final Writer out) throws Exception;

    /**
     * Если код инициализации состояния страницы на стороне клиента должен быть выполнен по наступлению определенного события то
     * данный метод должен вернуть имя функции в вызов которой будет передан код отвечающий за инициализацию страницы.
     * @return  имя функции в которую должен быть передан код инициализации страницы или <code>null</code> если этот код инициализации
     * надо будет выполнить еще на стадии загрузки DOM страницы.
     */
    protected abstract String getInitCallbackFunction();

}
