package org.echosoft.framework.ui.core;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.echosoft.common.utils.StringUtil;

/**
 * Содержит описание подключаемых ресурсов к формируемой web странице. Подключаемые ресурсы имеют две значимые для фреймворка характеристики:
 * <ol>
 *  <li>Тип ресурса (javascript, css, flash, ...).
 *  <li>Способ подключения (внешний ресурс, внедренный на страницу)
 * </ol>
 * Задачи, решаемые данным классом:
 * <ol>
 *  <li>Определяет тип и способ подключения всех используемых на странице ресурсных элементов.
 *  <li>Предотвращает повторное подключение к странице одних и тех же внешних ресурсов (для внедренных ресурсов подобная проверка не производится).
 *  <li>Гарантирует оптимальный порядок подключения ресурсов на странице, учитывающий зависимости одного ресурсного элемента от другого. (в первом приближении достаточно чтобы сохранялся порядок подключения ресурсов в том виде в каком добавлялись данные ресурсы в объект этот класс)
 *  <li>Поддержка механизма т.н. точек сохранений (savepoint) которая давала бы возможность оперативно отменить последние изменения.
 * Насущность данного требования хорошо иллюстрирует задача организации в виджетах обработки исключений и в случае возникновения оных отображать соответствующий контент, для которого требуется совершенно другой набор подключаемых ресурсов.
 * </ol>
 * @author Anton Sharapov
 */
public class Resources {

    private static final String DEFAULT_SCRIPT_CONTENT_TYPE = "text/javascript";
    private static final String DEFAULT_STYLESHEET_CONTENT_TYPE = "text/css";

    private final LinkedHashMap<String, Resource> resources;
    private final LinkedHashSet<CharSequence> embeddedScripts;
    private final LinkedHashSet<CharSequence> embeddedStyles;
    private final LinkedHashSet<CharSequence> embeddedHTML;

    public Resources() {
        this.resources = new LinkedHashMap<String, Resource>();
        this.embeddedScripts = new LinkedHashSet<CharSequence>();
        this.embeddedStyles = new LinkedHashSet<CharSequence>();
        this.embeddedHTML = new LinkedHashSet<CharSequence>();
    }

    /**
     * Возвращает коллекцию всех подключаемых скриптов что были зарегистрированных для данной web страницы.
     * Элементы коллекции расположены в том порядке в котором они регистрировались в менеджере ресурсов. 
     * @return коллекция записей о каждом подключаемом к странице скрипте.
     */
    public Collection<Script> getExternalScripts() {
        final Collection<Script> result = new ArrayList<Script>();
        for (Resource resource : resources.values()) {
            if (Script.class.equals(resource.getClass()))
                result.add( (Script)resource );
        }
        return result;
    }

    /**
     * Подключает к формируемой странице скрипт доступный по указанной ссылке.
     * При подключении автоматически устанавливается язык скрипта в <code>text/javascript</code> и признак что скрипт
     * <u>участвует</u> в формировании содержимого данной страницы, то есть он должен быть загружен в синхронном режиме. 
     * @param url  ссылка по которой доступно содержимое подключаемого скрипта, может быть задана либо в абсолютной либо в относительной форме.
     */
    public void attachScript(final String url) {
        if (!resources.containsKey(url))
            resources.put(url, new Script(url,null));
    }

    /**
     * Подключает к формируемой странице скрипт доступный по указанной ссылке.
     * @param url  ссылка по которой доступно содержимое подключаемого скрипта, может быть задана либо в абсолютной либо в относительной форме.
     * @param contentType  язык на котором написан скрипт. По умолчанию (если аргумент равен <code>null</code>) имеет значение <code>text/javascript</code>.
     * @param defered  дает подсказку браузеру что данный скрипт можно загружать асинхронно относительно загрузки прочего содержимого страницы.
     */
    public void attachScript(final String url, final String contentType, final boolean defered) {
        if (!resources.containsKey(url)) {
            final Script script = new Script(url,contentType);
            script.setDefered(defered);
            resources.put(url, script);
        }
    }

    /**
     * Возвращает коллекцию всех подключаемых таблиц стилей что были зарегистрированных для данной web страницы.
     * Элементы коллекции расположены в том порядке в котором они регистрировались в менеджере ресурсов.
     * @return коллекция записей о каждой подключаемом к странице таблице стилей.
     */
    public Collection<StyleSheet> getExternalStyleSheets() {
        final Collection<StyleSheet> result = new ArrayList<StyleSheet>();
        for (Resource resource : resources.values()) {
            if (StyleSheet.class.equals(resource.getClass()))
                result.add( (StyleSheet)resource );
        }
        return result;
    }

