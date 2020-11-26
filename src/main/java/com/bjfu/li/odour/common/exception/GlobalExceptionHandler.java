package com.bjfu.li.odour.common.exception;

import com.bjfu.li.odour.common.pojo.SverResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public SverResponse<RuntimeException> doHandleRuntimeException(RuntimeException e) {
        log.error("error message{}", e.getMessage());
        return SverResponse.createByErrorMessage(e.getMessage());
    }
}
