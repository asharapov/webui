package org.echosoft.framework.ui.core;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import org.echosoft.framework.ui.core.theme.Theme;

/**
 * Содержит всю доступную информацию о пользовательском запросе
 * //TODO: озаботиться поддержкой порталов (методы encodeActionURL(...), encodeRenderURL(...) ) 
 * @author Anton Sharapov
 */
public interface RequestContext {

    /**
     * Возвращает коллекцию ресурсов (скриптов, таблиц стилей) которые должны быть слинкованы с формируемой страницей.
     * @return  коллекция ресурсов которые используются в формируемой для пользователя странице.
     */
    public Resources getResources();

    /**
     * Возвращает коллекцию сообщений ассоциированных для тех или иных компонентов которые участвуют в отрисовке страницы.
     * @return коллекция сообщений для компонент участвующих в отрисовке страницы.
     */
    public Messages getMessages();

    /**
     * Возвращает тему используемую при обработке данного запроса.
     * @return  выбранная тема.
     */
    public Theme getTheme();
    

    /**
     * Возвращает информацию по всем атрибутам из указанного пространства имен из данного контекста.
     * @param scope  пространство имен из которого извлекается информация по всем доступным в нем атрибутам.
     * @return набор пар 'имя атрибута' -> 'значение атрибута' для выбранного пространства имен.
     */
    public Map<String,Object> getAttributesMap(Scope scope);

    /**
     * Возвращает перечисление имен атрибутов доступных в указанном пространстве имен из данного контекста.
     * @param scope  пространство имен из которого извлекается информация по всем доступным в нем атрибутам.
     * @return  перечисление имен атрибутов доступных в данном пространстве имен.
     */
    public Enumeration<String> getAttributeNames(Scope scope);

    /**
     * Возвращает значение атрибута по его имени из указанного пространства имен.
     * @param name  имя атрибута в указанном пространстве имен.
     * @param scope  пространство имен в котором осуществляется поиск атрибута по его имени.
     * @return  значение искомого атрибута или <code>null</code> если такой атрибут не был найден.
     */
    public <T> T getAttribute(String name, Scope scope);

    /**
     * Ищет атрибут по его имени в одном указанных пространствах имен и возвращает его значение.<br/>
     * Сначала метод ищет атрибут в пространстве имен стоящем в первом элементе массива, потом во втором, и т.д.
     * Если метод не нашел указанный атрибут ни в одном из перечисленных пространств имен то метод возвращает <code>null</code>.
     * @param name  имя атрибута.
     * @param scopes  массив пространств имен в которых будет последовательно осуществляться поиск запрошенного атрибута.
     * @return  объект зарегистрированный под указанным в аргументе именем в одном из перечисленных пространств имен или <code>null</code>
     *      если такой атрибут не был найден ни в одном из перечисленных пространств имен.
     */
    public <T> T getAttribute(String name, Scope scopes[]);

    /**
     * Ассоциирует объект с некоторым именем в одном из пространств имен.
     * @param name  Имя атрибута находящегося в соответствующем пространстве имен.
     * @param value  объект который должен быть ассоциирован с указанным именем в соотв. пространстве имен. Если аргумент равен <code>null</code> то
     *          вызов метода приведет к тому же эффекту что и вызов метода {@link #removeAttribute(String, Scope)}.
     * @param scope  пространство имен. Не может быть <code>null</code>.
     */
    public void setAttribute(String name, Object value, Scope scope);

    /**
     * Удаляет объект ассоциированный с указанным именем соответствующего пространства имен.
     * @param name  Имя атрибута находящегося в соответствующем пространстве имен.
     * @param scope  пространство имен. Не может быть <code>null</code>.
     * @return  Объект который был ассоциирован с данным именем в пространстве имен перед удалением.
     */
    public <T> T removeAttribute(String name, Scope scope);

    /**
     * Возвращает перечисление имен доступных в контексте параметров запроса.</br> 
     * Метод работает точно так же как и <code>@link #getAttributeNames(Scope.PARAMS)</code>.
     * @return возвращает перечисление имен всех параметров запроса.
     */
    public Enumeration<String> getParameterNames();

    /**
     * Возвращает значение указанного параметра запроса.
     * @param name  имя параметра запроса.
     * @return  строка со значением параметра или <code>null</code> если в запросе запрошенный параметр не был указан.
     */
    public String getParameter(String name);

    /**
     * Возвращает все значения указанного параметра запроса.
     * @param name  имя параметра запроса.
     * @return  массив строк со значением параметра или <code>null</code> если в запросе запрошенный параметр не был указан.
     */
    public String[] getParameterValues(String name);

