package net.kegui.sdk.examples;

import net.kegui.sdk.AgentClient;
import net.kegui.sdk.AgentException;
import net.kegui.sdk.TaskType;

/**
 * CURL命令执行示例
 */
public class CurlExample {

    public static void main(String[] args) {
        // 创建客户端（替换为实际地址和密钥）
        AgentClient client = new AgentClient("http://localhost:8080", "your-secure-key");

        try {
            // 简单的GET请求
            String getResult = client.executeTask(
                    TaskType.CURL.getValue(),
                    "curl -s https://httpbin.org/get"
            );
            System.out.println("GET请求结果:\n" + getResult);

            // 带参数的POST请求
            String postResult = client.executeTask(
                    TaskType.CURL.getValue(),
                    "curl -s -X POST -H 'Content-Type: application/json' -d '{\"name\":\"test\"}' https://httpbin.org/post"
            );
            System.out.println("\nPOST请求结果:\n" + postResult);

            // 忽略SSL验证的请求
            String insecureResult = client.executeTask(
                    TaskType.CURL.getValue(),
                    "curl -s -k https://self-signed.badssl.com/"
            );
            System.out.println("\n忽略SSL验证请求结果长度: " + insecureResult.length());

        } catch (AgentException e) {
            System.err.println("执行CURL命令失败: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        }
    }
}
