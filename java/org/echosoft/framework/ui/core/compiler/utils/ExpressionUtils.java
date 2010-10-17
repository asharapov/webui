package org.echosoft.framework.ui.core.compiler.utils;

import java.math.BigDecimal;
import java.util.Date;

import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.compiler.ast.Mods;
import org.echosoft.framework.ui.core.compiler.ast.Variable;
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
import org.echosoft.framework.ui.core.compiler.ast.stmt.ASTBlockStmt;
import org.echosoft.framework.ui.core.compiler.binding.CompoundExpression;
import org.echosoft.framework.ui.core.compiler.binding.SimpleExpression;
import org.echosoft.framework.ui.core.compiler.binding.ValueExpression;

/**
 * Содержит вспомогательные методы для облегчения обработки динамически вычисляемых выражений.
 *
 * @author Anton Sharapov
 */
public final class ExpressionUtils {

    public static void invoke(final ASTBlockStmt container, final Variable bean, final String methodName, final ASTExpression... exprs) {
        container.addExpressionStmt( new ASTMethodCallExpr(new ASTNameExpr(bean), methodName, exprs) );
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
     * @param classNode  описание класса, в котором происходит генерация данного выражения.
     *          Используется для генерации приватного статического поля с определением вычисляемого выражения (если это потребуется).
     * @param ctx   информация о переменной со ссылкой на контекст компонента для которого будет происходить вычисление выражения.
     * @param text  текст который может оказаться как статическим так и динамическим выражением.
     * @return AST-выражение соответствующее указанной в аргументе метода строке. Результатом вычисления данного выражения может быть объект произвольного класса. 
     */
    public static ASTExpression makeExpression(final ASTClassDecl classNode, final Variable ctx, final String text) {
        final Class<? extends ValueExpression> exprClass = getExpressionClass(text);
        if (exprClass==null) {
            return new ASTStringLiteralExpr(text);
        } else {
            final String fieldName = defineExpression(classNode, exprClass, text);
            return new ASTMethodCallExpr(new ASTNameExpr(fieldName), "evaluate", new ASTNameExpr(ctx));
        }
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
     * @param classNode  описание класса, в котором происходит генерация данного выражения.
     *          Используется для генерации приватного статического поля с определением вычисляемого выражения (если это потребуется).
     * @param ctx   информация о переменной со ссылкой на контекст компонента для которого будет происходить вычисление выражения.
     * @param text  текст который может оказаться как статическим так и динамическим выражением.
     * @return AST-выражение соответствующее указанной в аргументе метода строке. Результатом вычисления данного выражения может быть только строка. 
     */
    public static ASTExpression makeStringExpression(final ASTClassDecl classNode, final Variable ctx, final String text) {
        final Class<? extends ValueExpression> exprClass = getExpressionClass(text);
        if (exprClass==null) {
            return new ASTStringLiteralExpr(text);
        } else {
            final String fieldName = defineExpression(classNode, exprClass, text);
            final ASTMethodCallExpr mce = new ASTMethodCallExpr(new ASTNameExpr(fieldName), "evaluate", new ASTNameExpr(ctx));
            return new ASTMethodCallExpr(StringUtil.class, "valueOf", mce);
        }
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
     * @param classNode  описание класса, в котором происходит генерация данного выражения.
     *          Используется для генерации приватного статического поля с определением вычисляемого выражения (если это потребуется).
     * @param ctx   информация о переменной со ссылкой на контекст компонента для которого будет происходить вычисление выражения.
     * @param text  текст который может оказаться как статическим так и динамическим выражением.
     * @return AST-выражение соответствующее указанной в аргументе метода строке. Результатом вычисления данного выражения может быть только строка.
     */
    public static ASTExpression makeBooleanExpression(final ASTClassDecl classNode, final Variable ctx, final String text) {
        if (text==null || text.isEmpty())
            return new ASTNullLiteralExpr();
        final Class<? extends ValueExpression> exprClass = getExpressionClass(text);
        if (exprClass==null) {
            return new ASTBooleanLiteralExpr(text);
        } else {
            final String fieldName = defineExpression(classNode, exprClass, text);
            return new ASTCastExpr(Boolean.class, new ASTMethodCallExpr(new ASTNameExpr(fieldName), "evaluate", new ASTNameExpr(ctx)));
        }
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
     * @param classNode  описание класса, в котором происходит генерация данного выражения.
     *          Используется для генерации приватного статического поля с определением вычисляемого выражения (если это потребуется).
     * @param ctx   информация о переменной со ссылкой на контекст компонента для которого будет происходить вычисление выражения.
     * @param text  текст который может оказаться как статическим так и динамическим выражением.
     * @return AST-выражение соответствующее указанной в аргументе метода строке. Результатом вычисления данного выражения может быть только объект класса Integer либо производного от него класса. 
     */
    public static ASTExpression makeIntegerExpression(final ASTClassDecl classNode, final Variable ctx, final String text) {
        if (text==null || text.isEmpty())
            return new ASTNullLiteralExpr();
        final Class<? extends ValueExpression> exprClass = getExpressionClass(text);
        if (exprClass==null) {
            return new ASTIntegerLiteralExpr(text);
        } else {
            final String fieldName = defineExpression(classNode, exprClass, text);
            return new ASTCastExpr(Integer.class, new ASTMethodCallExpr(new ASTNameExpr(fieldName), "evaluate", new ASTNameExpr(ctx)));
        }
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
     * @param classNode  описание класса, в котором происходит генерация данного выражения.
     *          Используется для генерации приватного статического поля с определением вычисляемого выражения (если это потребуется).
     * @param ctx   информация о переменной со ссылкой на контекст компонента для которого будет происходить вычисление выражения.
     * @param text  текст который может оказаться как статическим так и динамическим выражением.
     * @return AST-выражение соответствующее указанной в аргументе метода строке. Результатом вычисления данного выражения может быть только объект класса Long либо производного от него класса.
     */
    public static ASTExpression makeLongExpression(final ASTClassDecl classNode, final Variable ctx, final String text) {
        if (text==null || text.isEmpty())
            return new ASTNullLiteralExpr();
        final Class<? extends ValueExpression> exprClass = getExpressionClass(text);
        if (exprClass==null) {
            return new ASTLongLiteralExpr(text);
        } else {
            final String fieldName = defineExpression(classNode, exprClass, text);
            return new ASTCastExpr(Long.class, new ASTMethodCallExpr(new ASTNameExpr(fieldName), "evaluate", new ASTNameExpr(ctx)));
        }
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
     * @param classNode  описание класса, в котором происходит генерация данного выражения.
     *          Используется для генерации приватного статического поля с определением вычисляемого выражения (если это потребуется).
     * @param ctx   информация о переменной со ссылкой на контекст компонента для которого будет происходить вычисление выражения.
     * @param text  текст который может оказаться как статическим так и динамическим выражением.
     * @return AST-выражение соответствующее указанной в аргументе метода строке. Результатом вычисления данного выражения может быть только объект класса Double либо производного от него класса.
     */
    public static ASTExpression makeDoubleExpression(final ASTClassDecl classNode, final Variable ctx, final String text) {
        if (text==null || text.isEmpty())
            return new ASTNullLiteralExpr();
        final Class<? extends ValueExpression> exprClass = getExpressionClass(text);
        if (exprClass==null) {
            return new ASTDoubleLiteralExpr(text);
        } else {
            final String fieldName = defineExpression(classNode, exprClass, text);
            return new ASTCastExpr(Double.class, new ASTMethodCallExpr(new ASTNameExpr(fieldName), "evaluate", new ASTNameExpr(ctx)));
        }
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
     * @param classNode  описание класса, в котором происходит генерация данного выражения.
     *          Используется для генерации приватного статического поля с определением вычисляемого выражения (если это потребуется).
     * @param ctx   информация о переменной со ссылкой на контекст компонента для которого будет происходить вычисление выражения.
     * @param text  текст который может оказаться как статическим так и динамическим выражением.
     * @return AST-выражение соответствующее указанной в аргументе метода строке. Результатом вычисления данного выражения может быть только объект класса BigDecimal либо производного от него класса.
     */
    public static ASTExpression makeBigDecimalExpression(final ASTClassDecl classNode, final Variable ctx, final String text) {
        if (text==null || text.isEmpty())
            return new ASTNullLiteralExpr();
        final Class<? extends ValueExpression> exprClass = getExpressionClass(text);
        if (exprClass==null) {
                return new ASTObjectCreationExpr(BigDecimal.class, new ASTDoubleLiteralExpr(text));
        } else {
            final String fieldName = defineExpression(classNode, exprClass, text);
            return new ASTCastExpr(BigDecimal.class, new ASTMethodCallExpr(new ASTNameExpr(fieldName), "evaluate", new ASTNameExpr(ctx)));
        }
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
     * @param classNode  описание класса, в котором происходит генерация данного выражения.
     *          Используется для генерации приватного статического поля с определением вычисляемого выражения (если это потребуется).
     * @param ctx   информация о переменной со ссылкой на контекст компонента для которого будет происходить вычисление выражения.
     * @param text  текст который может оказаться как статическим так и динамическим выражением.
     * @return AST-выражение соответствующее указанной в аргументе метода строке. Результатом вычисления данного выражения может быть только объект класса Date либо производного от него класса.
     */
    public static ASTExpression makeDateExpression(final ASTClassDecl classNode, final Variable ctx, final String text) {
        if (text==null || text.isEmpty())
            return new ASTNullLiteralExpr();
        final Class<? extends ValueExpression> exprClass = getExpressionClass(text);
        if (exprClass==null) {
            return new ASTMethodCallExpr(StringUtil.class, "parseDate", new ASTStringLiteralExpr(text));
        } else {
            final String fieldName = defineExpression(classNode, exprClass, text);
            return new ASTCastExpr(Date.class, new ASTMethodCallExpr(new ASTNameExpr(fieldName), "evaluate", new ASTNameExpr(ctx)));
        }
    }




    /**
     * Создает статическое поле в классе с объявлением динамически вычисляемого выражения.<br/>
     * Результатом выполнения данного метода является примерно следующее выражение в коде формируемого .java класса:
     * <pre>
     *  private static final ValueExpression expr1 = new CompoundExpression("<i>Hello, @{request:user.name}!</i>");
     * </pre>
     * @param classNode  Узел AST-дерева, с описанием класса.
     * @param exprClass  класс динамически вычисляемого выражения. Обязательно должен реализовывать интерфейс {@link ValueExpression}.
     * @param expr  текст динамически вычисляемого выражения.
     * @return  имя созданного в AST-дереве приватного неизменяемого статического поля в классе ссылающегося на описание данного динамического выражения.<br/>
     *      Для вышеприведенного примера это будет строка <code>"EXPR1"</code>. 
     */
    private static String defineExpression(final ASTClassDecl classNode, final Class<? extends ValueExpression> exprClass, final String expr) {
        final ASTObjectCreationExpr exprNode = new ASTObjectCreationExpr(exprClass, new ASTStringLiteralExpr(expr));
        final String fieldName = classNode.findUnusedFieldName("EXPR");
        classNode.addMember( new ASTFieldDecl(Mods.PRIVATE|Mods.STATIC|Mods.FINAL, ValueExpression.class, fieldName, exprNode) );
        return classNode.getName()+'.'+ fieldName;
    }

    /**
     * Определяет класс вычисляемого выражения для переданной аргументе строки.
     * @param text  строка, которую требуется проверить на наличие вычисляемых выражений.
     * @return Возвращает ссылку на класс вычисляемого выражения или <code>null</code> если в строке вычисляемых выражений не было обнаружено.
     */
    private static Class<? extends ValueExpression> getExpressionClass(final String text) {
        switch (containsExpression(text)) {
            case 0 : return null;
            case 1 : return SimpleExpression.class;
            case 2 :
            default : return CompoundExpression.class;
        }
    }

}
