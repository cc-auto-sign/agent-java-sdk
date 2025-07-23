/**
 * 代理客户端SDK包，提供与远程代理服务进行通信的API。
 * <p>
 * 此包包含用于执行以下操作的类：
 * <ul>
 *   <li>与远程代理服务建立连接</li>
 *   <li>获取系统信息</li>
 *   <li>执行健康检查</li>
 *   <li>执行各种任务</li>
 * </ul>
 * <p>
 * 主要入口点是{@link net.kegui.sdk.AgentClient}类，它提供了与远程服务交互的所有方法。
 * <p>
 * 示例用法：
 * <pre>
 * AgentClient client = new AgentClient("http://example.com", "your-secure-key");
 * SystemInfoResponse info = client.getSystemInfo();
 * </pre>
 */
package net.kegui.sdk;
