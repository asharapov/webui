package org.echosoft.framework.ui.core.web;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.echosoft.common.utils.ObjectUtil;
import org.echosoft.framework.ui.core.Application;
import org.echosoft.framework.ui.core.Utils;
import org.echosoft.framework.ui.core.spi.Constants;
import org.echosoft.framework.ui.core.theme.Theme;

/**
 * Данный сервлет предназначен для предоставления клиентам доступа к ресурсам приложения.
 * Ресурсы доступны из следующих источников:
 * <ol>
 *  <li> Собственно web приложения.
 *  <li> Путей классов.
 *  <li> Темы приложения.
 * </ol>
 * Для всех ресурсов, доступ к которым предоставляет данный сервлет, автоматически выставляются заголовки, указывающие клиентам что данный контент
 * может быть закэширован в течение не менее 12 часов.
 *
 * @author Anton Sharapov
 */
public final class ResourceServlet extends HttpServlet {

    public static final int LARGEST_CONTENT_SIZE_IN_CACHE = 1024*1024;   // 1Mb
    public static final long CACHING_TIME = 1000*60*60*12;               // 12 hours.
    private static final long SERVER_START_TIME = System.currentTimeMillis();

    private final Map<String,ResourceLoader> storages;
    private final ConcurrentHashMap<String,byte[]> cache;
    public ResourceServlet() {
        super();
        storages = new HashMap<String,ResourceLoader>();
        cache = new ConcurrentHashMap<String,byte[]>();
    }

    @Override
    public void init() {
        storages.clear();
        storages.put("/web", new ServletResourceLoader(this));
        storages.put("/lib", new ClassPathResourceLoader());
        storages.put("/theme", new ThemeResourceLoader());
        final String ttl = getInitParameter("ttl");
    }

    @Override
    public void destroy() {
        for (ResourceLoader loader : storages.values()) {
            loader.destroy();
        }
    }

    @Override
    public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final String path = request.getPathInfo();
        byte[] content = loadContent(path, request);
        if (content==null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // определяем тип содержимого ...
        String contentType = getServletContext().getMimeType(path);
        if (contentType==null) {
            contentType = Utils.getMimeType(path);
        }

        // отправляем содержимое ресурса клиенту ...
        response.setContentType(contentType);
        response.setContentLength(content.length);
        response.setDateHeader("Last-Modified", SERVER_START_TIME);
        response.setDateHeader("Expires", System.currentTimeMillis()+CACHING_TIME);
        final ServletOutputStream out = response.getOutputStream();
        try {
            out.write(content);
            out.flush();
        } finally {
            out.close();
        }
    }

    private byte[] loadContent(final String path, final HttpServletRequest request) throws IOException {
        if (path==null || path.length()==0)
            return null;
        final int s = path.indexOf('/', 1);
        if (s<0)
            return null;
        final String sid = path.substring(0,s);
        final String resource = path.substring(s);
        final ResourceLoader loader = storages.get( sid );
        if (loader==null)
            return null;
        if ("1".equals(request.getParameter("cache"))) {
            byte[] result = cache.get(path);
            if (result==null) {
                result = loader.load(resource, request);
                if (result!=null && result.length < ResourceServlet.LARGEST_CONTENT_SIZE_IN_CACHE)
                    cache.put(resource, result);
            }
            return result;
        } else {
            return loader.load(resource, request);
        }
    }

}

interface ResourceLoader {
    public byte[] load(String resource, HttpServletRequest request) throws IOException;
    public void destroy();
}

final class ServletResourceLoader implements ResourceLoader {
    private final HttpServlet servlet;
    public ServletResourceLoader(final HttpServlet servlet) {
        this.servlet = servlet;
    }
    public byte[] load(final String resource, final HttpServletRequest request) throws IOException {
        if (resource.startsWith("/WEB-INF/"))
            return null;
        final InputStream in = servlet.getServletContext().getResourceAsStream(resource);
        if (in==null)
            return null;
        try {
            return ObjectUtil.streamToBytes(in);
        } finally {
            in.close();
        }
    }
    public void destroy() {
    }
}

final class ClassPathResourceLoader implements ResourceLoader {
    public byte[] load(final String resource, final HttpServletRequest request) throws IOException {
            final InputStream in = ResourceServlet.class.getResourceAsStream(resource);
            if (in==null)
                return null;
            try {
                return ObjectUtil.streamToBytes(in);
            } finally {
                in.close();
            }
    }
    public void destroy() {
    }
}

final class ThemeResourceLoader implements ResourceLoader {
    public byte[] load(final String resource, final HttpServletRequest request) throws IOException {
        final String themeName = (String)request.getSession().getAttribute(Constants.CURRENT_THEME);
        final Theme theme = Application.THEMES_MANAGER.getTheme(Utils.detectEffectiveLocale(request), themeName);
        final InputStream in = theme.getResourceAsStream(resource);
        if (in==null)
            return null;
        try {
            return ObjectUtil.streamToBytes(in);
        } finally {
            in.close();
        }
    }
    public void destroy() {
    }
}