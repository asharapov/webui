package org.echosoft.framework.ui.core.compiler.binding;

import java.math.BigDecimal;
import java.util.Date;

import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.compiler.ast.ASTVariableDecl;
import org.echosoft.framework.ui.core.compiler.ast.Mods;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTClassDecl;
import org.echosoft.framework.ui.core.compiler.ast.body.ASTFieldDecl;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTBooleanLiteralExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTCastExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTDoubleLiteralExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTExpression;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTIntegerLiteralExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTLongLiteralExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTMethodCallExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTNameExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTNullLiteralExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTObjectCreationExpr;
import org.echosoft.framework.ui.core.compiler.ast.expr.ASTStringLiteralExpr;
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTExpressionStmt;
import org.echosoft.framework.ui.core.compiler.ast.type.RefType;
import org.echosoft.framework.ui.core.compiler.xml.Tag;

/**
 * Содержит вспомогательные методы для облегчения обработки динамически вычисляемых выражений.
 *
 * @author Anton Sharapov
 */
public class ExpressionUtils {

    public static void setBeanProperty(final Tag tag, final String methodName, final ASTExpression expr) {
        final ASTMethodCallExpr mce = new ASTMethodCallExpr(new ASTNameExpr(tag.getBean().getName()), methodName);
        mce.addArgument( expr );
        tag.getContainer().addStatement( new ASTExpressionStmt(mce) );
    }

    /**
     * Определяет, содержит ли указанная в аргументе строка динамически вычисляемые выражения.
     * @param text  строка, которую требуется проверить на наличие вычисляемых выражений.
     * @return Возвращает одно из следующих значений:
     * <li> 0 - динамически вычисляемые выражения в строке отсутствуют.
     * <li> 1 - строка представляет собой одно динамически вычисляемое выражение.
     * <li> 2 - строка состоит из двух и более динамически вычисляемых выражений.
     */
    public static int containsExpression(final String text) {
        if (text==null)
            return 0;
        final int len = text.length();
        int ts = -1;
        char cc = (char)0;
        for (int i=0; i<len; i++) {
            final char c = text.charAt(i);
            if (c=='{' && cc=='@') {
                ts = i-1;
            } else
            if (c=='}' && ts>=0) {
                return (ts>0 || i+1<len) ? 2 : 1;
            }
            cc = c;
        }
        return 0;
    }

    /**
     * <p>Подготавливает соотвествующее AST-выражение на основе указанной в аргументах метода строки.</p>  
     * <p>Если аргумент <code>text</code> содержит динамически вычисляемое выражение то метод создает статическое поле с ссылкой на это предкомпилированную форму данного выражения и в дальнейшем использует уже именно ее.
     * Подобный подход позволяет существенно ускорять вычисление значений за счет того что они компилируются единожды на стадии загрузки класса в память.</p>
     * Результатом выполнения данного метода является примерно следующее выражение в коде формируемого .java класса:
     * <pre>
     *  private static final ValueExpression expr1 = new CompoundExpression("<i>Hello, @{request:user.name}!</i>");
     *  ...
     *  expr1.evaluate(ctx);
     * </pre>
     * @param tag описание xml тега в исходном .wui файле. Оттуда извлекается информация об экземпляре текущего обрабатываемого компонента в .java файле.
     * @param text  текст который может оказаться как статическим так и динамическим выражением.
     * @return AST-выражение соответствующее указанной в аргументе метода строке. Результатом вычисления данного выражения может быть объект произвольного класса. 
     */
    public static ASTExpression makeExpression(final Tag tag, final String text) {
        final Class exprClass;
        switch (containsExpression(text)) {
            case 0 : return new ASTStringLiteralExpr(text);
            case 1 : exprClass = SimpleExpression.class; break;
            case 2 :
            default : exprClass = CompoundExpression.class; break;
        }
        final String fieldName = defineExpression(tag.getEnclosingClass(), exprClass, text);
        final ASTMethodCallExpr mce = new ASTMethodCallExpr( new ASTNameExpr(fieldName), "evaluate");
        mce.addArgument( new ASTNameExpr(tag.getContext().getName()) );
        return mce;
    }

