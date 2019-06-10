package com.kanlon.common;

/**
 * 常用的常量
 *
 * @author zhangcanlong
 * @since 2019/4/15 14:58
 **/
public class ConstantUtils {
    /**
     * 表示http协议的任务
     */
    public final static String HTTP_STR = "http";
    /**
     * 表示执行shell的任务
     */
    public final static String SHELL_STR = "shell";
    /**
     * 表示执行邮箱调度的任务
     */
    public final static String EMAIL_STR = "email";
    /**
     * 任务的参数key
     */
    public final static String INVOKE_PARAM_STR = "invokeParam";
    /**
     * 任务的参数2 key
     */
    public final static String INVOKE_PARAM2_STR = "invokeParam2";
    /**
     * rpc提供者应用名
     */
    public final static String PROVIDER_NAME_STR = "provider_name";
    /**
     * 数据库任务id参数
     */
    public final static String QUARTZ_ID_STR = "quartzId";
    /**
     * 东八区时间
     */
    public final static String TIMEZONE_STR = "GMT+8";
    /**
     * window系统标志
     */
    public final static String OS_NAME = "win";

    /**
     * 本地，执行本地的调度
     **/
    public final static String LOCALHOST_STR = "localhost";
}