    /**
     * Возвращает список всех <code>cookies</code> которые были получены из пользовательского запроса.
     * @return массив всех <code>cookies</code> из пользовательского запроса или <code>null</code> если cookies отсутствуют в запросе.
     */
    public Cookie[] getRequestCookies();

    /**
     * Возвращает значение указанного в аргументе заголовка запроса.
     * @param name название заголовка запроса.
     * @return  значение данного заголовка или <code>null</code> если таковой отсутствует в запросе.
     */
    public String getRequestHeader(String name);

    /**
     * Возвращает информацию по всем заголовкам запроса. Для каждого заголовка указано его название (ключ) и массив всех его значений (значение).
     * @return  информация по всем заголовкам запроса.
     */
    public Map<String,String[]> getRequestHeaders();

    /**
     * Добавляет указанный <code>cookie</code> в ответ на пользовательский запрос.
     * @param cookie  экземпляр <code>cookie</code> который должен быть отправлен пользователю (транслируется в заголовок "Set-Cookie").
     */
    public void addResponseCookie(Cookie cookie);

    /**
     * Проверяет наличие указанного заголовка в ответе на пользовательский запрос.
     * @param name  название заголовка.
     * @return <code>true</code> если указанный заголовок уже присутствует в ответном сообщении от системы пользователю.
     */
    public boolean containsResponseHeader(String name);

    /**
     * Добавляет новый заголовок в ответное сообщение пользователю.
     * @param name  название заголовка.
     * @param value  значение заголовка.
     */
    public void addResponseHeader(String name, String value);

    /**
     * Устанавливает новое значение заголовка в ответном сообщении пользователю.
     * @param name  название заголовка.
     * @param value  новое значение заголовка.
     */
    public void setResponseHeader(String name, String value);


    /**
     * Сбрасывает текущую пользовательскую сессию.
     */
    public void invalidateSession();

    /**
     * Включает содержимое указанного ресурса в заданный поток.<br/>
     * <strong>Важно!</strong> Метод работает только с текстовыми ресурсами.
     * @param url  URL ресурса содержимое которого должно быть включено в выходной поток.
     * @param out  выходной символьный поток.
     * @throws java.io.IOException в случае каких-либо проблем.
     */
    public void include(String url, Writer out) throws IOException;

    /**
     * Возвращает имя под которым был зарегистрирован пользователь, инициировавший данный запрос.
     * В случае, если пользователь не был аутентифицирован, метод вернет <code>null</code>.
     * @return  имя пользователя инициировавшего данный запрос>
     * @see javax.servlet.http.HttpServletRequest#getRemoteUser()     
     */
    public String getUserName();

    /**
     * Возвращает информацию об используемом пользователем браузере.
     * @return  информация об используемом пользователем браузере.
     */
    public Agent getAgent();

    /**
     * Возвращает информацию об используемой при обработке данного запроса локали.
     * @return  локаль которая должна использоваться при обработке данного запроса.
     */
    public Locale getLocale();

    /**
     * Возвращает название кодировки символов, используемой в запросе.
     * Метод возвращает <code>null</code> в случае когда запрос не содержит явного указания на его кодировку.
     * @return  используемая в запросе кодировка символов.
     * @see javax.servlet.http.HttpServletRequest#getCharacterEncoding()
     */
    public String getCharacterEncoding();

    /**
     * Возвращает строку содержащую уникальный идентификатор сессии, в рамках которой обрабатывается данный запрос.
     * @return  идентификатор сессии HTTP.
     * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()     
     */
    public String getSessionId();

    /**
     * Возвращает название схемы используемой в данном запросе (к примеру http, https, ftp).
     * Различные схемы имеют различные правила по которым формируются URL (см. RFC 1738).
     * @return  схема, используемая в данном запросе.
     * @see javax.servlet.http.HttpServletRequest#getScheme()   
     */
    public String getScheme();

    /**
     * Возвращает имя или IP адрес сервера, обрабатывающего данный запрос.
     * @return  имя или IP адрес сервера обрабатывающего данный запрос.
     * @see javax.servlet.http.HttpServletRequest#getServerName()
     */
    public String getHost();

    /**
     * Возвращает номер порта на сервере по которому поступил данный запрос.
     * @return номер порта сервера.
     * @see javax.servlet.http.HttpServletRequest#getServerPort()  
     */
    public int getPort();

    /**
     * Часть URL, определяющая путь до корневого каталога данного веб приложения.
     * Всегда начинается с символа "/" но никогда не заканчивается этим символом. Для сервлетов в корневом контексте метод
     * возвращает пустую строку.
     * @return  часть URL, определяющая путь до корневого каталога данного веб приложения.
     * @see javax.servlet.http.HttpServletRequest#getContextPath() 
     */
    public String getContextPath();

