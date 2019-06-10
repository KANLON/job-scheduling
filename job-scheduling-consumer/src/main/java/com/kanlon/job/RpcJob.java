package com.kanlon.job;

import com.kanlon.common.ConstantUtils;
import com.kanlon.job.once.EmailOnceJob;
import com.kanlon.job.once.HttpOnceJob;
import com.kanlon.job.once.ShellOnceJob;
import com.kanlon.model.CommonResponse;
import com.kanlon.model.ConsumerRequest;
import com.kanlon.model.QuartzResultModel;
import com.kanlon.service.GrpcConsumerService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * rpc远程调用的任务
 *
 * @author zhangcanlong
 * @since 2019/5/4 21:39
 **/
@Component
public class RpcJob extends QuartzJobBean {

    Logger logger = LoggerFactory.getLogger(RpcJob.class);


    @Autowired
    private GrpcConsumerService grpcConsumerService;
    @Autowired
    private CommonJobService commonJobService;
    @Autowired
    private ShellOnceJob shellOnceJob;
    @Autowired
    private HttpOnceJob httpOnceJob;
    @Autowired
    private EmailOnceJob emailOnceJob;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        //初始调度结果
        QuartzResultModel model = commonJobService.startSchedule();
        Long oldTime = System.currentTimeMillis();
        //存放执行结果日志
        StringBuffer resultLog = new StringBuffer();
        try {
            logger.info("执行的任务名为：" + context.getTrigger().getKey());
            // 包含传递参数信息的map
            JobDataMap data = context.getTrigger().getJobDataMap();
            model.setQuartzId((Long) data.get(ConstantUtils.QUARTZ_ID_STR));
            String param1 = (String) data.get(ConstantUtils.INVOKE_PARAM_STR);
            String param2 = (String) data.get(ConstantUtils.INVOKE_PARAM2_STR);
            String providerName = (String) data.get(ConstantUtils.PROVIDER_NAME_STR);
            // 调度的类型，http，shell，还是email，由组名决定
            String type = context.getJobDetail().getKey().getGroup();
            CommonResponse commonResponse;
            // 如果是localhost执行本地的调度方法
            if (ConstantUtils.LOCALHOST_STR.equals(providerName)) {
                if (ConstantUtils.SHELL_STR.equals(type)) {
                    commonResponse = shellOnceJob.executeInternal(param1);
                } else if (ConstantUtils.HTTP_STR.equals(type)) {
                    commonResponse = httpOnceJob.executeInternal(param1);
                } else if (ConstantUtils.EMAIL_STR.equals(type)) {
                    commonResponse = emailOnceJob.executeInternal(param1, param2);
                } else {
                    commonResponse = CommonResponse.failedResult("执行任务的类型错误");
                }
            } else {
                ConsumerRequest request = new ConsumerRequest();
                request.setType(type);
                request.setParam1(param1);
                request.setParam2(param2);
                request.setProviderName(providerName);
                //执行远程rpc方法
                commonResponse = grpcConsumerService.callMethod(request);
            }
            //如果返回正确时，才设置正确的结果
            if (commonResponse.getCode() == 1) {
                model.setExecResult(1);
                // 将返回的信息数据加入到结果中
                resultLog.append(commonResponse.getResultData());
            } else {
                //如果失败，则添加结果信息
                resultLog.append(commonResponse.getMessage());
            }
        } catch (Exception e) {
            logger.error("调度任务错误！", e);
            resultLog.append("调度任务错误！").append(e.getLocalizedMessage());
        } finally {
            commonJobService.addResult(oldTime, resultLog.toString(), model, logger);
        }
    }

}
