package org.echosoft.framework.ui.core.web.ajax;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.query.BeanIterator;
import org.echosoft.common.query.Query;
import org.echosoft.common.query.QueryResult;
import org.echosoft.common.query.providers.ClassDataProvider;
import org.echosoft.common.query.providers.ListDataProvider;
import org.echosoft.common.types.Type;
import org.echosoft.common.utils.Any;
import org.echosoft.common.utils.BeanUtil;
import org.echosoft.common.utils.ObjectUtil;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.Application;

/**
 * Разбирает параметры ajax запроса для определения источника данных которые необходимо вернуть пользователю.
 * Соответствующая информация передается в свойстве {@link HttpServletRequest#getPathInfo()} и в ряде определенных парамаметров запроса.
 * <ul>
 * <li> <b>Источник данных</b> - определяется по значению свойства запроса {@link HttpServletRequest#getPathInfo()}. Эта строка в целом имеет вид:
 *   <b><code>scope/object[.properties][/method]</code></b>, где:
 *  <ul>
 *   <li> <b>scope</b> - пространство имен в котором следует искать данные. На данный момент может принимать одно из двух значений - <i>session</i> или <i>application</i>.
 *   <li> <b>object</b> - имя атрибута в указанном пространстве имен чье значение или содержит требуемые данные или несет в себе информацию о том как до них добраться в системе.
 *   <li> <b>properties</b> - опционально указывается имя свойства объекта (вычисленного на основании свойств SCOPE и OBJECT) которое или содержит требуемые данные или несет в себе информацию о том как до них добраться в системе.
 *   <li> <b>method</b> - Имя метода вызов которого возвращает требуемые данные. Данный метод должен удовлетворять требованиям предъявляемым в классе {@link ClassDataProvider}.
 *  </ul>
 * </li>
 * <li> <b>Ограничения наложенные на возвращаемые данные</b> - определяются по следующим параметрам запроса:
 *  <ul>
 *   <li> <b>query</b> - сериализованная форма объекта {@link Query}.
 *   <li> <b>sort</b> - возвращаемые данные должны быть отсортированы по полю указанному в этом параметре. Транслируются в соотв. свойства {@link Query}.
 *   <li> <b>dir</b> - направление сортировки. Допустимые значения: ASC, DESC. Транслируются в соотв. свойства {@link Query}.
 *   <li> <b>start</b> - позволяет пропустить N первых записей в итоговом наборе данных. Используется для постраничной организации данных. Транслируются в соотв. свойства {@link Query}.
 *   <li> <b>limit</b> - указывает максимально-допустимое количество записей которое надо отдать клиенту. Транслируются в соотв. свойства {@link Query}.
 *   <li> <b>qp.*</b> - все параметры удовлетворяющие данной маске транслируются в дополнительные именованные параметры объекта {@link Query}.
 *  </ul>
 * </li>
 * <li> Форматирование результата - определяется по следующим параметрам запроса: 
 *  <ul>
 *   <li> <b>callback</b> - Если этот парамет указан то результат работы сервлета будет оформлен в виде вызова js функции в которую в качестве аргумента будет переданы возвращаемые данные. 
 *   <li> <b>total</b> - Название свойства объекта в котором будет указано количество записей
 *   <li> <b>items</b>
 *  </ul>
 * </ul>

 * Примеры запросов:
 * <div style="margin:4px;border:1px solid black">
 *  <i>/ajax/provider/session/users?sort=name&dir=ASC</i><br>
 *  Из атрибута <i>users</i> сессии берется объект (или коллекция объектов в нашем случае) и используется в качестве источника данных
 *  (см. {@link ListDataProvider}).
 *  Полученный набор данных сортируется по полю name.
 * </div>
 * <div style="margin:4px;border:1px solid black">
 *  <i>/ajax/provider/session/order.items</i><br>
 *  Из атрибута <i>order</i> сессии берется объект. У него вызывается метод <i>getItems()</i> результат которого и используется в качестве источника данных
 *  (см. {@link ListDataProvider}).
 * </div>
 * <div style="margin:4px;border:1px solid black">
 *  <i>/ajax/provider/application/facade.companyDAO/listCompanies</i><br>
 *  В пространстве имен уровня приложения ищется атрибут <i>facade</i>. У него вызывается метод <i>getCompanyDAO</i> который возвращает некоторый объект.
 *  Обращение к методу listCompanies данного объекта и даст нам искомый набор данных
 *  (см. {@link ClassDataProvider}).
 * </div>
 * @author Anton Sharapov
 */
public final class DataProviderAjaxRequest extends AjaxRequest {

    private String callbackMethod;
    private String totalProperty;
    private String itemsProperty;

    private String scope;
    private String object;
    private String property;
    private String methodName;
    private String[] argnames;
    private Query query;

    public DataProviderAjaxRequest(HttpServletRequest request, ServletContext context, String resource) {
        super(request, context, resource);
    }

    public void execute(final Writer out) throws Throwable {
        init();
        final QueryResult qr = getData();
        //final QueryResult qr = provider.executePaged(query);
        if (qr==null) {
            return;
        }
        if (callbackMethod!=null) {
            out.write(callbackMethod);
            out.write('(');
        }
        final JsonWriter writer = Application.jsonContext.makeJsonWriter(out);
        writer.beginObject();
        writer.writeProperty(totalProperty, qr.getTotalSize());
        writer.writeProperty(itemsProperty, qr.getBeans());
        writer.endObject();
        if (callbackMethod!=null) {
            out.write(')');
        }
    }


