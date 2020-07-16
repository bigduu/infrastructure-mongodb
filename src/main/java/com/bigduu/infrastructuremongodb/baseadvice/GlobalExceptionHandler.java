package com.bigduu.infrastructuremongodb.baseadvice;

import com.bigduu.infrastructuremongodb.baseexception.AlertException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author mugeng.du
 */
@Slf4j
@RestController
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 使用ExceptionHandler 捕获Exception 并且交给handleExceptionInternal处理
     *
     * @param e 捕获到的异常
     * @return 返回标准的ResonanceEntity
     */
    @ExceptionHandler({Exception.class})
    @Order(1)
    protected ResponseEntity<Object> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(GlobalResponseBody.fail(HttpCode.FAIL, "系统异常，请联系网站管理员!"), HttpStatus.OK);
    }

    /**
     * 使用ExceptionHandler 捕获AlertException 并且交给handleExceptionInternal处理
     *
     */
    @ExceptionHandler({AlertException.class})
    @Order(0)
    protected ResponseEntity<Object> handleException(AlertException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(GlobalResponseBody.fail(HttpCode.FAIL, e.getMessage()), HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage(), ex);
        if (ex instanceof AlertException) {
            body = GlobalResponseBody.fail(body, HttpCode.FAIL_MSG, ex.getMessage());
        } else if (ex instanceof AuthenticationException) {
            body = GlobalResponseBody.fail(body, HttpCode.FAIL_MSG, ex.getMessage());
        } else if (ex instanceof MethodArgumentNotValidException) {
            body = GlobalResponseBody.fail(body, HttpCode.FAIL_MSG, ex.getMessage());
        } else {
            body = GlobalResponseBody.fail(body, HttpCode.FAIL, ex.getMessage());
        }
        return new ResponseEntity<>(body, HttpStatus.OK);
    }


}
