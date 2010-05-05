package org.echosoft.framework.ui.core.compiler;

import javax.servlet.ServletConfig;
import java.io.File;
import java.io.Serializable;

import org.echosoft.common.utils.StringUtil;

/**
 * @author Anton Sharapov
 */
public final class Options implements Serializable {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String DEFAULT_BASE_PACKAGE = "org.echosoft.wui";

    public final File rootSrcDir;       // корневой каталог для всех .wui файлов приложения.
    public final File rootDstDir;       // корневой каталог для всех генерируемых .java и .class файлов.
    public final String basePkgName;    // префикс к имени пакета генерируемых классов записанный в виде a.b.c
    public final String charset;        // кодировка генерируемой страницы.
    public final boolean development;   // генерация страницы в отладочном режиме.

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

        basePkgName = StringUtil.getNonEmpty(config.getInitParameter("target-package"), DEFAULT_BASE_PACKAGE);
        charset = StringUtil.getNonEmpty(config.getInitParameter("charset"), DEFAULT_CHARSET);
        development = "development".equalsIgnoreCase(config.getInitParameter("mode"));
    }
}
