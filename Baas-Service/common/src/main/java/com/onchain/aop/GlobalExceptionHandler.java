package com.onchain.aop;

import com.alibaba.fastjson.JSONException;
import com.onchain.constants.ReturnCode;
import com.onchain.entities.ResponseFormat;
import com.onchain.exception.CommonException;
import com.onchain.exception.ParameterException;
import com.onchain.exception.SqlGetValueException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseFormat<?> exception(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseFormat<>(ReturnCode.REQUEST_FAILED);
    }

    @ExceptionHandler(SqlGetValueException.class)
    @ResponseBody
    public ResponseFormat<?> sqlException(SqlGetValueException e) {
        log.error(e.getMessage(), e);
        return new ResponseFormat<>(ReturnCode.REQUEST_FAILED);
    }

    @ExceptionHandler({CommonException.class})
    @ResponseBody
    public ResponseFormat<?> commonException(CommonException e) {
        log.error(e.getMessage(), e);
        return new ResponseFormat<>(e.getReturnCode(), e.getMessage());
    }

    @ExceptionHandler(ParameterException.class)
    @ResponseBody
    public ResponseFormat<?> paramException(ParameterException e) {
        log.error(e.getMessage(), e);
        return new ResponseFormat<>(ReturnCode.PARAMETER_FAILED, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseFormat<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        StringBuffer errorMsg = new StringBuffer();
        errors.forEach(x -> errorMsg.append(x.getDefaultMessage()).append(";"));
        log.error(errorMsg.toString(), e);
        return new ResponseFormat<>(ReturnCode.PARAMETER_FAILED, errorMsg.toString());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseFormat<?> constraintViolationExceptionException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        return new ResponseFormat<>(ReturnCode.PARAMETER_FAILED, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseFormat<?> methodArgumentTypeMismatchExceptionException(MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage(), e);
        return new ResponseFormat<>(ReturnCode.PARAMETER_FAILED, e.getName());
    }

    @ExceptionHandler(JSONException.class)
    @ResponseBody
    public ResponseFormat<?> jsonException(JSONException e) {
        log.error(e.getMessage(), e);
        return new ResponseFormat<>(ReturnCode.JSON_FORMAT_ERROR, e.getMessage());
    }

}