    /**
     * <p>Подготавливает соотвествующее AST-выражение на основе указанной в аргументах метода строки.</p>
     * <p>Если аргумент <code>text</code> содержит динамически вычисляемое выражение то метод создает статическое поле с ссылкой на это предкомпилированную форму данного выражения и в дальнейшем использует уже именно ее.
     * Подобный подход позволяет существенно ускорять вычисление значений за счет того что они компилируются единожды на стадии загрузки класса в память.</p>
     * Результатом выполнения данного метода является примерно следующее выражение в коде формируемого .java класса:
     * <pre>
     *  private static final ValueExpression expr1 = new CompoundExpression("<i>Hello, @{request:user.name}!</i>");
     *  ...
     *  StringUtil.valueOf(expr1.evaluate(ctx));
     * </pre>
     * @param tag описание xml тега в исходном .wui файле. Оттуда извлекается информация об экземпляре текущего обрабатываемого компонента в .java файле.
     * @param text  текст который может оказаться как статическим так и динамическим выражением.
     * @return AST-выражение соответствующее указанной в аргументе метода строке. Результатом вычисления данного выражения может быть только строка. 
     */
    public static ASTExpression makeStringExpression(final Tag tag, final String text) {
        final Class exprClass;
        switch (containsExpression(text)) {
            case 0 : return new ASTStringLiteralExpr(text);
            case 1 : exprClass = SimpleExpression.class; break;
            case 2 :
            default : exprClass = CompoundExpression.class; break;
        }
        final String fieldName = defineExpression(tag.getEnclosingClass(), exprClass, text);
        final ASTMethodCallExpr mce1 = new ASTMethodCallExpr( new ASTNameExpr(fieldName), "evaluate");
        mce1.addArgument( new ASTNameExpr(tag.getContext().getName()) );
        final ASTMethodCallExpr mce2 = new ASTMethodCallExpr(new RefType(StringUtil.class), "valueOf");
        mce2.addArgument(mce1);
        return mce2;
    }

    /**
     * <p>Подготавливает соотвествующее AST-выражение на основе указанной в аргументах метода строки.</p>
     * <p>Если аргумент <code>text</code> содержит динамически вычисляемое выражение то метод создает статическое поле с ссылкой на это предкомпилированную форму данного выражения и в дальнейшем использует уже именно ее.
     * Подобный подход позволяет существенно ускорять вычисление значений за счет того что они компилируются единожды на стадии загрузки класса в память.</p>
     * Результатом выполнения данного метода является примерно следующее выражение в коде формируемого .java класса:
     * <pre>
     *  private static final ValueExpression expr1 = new CompoundExpression("<i>Hello, @{request:user.name}!</i>");
     *  ...
     *  (Boolean)expr1.evaluate(ctx);
     * </pre>
     * @param tag описание xml тега в исходном .wui файле. Оттуда извлекается информация об экземпляре текущего обрабатываемого компонента в .java файле.
     * @param text  текст который может оказаться как статическим так и динамическим выражением.
     * @return AST-выражение соответствующее указанной в аргументе метода строке. Результатом вычисления данного выражения может быть только строка.
     */
    public static ASTExpression makeBooleanExpression(final Tag tag, final String text) {
        if (text==null || text.isEmpty())
            return new ASTNullLiteralExpr();
        final Class exprClass;
        switch (containsExpression(text)) {
            case 0 : return new ASTBooleanLiteralExpr(text);
            case 1 : exprClass = SimpleExpression.class; break;
            case 2 :
            default : exprClass = CompoundExpression.class; break;
        }
        final String fieldName = defineExpression(tag.getEnclosingClass(), exprClass, text);
        final ASTMethodCallExpr mce = new ASTMethodCallExpr( new ASTNameExpr(fieldName), "evaluate");
        mce.addArgument( new ASTNameExpr(tag.getContext().getName()) );
        return new ASTCastExpr(new RefType(Boolean.class), mce);
    }

