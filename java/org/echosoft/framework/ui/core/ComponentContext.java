package org.echosoft.framework.ui.core;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.echosoft.common.collections.PrefixFilterEnumeration;
import org.echosoft.framework.ui.core.theme.Theme;

/**
 * Предоставляет ограниченный доступ к информации связанной с обработкой пользовательского запроса.<br/>
 * Активно используется при работе с компонентами, каждому из которых доступна только та часть данных из пользовательского запроса которая
 * касается только данного компонента или его дочерних компонент.
 *
 * @author Anton Sharapov
 */
public final class ComponentContext implements RequestContext {

    private static final char SEPARATOR = '.';
    private final UIContext root;
    private final String clientId;
    private final String id;
    private final String prefix;
    private final int prefixLength;

    /**
     * Конструирует новый экземпляр контекста компонента без накладывания каких-либо дополнительных ограничений на видимость данных.<b/>
     * Такие экземпляры класса <code>ComponentContext</code> могут использоваться в компонентах ассоциированных со всей формируемой страницей целиком.
     * @param root  экземпляр {@link UIContext}. Не может быть <code>null</code>.
     */
    public ComponentContext(final UIContext root) {
        this.root = root;
        this.id = "";
        this.clientId = root.getParamsPrefix();
        this.prefix = this.clientId;
        this.prefixLength = prefix.length();
    }

    private ComponentContext(final ComponentContext owner, final String id) {
        this.root = owner.root;
        this.id = id;
        this.clientId = owner.clientId + SEPARATOR + id;
        this.prefix = this.clientId + SEPARATOR;
        this.prefixLength = this.prefix.length();
    }

    /**
     * Возвращяет контекст для дочернего компонента идентификатор которого указан в параметре <code>childId</code>.
     * @param childId  идентификатор дочернего компонента.
     * @return  контекст дочернего компонента, еще более ограничивающий доступные данные (по сравненнию с текущим контекстом) и дающий доступ только
     *   к информации относящейся к указанному дочернему компоненту.
     */
    public ComponentContext getChild(String childId) {
        if (childId==null || (childId=childId.trim()).isEmpty())
            throw new IllegalArgumentException("Component id can't have an empty value");
        if (childId.charAt(0)=='_' || childId.indexOf(SEPARATOR)>=0)
            throw new IllegalArgumentException("Component id has invalid value: ["+childId+"]");
        return new ComponentContext(this, childId);
    }

    /**
     * Возвращает идентификатор компонента для которого был сконструирован данный экземпляр контекста.<br/>
     * Идентификатор должен быть уникален для всего множества компонент имеющих одного и того же родительского компонента и не должен содержать символ '.'.
     * @return  идентификатор компонента в контексте родительского компонента.
     */
    public String getId() {
        return id;
    }

    /**
     * Возвращает полный идентификатор  компонента, который включает в себя полный идентификатор родительского компонента.<br/>
     * Данный идентификатор имеет формат: <code> [extra_prefix]&lt;parent_id&gt;.&lt;id&gt; </code><br/>
     * где:
     * <li> <code>extra_prefix</code> - Строка, имеющая ненулевую длину как правило только в портальных движках.
     * <li> <code>parent_id</code> - полный идентификатор родительского компонента.
     * <li> <code>id</code> - идентификатор компонента в контексте родительского компонента.
     * @return  полный идентификатор компонента, включающий в себя идентификаторы всех его родительских компонент.
     */
    public String getClientId() {
        return clientId;
    }



    @Override
    public Resources getResources() {
        return root.getResources();
    }

    @Override
    public Messages getMessages() {
        return root.getMessages();
    }

    @Override
    public Theme getTheme() {
        return root.getTheme();
    }

    @Override
    public Map<String,Object> getAttributesMap(final Scope scope) {
        final Map<String,Object> map = root.getAttributesMap(scope);
        if (scope.isGuarded() && prefixLength>0) {
            final HashMap<String,Object> result = new HashMap<String,Object>();
            for (Map.Entry<String,Object> entry : map.entrySet()) {
                final String key = entry.getKey();
                if (key.startsWith(prefix))
                    result.put(key.substring(prefixLength), entry.getValue());
            }
            return Collections.unmodifiableMap(result);
        } else {
            return map;
        }
    }

