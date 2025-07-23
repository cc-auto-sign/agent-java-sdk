package net.kegui.sdk;

/**
 * 系统信息响应数据模型
 */
public class SystemInfoResponse {
    private final int totalMemoryMb;
    private final int usedMemoryMb;
    private final double memoryUsagePerc;
    private final double cpuUsagePerc;

    public SystemInfoResponse(int totalMemoryMb, int usedMemoryMb, double memoryUsagePerc, double cpuUsagePerc) {
        this.totalMemoryMb = totalMemoryMb;
        this.usedMemoryMb = usedMemoryMb;
        this.memoryUsagePerc = memoryUsagePerc;
        this.cpuUsagePerc = cpuUsagePerc;
    }

    /**
     * 获取总内存大小（MB）
     */
    public int getTotalMemoryMb() {
        return totalMemoryMb;
    }

    /**
     * 获取已使用内存大小（MB）
     */
    public int getUsedMemoryMb() {
        return usedMemoryMb;
    }

    /**
     * 获取内存使用百分比
     */
    public double getMemoryUsagePerc() {
        return memoryUsagePerc;
    }

    /**
     * 获取CPU使用百分比
     */
    public double getCpuUsagePerc() {
        return cpuUsagePerc;
    }

    /**
     * 获取可用内存大小（MB）
     */
    public int getFreeMemoryMb() {
        return totalMemoryMb - usedMemoryMb;
    }

    @Override
    public String toString() {
        return "SystemInfoResponse{" +
                "totalMemoryMb=" + totalMemoryMb +
                ", usedMemoryMb=" + usedMemoryMb +
                ", memoryUsagePerc=" + memoryUsagePerc +
                ", cpuUsagePerc=" + cpuUsagePerc +
                '}';
    }
}
