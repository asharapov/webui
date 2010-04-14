package org.echosoft.framework.ui.core.theme;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.echosoft.common.io.FastStringWriter;
import org.echosoft.common.model.Version;
import org.echosoft.common.utils.Any;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.Application;
import org.echosoft.framework.ui.core.UIException;

/**
 * Реализация интерфейса {@link Theme} по умолчанию.
 *
 * @author Anton Sharapov
 */
public class JarTheme implements Theme {

    private static final String GLOBAL_CSS_FILES = "global.css.files";
    private static final String GLOBAL_JS1_FILES = "global.js1.files";  // скрипты подгружаемые в первую очередь.
    private static final String GLOBAL_JS2_FILES = "global.js2.files";  // скрипты подгружаемые в последнюю очередь.

    private final String name;
    private final Version version;
    private final Locale locale;
    private final URL[] urls;

    private final Map<String,String> messages;
    private final Map<String,String> params;
    private final Map<String,String> resources;
    private final String urlPrefix;
    private final String pathPrefix;
    private final String[] globalCSSFiles;
    private final String[] globalJS1Files;
    private final String[] globalJS2Files;

    /**
     * Создаем экземпляр новой темы.
     * @param name  Кодовое название темы. Не может быть <code>null</code>.
     * @param version  версия темы.
     * @param locale  Выбранная локаль. Не может быть <code>null</code>.
     * @param urls  упорядоченный массив ссылок на .jar файлы находящиеся в ClassPath в которых содержатся ресурсы относящиеся к данной теме. Массив не может быть пустым.
     *      При обращении к определенному ресурсу темы сначала осуществляется его поиск в первом .jar файле, если он там не был найден то осуществляем его поиск в следующем файле и так далее...
     */
    public JarTheme(String name, Version version, Locale locale, URL[] urls) {
        if ((name=StringUtil.trim(name))==null)
            throw new IllegalArgumentException("Theme identifier must be specified");
        if (locale==null)
            throw new IllegalArgumentException("Locale must be specified");
        if (urls==null || urls.length==0) {
            throw new IllegalArgumentException("Theme URL must be specified");
        }

        this.name = name;
        this.version = version;
        this.locale = locale;
        this.urls = urls;
        final StringBuilder p = new StringBuilder(name.replace('.', '/') );
        if (p.charAt(0)!='/')
            p.insert(0, '/');
        if (p.charAt(p.length()-1)=='/')
            p.deleteCharAt(p.length()-1);
        this.pathPrefix = p.toString();  // convert theme name 'XXX.YYY' to '/XXX/YYY'
        this.urlPrefix =  "/lib" + pathPrefix;

        this.messages = loadBundle(pathPrefix+"/messages", locale, urls);
        this.params = loadBundle(pathPrefix+"/params", locale, urls);
        this.resources = loadBundle(pathPrefix+"/resources", locale, urls);

        final ArrayList<String> list = new ArrayList<String>();
        for (String key : Any.asStringArray(params.get(GLOBAL_JS1_FILES), StringUtil.EMPTY_STRING_ARRAY)) {
            key = StringUtil.trim(key);
            if (key!=null) {
                list.add( key );
            }
        }
        globalJS1Files = list.toArray(new String[list.size()]);

        list.clear();
        for (String key : Any.asStringArray(params.get(GLOBAL_JS2_FILES), StringUtil.EMPTY_STRING_ARRAY)) {
            key = StringUtil.trim(key);
            if (key!=null) {
                list.add( key );
            }
        }
        globalJS2Files = list.toArray(new String[list.size()]);

        list.clear();
        for (String key : Any.asStringArray(params.get(GLOBAL_CSS_FILES), StringUtil.EMPTY_STRING_ARRAY)) {
            key = StringUtil.trim(key);
            if (key!=null) {
                list.add( key );
            }
        }
        globalCSSFiles = list.toArray(new String[list.size()]);
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public Version getVersion() {
        return version;
    }


    /**
     * {@inheritDoc}
     */
    public String getMessage(final String key) {
        return messages.get(key);
    }

    /**
     * {@inheritDoc}
     */
    public String getMessage(final String key, final Object... params) {
        final String message = messages.get(key);
        return params==null || params.length==0
                ? message
                : new MessageFormat(message, locale).format(params);
    }

    /**
     * {@inheritDoc}
     */
    public String getProperty(final String key) {
        return params.get(key);
    }

    /**
     * {@inheritDoc}
     */
    public String getProperty(final String key, final String defaultValue) {
        final String result = params.get(key);
        return result!=null ? result : defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    public String[] getGlobalStylesheets() {
        return globalCSSFiles;
    }

    /**
     * {@inheritDoc}
     */
    public String[] getGlobalJS1Files() {
        return globalJS1Files;
    }

    /**
     * {@inheritDoc}
     */
    public String[] getGlobalJS2Files() {
        return globalJS2Files;
    }

    /**
     * {@inheritDoc}
     */
    public String getResourceURL(final String key) {
        String uri = resources.get(key);
        if (uri==null)
            uri = key;

        final int s = key.indexOf(':',0);
        final int q = key.indexOf('?',0);
        if ( s<0 || (q>0 && s>q) ) {
            // это относительная ссылка ...
            uri = Application.RESOURCES_SERVLET_CONTEXT + urlPrefix + (uri.charAt(0)=='/' ? uri : '/'+uri);
        }
        return uri;
    }

    /**
     * {@inheritDoc}
     */
    public InputStream getResourceAsStream(final String key) {
        try {
            String uri = resources.get(key);
            if (uri==null)
                uri = key;
            final int s = key.indexOf(':',0);
            final int q = key.indexOf('?',0);
            if ( s<0 || (q>0 && s>q) ) {
                // it's relative url (url to something resource from theme's resources) ...
                uri = pathPrefix + (uri.charAt(0)=='/' ? uri : '/'+uri);
                for (URL url : urls) {
                    final URL resUrl = UrlUtil.makeResourceURL(url, uri);
                    try {
                        final InputStream in = resUrl.openStream();
                        if (in != null)
                            return in;
                    } catch (IOException ee) {
                        // go to next url from list
                    }
                }
                return null;    
            } else {
                // it's an absolute url ...
                final URL resUrl = new URL(uri);
                return resUrl.openStream();
            }
        } catch (Exception e) {
            Application.log.warn("Unable to obtain stream for resource: "+key, e);
            return null;
        }
    }

    public String toString() {
        return "[JarTheme{name:"+name+", ver:"+version+", locale:"+locale+", urls:"+Arrays.toString(urls)+"}]";
    }


    private static Map<String,String> loadBundle(final String bundleBaseName, final Locale locale, final URL[] urls) {
        final String[] resources = calculateBundleNames(bundleBaseName, locale);

        final Map<String,String> result = new HashMap<String,String>();
        int found = 0;
        for (URL url : urls) {
            for (int r = resources.length - 1; r >= 0; r--) {
                if (resources[r] == null) continue;
                try {
                    final URL resUrl = UrlUtil.makeResourceURL(url, resources[r] + ".properties");
                    final InputStream stream = resUrl.openStream();
                    if (stream == null) {
                        Application.log.warn("resource not found for url: " + resUrl);
                        continue;
                    }
                    try {
                        found++;
                        final Properties props = new Properties();
                        props.load(stream);
                        for (Enumeration e = props.propertyNames(); e.hasMoreElements();) {
                            final String n = (String) e.nextElement();
                            if (!result.containsKey(n)) {
                                result.put(n, props.getProperty(n));
                            }
                        }
                    } finally {
                        stream.close();
                    }
                } catch (MalformedURLException ue) {
                    throw new UIException("Unable to load resource " + resources[r] + " from jars " + Arrays.toString(urls), ue);
                } catch (IOException ie) {
                    // do nothing...
                }
            }
        }

        if (found==0) {
            Application.log.warn("Unable to load resource bundle ["+bundleBaseName+"] from theme jars "+Arrays.toString(urls));
            throw new UIException("Resource bundle ["+bundleBaseName+"' not found from theme jars "+Arrays.toString(urls));
        }

        return result;
    }

    private static String[] calculateBundleNames(final String baseName, final Locale locale) {
        final String[] result = new String[4];
        result[0] = baseName;
        final String language = locale.getLanguage();
        final int languageLength = language.length();
        if (languageLength>0) {
            final FastStringWriter temp = new FastStringWriter();
            temp.write(baseName);
            temp.write('_');
            temp.write(language);
            result[1] = temp.toString();
            final String country = locale.getCountry();
            final int countryLength = country.length();
            if (countryLength>0) {
                temp.write('_');
                temp.write(country);
                result[2] = temp.toString();
                final String variant = locale.getVariant();
                final int variantLength = variant.length();
                if (variantLength>0) {
                    temp.write('_');
                    temp.write(variant);
                    result[3] = temp.toString();
                }
            }
        }
        return result;
    }
}
