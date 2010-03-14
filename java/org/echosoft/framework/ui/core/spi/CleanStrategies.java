package org.echosoft.framework.ui.core.spi;

import org.echosoft.framework.ui.core.CleanStrategy;
import org.echosoft.framework.ui.core.ViewStateDescriptor;

/**
 * Стандартные реализации интерфейса {@link CleanStrategy}, предназначенного для удаления сохраненных в контексте состояний не нужных более форм.
 * 
 * @author Anton Sharapov
 */
public class CleanStrategies {
    private CleanStrategies() {}

    /**
     * Удаляет состояния абсолютно всех форм.
     */
    public static final CleanStrategy ALL =
            new CleanStrategy() {
                public String getId() {
                    return "ALL";
                }
                public boolean needToClean(final ViewStateDescriptor current, final ViewStateDescriptor candidate) {
                    return true;
                }
            };

    /**
     * Оставляет все состояния без изменений.
     */
    public static final CleanStrategy NONE =
            new CleanStrategy() {
                public String getId() {
                    return "NONE";
                }
                public boolean needToClean(final ViewStateDescriptor current, final ViewStateDescriptor candidate) {
                    return false;
                }
            };

    /**
     * Удаляет состояния всех форм, относящихся к другой группе страниц.
     */
    public static final CleanStrategy PKG =
            new CleanStrategy() {
                public String getId() {
                    return "PACKAGE";
                }
                public boolean needToClean(final ViewStateDescriptor current, final ViewStateDescriptor candidate) {
                    return current!=null && !current.getPackage().equals(candidate.getPackage());
                }
            };

    /**
     * Удаляет состояния всех форм за исключением той, что помечена как текущая.
     */
    public static final CleanStrategy CURRENT =
            new CleanStrategy() {
                public String getId() {
                    return "CURRENT";
                }
                public boolean needToClean(final ViewStateDescriptor current, final ViewStateDescriptor candidate) {
                    return !candidate.equals(current);
                }
            };

    /**
     * Удаляет состояния всех форм, чей ранг больше чем ранг текущей страницы.
     */
    public static final CleanStrategy RANK =
            new CleanStrategy() {
                public String getId() {
                    return "RANK";
                }
                public boolean needToClean(final ViewStateDescriptor current, final ViewStateDescriptor candidate) {
                    return current==null || candidate.getRank()>current.getRank();
                }
            };
    
}
