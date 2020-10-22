package model;

import java.util.ArrayList;

public class BaseResponse {
    private final String status;
    private final Integer code;
    private final ArrayList<String> content;

    public BaseResponse(String status, Integer code, ArrayList<String> content) {
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

    public ArrayList<String> getContent() {
        return content;
    }
}
