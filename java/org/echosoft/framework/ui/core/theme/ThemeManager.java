package org.echosoft.framework.ui.core.theme;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.echosoft.common.model.Version;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.Application;
import org.echosoft.framework.ui.core.UIException;

/**
 * <p>Используется для управления темами. Приложению может быть доступно несколько тем в том числе и несколько версий одной и той же темы</p>
 * <p>Тема по умолчанию может быть задана с помощью вызова {@link #setDefaultTheme}. Эта тема должна быть уже ранее зарегистрирована в менеджере тем.</p>
 * @author Anton Sharapov
 */
public class ThemeManager {

    private static final String MANIFEST = "META-INF/MANIFEST.MF";
    private static final String THEME_SECTION = "ru/topsbi/framework/ui/theme";
    private static final String THEME_NAME = "X-TWUI-Theme-Name";
    private static final String THEME_VERSION = "X-TWUI-Theme-Version";
    private static final String THEME_ADDON = "X-TWUI-Theme-Addon";

    private final Map<String,ThemeInfo> themes;
    private String defaultTheme;

    public ThemeManager() throws IOException {
        themes = new HashMap<String,ThemeInfo>();
        init(null);
    }

    /**
     * Возвращает список зарегистрированных в приложении тем.
     * @return массив из имен зарегистрированных в приложении тем. Метод никогда не возвращает <code>null</code>,
     *          хотя массив может быть пустым если небыло зарегистрировано ни одной темы.
     */
    public String[] getRegisteredThemes() {
        return themes.keySet().toArray(new String[themes.size()]);
    }

    /**
     * Возвращает логическое имя используемой по умолчанию темы.
     * @return логическое имя темы по умолчанию.
     */
    public String getDefaultTheme() {
        return defaultTheme;
    }

    /**
     * Устанавливает тему по умолчанию. Тема с указанным в аргументе логическим именем уже должна быть доступна для приложения,
     * в противном случае метод поднимает исключительну ситуацию.
     * @param themeName  логическое имя новой темы по умолчанию. В системе уже должна быть к этому времени зарегистрирована тема с таким именем.
     * @throws UIException  поднимается в случае когда в система ничего не знает про указанную в аргументе тему.
     */
    public void setDefaultTheme(final String themeName) {
        if (!themes.containsKey(themeName))
            throw new UIException("Theme ["+ themeName +"] not found in classpath");
        this.defaultTheme = themeName;
    }

    /**
     * Для указанной в аргументе локали возвращает соответствующую тему.
     * @param locale  локаль.
     * @return  тема для указанной локали.
     * @throws UIException  поднимается в случае когда тема с указанным идентификатором не доступна в системе.
     */
    public Theme getTheme(final Locale locale) {
        return getTheme(locale, defaultTheme);
    }

    /**
     * Возвращает тему для указанных в аргументах локали и имени темы.
     * @param locale  локаль, для которой возвращается тема.
     * @param themeName  логическое имя темы. Если <code>null</code> то используется тема по умолчанию.
     * @return  экземпляр темы с указанным именем.
     * @throws UIException  поднимается в случае когда тема с указанным идентификатором не доступна в системе.
     */
    public Theme getTheme(final Locale locale, String themeName) {
        if (themeName==null)
            themeName = defaultTheme;
        final ThemeInfo ti = themes.get(themeName);
        if (ti!=null) {
            return ti.getTheme(locale);
        } else
        if (themeName==null) {
            return null;
        } else
            throw new UIException("Theme ["+themeName+"] not found in classpath");
    }


    /**
     * Ищет все присутствующие темы для указанного загрузчика классов.
     * @param loader  загрузчик классов, если не указан, то используется загрузчик классов по умолчанию.
     * @throws IOException  в случае каких-либо проблем.
     */
    protected void init(ClassLoader loader) throws IOException {
        if (loader==null) {
            loader = this.getClass().getClassLoader();
        }
        for (Enumeration e = loader.getResources(MANIFEST); e.hasMoreElements(); ) {
            final URL url = (URL)e.nextElement();
            final InputStream in = url.openStream();
            try {
                final Manifest manifest = new Manifest(in);
                final Attributes attrs = manifest.getAttributes(THEME_SECTION);
                if (attrs!=null) {
                    final String name = StringUtil.trim(attrs.getValue(THEME_NAME));
                    final String v = StringUtil.trim(attrs.getValue(THEME_VERSION));
                    if (name==null) {
                        Application.log.warn("Illegal webui theme configuration: name of the theme not specified");
                        continue;
                    }
                    final Version version;
                    try {
                        version = Version.parseVersion(v);
                        if (version==null)
                            throw new NullPointerException("version not specified");
                    } catch (Exception err) {
                        Application.log.warn("Illegal webui theme configuration theme '"+name+"' hasn't valid version: "+v);
                        continue;
                    }
                    final String _order = StringUtil.trim(attrs.getValue(THEME_ADDON));
                    final int order = _order!=null ? Integer.parseInt(_order,10) : 0;
                    ThemeInfo ti = themes.get(name);
                    if (ti==null || ti.version.compareTo(version)<0) {
                        ti = new ThemeInfo(name, version);
                        themes.put(name, ti);
                        if (defaultTheme==null) {
                            defaultTheme = name;
                        }
                    }
                    ti.attachUrl(UrlUtil.normalize(url), order);
                }
            } finally {
                in.close();
            }
        }

        for (ThemeInfo ti : themes.values()) {
            Application.log.info("Registered webui theme: " + ti);
        }
    }

    private static final class ThemeInfo {
        public final String name;
        public final Version version;
        public final ArrayList<OrderedURL> urls;
        public final HashMap<Locale,Theme> localizedThemes;
        public ThemeInfo(final String name, final Version version) {
            this.name = name;
            this.version = version;
            this.urls = new ArrayList<OrderedURL>();
            this.localizedThemes = new HashMap<Locale,Theme>();
        }
        public void attachUrl(final URL url, final int order) {
            urls.add( new OrderedURL(url, order) );
            if (urls.size()>1) {
                Collections.sort(urls);
            }
        }
        public Theme getTheme(final Locale locale) {
            Theme theme = localizedThemes.get(locale);
            if (theme==null) {
                synchronized (this) {
                    if (theme==null) {
                        if (urls.size()<1)
                            return null;
                        final URL[] urlsArray = new URL[urls.size()];
                        for (int i=0; i<urls.size(); i++)
                            urlsArray[i] = (urls.get(i)).url;
                        theme = new JarTheme(name, version, locale, urlsArray);
                        localizedThemes.put(locale, theme);
                    }
                }
            }
            return theme;
        }
        public int hashCode() {
            return name.hashCode();
        }
        public boolean equals(final Object obj) {
            if (obj==null || !getClass().equals(obj.getClass()))
                return false;
            final ThemeInfo other = (ThemeInfo)obj;
            return name.equals(other.name) && version.equals(other.version) && urls.equals(other.urls);
        }
        public String toString() {
            return "[Theme{name:"+name+", version:"+version+", url:"+urls+"}]";
        }
    }

    private static final class OrderedURL implements Comparable<OrderedURL> {
        public final URL url;
        public final int order;
        public OrderedURL(final URL url, final int order) {
            this.url = url;
            this.order = order;
        }
        public int compareTo(final OrderedURL other) {
            if (order>other.order) {
                return 1;
            } else
            if (order<other.order) {
                return -1;
            } else
                return 0;
        }
        public String toString() {
            return url.toString();
        }

    }
}
