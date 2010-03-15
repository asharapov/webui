package org.echosoft.framework.ui.extjs.spi.layout;

import java.util.HashMap;

import org.echosoft.common.utils.ObjectUtil;
import org.echosoft.framework.ui.extjs.layout.Layout;
import org.echosoft.framework.ui.extjs.layout.LayoutItem;

/**
 * <p>Предоставляет доступ к классам, отвечающим за сериализацию информации об используемых в ExtJS компонентах менеджерах упаковки (layout managers).<br/>
 * Каждому типу упаковщиков который поддерживается фреймворком ExtJS соответствует свой класс, отвечающий за корректную сериализацию свойств
 * этого упаковщика в надлежащую структуру JSON.</p>
 * <p>Данный класс используется при сериализации дерева компонент в структуру JSON, используемую в ExtJS.</p>
 * @author Anton Sharapov
 */
public class LayoutRegistry {

    private static final HashMap<Class<?>,LayoutRenderer> mcfg = new HashMap<Class<?>,LayoutRenderer>();
    private static final HashMap<Class<?>,LayoutRenderer> mitm = new HashMap<Class<?>,LayoutRenderer>();

    static {
        registerLayout(Layout.class, LayoutItem.class, new DefaultLayoutRenderer());
    }

    /**
     * Регистрирует класс, отвечающий за сериализацию моделей определенного типа упаковщика (layout manager) в структуру JSON.<br/>
     * @param cfgCls  класс описывающий собственно модель упаковщика.
     * @param itmCls  класс описывающий свойства ExtJS компонент которые характеризуют их расположение в компоненте-контейнере
     *              используещего упаковщик соответствующего класса.
     * @param renderer экземпляр класса, отвечающего за сериализацию данных относящихся к данному типу упаковщика.
     */
    public static void registerLayout(final Class<? extends Layout> cfgCls, final Class<? extends LayoutItem> itmCls, final LayoutRenderer renderer) {
        if (cfgCls==null || itmCls==null || renderer==null)
            throw new IllegalArgumentException("All arguments must be specified");
        mcfg.put(cfgCls, renderer);
        mitm.put(itmCls, renderer);
    }

    /**
     * Регистрирует класс, отвечающий за сериализацию моделей определенного типа упаковщика (layout manager) в структуру JSON.<br/>
     * @param cfgCls  класс описывающий собственно модель упаковщика.
     * @param renderer экземпляр класса, отвечающего за сериализацию данных относящихся к данному типу упаковщика.
     */
    public static void registerLayout(final Class<? extends Layout> cfgCls, final LayoutRenderer renderer) {
        if (cfgCls==null || renderer==null)
            throw new IllegalArgumentException("All arguments must be specified");
        try {
            final Layout layout = ObjectUtil.makeInstance(cfgCls, Layout.class);
            mcfg.put(cfgCls, renderer);
            mitm.put(layout.makeItem().getClass(), renderer);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Возвращает экземпляр класса, отвечающего за сериализацию модели определенного типа упаковщика (layout manager)
     * в структуру JSON (как часть описания модели ExtJS компонента).
     * @param config  модель упаковщика.
     * @return  обработчик данного типа упаковщиков или <code>null</code>
     */
    public static LayoutRenderer getRenderer(final Layout config) {
        for (Class<?> cl = config.getClass(); cl!=null; cl=cl.getSuperclass()) {
            final LayoutRenderer result = mcfg.get(cl);
            if (result!=null)
                return result;
        }
        return null;
    }

    /**
     * Возвращает экземпляр класса, отвечающего за сериализацию модели определенного типа упаковщика (layout manager)
     * в структуру JSON (как часть описания модели ExtJS компонента).
     * @param item  объект, расширяющий свойства компонента за счет свойств, специфичных для определенного типа упаковщика компонент.
     * @return  обработчик данного типа упаковщиков или <code>null</code>
     */
    public static LayoutRenderer getRenderer(final LayoutItem item) {
        for (Class<?> cl = item.getClass(); cl!=null; cl=cl.getSuperclass()) {
            final LayoutRenderer result = mitm.get(cl);
            if (result!=null)
                return result;
        }
        return null;
    }
}
