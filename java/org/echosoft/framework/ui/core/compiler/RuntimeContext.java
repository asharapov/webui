package org.echosoft.framework.ui.core.compiler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Содержит ссылки на все .wui ресурсы.
 * @author Anton Sharapov
 */
public class RuntimeContext {

    private final Options options;
    private final Map<String,Resource> resources;

    public RuntimeContext(final Options options) {
        this.options = options;
        this.resources = new ConcurrentHashMap<String,Resource>();
    }

    public Options getOptions() {
        return options;
    }

    /**
     * По относительному пути до ресурса возвращает его описание.
     * Если указанный ресурс отсутствует на сервере то метод возвращает <code>null</code>.
     * @param uri  относительный путь до ресурса.
     * @return описание запрошенного ресурса или <code>null</code> если он отсутствует.
     */
    public Resource getResource(final String uri) {
        Resource resource = resources.get(uri);
        if (resource == null) {
            resource = Resource.getResource(uri, options);
            if (resource != null)
                resources.put(uri, resource);
        }
        return resource;
    }

}
