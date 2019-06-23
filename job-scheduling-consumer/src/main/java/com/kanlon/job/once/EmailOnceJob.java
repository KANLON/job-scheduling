package com.kanlon.job.once;

import com.kanlon.common.MailUtils;
import com.kanlon.response.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 发送邮件的定时任务
 *
 * @author zhangcanlong
 * @since 2019/4/22 18:27
 **/
@Component
public class EmailOnceJob {

    private Logger logger = LoggerFactory.getLogger(EmailOnceJob.class);

    @Autowired
    private MailUtils mailUtils;

    /**
     * 执行发送邮箱任务
     *
     * @param to     参数1，收件人
     * @param param2 参数2 主题和内容
     * @return com.kanlon.model.CommonResponse
     **/
    public CommonResponse executeInternal(String to, String param2) {
        String str = "#";
        if (!param2.contains(str) || param2.split(str).length != 2) {
            return CommonResponse.failedResult("参数二不含‘#’或者包含太多个‘#’，需要重新编写内容");
        }
        try {
            String[] invokeParam2 = param2.split(str);
            //以#为分割，第一个#之前的内容为subject,即标题
            String subject = invokeParam2[0];
            String content = invokeParam2[1];
            mailUtils.sendHtmlMail(to, subject, content);
        } catch (Exception e) {
            logger.error("定时发送邮件错误！", e);
            return CommonResponse.failedResult(e.getLocalizedMessage());
        }
        return CommonResponse.succeedResult();
    }
}