    @Override
    public Enumeration<String> getAttributeNames(final Scope scope) {
        final Enumeration<String> e = root.getAttributeNames(scope);
        return scope.isGuarded() && prefixLength>0 ? new PrefixFilterEnumeration(e, prefix) : e;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(final String name, final Scope scope) {
        if (scope.isGuarded() && prefixLength>0) {
            return (T)root.getAttribute(prefix+name, scope);
        } else {
            return (T)root.getAttribute(name, scope);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(final String name, final Scope scopes[]) {
        for (Scope scope : scopes) {
            final T result = (T)getAttribute(name, scope);
            if (result!=null)
                return result;
        }
        return null;
    }

    @Override
    public void setAttribute(final String name, final Object value, final Scope scope) {
        if (scope.isGuarded() && prefixLength>0) {
            root.setAttribute(prefix+name, value, scope);
        } else {
            root.setAttribute(name, value, scope);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T removeAttribute(final String name, final Scope scope) {
        if (scope.isGuarded() && prefixLength>0) {
            return (T)root.removeAttribute(prefix+name, scope);
        } else {
            return (T)root.removeAttribute(name, scope);
        }
    }

    @Override
    public Enumeration<String> getParameterNames() {
        final Enumeration<String> e = root.getParameterNames();
        return prefixLength>0 ? new PrefixFilterEnumeration(e, prefix) : e;
    }

    @Override
    public String getParameter(final String name) {
        return prefixLength>0 ? root.getParameter(prefix+name) : root.getParameter(name);
    }

    @Override
    public String[] getParameterValues(final String name) {
        return prefixLength>0 ? root.getParameterValues(prefix+name) : root.getParameterValues(name);
    }


    @Override
    public Cookie[] getRequestCookies() {
        return root.getRequestCookies();
    }

    @Override
    public String getRequestHeader(final String name) {
        return root.getRequestHeader(name);
    }

    @Override
    public Map<String,String[]> getRequestHeaders() {
        return root.getRequestHeaders();
    }

    @Override
    public void addResponseCookie( final Cookie cookie ) {
        root.addResponseCookie( cookie );
    }

    @Override
    public boolean containsResponseHeader(final String name) {
        return root.containsResponseHeader(name);
    }

    @Override
    public void addResponseHeader(final String name, final String value) {
        root.addResponseHeader(name, value);
    }

    @Override
    public void setResponseHeader(final String name, final String value) {
        root.setResponseHeader(name, value);
    }


    @Override
    public void invalidateSession() {
        root.invalidateSession();
    }

    @Override
    public void include(String url, Writer out) throws IOException {
        root.include(url, out);
    }

    @Override
    public String getUserName() {
        return root.getUserName();
    }

    @Override
    public Agent getAgent() {
        return root.getAgent();
    }

    @Override
    public Locale getLocale() {
        return root.getLocale();
    }

    @Override
    public String getCharacterEncoding() {
        return root.getCharacterEncoding();
    }

    @Override
    public String getSessionId() {
        return root.getSessionId();
    }

    @Override
    public String getScheme() {
        return root.getScheme();
    }

    @Override
    public String getHost() {
        return root.getHost();
    }

    @Override
    public int getPort() {
        return root.getPort();
    }

    @Override
    public String getContextPath() {
        return root.getContextPath();
    }

    @Override
    public String getServletPath() {
        return root.getServletPath();
    }

    @Override
    public String getPathInfo() {
        return root.getPathInfo();
    }

    @Override
    public String getRequestUrl() {
        return root.getRequestUrl();
    }

    @Override
    public String encodeURL(final String url) {
        return root.encodeURL(url);
    }

    @Override
    public String encodeThemeURL(final String path, final boolean persistent) {
        return root.encodeThemeURL(path, persistent);
    }

    public String toString() {
        return "ComponentContext["+clientId+", "+root+"]";
    }
}
