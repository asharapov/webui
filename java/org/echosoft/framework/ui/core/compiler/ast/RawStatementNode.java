package org.echosoft.framework.ui.core.compiler.ast;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Anton Sharapov
 */
public class RawStatementNode extends StatementNode {
    private String stmt;

    public RawStatementNode(final String stmt) {
        super();
        this.stmt = stmt;
    }

    public String getStatement() {
        return stmt;
    }
    public RawStatementNode setStatement(final String stmt) {
        this.stmt = stmt;
        return this;
    }

    @Override
    public StatementNode append(final ASTNode node) {
        throw new IllegalStateException("RawStatement node can't have any children");
    }

    @Override
    public void translate(final Writer out) throws IOException {
        if (stmt!=null) {
            indent(out);
            out.write(stmt);
            out.write(";\n");
        }
    }

}
