# 自动签到agent的JAVA SDK

这是一个Java SDK，用于调用远程agent服务的HTTP API。

## 功能特性

- 系统信息查询：获取远程系统的内存和CPU使用情况
- 健康检查：检查远程服务是否正常运行
- 任务执行：向远程服务发送执行任务的命令

## 快速开始

### Maven依赖
> (还在往maven上传,现在请自行打jar包)
```xml
<dependency>
    <groupId>net.kegui</groupId>
    <artifactId>cc-auto-sign-agent-sdk</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 使用示例

```java
import net.kegui.sdk.AgentClient;
import net.kegui.sdk.AgentException;
import net.kegui.sdk.SystemInfoResponse;

public class Example {
    public static void main(String[] args) {
        // 创建客户端
        AgentClient client = new AgentClient("http://your-server-url", "your-secure-key");

        try {
            // 健康检查
            boolean isHealthy = client.checkHealth();
            System.out.println("服务健康状态: " + isHealthy);

            // 获取系统信息
            SystemInfoResponse sysInfo = client.getSystemInfo();
            System.out.println("总内存: " + sysInfo.getTotalMemoryMb() + "MB");
            System.out.println("已用内存: " + sysInfo.getUsedMemoryMb() + "MB");
            System.out.println("内存使用率: " + sysInfo.getMemoryUsagePerc() + "%");
            System.out.println("CPU使用率: " + sysInfo.getCpuUsagePerc() + "%");

            // 执行任务
            String result = client.executeTask(1, "your-command");
            System.out.println("任务执行结果: " + result);

        } catch (AgentException e) {
            e.printStackTrace();
        }
    }
}
```

## 配置选项

创建客户端时，您可以指定自定义的HTTP客户端配置：

```java
// 自定义HTTP客户端
OkHttpClient customClient = new OkHttpClient.Builder()
    .connectTimeout(5, TimeUnit.SECONDS)
    .readTimeout(10, TimeUnit.SECONDS)
    .writeTimeout(10, TimeUnit.SECONDS)
    .build();

AgentClient client = new AgentClient("http://your-server-url", "your-secure-key", customClient);
```

## API文档

### AgentClient

主要类，用于与远程服务交互。

#### 构造函数

- `AgentClient(String baseUrl, String secureKey)` - 使用默认HTTP配置创建客户端
- `AgentClient(String baseUrl, String secureKey, OkHttpClient httpClient)` - 使用自定义HTTP客户端创建

#### 方法

- `SystemInfoResponse getSystemInfo()` - 获取系统信息
- `boolean checkHealth()` - 执行健康检查
- `String executeTask(int type, String command)` - 执行任务

### SystemInfoResponse

系统信息响应的数据模型。

#### 方法

- `int getTotalMemoryMb()` - 获取总内存（MB）
- `int getUsedMemoryMb()` - 获取已用内存（MB）
- `double getMemoryUsagePerc()` - 获取内存使用百分比
- `double getCpuUsagePerc()` - 获取CPU使用百分比
- `int getFreeMemoryMb()` - 获取可用内存（MB）

### TaskType

任务类型枚举。

- `TaskType.CURL` - 执行curl命令，安全解析并执行HTTP请求（支持忽略SSL验证）
- `TaskType.NODEJS` - Node.js命令执行（尚未实现）
- `TaskType.PYTHON` - Python命令执行（尚未实现）

## 错误处理

SDK使用`AgentException`处理所有错误，包括网络错误、API错误和验证错误。

```java
try {
    client.executeTask(1, "command");
} catch (AgentException e) {
    System.err.println("错误: " + e.getMessage());
    // 获取原始异常（如果有）
    if (e.getCause() != null) {
        e.getCause().printStackTrace();
    }
}
```

## 开发要求

- Java 17 或更高版本
- Maven 3.6 或更高版本

## 许可证

[MIT License](LICENSE)
