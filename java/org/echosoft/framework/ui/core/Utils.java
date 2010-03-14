package org.echosoft.framework.ui.core;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Содержит ряд методов, полезных при обработке пользовательских запросов.
 *
 * @author Anton Sharapov
 */
public class Utils {

    /**
     * Тип содержимого по умолчанию.
     */
    public static final String DEFAULT_MIME_TYPE = "application/octet-stream";

    private static HashMap<String,String> mimeExtensions = new HashMap<String,String>();
    static {
        final ResourceBundle bundle = ResourceBundle.getBundle("org.echosoft.framework.ui.core.mime");
        for (Enumeration<String> en = bundle.getKeys(); en.hasMoreElements(); ) {
            final String key = en.nextElement();
            mimeExtensions.put(key, bundle.getString(key));
        }
    }

    /**
     * По расширению файла определяет тип его содержимого. Работает для наиболее популярных в сети расщирений файлов.
     * @see javax.servlet.ServletContext#getMimeType(String)
     * @param fileName строка с именем файла.
     * @return  строка с кодовым названием типа содержимого. Если метод не смог определить подходящий тип содержимого то возвращается константа
     *      {@link Utils#DEFAULT_MIME_TYPE}.
     */
    public static String getMimeType(final String fileName) {
        if (fileName==null)
            return DEFAULT_MIME_TYPE;
        final int index = fileName.lastIndexOf('.');
        final String extension = index >= 0 ? fileName.substring(index + 1).toLowerCase() : "";
        final String mimeType = mimeExtensions.get(extension);
        return mimeType != null ? mimeType : DEFAULT_MIME_TYPE;
    }

    /**
     * Пытается определить локаль пользователя основываясь на информации полученной из пользовательского запроса (основываясь на алгоритме определенном в JSTL 8.3.2).<br/>
     * В случае неудачи использует установленную для приложения локаль по умолчанию.
     * @param request  пользовательский запрос.
     * @return  пользовательская локаль.
     */
    public static Locale detectEffectiveLocale(final HttpServletRequest request) {
        Locale result = null;
        final Locale d = request.getLocale();
        for (int i=0; i<Application.SUPPORTED_LOCALES.length; i++) {
            final Locale s = Application.SUPPORTED_LOCALES[i];
            if ( !d.getLanguage().equals(s.getLanguage()) )
                continue;
            if (d.getCountry().equals(s.getCountry()) || s.getCountry().equals("") || d.getCountry().equals(""))
                return s;
            if (result==null)
                result = s;
        }

        for (Enumeration e = request.getLocales(); e.hasMoreElements(); ) {
            final Locale l = (Locale)e.nextElement();
            for (int i=0; i<Application.SUPPORTED_LOCALES.length; i++) {
                final Locale s = Application.SUPPORTED_LOCALES[i];
                if ( !l.getLanguage().equals(s.getLanguage()) )
                    continue;
                if (l.getCountry().equals(s.getCountry()) || s.getCountry().equals(""))
                    return s;
                if (result==null)
                    result = s;
            }
        }

        if (result==null)
            result = Application.DEFAULT_LOCALE!=null ? Application.DEFAULT_LOCALE : Locale.getDefault();
        return result;
    }

    /**
     * Определяет браузер пользователя на основе заголовка запроса <code>User-Agent</code>.
     * @param request  пользовательский запрос
     * @return  информация о браузере и операционной системе пользователя.
     */
    public static Agent detectUserAgent(final HttpServletRequest request) {
        return detectUserAgent( request.getHeader("User-Agent") );
    }
    public static Agent detectUserAgent(final String agent) {
        if (agent==null)
            return new Agent(null, null, 0, 0, "");

        final Agent.OS os;
        if (agent.indexOf("Windows")>=0) {
            os = Agent.OS.WINDOWS;
        } else
        if (agent.indexOf("Linux")>=0 || agent.indexOf("BSD")>=0 || agent.indexOf("Fedora")>=0) {
            os = Agent.OS.LINUX;
        } else
        if (agent.indexOf("Mac")>=0) {
            os = Agent.OS.MACOS;
        } else
        if (agent.indexOf("Sun")>=0) {
            os = Agent.OS.SOLARIS;
        } else
            os = null;

        int pos;
        if ((pos=agent.indexOf("Konqueror/"))>=0) {
            int start = pos + 10;
            int end = agent.indexOf(';', start);
            if (end<0)
                end = agent.indexOf(')', start);
            return makeAgent(Agent.Type.KONQUEROR, os, end>0 ? agent.substring(start, end) : "", agent);
        } else
        if ( (pos=agent.indexOf("Opera"))>=0 ) {
            int start = pos + 5;
            while( start<agent.length() && !Character.isDigit(agent.charAt(start)) ) start++;
            int end = agent.indexOf(' ', start);
            if (end<0)
                end = agent.length();
            return makeAgent(Agent.Type.OPERA, os, end>0 ? agent.substring(start, end) : "", agent);
        } else
        if ( (pos=agent.indexOf("MSIE "))>=0 ) {
            int start = pos + 5;
            int end = agent.indexOf(';', start);
            return makeAgent(Agent.Type.IEXPLORER, os, end>0 ? agent.substring(start, end) : "", agent);
        } else
        if ( agent.indexOf("Gecko/")>=0 && (pos=agent.indexOf(" rv:"))>=0) {
            int start = pos + 4;
            int end = agent.indexOf(')', start);
            return makeAgent(Agent.Type.GECKO, os, end>0 ? agent.substring(start, end) : "", agent);
        } else
        if ( (pos=agent.indexOf("Links/"))>=0 ) {
            int start = pos + 6;
            int end = agent.indexOf(' ', start);
            return makeAgent(Agent.Type.LINKS, os, end>0 ? agent.substring(start, end) : "", agent);
        } else
        if ( (pos=agent.indexOf("Lynx/"))>=0 ) {
            int start = pos + 5;
            int end = agent.indexOf(' ', start);
            return makeAgent(Agent.Type.LYNX, os, end>0 ? agent.substring(start, end) : "", agent);
        }

        return new Agent(null, os, 0, 0, agent);
    }

    private static Agent makeAgent(final Agent.Type type, final Agent.OS os, final String version, final String agent) {
        int major = 0, minor = 0;
        try {
            int d1 = version.indexOf('.');
            major = Integer.parseInt(version.substring(0, d1));
            int d2 = version.indexOf('.', d1+1);
            minor = Integer.parseInt(version.substring(d1+1, d2>0 ? d2 : version.length()));
        } catch (NumberFormatException e) {
            // do nothing
        } catch (Exception e) {
            // do nothing
        }
        return new Agent(type, os, major, minor, agent);
    }
}
