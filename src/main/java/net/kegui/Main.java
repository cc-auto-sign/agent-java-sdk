package net.kegui;

//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
import net.kegui.sdk.AgentClient;
import net.kegui.sdk.AgentException;
import net.kegui.sdk.SystemInfoResponse;
import net.kegui.sdk.TaskType;

/**
 * SDK使用示例
 */
public class Main {
    public static void main(String[] args) {
        String serverUrl = "http://localhost:8080";
        String secureKey = "a87a8b83643834c059f083a652f2e94465e3f94f1601b0e9449043d06c32171a";

        // 创建客户端
        AgentClient client = new AgentClient(serverUrl, secureKey);

        try {
            // 执行健康检查
            boolean isHealthy = client.checkHealth();
            System.out.println("服务健康状态: " + (isHealthy ? "正常" : "异常"));

            // 获取系统信息
            SystemInfoResponse sysInfo = client.getSystemInfo();
            System.out.println("\n系统信息:");
            System.out.println("总内存: " + sysInfo.getTotalMemoryMb() + "MB");
            System.out.println("已用内存: " + sysInfo.getUsedMemoryMb() + "MB");
            System.out.println("可用内存: " + sysInfo.getFreeMemoryMb() + "MB");
            System.out.println("内存使用率: " + sysInfo.getMemoryUsagePerc() + "%");
            System.out.println("CPU使用率: " + sysInfo.getCpuUsagePerc() + "%");

            // 执行任务
            System.out.println("\n执行任务:");
            String result = client.executeTask(TaskType.CURL.getValue(), "curl -s https://www.baidu.com");
            System.out.println("任务执行结果: " + result);

        } catch (AgentException e) {
            System.err.println("操作失败: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        }
    }
}