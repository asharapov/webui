package org.echosoft.framework.ui.core.compiler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.echosoft.common.io.FastStringWriter;
import org.echosoft.common.utils.StringUtil;

/**
 * Содержит информацию о всех переменных объявленных в отдельном транслируемом методе.
 * Для каждой переменной указывается ее класс и область видимости (в каком блоке метода она объявлена) и особо отмечается момент
 * когда переменная еще доступна для использования но реально уже не используется. Обработка данной ситуации дает нам
 * возможность повторно использовать переменную вместо создания на стеке еще одной переменной (такой вот своего рода пул переменных).
 * @author Anton Sharapov
 */
public class MethodContext {

    public final TranslationContext tc;
    public final FastStringWriter content;
    public final Class resultType;
    public final String name;
    public final Map<String, Variable> vars;
    private int vLevel;

    public MethodContext(final TranslationContext tc, final Class resultType, final String name) {
        this.tc = tc;
        this.content = new FastStringWriter(512);
        this.resultType = resultType!=null ? resultType : Void.class;
        this.name = name;
        this.vars = new HashMap<String,Variable>();
        this.vLevel = 0;
        tc.methods.add( this );
    }

    /**
     * @return уровень вложенности текущего (обрабатываемого в настоящее время) блока кода в методе.
     */
    public int getLevel() {
        return vLevel;
    }

    /**
     * Вызывается при старте очередного вложенного блока кода ( после записи очередного символа '{' )
     * в теле метода генерируемого java класса.
     * Используется для автоматического контроля видимости (и используемости) локальных переменных в методе.
     */
    public void incLevel() {
        vLevel++;
    }

    /**
     * Вызывается по завершению очередного вложенного блока кода ( после записи очередного символа '}' )
     * в теле метода генерируемого java класса.
     * Используется для автоматического контроля видимости (и используемости) локальных переменных в методе.
     */
    public void decLevel() {
        if (vLevel <=0)
            throw new IllegalStateException();
        vLevel--;
        for (Iterator<Map.Entry<String,Variable>> it=vars.entrySet().iterator(); it.hasNext(); ) {
            final Variable v = it.next().getValue();
            if (vLevel < v.useLevel) {
                v.useLevel = null;
            }
            if (vLevel < v.declareLevel) {
                it.remove();
            }
        }
    }

    public Variable addArgument(final Class cls, final String name) {
        final Variable v = new Variable(this, cls, name);
        v.alreadyDeclared = true;
        v.useLevel = 0;
        vars.put(v.name, v);
        return v;
    }

    /**
     * Находит неиспользуемую локальную переменную требуемого типа и помечает ее как используемую или
     * регистрирует запись о новой переменной в методе.
     * Выбранная таким образом переменная помечается как используемая в текущий момент.
     * @param cls  класс требуемой переменной.
     * @param desiredName  желаемое имя переменной. Может не совпадать с тем именем которое переменная будет иметь по факту.
     * @return  запись о выделенной локальной переменной.
     */
    public Variable allocateVariable(final Class cls, final String desiredName) {
        final Variable result;
        // определимся со стратегией подбора имени переменной.
        // Если желаемое имя равно null или не является идентификатором java то в первую очередь
        // стараемся использовать ранее объявленную свободную переменную данного типа
        //  (подходят не все переменные а те которые удовлетворяют опр. требованиям по именованию)
        // В противном случае, если желаемое имя может быть идентификатором java то создаем новую переменную.
        if (StringUtil.isJavaIdentifier(desiredName) && !vars.containsKey(desiredName)) {
            // создаем новую переменную ...
            //   убедимся что соответствующий класс переменной перечислен в конструкции "import" ...
            result = new Variable(this, cls, desiredName);
        } else {
            // ищем подходящую переменную из уже объявленных но в данный момент не используемых переменных...
            for (Variable v : vars.values()) {
                if (v.useLevel==null && cls.equals(v.getClass()) && v.name.startsWith("cmp")) {
                    v.useLevel = vLevel;
                    return v;
                }
            }
            // создаем новую переменную ...
            //   подберем неиспользуемое имя ...
            final String prefix = "cmp";
            int suffix = 1;
            while (vars.containsKey(prefix+suffix)) suffix++;
            //   убедимся что соответствующий класс переменной перечислен в конструкции "import" ...
            final boolean qualified = !tc.ensureClassImported(cls);
            result = new Variable(this, cls, prefix+suffix);
        }
        result.useLevel = vLevel;
        vars.put(result.name, result);
        return result;
    }

}
