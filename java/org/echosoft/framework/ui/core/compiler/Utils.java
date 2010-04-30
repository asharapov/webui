package org.echosoft.framework.ui.core.compiler;

import java.io.File;

import org.echosoft.common.utils.StringUtil;

/**
 * @author Anton Sharapov
 */
public class Utils {

    /**
     * Для строки с путем до .wui Файла (указанным относительно корневого каталога веб приложения)
     * возвращает имя пакета java в котором будет находиться java версия исходного файла.<br/>
     * пример:
     * <ul>
     *  <li> <code>"/a/b/c.wui"</code> -> <code>"a.b"</code>
     *  <li> <code>"/a.wui"</code> -> <code>""</code>
     * </ul> 
     * @param filePath путь до файла относительно корневого каталога веб приложения.
     * @return имя пакета java в котором будет находиться оттранслированный .java класс.
     */
    public static String getPackageNameByPath(final String filePath) {
        final String[] parts = StringUtil.split(StringUtil.trim(filePath), File.separatorChar);
        if (parts==null || parts.length<2)
            return "";
        final StringBuilder result = new StringBuilder(filePath.length());
        for (int i=0; i<parts.length-1; i++) {
            final String p = StringUtil.trim( parts[i] );
            if (p==null)
                continue;
            if (result.length()>0)
                result.append('.');
            result.append(p);
        }
        return result.toString();
    }
}
