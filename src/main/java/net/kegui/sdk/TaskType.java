package net.kegui.sdk;

/**
 * 任务类型枚举
 */
public enum TaskType {
    /**
     * 执行curl命令，安全解析并执行HTTP请求（支持忽略SSL验证）
     */
    CURL(1),

    /**
     * Node.js命令执行（尚未实现）
     */
    NODEJS(2),

    /**
     * Python命令执行（尚未实现）
     */
    PYTHON(3);

    private final int value;

    TaskType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TaskType fromValue(int value) {
        for (TaskType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知任务类型: " + value);
    }
}
