package org.echosoft.framework.ui.core.compiler.xml;

import org.echosoft.common.model.Predicate;
import org.echosoft.framework.ui.core.compiler.ast.ASTNode;
import org.echosoft.framework.ui.core.compiler.ast.Variable;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTClassDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTMethodDecl;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTBlockStmt;
import org.xml.sax.Attributes;

/**
 * Содержит всю информацию используемую при обработке отдельного xml тега из исходного .wui файла.
 * @author Anton Sharapov
 */
public class Tag {

    private final Tag parent;
    private final String uri;
    private final String qname;
    private final String name;
    private final Attributes attrs;
    private final TagHandler handler;
    private final ASTBlockStmt container;
    private ASTBlockStmt childrenContainer;
    private Variable bean;
    private Variable context;
    private Object data;
    private transient ASTClassDecl classDecl;
    private transient ASTMethodDecl methodDecl;

    public Tag(final Tag parent, final String uri, final String qname, final String name, final Attributes attrs, final TagHandler handler) {
        this.parent = parent;
        this.uri = uri;
        this.qname = qname;
        this.name = name;
        this.attrs = attrs;
        this.handler = handler;
        this.container = parent.childrenContainer;
        this.context = parent.context;
    }

    public Tag(final ASTBlockStmt rootContainer, final Variable rootContext, final String uri, final String qname, final String name, final Attributes attrs, final TagHandler handler) {
        this.parent = null;
        this.uri = uri;
        this.qname = qname;
        this.name = name;
        this.attrs = attrs;
        this.handler = handler;
        this.container = rootContainer;
        this.context = rootContext;
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

    /**
     * Ссылка на описание родительского тега в исходном xml документе.
     * @return на описание родительского тега в исходном xml документе.
     */
    public Tag getParent() {
        return parent;
    }

    /**
     * Идентификатор пространства имен для данного тега.
     * @return Идентификатор пространства имен для данного тега.
     */
    public String getUri() {
        return uri;
    }

    /**
     * Полное имя тега. Включает идентификатор адресного пространства и локальное имя тега.
     * @return Полное имя тега. Включает идентификатор адресного пространства и локальное имя тега.
     */
    public String getQname() {
        return qname;
    }

    /**
     * Локальное имя тега.
     * @return Локальное имя тега.
     */
    public String getName() {
        return name;
    }

    /**
     * Перечень атрибутов, указанных для данного тега.
     * @return  Перечень атрибутов, указанных для данного тега.
     */
    public Attributes getAttrs() {
        return attrs;
    }

    /**
     * Обработчик данного тега.
     * @return Обработчик данного тега.
     */
    public TagHandler getHandler() {
        return handler;
    }

    /**
     * Узел синтаксического дерева, под которым будут добавляться узлы, соответствующие данному xml тегу.
     * @return Узел синтаксического дерева, под которым будут добавляться узлы, соответствующие данному xml тегу.
     */
    public ASTBlockStmt getContainer() {
        return container;
    }

    /**
     * Узел синтаксического дерева, под которым будут добавляться узлы, соответствующие дочерним xml тегам.
     * @return экземпляр {@link org.echosoft.framework.ui.core.compiler.ast.stmt.ASTBlockStmt} под которым будут расположены узлы,
     *      соответствующие выражениям транслированным на основе дочерних xml тегов.
     */
    public ASTBlockStmt getChildrenContainer() {
        return childrenContainer;
    }
    public void setChildrenContainer(final ASTBlockStmt childrenContainer) {
        this.childrenContainer = childrenContainer;
    }

    /**
     * Результатом работы абсолютного большинства обработчиков тегов является конструирование какого-либо одного объекта
     * некоторого класса и последующая установка его свойств, т.е. фрагмент исходного .xml файла
     * <pre>
     *   &lt;my-class id="ref" a="..." b="..."&gt; ... &lt;/my-class&gt;
     * </pre>
     * будет транслирован в соответствующий фрагмент .java файла: 
     * <pre>
     *  MyClass ref = new MyClass();
     *  ref.setA(...);
     *  ref.setB(...)
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
     * @return информация о переменной в транслируемом .java файле, в которой содержится ссылка на объект,
     * созданный и, вероятно, инициализированный в рамках выполнения обработчика данного тега.
     */
    public Variable getBean() {
        return bean;
    }
    public void setBean(final Variable bean) {
        this.bean = bean;
    }

    /**
     * Ссылка на переменную в java коде, в которой хранится контекст родительского компонента.</br>
     * Именно от этого котекста наследуются контексты все дочерних компонент.
     * @return описывает имя переменной, содержащей контекст родительского компонента.
     */
    public Variable getContext() {
        return context;
    }
    public void setContext(final Variable context) {
        this.context = context;
    }

    /**
     * Использование данного поля никак не регламентировано на данном этапе.
     * Разработчики новых обработчиков тегов вольны использовать данное свойство на свое усмотрение.
     * @return разработчики тегов могут использовать данное свойство произвольным образом.
     */
    public Object getData() {
        return data;
    }
    public void setData(final Object data) {
        this.data = data;
    }


    public ASTClassDecl getEnclosingClass() {
        if (classDecl==null) {
            ASTNode node = this.container.getParent();
            while (node!=null && !(node instanceof ASTClassDecl))
                node = node.getParent();
            classDecl = (ASTClassDecl)node;
        }
        return classDecl;
    }

    public ASTMethodDecl getEnclosingMethod() {
        if (methodDecl==null) {
            ASTNode node = this.container.getParent();
            while (node!=null && !(node instanceof ASTMethodDecl))
                node = node.getParent();
            methodDecl = (ASTMethodDecl)node;
        }
        return methodDecl;
    }

}