    /**
     * Часть URL определяющая путь до вызываемого сервлета начиная от корневого каталога данного приложения.
     * Всегда начинается с символа "/" и включает в себя либо имя сервлета либо путь до него.
     * @return  часть URL, определяющая путь до вызываемого сервлета.
     * @see javax.servlet.http.HttpServletRequest#getServletPath()
     */
    public String getServletPath();

    /**
     * <p>Returns any extra path information associated with the URL the client sent when it made this request.
     * The extra path information follows the servlet path but precedes the query string and will start
     * with a "/" character.</p>
     * <p>This method returns null if there was no extra path information.</p>
     * @return  a {@link String}, decoded by the web container, specifying extra path information
     * that comes after the servlet path but before the query string in the request URL; or null
     * if the URL does not have any extra path information.
     * @see javax.servlet.http.HttpServletRequest#getPathInfo()
     */
    public String getPathInfo();

    /**
     * Воссоздает URL запроса который был отправлен клиентом.<br/>
     * Он вычисляется как:<br/>
     * <code> [scheme]://[host]:[port][context path][servlet path][path info]</code>
     * @return request url.
     * @see javax.servlet.http.HttpServletRequest#getRequestURI()   
     */
    public String getRequestUrl();

    /**
     * Преобразовывает относительные пути к ресурсам по следующим правилам:</br/>
     * <ol>
     *  <li> Если путь равен <code>null</code> или пустой строке то возвращается ссылка на текущий путь (по которому был сделан текущий запрос).
     *  <li> Если путь представлен в абсолютной форме, т.е. имеет вид: <code> schema://host[:port]/somepath </code> то он возвращается без изменений.
     *  <li> Если путь представлен в относительной форме и начинается с символа <code>/</code> то он будет дополнен до следующего вида:
             <pre> /app/originalpath </pre>
     *       где: <br/>
     *       <code>app</code> - корневой путь к приложению на сервере;<br/>
     *       <code>originalpath</code> - исходный путь к ресурсу полученный в аргументе метода.
     *  <li> Во всех прочих случаях путь будет дополнен до следующего вида:
     *       <pre> /app/servlet/pathinfo/originalpath </pre>
     *       где:<br/>
     *       <code>app</code> - корневой путь к приложению на сервере;<br/>
     *       <code>servlet</code> - путь к текущему сервлету в приложении;<br/>
     *       <code>pathinfo</code> - опционально;<br/>
     *       <code>originalpath</code> - исходный путь к ресурсу полученный в аргументе метода.
     * </ol>
     * @param url  путь к ресурсу который надо проверить/преобразовать.
     * @return  URL, используя который, клиент может получить содержимое затребованного ресурса.
     *      В зависимости от реализации данного интерфейса, этот URL может быть представлен как в абсолютной так и в относительной форме.
     */
    public String encodeURL(String url);

    /**
     * Формирует валидный URL для указанного в аргументе логического пути к определенному ресурсу текущей темы относительно корневого каталога последней.<br/>
     * Таким образом путь <pre> /originalpath </pre> будет преобразован в URL одного из следующих видов:
     * <ul>
     *  <li> <code> /app/resources/theme/originalpath </code> - дает доступ клиентскому приложению к ресурсу <code>/originalpath</code> доступной из <strong>текущей</strong> темы.
     *  <li> <code> /app/resources/lib/themepath/originalpath </code> - данная т.н. "постоянная" ссылка дает доступ к ресурсу заданной темы вне зависимости от того, является ли она к тому времени текущей или нет.
     * </ul>
     * где: <br/>
     * <code>app</code> - корневой путь к приложению на сервере;<br/>
     * <code>resources</code> - путь в приложении к сервлету фреймворка, отвечающему за доступ к ресурсам доступных приложению тем;<br/>
     * <code>theme</code> - флаг, который указывает что далее идет путь к ресурсу из текущей темы.<br/>
     * <code>lib</code> - флаг, который указывает что далее идет путь к ресурсу расположенному где-то в ClassPath приложения.<br/>
     * <code>originalpath</code> - исходный путь к ресурсу полученный в аргументе метода.
     * @param path  логический путь к определенному ресурсу темы относительно корневого каталога темы.
     * @param persistent  определяет тип генерируемой ссылки (см. выше).
     * @return  URL, используя который, клиент может получить содержимого затребованного ресурса из данной темы.
     *      В зависимости от реализации данного интерфейса, этот URL может быть представлен как в абсолютной так и в относительной форме.
     */
    public String encodeThemeURL(String path, boolean persistent);

}