    /**
     * <p>Подготавливает соотвествующее AST-выражение на основе указанной в аргументах метода строки.</p>
     * <p>Если аргумент <code>text</code> содержит динамически вычисляемое выражение то метод создает статическое поле с ссылкой на это предкомпилированную форму данного выражения и в дальнейшем использует уже именно ее.
     * Подобный подход позволяет существенно ускорять вычисление значений за счет того что они компилируются единожды на стадии загрузки класса в память.</p>
     * Результатом выполнения данного метода является примерно следующее выражение в коде формируемого .java класса:
     * <pre>
     *  private static final ValueExpression expr1 = new CompoundExpression("<i>Hello, @{request:user.name}!</i>");
     *  ...
     *  (Integer)expr1.evaluate(ctx);
     * </pre>
     * @param tag описание xml тега в исходном .wui файле. Оттуда извлекается информация об экземпляре текущего обрабатываемого компонента в .java файле.
     * @param text  текст который может оказаться как статическим так и динамическим выражением.
     * @return AST-выражение соответствующее указанной в аргументе метода строке. Результатом вычисления данного выражения может быть только объект класса Integer либо производного от него класса. 
     */
    public static ASTExpression makeIntegerExpression(final Tag tag, final String text) {
        if (text==null || text.isEmpty())
            return new ASTNullLiteralExpr();
        final Class exprClass;
        switch (containsExpression(text)) {
            case 0 : return new ASTIntegerLiteralExpr(text);
            case 1 : exprClass = SimpleExpression.class; break;
            case 2 :
            default : exprClass = CompoundExpression.class; break;
        }
        final String fieldName = defineExpression(tag.getEnclosingClass(), exprClass, text);
        final ASTMethodCallExpr mce = new ASTMethodCallExpr( new ASTNameExpr(fieldName), "evaluate");
        mce.addArgument( new ASTNameExpr(tag.getContext().getName()) );
        return new ASTCastExpr(new RefType(Integer.class), mce);
    }

    /**
     * <p>Подготавливает соотвествующее AST-выражение на основе указанной в аргументах метода строки.</p>
     * <p>Если аргумент <code>text</code> содержит динамически вычисляемое выражение то метод создает статическое поле с ссылкой на это предкомпилированную форму данного выражения и в дальнейшем использует уже именно ее.
     * Подобный подход позволяет существенно ускорять вычисление значений за счет того что они компилируются единожды на стадии загрузки класса в память.</p>
     * Результатом выполнения данного метода является примерно следующее выражение в коде формируемого .java класса:
     * <pre>
     *  private static final ValueExpression expr1 = new CompoundExpression("<i>Hello, @{request:user.name}!</i>");
     *  ...
     *  (Long)expr1.evaluate(ctx);
     * </pre>
     * @param tag описание xml тега в исходном .wui файле. Оттуда извлекается информация об экземпляре текущего обрабатываемого компонента в .java файле.
     * @param text  текст который может оказаться как статическим так и динамическим выражением.
     * @return AST-выражение соответствующее указанной в аргументе метода строке. Результатом вычисления данного выражения может быть только объект класса Long либо производного от него класса.
     */
    public static ASTExpression makeLongExpression(final Tag tag, final String text) {
        if (text==null || text.isEmpty())
            return new ASTNullLiteralExpr();
        final Class exprClass;
        switch (containsExpression(text)) {
            case 0 : return new ASTLongLiteralExpr(text);
            case 1 : exprClass = SimpleExpression.class; break;
            case 2 :
            default : exprClass = CompoundExpression.class; break;
        }
        final String fieldName = defineExpression(tag.getEnclosingClass(), exprClass, text);
        final ASTMethodCallExpr mce = new ASTMethodCallExpr( new ASTNameExpr(fieldName), "evaluate");
        mce.addArgument( new ASTNameExpr(tag.getContext().getName()) );
        return new ASTCastExpr(new RefType(Long.class), mce);
    }

