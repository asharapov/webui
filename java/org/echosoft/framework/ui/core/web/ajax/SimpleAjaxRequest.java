package org.echosoft.framework.ui.core.web.ajax;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.types.Type;
import org.echosoft.common.utils.BeanUtil;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.Application;

/**
 * <p>
 * Примеры запросов:
 * <div style="margin:4px;border:1px solid black">
 *  <i>/ajax/simple/session/mydata</i><br>
 *  Из пространства имен <u><b>session</b></u> берется объект <u><b>mydata</b></u>, сериализуется в формат JSON и возвращается клиенту.
 * </div>
 * <div style="margin:4px;border:1px solid black">
 *  <i>/ajax/simple/application/facade.documentServices/getCurrentDocument(user,type)?type=x&user=y</i><br>
 *  Из пространства имен <u><b>application</b></u> берется объект <u><b>facade</b></u>, у него берется значение свойства <u><b>documentServices</b></u>.
 *  В полученном объекте ищется метод <u><b>getCurrentDocument</b></u> с двумя аргументами (их тип не имеет значения). Если такой метод отсутствует либо
 *  было найдено более одного то возвращается исключительная ситуация. В противном случае
 *  сериализуется в формат JSON и возвращается клиенту.
 * </div>
 * </p>
 * @author Anton Sharapov
 */
public final class SimpleAjaxRequest extends AjaxRequest {

    private String callbackMethod;
    private String scope;
    private String object;
    private String property;
    private String methodName;
    private String[] argnames;

    public SimpleAjaxRequest(HttpServletRequest request, ServletContext context, String resource) {
        super(request, context, resource);
    }


    public void execute(final Writer out) throws Throwable {
        init();
        final Object data = getData();

        if (callbackMethod!=null) {
            out.write(callbackMethod);
            out.write('(');
        }
        final JsonWriter writer = Application.jsonContext.makeJsonWriter(out);
        writer.writeObject(data);
        if (callbackMethod!=null) {
            out.write(')');
        }
    }


    protected void init() throws Exception {
        // parameters for AJAX responce generation
        callbackMethod = request.getParameter("callback");

        // datasource parsing ...
        if (resource.length()<1)
            throw new Exception("Illegal path: "+resource);
        final int s1 = resource.indexOf('/', 1);
        if (s1<0 || s1>=resource.length()-1)
            throw new Exception("Illegal path: "+resource);
        final int s2 = resource.indexOf('/', s1+1);
        scope = resource.substring(1,s1);
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
        methodName = s2>0 ? resource.substring(s2+1) : null;
        if (methodName!=null) {
            final int b1 = methodName.indexOf('(');
            final int b2 = methodName.indexOf(')');
            if (b1>0 && b2>0 && b2>b1) {
                argnames = methodName.substring(b1+1,b2).split(",");
                methodName = methodName.substring(0,b1);
            } else
                argnames = StringUtil.EMPTY_STRING_ARRAY;
        }
    }


    protected Object getData() throws Throwable {
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
        } else
            return obj;
    }
}
