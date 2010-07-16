package org.echosoft.framework.ui.core.compiler.ast;

/**
 * @author Anton Sharapov
 */
public enum Visibility {
    PUBLIC("public"),
    PROTECTED("protected"),
    DEFAULT(""),
    PRIVATE("private");

    private final String token;
    private Visibility(String token) {
        this.token = token;
    }

    public String toJavaString() {
        return token;
    }
}
