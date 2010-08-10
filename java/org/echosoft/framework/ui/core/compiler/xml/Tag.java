package org.echosoft.framework.ui.core.compiler.xml;

import java.io.Serializable;

import org.echosoft.common.model.Predicate;
import org.echosoft.framework.ui.core.compiler.ast.Variable;
import org.echosoft.framework.ui.core.compiler.ast.stmt.BlockStmt;
import org.xml.sax.Attributes;

/**
 * Содержит всю информацию используемую при обработке отдельного xml тега из исходного .wui файла.
 * @author Anton Sharapov
 */
public class Tag implements Serializable {

    /**
     * Ссылка на описание родительского тега в исходном xml документе.
     */
    public final Tag parent;
    /**
     * Идентификатор пространства имен для данного тега.
     */
    public final String uri;
    /**
     * Полное имя тега. Включает идентификатор адресного пространства и локальное имя тега.
     */
    public final String qname;
    /**
     * Локальное имя тега.
     */
    public final String name;
    /**
     * Перечень атрибутов, указанных для данного тега.
     */
    public final Attributes attrs;
    /**
     * Обработчик данного тега.
     */
    public final TagHandler handler;
    /**
     * Узел синтаксического дерева, под которым будут добавляться узлы, соответствующие данному xml тегу.
     */
    public final BlockStmt container;
    /**
     * Узел синтаксического дерева, под которым будут добавляться узлы, соответствующие дочерним xml тегам.
     */
    public BlockStmt childrenContainer;
    /**
     * Результатом работы абсолютного большинства обработчиков тегов является конструирование какого-либо одного объекта
     * некоторого класса и последующая установка его свойств:
     * <pre>
     *  MyClass ref = new MyClass();
     *  ref.setXXX(...);
     *  ref.setYYY(...)
     *  ...
     * </pre>
     * <p>Очень часто, дочерние нижележащие элементы в исходном xml дереве используются для продолжения установки свойств
     * объекта, сконструированного обработчиком родительского тега. В этих целях мы выносим в описание тега краткую информацию
     * о том какой java объект был сконструирован обработчиком данного тега чтобы этим могли воспользоваться обработчики
     * дочерних тегов.</p><br/>
     * <p>Если обработчик тега не конструировал никаких новых java объектов то данное поле останется равным null
     * (пример: группа тегов описывающих различные управляющие инструкции java, такие как <code>if</code>, <code>for</code>,
     * <code>while</code>).</p><br/>
     * <p>Если обработчик тега в ходе своей работы объявил в транслируемом java файле несколько переменных которые
     * потенциально могут представлять интерес для обработчиков дочерних тегов то следует либо воспользоваться свойством
     * {@link #data} либо создать новый класс-дескриптор тегов унаследовав его от данного класса.
     * </p>
     */
    public Variable bean;
    /**
     * Обработчики тегов могут использовать данное поле на свое усмотрение.
     */
    public Object data;

    public Tag(final Tag parent, final String uri, final String qname, final String name, final Attributes attrs, final TagHandler handler) {
        this.parent = parent;
        this.container = parent.childrenContainer;
        this.uri = uri;
        this.qname = qname;
        this.name = name;
        this.attrs = attrs;
        this.handler = handler;
    }

    public Tag(final BlockStmt rootContainer, final String uri, final String qname, final String name, final Attributes attrs, final TagHandler handler) {
        this.parent = null;
        this.container = rootContainer;
        this.uri = uri;
        this.qname = name;
        this.name = name;
        this.attrs = attrs;
        this.handler = handler;
    }

    /**
     * Осуществляет поиск ближайшего родительского тега который удовлетворяет заданным в аргументе условиям.
     * @param predicate  условия поиска.
     * @return ближайший тег, удовлетворяющий условиям поиска, или <code>null</code> если ни один тег не
     *          удовлетворяет заданным условиям.
     */
    public Tag findParent(final Predicate<Tag> predicate) {
        for (Tag t=this; t!=null; t=t.parent) {
            if (predicate.accept(t))
                return t;
        }
        return null;
    }
}
