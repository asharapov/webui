package org.echosoft.framework.ui.core.compiler.ast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.echosoft.common.io.FastStringWriter;
import org.echosoft.framework.ui.core.web.wui.Options;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Anton Sharapov
 */
public class IfNodeTest {

    private FileNode root;
    private MethodNode method;

    @Before
    public void setup() throws IOException {
        final Map<String,String> env = new HashMap<String,String>();
        env.put("src-dir", "");
        env.put("dst-dir", "");
        env.put("charset", "UTF-8");
        final Options opts = new Options(env);
        root = new FileNode("", opts);
    }

    @Test
    public void test1() throws IOException {
        final MethodNode mn = root.getServletService();
        mn.append( new IfNode()
            .append( new RawExpressionNode("true") )
            .append( new StatementListNode().noLeadIndent()
                        .append( new RawStatementNode("System.out.println(\"then statement 1\")") )
            )
        );

        mn.append( new IfNode()
            .append( new RawExpressionNode("true") )
            .append( new StatementListNode().noLeadIndent()
                        .append( new RawStatementNode("System.out.println(\"then statement 2\")") )
            )
            .append( new StatementListNode().noLeadIndent()
                        .append( new RawStatementNode("System.out.println(\"else statement 2\")") )
            )
        );

        final FastStringWriter out = new FastStringWriter();
        mn.translate(out);
        System.out.println(out.toString());
    }

    @Test
    public void test2() throws IOException {
        final MethodNode mn = root.getServletService();
        final IfNode in1 =
                new IfNode()
                .append( new RawExpressionNode("true") )
                .append( ((IfNode)new IfNode().noLeadIndent())
                        .setExpressionNode( new RawExpressionNode("1==1") )
                        .setThenNode(
                                new StatementListNode().noLeadIndent()
                                .append( new RawStatementNode("System.out.println(\"ok 2\")") )
                        )
                ).append(
                        new StatementListNode().noLeadIndent()
                                .append( new RawStatementNode("System.out.println(\"cancel\")") )
                );
        mn.append(in1);

        final FastStringWriter out = new FastStringWriter();
        mn.translate(out);
        System.out.println(out.toString());
    }
}
