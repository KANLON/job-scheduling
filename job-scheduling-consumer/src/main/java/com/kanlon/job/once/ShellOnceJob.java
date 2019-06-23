package com.kanlon.job.once;

import com.kanlon.job.AsyncJobService;
import com.kanlon.response.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Autowired
    private AsyncJobService asyncJobService;

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
        Integer exitValue;
        try {
            asyncJobService.runShell(param1, resultLog, latch, tempInteger);
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


}
