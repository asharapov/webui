package org.echosoft.framework.ui.core;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.echosoft.common.json.JsonContext;
import org.echosoft.common.model.Version;
import org.echosoft.framework.ui.core.spi.CleanStrategies;
import org.echosoft.framework.ui.core.spi.ContextFactory;
import org.echosoft.framework.ui.core.spi.GZIPStateSerializer;
import org.echosoft.framework.ui.core.spi.StateSerializer;
import org.echosoft.framework.ui.core.spi.generic.ServletUIContextFactory;
import org.echosoft.framework.ui.core.theme.ThemeManager;

/**
 * Содержит набор методов и констант используемых для базовой настройки фреймворка.
 * 
 * @author Anton Sharapov
 */
public class Application {

    /**
     * Версия фреймворка.
     */
    public static final Version VERSION;

    public static final ThemeManager THEMES_MANAGER;

    /**
     * Языковая локаль по умолчанию.
     */
    public static Locale DEFAULT_LOCALE;

    /**
     * Массив поддерживаемых приложением языковых локалей (включая локаль по умолчанию).
     */
    public static Locale[] SUPPORTED_LOCALES;

    /**
     * Путь к сервлету используемому для отдачи различного класса статических (в первую очередь) ресурсов таких как изображения,
     * файлы CSS и Javascript, и т.д.
     * Ресурсы могут получаться из разных источников: используемой фреймворком темы, из ресурсов приложения (jar Файлы), итд.
     */
    public static String RESOURCES_SERVLET_CONTEXT = "/resource";

    /**
     * Путь к сервлету отвечающему за отправку клиенту данных в формате AJAX.
     */
    public static String AJAX_SERVLET_CONTEXT = "/ajax";

    /**
     * Содержит правила для трансляции объектов различного типа в текстовые выражения JSON.
     */
    public static final JsonContext jsonContext = new JsonContext();

    /**
     * Логгер который рекомендован для использования всеми компонентами общего назначения.
     */
    public static final Log log;

    private static final HashMap<String, CleanStrategy> strategies;
    private static StateSerializer stateSerializer;
    private static ContextFactory contextFactory;

    static {
        DEFAULT_LOCALE = new Locale("ru", "");
        SUPPORTED_LOCALES = new Locale[]{DEFAULT_LOCALE, Locale.ENGLISH};

        log = LogFactory.getLog("topsbi.framework.ui");

        Version version = null;
        try {
            final Package pkg = Application.class.getPackage();
            version = Version.parseVersion(pkg.getSpecificationVersion());
        } catch (Exception e) {
            log.error("Unable to parse framework version: "+e.getMessage(), e);
        }
        VERSION = version;

        ThemeManager manager = null;
        try {
            manager = new ThemeManager();
        } catch (Exception e) {
            log.error("Unable to initialize themes manager: "+e.getMessage(), e);
        }
        THEMES_MANAGER = manager;

        strategies  = new HashMap<String, CleanStrategy>();
        registerCleanStrategy(CleanStrategies.ALL);
        registerCleanStrategy(CleanStrategies.CURRENT);
        registerCleanStrategy(CleanStrategies.NONE);
        registerCleanStrategy(CleanStrategies.PKG);
        registerCleanStrategy(CleanStrategies.RANK);
        setStateSerializer( new GZIPStateSerializer() );
        setContextFactory( new ServletUIContextFactory() );
    }

    /**
     * Регистрирует новый алгоритм удаления информации о неактуальных более данных в менеджере состояний.
     * @param strategy  алгоритм сборки мусора в менеджере состояний.
     */
    public static void registerCleanStrategy(final CleanStrategy strategy) {
        strategies.put(strategy.getId().toUpperCase(), strategy);
    }

    /**
     * Возвращает реализацию определенного алгоритма удаления информации о неактуальных более данных в менеджере состояний.
     * @param strategyId  идентификатор стратегии сборки мусора.
     * @return  стратегия сборки мусора или
     */
    public static CleanStrategy getCleanStrategy(final String strategyId) {
        return strategies.get( strategyId.toUpperCase() );
    }

    /**
     * Указывает используемый системой алгоритм сохранения состояний посещенных страниц между запросами пользователя (привязано к определенному окну браузера).<br/>
     * @return класс, реализующий определенный алгоритм сохранения состояний посещенных страниц между пользовательскими запросами.
     */
    public static StateSerializer getStateSerializer() {
        return stateSerializer;
    }

    /**
     * Указывает используемый системой алгоритм сохранения состояний посещенных страниц.
     * @param serializer  класс, реализующий определенный алгоритм сохранения состояний посещенных страниц между пользовательскими запросами.
     */
    public static void setStateSerializer(final StateSerializer serializer) {
        stateSerializer = serializer;
    }

    /**
     * Определяет алгоритм конструирования экземпляров {@link UIContext}.
     * @param contextFactory  класс, реализующий стратегию конструирования экземпляров {@link UIContext}
     */
    public static void setContextFactory(final ContextFactory contextFactory) {
        if (contextFactory==null)
            throw new NullPointerException("Context factory not specified");
        Application.contextFactory = contextFactory;
    }

    /**
     * Конструирует экземпляр {@link UIContext} на основании информации о текущем пользовательском запросе.
     * @param request  запрос пользователя.
     * @param response  ответ пользователю.
     * @param config  конфигурация сервлета на который пришел пользовательский запрос.
     * @return  полностью настроенный и готовый к применению экземпляр {@link UIContext}.
     * @throws Exception в случае каких-либо проблем с созданием или настройкой созданного экземпляра {@link UIContext}.
     */
    public static UIContext makeContext(final HttpServletRequest request, final HttpServletResponse response, final ServletConfig config) throws Exception {
        return contextFactory.makeContext(request, response, config);
    }

}