    /**
     * <p>Подготавливает соотвествующее AST-выражение на основе указанной в аргументах метода строки.</p>
     * <p>Если аргумент <code>text</code> содержит динамически вычисляемое выражение то метод создает статическое поле с ссылкой на это предкомпилированную форму данного выражения и в дальнейшем использует уже именно ее.
     * Подобный подход позволяет существенно ускорять вычисление значений за счет того что они компилируются единожды на стадии загрузки класса в память.</p>
     * Результатом выполнения данного метода является примерно следующее выражение в коде формируемого .java класса:
     * <pre>
     *  private static final ValueExpression expr1 = new CompoundExpression("<i>Hello, @{request:user.name}!</i>");
     *  ...
     *  (Double)expr1.evaluate(ctx);
     * </pre>
     * @param tag описание xml тега в исходном .wui файле. Оттуда извлекается информация об экземпляре текущего обрабатываемого компонента в .java файле.
     * @param text  текст который может оказаться как статическим так и динамическим выражением.
     * @return AST-выражение соответствующее указанной в аргументе метода строке. Результатом вычисления данного выражения может быть только объект класса Double либо производного от него класса.
     */
    public static ASTExpression makeDoubleExpression(final Tag tag, final String text) {
        if (text==null || text.isEmpty())
            return new ASTNullLiteralExpr();
        final Class exprClass;
        switch (containsExpression(text)) {
            case 0 : return new ASTDoubleLiteralExpr(text);
            case 1 : exprClass = SimpleExpression.class; break;
            case 2 :
            default : exprClass = CompoundExpression.class; break;
        }
        final String fieldName = defineExpression(tag.getEnclosingClass(), exprClass, text);
        final ASTMethodCallExpr mce = new ASTMethodCallExpr( new ASTNameExpr(fieldName), "evaluate");
        mce.addArgument( new ASTNameExpr(tag.getContext().getName()) );
        return new ASTCastExpr(new RefType(Double.class), mce);
    }

    /**
     * <p>Подготавливает соотвествующее AST-выражение на основе указанной в аргументах метода строки.</p>
     * <p>Если аргумент <code>text</code> содержит динамически вычисляемое выражение то метод создает статическое поле с ссылкой на это предкомпилированную форму данного выражения и в дальнейшем использует уже именно ее.
     * Подобный подход позволяет существенно ускорять вычисление значений за счет того что они компилируются единожды на стадии загрузки класса в память.</p>
     * Результатом выполнения данного метода является примерно следующее выражение в коде формируемого .java класса:
     * <pre>
     *  private static final ValueExpression expr1 = new CompoundExpression("<i>Hello, @{request:user.name}!</i>");
     *  ...
     *  (BigDecimal)expr1.evaluate(ctx);
     * </pre>
     * @param tag описание xml тега в исходном .wui файле. Оттуда извлекается информация об экземпляре текущего обрабатываемого компонента в .java файле.
     * @param text  текст который может оказаться как статическим так и динамическим выражением.
     * @return AST-выражение соответствующее указанной в аргументе метода строке. Результатом вычисления данного выражения может быть только объект класса BigDecimal либо производного от него класса.
     */
    public static ASTExpression makeBigDecimalExpression(final Tag tag, final String text) {
        if (text==null || text.isEmpty())
            return new ASTNullLiteralExpr();
        final Class exprClass;
        switch (containsExpression(text)) {
            case 0 :
                final ASTObjectCreationExpr expr = new ASTObjectCreationExpr(new RefType(BigDecimal.class));
                expr.addArgument( new ASTDoubleLiteralExpr(text) );
                return expr;
            case 1 : exprClass = SimpleExpression.class; break;
            case 2 :
            default : exprClass = CompoundExpression.class; break;
        }
        final String fieldName = defineExpression(tag.getEnclosingClass(), exprClass, text);
        final ASTMethodCallExpr mce = new ASTMethodCallExpr( new ASTNameExpr(fieldName), "evaluate");
        mce.addArgument( new ASTNameExpr(tag.getContext().getName()) );
        return new ASTCastExpr(new RefType(BigDecimal.class), mce);
    }

