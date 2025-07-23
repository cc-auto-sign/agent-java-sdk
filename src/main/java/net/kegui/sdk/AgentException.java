package net.kegui.sdk;

/**
 * 代理客户端异常
 */
public class AgentException extends Exception {

    public AgentException(String message) {
        super(message);
    }

    public AgentException(String message, Throwable cause) {
        super(message, cause);
    }
}
