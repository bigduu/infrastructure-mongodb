package com.bigduu.infrastructuremongodb.utils;

import com.bigduu.infrastructuremongodb.baseadvice.GlobalResponseBody;
import com.bigduu.infrastructuremongodb.baseadvice.HttpCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtils {

    public static void writeMsgToResponse(HttpServletResponse response, String msg) throws IOException {
        writeMsgToResponse(response, null, msg);
    }

    public static void writeMsgToResponse(HttpServletResponse response, Object data, String msg) throws IOException {
        writeMsgToResponse(response, false, data, HttpCode.FAIL, msg);
    }

    public static void writeMsgToResponse(HttpServletResponse response, Boolean isSuccess, Object data, HttpCode httpCode, String msg) throws IOException {
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTION");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,X-CAF-Authorization-Token,sessionToken,X-TOKEN");
        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        GlobalResponseBody objectGlobalResponseBody = new GlobalResponseBody(isSuccess, data, httpCode, msg);
        String body = objectMapper.writeValueAsString(objectGlobalResponseBody);
        printWriter.write(body);
        printWriter.flush();
    }

}
