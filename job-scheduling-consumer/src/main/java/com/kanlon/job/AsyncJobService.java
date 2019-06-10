package com.kanlon.job;

import com.kanlon.common.ConstantUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

/**
 * 异步执行工作的辅助类
 *
 * @author zhangcanlong
 * @since 2019/6/10 21:31
 **/
@Service
public class AsyncJobService {

    /**
     * 异步运行命令
     *
     * @param shStr 命令
     * @return 运行结果
     **/
    @Async
    public Integer[] runShell(String shStr, StringBuffer result, CountDownLatch latch, Integer[] results) throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        Process process;
        if (os.startsWith(ConstantUtils.OS_NAME)) {
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
