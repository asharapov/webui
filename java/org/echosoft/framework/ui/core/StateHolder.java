package org.echosoft.framework.ui.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Сохраняет состояние посещенных страниц приложения между запросами.
 * Используется как альтернатива сессии в тех случаях когда использование последней затруднено,
 * В отличие от сессий, состояния хранятся в сериализованном виде на клиентской стороне в специальном параметре формы и передаются на сервер с каждым постом. Полноценная поддержка состояний возможно только если пользоваться запросами типа POST.
 * В случае с GET запросами передача состояний данным способом фактически невозможна из-за жесткого ограничения на размер запроса данного типа.
 * 
 * @author Anton Sharapov
 */
public class StateHolder {

    private final HashMap<ViewStateDescriptor, Map<String,Object>> states;
    private ViewStateDescriptor current;

    public StateHolder() {
        states = new HashMap<ViewStateDescriptor,Map<String,Object>>();
    }

    /**
     * Возвращает список дескрипторов всех страниц, состояния которых в настоящий момент присутствуют в менеджере состояний.
     * @return дескрипторы всех страниц, присутствующих в настояший момент в менеджере состояний.
     */
    public Set<ViewStateDescriptor> getViewStates() {
        return states.keySet();
    }

    /**
     * Возвращает состояние страницы, заданной указанным дескриптором.
     * Если это состояние отсутствует в данный момент в менеджере состояний то метод возвращает <code>null</code>.
     * @param desc  дескриптор страницы.
     * @return  состояние страницы или <code>null</code> если данная страница отсутствует в менеджере состояний.
     */
    public Map<String,Object> getViewState(final ViewStateDescriptor desc) {
        return states.get(desc);
    }

    /**
     * Возвращает состояние страницы, заданной указанным дескриптором.
     * Если это состояние отсутствует в данный момент в менеджере состояний то оно регистрируется в менеджере и
     * метод возвращает пустое состояние.
     * @param desc дескриптор страницы.
     * @return  состояние страницы.
     */
    public Map<String,Object> ensureStateExists(final ViewStateDescriptor desc) {
        Map<String,Object> state = states.get(desc);
        if (state==null) {
            if (desc==null)
                throw new NullPointerException("state descriptor not specified");
            state = new HashMap<String,Object>();
            states.put(desc, state);
        }
        return state;
    }

    /**
     * Возвращает дескриптор того состояния которое в текущий момент помечено в менеджере состояний как текущее,
     * то есть состояние текущей (отображаемой в настоящий момент) страницы.
     * Если ни одно из зарегистрированных в менеджере состояний не помечено как текущее то метод возвращает <code>null</code>.
     * @return дескриптор страницы,  которая была помечена как текущая или <code>null</code>.
     */
    public ViewStateDescriptor getCurrentDescriptor() {
        return current;
    }

    /**
     * Помечает указанное в аргументе состояние как текущее.
     * Данное состояние уже должно быть зарегистрировано в менеджере состояний.
     * Если в менеджере состояний уже было какое-то другое состояние помечено как текущее, то после вызова данного метода оно
     * автоматически перестает таковым быть, т.е. в один момент времени в менеджере может быть 0 или 1 состояние помечено как текущее.
     * @param desc  дескриптор состояния которое должно быть помечено как текущее. Может быть <code>null</code>.
     */
    public void setCurrentDescriptor(final ViewStateDescriptor desc) {
        if (desc!=null && !states.containsKey(desc)) {
            states.put(desc, new HashMap<String,Object>());
        }
        this.current = desc;
    }

    /**
     * Возвращает состояние текущей страницы.
     * @return  состояние текущей страницы.
     * @throws NullPointerException  если ни одна страница в менеджере не была помечена как текущая.
     */
    public Map<String,Object> getCurrentState() {
        return ensureStateExists(current);
    }


    /**
     * Удаляет информацию о состояниях страниц которые более не представляют интереса.
     * @param strategy  используемая стратегия сборки мусора.
     */
    public void clean(final CleanStrategy strategy) {
        for (Iterator<Map.Entry<ViewStateDescriptor,Map<String,Object>>> it=states.entrySet().iterator(); it.hasNext(); ) {
            final Map.Entry<ViewStateDescriptor, Map<String,Object>> entry = it.next();
            if (strategy.needToClean(current, entry.getKey()) || entry.getValue().isEmpty()) {
                it.remove();
            }
        }
    }


    @Override
    public int hashCode() {
        int h = 0;
        for (Map.Entry<ViewStateDescriptor, Map<String, Object>> entry : states.entrySet()) {
            h += entry.hashCode();
        }
        return h;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj==null || !getClass().equals(obj.getClass()))
            return false;
        final StateHolder other = (StateHolder)obj;
        return states.equals(other.states) &&
               current!=null ? current.equals(other.current) : other.current==null;
    }

    @Override
    public String toString() {
        final StringBuilder out = new StringBuilder(256);
        out.append("[States:");
        for (Map.Entry<ViewStateDescriptor, Map<String, Object>> entry : states.entrySet()) {
            final ViewStateDescriptor desc = entry.getKey();
            out.append("{pkg:").append(desc.getPackage()).append(", view:").append(desc.getView()).append(", rank:").append(desc.getRank()).append(", size:").append(entry.getValue().size());
            if (current==desc)
                out.append(" (current)");
            out.append("} ");
        }
        out.append("]");
        return out.toString();
    }
}
