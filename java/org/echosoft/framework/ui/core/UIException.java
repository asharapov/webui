package org.echosoft.framework.ui.core;

/**
 * Поднимается при возникновении разного рода ошибок касающихся формирования выходных html форм.
 * This class encapsulates general UI framework exceptions.
 * @author Anton Sharapov
 */
public class UIException extends RuntimeException {

    public UIException() {
        super();
    }

    public UIException(String message) {
        super(message);
    }

    public UIException(Throwable cause) {
        super(cause);
    }

    public UIException(String message, Throwable cause) {
        super(message, cause);
    }

}
