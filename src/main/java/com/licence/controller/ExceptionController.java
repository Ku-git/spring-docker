package com.licence.controller;

import com.licence.model.util.ResponseWrapper;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    /**
     * 在沒有後備方案時，所使用的回傳方式
     */
    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<ResponseWrapper> handleCallNotPermittedException(CallNotPermittedException ex) {
        // 返回一個自定義的錯誤響應
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(HttpStatus.SERVICE_UNAVAILABLE.value());
        responseWrapper.setMessage(ex.getMessage());

        return ResponseEntity.ok(responseWrapper);
    }

}
