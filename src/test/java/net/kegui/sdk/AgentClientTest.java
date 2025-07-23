package net.kegui.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AgentClientTest {

    private OkHttpClient mockClient;
    private Call mockCall;
    private Response mockResponse;
    private ResponseBody mockResponseBody;
    private AgentClient client;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws IOException {
        // 设置模拟对象
        mockClient = mock(OkHttpClient.class);
        mockCall = mock(Call.class);
        mockResponse = mock(Response.class);
        mockResponseBody = mock(ResponseBody.class);
        objectMapper = new ObjectMapper();

        // 配置模拟行为
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);

        // 创建客户端实例
        client = new AgentClient("http://localhost:8080", "test-secure-key", mockClient);
    }

    @Test
    void getSystemInfo_shouldReturnSystemInfo() throws Exception {
        // 准备模拟响应
        String responseJson = "{\"success\": true, \"data\": {\"total_memory_mb\": 32601, \"used_memory_mb\": 25389, \"memory_usage_perc\": 77.88, \"cpu_usage_perc\": 13.86}}";
        when(mockResponseBody.string()).thenReturn(responseJson);

        // 执行测试
        SystemInfoResponse response = client.getSystemInfo();

        // 验证请求
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        verify(mockClient).newCall(requestCaptor.capture());
        Request request = requestCaptor.getValue();

        assertEquals("http://localhost:8080/api/system/info", request.url().toString());
        assertEquals("test-secure-key", request.header("X-Secure-Key"));

        // 验证响应解析
        assertEquals(32601, response.getTotalMemoryMb());
        assertEquals(25389, response.getUsedMemoryMb());
        assertEquals(77.88, response.getMemoryUsagePerc());
        assertEquals(13.86, response.getCpuUsagePerc());
    }

    @Test
    void checkHealth_shouldReturnTrue_whenSuccessful() throws Exception {
        // 准备模拟响应
        String responseJson = "{\"success\": true, \"message\": \"done\"}";
        when(mockResponseBody.string()).thenReturn(responseJson);

        // 执行测试
        boolean result = client.checkHealth();

        // 验证
        assertTrue(result);
        verify(mockClient).newCall(any(Request.class));
    }

    @Test
    void executeTask_shouldReturnTaskResult() throws Exception {
        // 准备模拟响应
        String responseJson = "{\"success\": true, \"data\": \"Task executed successfully\"}";
        when(mockResponseBody.string()).thenReturn(responseJson);

        // 执行测试
        String result = client.executeTask(TaskType.CURL.getValue(), "curl -s https://example.com");

        // 验证请求
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        verify(mockClient).newCall(requestCaptor.capture());
        Request request = requestCaptor.getValue();

        assertEquals("http://localhost:8080/api/task/execute", request.url().toString());
        assertEquals("test-secure-key", request.header("X-Secure-Key"));
        assertEquals("POST", request.method());

        // 验证响应解析
        assertEquals("Task executed successfully", result);
    }

    @Test
    void executeTask_shouldThrowException_whenApiReturnsError() throws Exception {
        // 准备模拟响应
        String responseJson = "{\"success\": false, \"message\": \"Node.js命令执行功能尚未实现\"}";
        when(mockResponseBody.string()).thenReturn(responseJson);

        // 执行测试并验证异常
        AgentException exception = assertThrows(AgentException.class, () -> {
            client.executeTask(TaskType.NODEJS.getValue(), "console.log('hello')");
        });

        assertEquals("Node.js命令执行功能尚未实现", exception.getMessage());
    }

    @Test
    void executeTask_shouldThrowException_whenInvalidTaskType() {
        // 验证异常：任务类型无效
        assertThrows(IllegalArgumentException.class, () -> {
            client.executeTask(0, "test command");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            client.executeTask(4, "test command");
        });
    }

    @Test
    void executeTask_shouldThrowException_whenEmptyCommand() {
        // 验证异常：命令为空
        assertThrows(IllegalArgumentException.class, () -> {
            client.executeTask(TaskType.CURL.getValue(), "");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            client.executeTask(TaskType.CURL.getValue(), null);
        });
    }
}