    /**
     * Подключает к формируемой странице таблицу стилей доступную по указанной ссылке.
     * При подключении автоматически устанавливается язык описания стилей в <code>text/css</code>.
     * @param url  ссылка по которой доступно содержимое подключаемой таблицы стилей, может быть задана либо в абсолютной либо в относительной форме.
     */
    public void attachStyleSheet(final String url) {
        if (!resources.containsKey(url))
            resources.put(url, new StyleSheet(url,null));
    }

    /**
     * Подключает к формируемой странице таблицу стилей доступную по указанной ссылке.
     * @param url  ссылка по которой доступно содержимое подключаемого скрипта, может быть задана либо в абсолютной либо в относительной форме.
     * @param contentType  язык на котором написан скрипт. По умолчанию (если аргумент равен <code>null</code>) имеет значение <code>text/javascript</code>.
     * @param media  определяет устройство, для которого предназначена данная таблица стилей.
     */
    public void attachStylesheet(final String url, final String contentType, final String media) {
        if (!resources.containsKey(url)) {
            final StyleSheet styleSheet = new StyleSheet(url,contentType);
            styleSheet.setMedia(media);
            resources.put(url, styleSheet);
        }
    }



    /**
     * Возвращает строковый буфер в котором агрегируются все внедряемые в тело страницы скрипты. Используемый язык скрипта един на всю страницу,
     * задается в корневом компоненте страницы и по умолчанию равен <code>text/javascript</code>.
     * @param out выходной поток куда будут переписаны все внедряемые в тело страницы скрипты.
     * @throws IOException  возникает в случае ошибок вывода данных в поток.
     */
    public void writeOutScripts(final Writer out) throws IOException {
        for (CharSequence content : embeddedScripts) {
            out.write(content.toString());
            out.write('\n');
        }
    }

    /**
     * Возвращает строковый буфер в котором агрегируются все внедряемые в тело страницы стили. Используемый язык описания стилей един на всю страницу и равен <code>text/css</code>.
     * @param out выходной поток куда будут переписаны все внедряемые в тело страницы стили.
     * @throws IOException  возникает в случае ошибок вывода данных в поток.
     */
    public void writeOutStyles(final Writer out) throws IOException {
        for (CharSequence content : embeddedStyles) {
            out.write(content.toString());
            out.append('\n');
        }
    }

    /**
     * Возвращает строковый буфер в котором агрегируются все внедряемые в тело страницы фрагменты HTML.
     * @param out выходной поток куда будут переписаны все внедряемые в тело страницы фрагменты HTMLю
     * @throws IOException  возникает в случае ошибок вывода данных в поток.
     */
    public void writeOutHTML(final Writer out) throws IOException {
        for (CharSequence content : embeddedHTML) {
            out.write(content.toString());
            out.write('\n');
        }
    }

    /**
     * Внедряет в страницу последовательность выражений на языке Javascript. 
     * @param content  выражения на языке JavaScript. Не может быть <code>null</code>.
     */
    public void embedScript(final CharSequence content) {
        if (content!=null && content.length()>0)
            embeddedScripts.add( content );
    }

    /**
     * Внедряет в страницу одно или несколько описаний стилей CSS.
     * @param content набор правил CSS. Не может быть <code>null</code>.
     */
    public void embedStyle(final CharSequence content) {
        if (content!=null && content.length()>0)
            embeddedStyles.add( content );
    }

    /**
     * Внедряет в страницу фрагмент HTML. Как правило используется в качестве шаблона при построении UI на стороне пользователя.
     * @param content фрагмент HTML. Не может быть <code>null</code>.
     */
    public void embedHTML(final CharSequence content) {
        if (content!=null && content.length()>0)
            embeddedHTML.add( content );
    }

    /**
     * Возвращает количество ресурсов разных типов которые требуется внедрить/подключить к web странице на текущий момент.
     * @return  общее количество ресурсов всех типов находящихся под управлением данного менеджера ресурсов.
     */
    public int size() {
        return resources.size() + embeddedScripts.size() + embeddedStyles.size() + embeddedHTML.size();
    }

