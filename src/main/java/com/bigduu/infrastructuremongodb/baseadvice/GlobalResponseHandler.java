package com.bigduu.infrastructuremongodb.baseadvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@RestController
@RestControllerAdvice(annotations = RestController.class)
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    static final String globalResponseBodyBuilder = "com.csjx.qszs.infrastructure.advice.GlobalResponseBody";
    static final String responseEntityBuilder = "org.springframework.http.ResponseEntity";

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        final String name = returnType.getParameterType().getName();
        return !globalResponseBodyBuilder.equals(name) && !responseEntityBuilder.equals(name);
    }

    @Override
    @ResponseBody
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        final String name = returnType.getParameterType().getName();
        String stringBuilder = "void";
        if (stringBuilder.equals(name)) {
            return GlobalResponseBody.success();
        }
        //如果返回体里面不是json格式数据 则直接返回body内容
        if (!selectedContentType.includes(MediaType.APPLICATION_JSON) && !name.equalsIgnoreCase("java.lang.string")) {
            return body;
        }
        return GlobalResponseBody.success(body);
    }
}
