package com.kanlon.exception;

/**
 * 自定义的任务异常
 *
 * @author zhangcanlong
 * @since 2019/4/15 14:25
 **/
public class QuartzException extends RuntimeException {


    private static final long serialVersionUID = 1356439445687837319L;

    public QuartzException(String message) {
        super(message);
    }
}
