package com.example.fivedata.ex;

import com.example.fivedata.common.ResponseResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseResult<String> exceptionHandler(CustomException ex){
        return new ResponseResult(200,ex.getMessage());
    }

}

