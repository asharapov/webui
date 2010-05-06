package org.echosoft.framework.ui.core.compiler;

import javax.servlet.ServletConfig;
import java.io.File;
import java.io.Serializable;
import java.util.Map;

import org.echosoft.common.utils.StringUtil;

/**
 * Структура с конфигурационными параметрами модуля трансляции .wui файлов в соответствующие java сервлеты.
 *
 * @author Anton Sharapov
 */
public final class Options implements Serializable {

    public static final String DEFAULT_BASE_PACKAGE = "org.echosoft.wui";

    /**
     * корневой каталог для всех .wui файлов приложения.
     */
    public final File rootSrcDir;
    /**
     * корневой каталог для всех генерируемых .java и .class файлов.
     */
    public final File rootDstDir;
    /**
     * префикс к имени пакета генерируемых классов записанный в виде a.b.c
     */
    public final String basePkgName;
    /**
     * пути к используемым при компиляции классам и библиотекам классов.
     */
    public final String classpath;
    /**
     * кодировка генерируемой страницы.
     */
    public final String charset;
    /**
     * генерация страницы в отладочном режиме.
     */
    public final boolean development;

    /**
     * Предназначен для вызова из приложения запущенного под сервером приложений (основной режим).
     * @param config  конфигурация сервлета {@link org.echosoft.framework.ui.core.web.WUIServlet}.
     */
    public Options(final ServletConfig config) {
        rootSrcDir = new File( config.getServletContext().getRealPath("/") );

        File dir;
        String str = config.getInitParameter("dst-dir");
        if (str != null) {
            dir = new File(str);
        } else {
            dir = (File)config.getServletContext().getAttribute("javax.servlet.context.tempdir");    // согласно спецификации Servlet 2.2+
            if (dir == null) {
                str = System.getProperty("java.io.tmpdir");     // согласно спецификации Servlet 1.2+
                if (str != null)
                    dir = new File(str);
            }
        }
        if (dir == null) {
            throw new IllegalArgumentException("No destination dir configured");
        } else
        if (!(dir.exists() && dir.canRead() && dir.canWrite() && dir.isDirectory())) {
            throw new IllegalArgumentException("Invalid destination dir: "+dir.getAbsolutePath());
        }
        rootDstDir = dir;

        str = config.getInitParameter("target-package");
        if (str!=null) {
            str = str.trim();
            if (str.endsWith("."))
                str = str.substring(0, str.length()-1).trim();
            basePkgName = StringUtil.trim(str);
        } else {
            basePkgName = DEFAULT_BASE_PACKAGE;
        }
        classpath = Utils.resolveClassPath(config.getServletContext());
        charset = StringUtil.trim(config.getInitParameter("charset"));
        development = "development".equalsIgnoreCase(config.getInitParameter("mode"));
    }

    /**
     * Предназначен для вызова из автономного (standalone) приложения (отладка, консольные утилиты).
     * @param env содержит значения всех конфигурационных параметров
     */
    public Options(final Map<String,String> env) {
        if (!env.containsKey("src-dir"))
            throw new IllegalArgumentException("Source directory not specified");
        rootSrcDir = new File( env.get("src-dir") );
        if (!env.containsKey("dst-dir"))
            throw new IllegalArgumentException("Destination directory not specified");
        rootDstDir = new File( env.get("dst-dir") );
        if (env.containsKey("target-package")) {
            basePkgName = StringUtil.trim( env.get("target-package") );
        } else {
            basePkgName = DEFAULT_BASE_PACKAGE;
        }
        classpath = Utils.resolveClassPath(null);
        charset = StringUtil.trim(env.get("charset"));
        development = "development".equalsIgnoreCase(env.get("mode"));
    }
}
