package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class ApiResponse<T> {
    @JsonProperty("data")
    private T data;

    @JsonProperty("meta")
    private Map<String, Object> meta;

    @JsonProperty("errors")
    private Object errors;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.data = data;
        return response;
    }

    public static <T> ApiResponse<T> success(T data, Map<String, Object> meta) {
        ApiResponse<T> response = new ApiResponse<>();
        response.data = data;
        response.meta = meta;
        return response;
    }

    public static <T> ApiResponse<T> error(String message) {
        return error(message, 400);
    }

    public static <T> ApiResponse<T> error(String message, int code) {
        ApiResponse<T> response = new ApiResponse<>();
        response.errors = Map.of("message", message, "code", code);
        return response;
    }

    // Getter和Setter方法
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public Map<String, Object> getMeta() { return meta; }
    public void setMeta(Map<String, Object> meta) { this.meta = meta; }

    public Object getErrors() { return errors; }
    public void setErrors(Object errors) { this.errors = errors; }
}
