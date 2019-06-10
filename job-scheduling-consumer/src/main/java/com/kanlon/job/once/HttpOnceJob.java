package com.kanlon.job.once;

import com.kanlon.model.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * 执行http协议
 *
 * @author zhangcanlong
 * @since 2019-04-10
 **/
@Component
public class HttpOnceJob {

    private Logger logger = LoggerFactory.getLogger(HttpOnceJob.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 执行http请求
     *
     * @param invokeParam 参数1
     * @return com.kanlon.model.CommonResponse
     **/
    public CommonResponse executeInternal(String invokeParam) {
        try {
            ResponseEntity<CommonResponse> entity;
            try {
                entity = restTemplate.getForEntity(invokeParam, CommonResponse.class);
            } catch (HttpClientErrorException e) {
                //40*，50* 错误
                logger.error(e.getLocalizedMessage(), e);
                return CommonResponse.failedResult(e.getLocalizedMessage());
            }
            if (entity.getStatusCode() != HttpStatus.OK) {
                StringBuffer buffer = new StringBuffer("请求\"" + invokeParam + "\"任务失败！" + "链接错误、请求方法或服务器错误：" + entity);
                logger.error(buffer.toString());
                return CommonResponse.failedResult(buffer.toString());
            }
            CommonResponse commonResponse = entity.getBody();
            //如果返回的数据为null
            if (commonResponse == null) {
                StringBuffer buffer = new StringBuffer("请求\"" + invokeParam + "\"任务失败！" + "链接返回的数据为null");
                logger.error(buffer.toString());
                return CommonResponse.failedResult(buffer.toString());
            }
            //如果数据返回不正确，即执行不正确
            if (commonResponse.getCode() != 1) {
                StringBuffer buffer =
                        new StringBuffer("请求\"" + invokeParam + "\"任务失败！" + "返回的错误信息为：" + commonResponse.getMessage() + "',错误信息数据为:" + commonResponse.getMessageData());
                logger.error(buffer.toString());
                return CommonResponse.failedResult(buffer.toString());
            }
            //如果任务执行成功
            StringBuffer buffer = new StringBuffer("请求\"" + invokeParam + "\"任务成功！" + "返回的信息为：" + commonResponse);
            logger.info(buffer.toString());
        } catch (Exception e) {
            //其他错误情况
            logger.error(e.getLocalizedMessage(), e);
            return CommonResponse.failedResult(e.getLocalizedMessage());
        }
        return CommonResponse.failedResult("未知异常");
    }


}
