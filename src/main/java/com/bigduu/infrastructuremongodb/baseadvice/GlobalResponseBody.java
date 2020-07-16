package com.bigduu.infrastructuremongodb.baseadvice;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class GlobalResponseBody {

    private Boolean success;
    private Object data;
    private HttpCode httpCode;
    private String errorMsg;

    public static GlobalResponseBody success() {
        return new GlobalResponseBody(true, "", HttpCode.SUCCESS, "");
    }

    public static GlobalResponseBody success(Object data) {
        return new GlobalResponseBody(true, data, HttpCode.SUCCESS, "");
    }

    public static GlobalResponseBody fail(HttpCode httpCode, String errorMsg) {
        return new GlobalResponseBody(false, "", httpCode, errorMsg);
    }

    public static GlobalResponseBody fail(Object data, HttpCode httpCode, String errorMsg) {
        return new GlobalResponseBody(false, data, httpCode, errorMsg);
    }
}
