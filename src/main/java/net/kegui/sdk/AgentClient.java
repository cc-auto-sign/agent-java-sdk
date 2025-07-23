package net.kegui.sdk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import okhttp3.*;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 代理客户端SDK主类
 */
public class AgentClient {
    private final String baseUrl;
    private final String secureKey;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    private static final String HEADER_SECURE_KEY = "X-Secure-Key";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 创建代理客户端
     * 
     * @param baseUrl 服务器基础URL，例如 "http://localhost:8080"
     * @param secureKey 安全密钥
     */
    public AgentClient(String baseUrl, String secureKey) {
        this(baseUrl, secureKey, new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build());
    }

    /**
     * 创建代理客户端（带自定义HTTP客户端）
     * 
     * @param baseUrl 服务器基础URL
     * @param secureKey 安全密钥
     * @param httpClient 自定义HTTP客户端
     */
    public AgentClient(String baseUrl, String secureKey, OkHttpClient httpClient) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.secureKey = secureKey;
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 获取系统信息
     * 
     * @return 系统信息响应
     * @throws AgentException 如果调用失败
     */
    public SystemInfoResponse getSystemInfo() throws AgentException {
        Request request = new Request.Builder()
                .url(baseUrl + "/api/system/info")
                .header(HEADER_SECURE_KEY, secureKey)
                .get()
                .build();

        try {
            Response response = executeRequest(request);
            JsonNode rootNode = parseResponse(response);

            if (rootNode.get("success").asBoolean()) {
                JsonNode dataNode = rootNode.get("data");
                return new SystemInfoResponse(
                        dataNode.get("total_memory_mb").asInt(),
                        dataNode.get("used_memory_mb").asInt(),
                        dataNode.get("memory_usage_perc").asDouble(),
                        dataNode.get("cpu_usage_perc").asDouble()
                );
            } else {
                throw new AgentException(rootNode.get("message").asText());
            }
        } catch (IOException e) {
            throw new AgentException("获取系统信息失败", e);
        }
    }

    /**
     * 执行健康检查
     * 
     * @return 健康检查是否成功
     * @throws AgentException 如果调用失败
     */
    public boolean checkHealth() throws AgentException {
        Request request = new Request.Builder()
                .url(baseUrl + "/api/health")
                .header(HEADER_SECURE_KEY, secureKey)
                .get()
                .build();

        try {
            Response response = executeRequest(request);
            JsonNode rootNode = parseResponse(response);

            return rootNode.get("success").asBoolean();
        } catch (IOException e) {
            throw new AgentException("健康检查失败", e);
        }
    }

    /**
     * 执行任务
     * 
     * @param type 任务类型：
     *             1 - CURL：执行curl命令，安全解析并执行HTTP请求（支持忽略SSL验证）
     *             2 - NODEJS：Node.js命令执行（尚未实现）
     *             3 - PYTHON：Python命令执行（尚未实现）
     * @param command 任务命令
     * @return 任务执行结果
     * @throws AgentException 如果调用失败
     * @see TaskType 任务类型枚举
     */
    public String executeTask(int type, String command) throws AgentException {
        if (type < 1 || type > 3) {
            throw new IllegalArgumentException("任务类型必须为1、2或3");
        }

        if (command == null || command.trim().isEmpty()) {
            throw new IllegalArgumentException("任务命令不能为空");
        }

        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("type", String.valueOf(type));
        requestBody.put("command", command);

        Request request = new Request.Builder()
                .url(baseUrl + "/api/task/execute")
                .header(HEADER_SECURE_KEY, secureKey)
                .post(RequestBody.create(requestBody.toString(), JSON))
                .build();

        try {
            Response response = executeRequest(request);
            JsonNode rootNode = parseResponse(response);

            if (rootNode.get("success").asBoolean()) {
                return rootNode.get("data").asText();
            } else {
                throw new AgentException(rootNode.get("message").asText());
            }
        } catch (IOException e) {
            throw new AgentException("执行任务失败", e);
        }
    }

    /**
     * 执行HTTP请求
     */
    private Response executeRequest(Request request) throws IOException {
        Response response = httpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("请求失败: " + response.code() + " " + response.message());
        }
        return response;
    }

    /**
     * 解析HTTP响应为JSON
     */
    private JsonNode parseResponse(Response response) throws IOException {
        try (ResponseBody body = response.body()) {
            if (body == null) {
                throw new IOException("响应体为空");
            }
            return objectMapper.readTree(body.string());
        }
    }
}