    /**
     * <p>Подготавливает соотвествующее AST-выражение на основе указанной в аргументах метода строки.</p>
     * <p>Если аргумент <code>text</code> содержит динамически вычисляемое выражение то метод создает статическое поле с ссылкой на это предкомпилированную форму данного выражения и в дальнейшем использует уже именно ее.
     * Подобный подход позволяет существенно ускорять вычисление значений за счет того что они компилируются единожды на стадии загрузки класса в память.</p>
     * Результатом выполнения данного метода является примерно следующее выражение в коде формируемого .java класса:
     * <pre>
     *  private static final ValueExpression expr1 = new CompoundExpression("<i>Hello, @{request:user.name}!</i>");
     *  ...
     *  (Date)expr1.evaluate(ctx);
     * </pre>
     * @param tag описание xml тега в исходном .wui файле. Оттуда извлекается информация об экземпляре текущего обрабатываемого компонента в .java файле.
     * @param text  текст который может оказаться как статическим так и динамическим выражением.
     * @return AST-выражение соответствующее указанной в аргументе метода строке. Результатом вычисления данного выражения может быть только объект класса Date либо производного от него класса.
     */
    public static ASTExpression makeDateExpression(final Tag tag, final String text) {
        if (text==null || text.isEmpty())
            return new ASTNullLiteralExpr();
        final Class exprClass;
        switch (containsExpression(text)) {
            case 0 :
                final ASTMethodCallExpr mce = new ASTMethodCallExpr(new RefType(StringUtil.class), "parseDate");
                mce.addArgument( new ASTStringLiteralExpr(text) );
                return mce;
            case 1 : exprClass = SimpleExpression.class; break;
            case 2 :
            default : exprClass = CompoundExpression.class; break;
        }
        final String fieldName = defineExpression(tag.getEnclosingClass(), exprClass, text);
        final ASTMethodCallExpr mce = new ASTMethodCallExpr( new ASTNameExpr(fieldName), "evaluate");
        mce.addArgument( new ASTNameExpr(tag.getContext().getName()) );
        return new ASTCastExpr(new RefType(Date.class), mce);
    }

    /**
     * Создает статическое поле в классе с объявлением динамически вычисляемого выражения.<br/>
     * Результатом выполнения данного метода является примерно следующее выражение в коде формируемого .java класса:
     * <pre>
     *  private static final ValueExpression expr1 = new CompoundExpression("<i>Hello, @{request:user.name}!</i>");
     * </pre>
     * @param classNode  Узел AST-дерева, с описанием класса.
     * @param expressionClass  класс динамически вычисляемого выражения. Обязательно должен реализовывать интерфейс {@link ValueExpression}.
     * @param expression  текст динамически вычисляемого выражения.
     * @return  имя созданного в AST-дереве приватного неизменяемого статического поля в классе ссылающегося на описание данного динамического выражения.<br/>
     *      Для вышеприведенного примера это будет строка <code>"expr1"</code>. 
     */
    public static String defineExpression(final ASTClassDecl classNode, final Class expressionClass, final String expression) {
        final ASTObjectCreationExpr exprNode = new ASTObjectCreationExpr( new RefType(expressionClass) );
        exprNode.addArgument( new ASTStringLiteralExpr(expression) );
        final String fieldName = classNode.findUnusedFieldName("EXPR");
        final RefType type = new RefType(ValueExpression.class);
        final ASTVariableDecl varDecl = new ASTVariableDecl(fieldName, exprNode);
        classNode.addMember( new ASTFieldDecl(Mods.PRIVATE|Mods.STATIC|Mods.FINAL, type, varDecl) );
        return classNode.getName()+'.'+ fieldName;
    }

}
