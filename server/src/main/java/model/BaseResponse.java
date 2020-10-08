package model;

public class BaseResponse {
    private final String status;
    private final Integer code;
    private final String content;

    public BaseResponse(String status, Integer code, String content) {
        this.status = status;
        this.code = code;
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }

    public String getContent() {
        return content;
    }
}