    protected void init() throws Exception {
        // parameters for AJAX responce generation
        callbackMethod = request.getParameter("callback");
        totalProperty = StringUtil.getNonEmpty(request.getParameter("total"), "total");
        itemsProperty = StringUtil.getNonEmpty(request.getParameter("items"), "items");

        // datasource parsing ...
        if (resource.length()<1)
            throw new Exception("Illegal path: "+resource);
        final int s1 = resource.indexOf('/', 1);
        if (s1<0 || s1>=resource.length()-1)
            throw new Exception("Illegal path: "+resource);
        final int s2 = resource.indexOf('/', s1+1);
        scope = resource.substring(1,s1);
        methodName = s2>0 ? resource.substring(s2+1) : null;
        final String expr = resource.substring(s1+1,s2>0 ? s2 : resource.length());
        final int s3 = expr.indexOf('.',0);
        if (s3==0 || s3==expr.length()-1) {
            throw new Exception("Illegal path: "+resource);
        } else
        if (s3<0) {
            object = expr;
            property = null;
        } else {
            object = expr.substring(0,s3);
            property = expr.substring(s3+1);
        }
        if (methodName!=null) {
            final int b1 = methodName.indexOf('(');
            final int b2 = methodName.indexOf(')');
            if (b1>0 && b2>0 && b2>b1) {
                argnames = methodName.substring(b1+1,b2).split(",");
                methodName = methodName.substring(0,b1);
            } else
                argnames = null;
        }

        final String pq = StringUtil.trim(request.getParameter("query"));
        query = pq!=null ? Query.deserialize(pq) : new Query();

        final String sortAttr = StringUtil.trim(request.getParameter("sort"));
        if (sortAttr!=null) {
            final String dir = StringUtil.trim(request.getParameter("dir"));
            final boolean ascending = dir==null || !"DESC".equalsIgnoreCase(dir);
            query.getSortCriteria().append(sortAttr, ascending);
        }

        final int limit = Any.asInt(request.getParameter("limit"), 0);
        final int start = Any.asInt(request.getParameter("start"), 0);
        final int pageno;
        if (limit>0) {
            pageno = start / limit;
        } else {
            pageno = 0;
        }
        query.setPageSize(limit);
        query.setPageNumber(pageno);
        for (Enumeration e=request.getParameterNames(); e.hasMoreElements(); ) {
            final String name = (String)e.nextElement();
            if (name.startsWith("qp.")) {
                final String[] values = request.getParameterValues(name);
                query.getNamedParams().put(name.substring(3), values.length==1 ? values[0] : values);
            } else
            if (name.startsWith("ql.")) {
                final String linkname = request.getParameter(name);
                final String[] values = request.getParameterValues(linkname);
                query.getNamedParams().put(name.substring(3), values.length==1 ? values[0] : values);
            }
        }
    }


    @SuppressWarnings("unchecked")
    protected QueryResult getData() throws Throwable {
        Object obj;
        if ("application".equals(scope)) {
            obj = context.getAttribute(object);
        } else
        if ("session".equals(scope)) {
            obj = request.getSession().getAttribute(object);
        } else {
            Application.log.error("Unknown scope: "+scope);
            return null;
        }
        if (property!=null) {
            obj = BeanUtil.getProperty(obj, property);
        }

        if (obj!=null && methodName!=null) {
            if (argnames==null) {
                return new ClassDataProvider(obj, methodName).executePaged(query);
            } else {
                obj = invokeMethod(obj);
            }
        }

        if (obj == null) {
            final int pageSize = query!=null ? query.getPageSize() : 0;
            final int pageNum = query!=null ? query.getPageNumber() : 0;
            return new QueryResult(Collections.EMPTY_LIST, null, 0, pageNum*pageSize, pageSize);
        } else
        if (QueryResult.class.equals(obj)) {
            return (QueryResult)obj;
        } else
        if (obj instanceof BeanIterator) {
            final BeanIterator iter = (BeanIterator)obj;
            try {
                return new QueryResult(iter);
            } finally {
                iter.close();
            }
        } else
        if (obj instanceof Object[]) {
            return new ListDataProvider<Object>((Object[])obj).executePaged(query);
        } else
        if (obj instanceof List) {
            return new ListDataProvider<Object>((List)obj).executePaged(query);
        } else
            return new ListDataProvider<Object>(ObjectUtil.makeIterator(obj)).executePaged(query);
    }


    protected Object invokeMethod(final Object obj) throws Throwable {
        final Object[] args = new Object[argnames.length];
        for (Method method : obj.getClass().getMethods()) {
            if (!method.getName().equals(methodName))
                continue;
            final Class<?>[] params = method.getParameterTypes();
            if (params.length!=argnames.length)
                continue;

            for (int i=0; i<argnames.length; i++) {
                final String encodedArg = request.getParameter(argnames[i]);
                if (encodedArg!=null) {
                    final Type<?> tt = AjaxRequest.types.findType(params[i]);
                    if (tt==null) {
                        method = null;
                        break;
                    }
                    args[i] = tt.decode(encodedArg);
                }
            }
            if (method!=null) {
                try {
                    return method.invoke(obj, args);
                } catch (InvocationTargetException ite) {
                    throw ite.getTargetException();
                }
            }
        }
        throw new Exception("Can't resolve method "+methodName+" with required characteristics");
    }
}