    /**
     * Создает новую точку сохранения.
     * Пример использования:
     * <code>
     * <pre>
     *  final Resources.SavePoint sp = resources.makeSavePoint();
     *  try {
     *      resources.attach(Resources.Type.Javascript, new URL("http://localhost:8080/myapp/js/utils.js"));
     *      // do something ...
     *  } catch (Exception e) {
     *      resources.rollback(sp);
     *  }
     * </pre>
     * </code>
     * @return точка сохранения.
     */
    public SavePoint makeSavePoint() {
        return new SavePoint();
    }

    /**
     * Откатывает все изменения, сделанные после создания указанной в аргументе точки сохранения.
     * Точка сохранения должна быть ранее получена при вызове метода makeSavePoint() этого же экземпляра класса Resources.
     * @param savePoint  точка сохранения.
     */
    public void rollback(final SavePoint savePoint) {
        if (savePoint==null || savePoint.getOwner()!=this)
            throw new IllegalArgumentException("no correct savepoint instance specified");
        if (savePoint.annuled)
            throw new IllegalStateException("given savepoint already annuled");
        int cnt = 0;
        for (Iterator<?> it=resources.entrySet().iterator(); it.hasNext(); ) {
            it.next();
            if (cnt++ >= savePoint.resources)
                it.remove();
        }
        cnt = 0;
        for (Iterator<?> it=embeddedScripts.iterator(); it.hasNext(); ) {
            it.next();
            if (cnt++ >= savePoint.embeddedScripts)
                it.remove();
        }
        cnt = 0;
        for (Iterator<?> it=embeddedStyles.iterator(); it.hasNext(); ) {
            it.next();
            if (cnt++ >= savePoint.embeddedStyles)
                it.remove();
        }
        cnt = 0;
        for (Iterator<?> it=embeddedHTML.iterator(); it.hasNext(); ) {
            it.next();
            if (cnt++ >= savePoint.embeddedHTML)
                it.remove();
        }
        savePoint.annuled = true;
    }


    /**
     * Описывает состояние менеджера ресурсов на определенный момент времени.
     * Дает возможность откатить текущее состояние менеджера ресурсов до состояния в котором находился менеджер в момент создания данной точки сохранения.
     */
    public final class SavePoint {
        private final int resources;
        private final int embeddedScripts;
        private final int embeddedStyles;
        private final int embeddedHTML;
        private boolean annuled;

        private SavePoint() {
            this.resources = Resources.this.resources.size();
            this.embeddedScripts = Resources.this.embeddedScripts.size();
            this.embeddedStyles = Resources.this.embeddedStyles.size();
            this.embeddedHTML = Resources.this.embeddedHTML.size();
            this.annuled = false;
        }
        public Resources getOwner() {
            return Resources.this;
        }
        public boolean isAnnuled() {
            return annuled;
        }
        public void rollback() {
            Resources.this.rollback(this);
        }
    }


    public static abstract class Resource implements Serializable {
        private final String url;
        private final String contentType;
        public Resource(final String url, final String contentType) {
            this.url = url;
            this.contentType = contentType;
        }

        public String getUrl() {
            return url;
        }
        public String getContentType() {
            return contentType;
        }
        @Override
        public int hashCode() {
            return url.hashCode();
        }
        @Override
        public boolean equals(final Object obj) {
            if (obj==null || !getClass().equals(obj.getClass()))
                return false;
            final Resource other = (Resource)obj;
            return url.equals(other.url);
        }
        @Override
        public String toString() {
            return "["+ StringUtil.extractClass(getClass().getName())+"{url:"+url+", type:"+contentType+"}]";
        }
    }


    public static final class Script extends Resource {
        private boolean defered;
        public Script(final String url, final String contentType) {
            super(url, StringUtil.getNonEmpty(contentType,DEFAULT_SCRIPT_CONTENT_TYPE));
        }
        public boolean isDefered() {
            return defered;
        }
        public void setDefered(boolean defered) {
            this.defered = defered;
        }
    }


    public static final class StyleSheet extends Resource {
        private String rel;
        private String media;
        private String title;
        public StyleSheet(final String url, final String contentType) {
            super(url, StringUtil.getNonEmpty(contentType,DEFAULT_STYLESHEET_CONTENT_TYPE));
            rel = "stylesheet";
        }
        public String getRel() {
            return rel;
        }
        public void setRel(final String rel) {
            this.rel = rel;
        }
        public String getMedia() {
            return media;
        }
        public void setMedia(final String media) {
            this.media = media;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(final String title) {
            this.title = title;
        }
    }

}
