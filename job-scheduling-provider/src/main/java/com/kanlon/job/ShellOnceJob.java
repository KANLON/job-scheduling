package com.kanlon.job;

import com.kanlon.common.Constant;
import com.kanlon.model.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 执行shell
 *
 * @author zhangcanlong
 * @since 2019-04-10
 **/
@Component
public class ShellOnceJob {

    private Logger logger = LoggerFactory.getLogger(ShellOnceJob.class);

    /**
     * 执行shell任务
     *
     * @param param1 参数1，shell内容
     * @return com.kanlon.model.CommonResponse
     **/
    public CommonResponse executeInternal(String param1) {
        //执行命令行
        CountDownLatch latch = new CountDownLatch(1);
        Integer[] tempInteger = new Integer[1];
        //执行结果记录
        StringBuffer resultLog = new StringBuffer();
        Integer exitValue = null;
        try {
            runShell(param1, resultLog, latch, tempInteger);
            //如果过了3个小时后，还没执行完，则直接返回
            latch.await(3, TimeUnit.HOURS);
            exitValue = tempInteger[0];
            if (exitValue == null) {
                return CommonResponse.failedResult("过了3小时后，命令还没执行完！");
            }
        } catch (Exception e) {
            logger.error("执行shell任务出错！", e);
            resultLog.append("\n" + e.getMessage());
            return CommonResponse.failedResult(resultLog.toString());
        }

        if (null == exitValue || 0 != exitValue) {
            resultLog.append("command exit value(" + exitValue + ") is failed");
            return CommonResponse.failedResult(resultLog.toString(), exitValue);
        }
        return CommonResponse.succeedResult();
    }

    /**
     * 运行命令
     *
     * @param shStr 命令
     * @return 运行结果
     **/
    @Async
    public Integer[] runShell(String shStr, StringBuffer result, CountDownLatch latch, Integer[] results) throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        Process process;
        if (os.startsWith(Constant.OS_NAME)) {
            process = Runtime.getRuntime().exec("cmd /c " + shStr);
        } else {
            process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", shStr});
        }
        process.waitFor();
        BufferedReader read = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = read.readLine()) != null) {
            result.append(line);
        }
        results[0] = process.exitValue();
        latch.countDown();
        return results;
    }

}
