package com.kanlon.controller;

import com.kanlon.common.ConstantUtils;
import com.kanlon.model.AppQuartz;
import com.kanlon.response.CommonResponse;
import com.kanlon.model.PageDatasModel;
import com.kanlon.model.PageModel;
import com.kanlon.service.AppQuartzService;
import com.kanlon.service.JobService;
import com.kanlon.service.QuartzResultService;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

/**
 * job的controller
 *
 * @author zhangcanlong
 * @since 2019-04-10
 **/
@RequestMapping("/")
@RestController
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private AppQuartzService appQuartzService;

    @Autowired
    private QuartzResultService quartzResultService;

    private final Logger logger = LoggerFactory.getLogger(JobController.class);

    /**
     * 从自己的数据库表获取所有任务，包含任务id
     *
     * @return com.kanlon.model.CommonResponse
     **/
    @GetMapping("/allJobs")
    public CommonResponse getAllTaskFromMyTable(final PageModel page) {
        final PageDatasModel model = new PageDatasModel(page);
        model.setDatas(appQuartzService.getAllTaskFromMyTable(page));
        logger.info("获取全部任务成功");
        return CommonResponse.succeedResult(model);
    }

    /**
     * 添加一个job
     *
     * @param appQuartz 任务信息
     * @return com.kanlon.model.ReturnMsg
     **/
    @PostMapping(value = "/addJob")
    public CommonResponse addJob(@Valid @RequestBody final AppQuartz appQuartz) throws Exception {
        final Date date = new Date();
        appQuartz.setCtime(date);
        appQuartz.setMtime(date);
        if (ConstantUtils.EMAIL_STR.equals(appQuartz.getJobGroup())) {
            final String flag = "#";
            if (appQuartz.getInvokeParam2() != null) {
                if (!appQuartz.getInvokeParam2().contains(flag)) {
                    return CommonResponse.failedResult("传递的参数2错误，应该包含#符号，第一个#符号之前的为邮件标题");
                }
            }else{
                return CommonResponse.failedResult("参数二不能为null，并且应该包含#符号，第一个#符号之前的为邮件标题");
            }
        }
        appQuartzService.insertAppQuartzSer(appQuartz);
        return CommonResponse.succeedResult();
    }

    /**
     * 更新,修改
     *
     * @param appQuartz 新的任务信息
     * @return com.kanlon.model.ReturnMsg
     **/
    @PostMapping(value = "/updateJob")
    public CommonResponse modifyJob(@Valid @RequestBody final AppQuartz appQuartz) throws SchedulerException,
            ParseException {
        if (appQuartz.getQuartzId() == null) {
            return CommonResponse.failedResult("更新的任务id不能为null", -1);
        }
        appQuartzService.updateAppQuartzSer(appQuartz);
        return CommonResponse.succeedResult();
    }

    /**
     * 删除job
     *
     * @param quartzIds 请求参数id
     * @return com.kanlon.model.ReturnMsg
     **/
    @PostMapping(value = "/deleteJob")
    public CommonResponse deleteJob(@Valid @NotEmpty @RequestBody final Long[] quartzIds) throws SchedulerException {
        for (final Long quartzId : quartzIds) {
            appQuartzService.deleteAppQuartzByIdSer(quartzId);
        }
        logger.info("要删除的任务id为：" + Arrays.toString(quartzIds));
        return CommonResponse.succeedResult();
    }

    /**
     * 根据任务id暂停job
     *
     * @param quartzIds 任务id
     * @return com.kanlon.model.ReturnMsg
     **/
    @PostMapping(value = "/pauseJob")
    public CommonResponse pauseJob(@Valid @NotEmpty @RequestBody final Long[] quartzIds) throws Exception {
        for (final Long quartzId : quartzIds) {
            final AppQuartz appQuartz = appQuartzService.selectAppQuartzByIdSer(quartzId);
            jobService.pauseJob(appQuartz.getJobName(), appQuartz.getJobGroup());
        }
        return CommonResponse.succeedResult();
    }

    /**
     * 根据id恢复job
     *
     * @param quartzIds 任务id
     * @return com.kanlon.model.ReturnMsg
     **/
    @PostMapping(value = "/resumeJob")
    public CommonResponse resumeJob(@Valid @NotEmpty @RequestBody final Long[] quartzIds) throws Exception {
        for (final Long quartzId : quartzIds) {
            final AppQuartz appQuartz = appQuartzService.selectAppQuartzByIdSer(quartzId);
            jobService.resumeJob(appQuartz.getJobName(), appQuartz.getJobGroup());
        }
        return CommonResponse.succeedResult();
    }

    /**
     * 暂停所有
     *
     * @return com.kanlon.model.ReturnMsg
     **/
    @GetMapping(value = "/pauseAll")
    public CommonResponse pauseAllJob() throws Exception {
        jobService.pauseAllJob();
        return CommonResponse.succeedResult();
    }

    /**
     * 恢复所有
     *
     * @return com.kanlon.model.ReturnMsg
     **/
    @RequestMapping(value = "/resumeAll", method = RequestMethod.GET)
    public CommonResponse resumeAllJob() throws Exception {
        jobService.resumeAllJob();
        return CommonResponse.succeedResult();
    }


    /**
     * 获取所有任务
     *
     * @return com.kanlon.model.ReturnMsg
     **/
    @GetMapping("/tasks")
    public CommonResponse getAllTask() throws SchedulerException {
        return jobService.getAllTask();
    }


    /**
     * 根据任务id获取任务执行记录
     *
     * @param quartzId  任务id
     * @param pageModel 分页参数
     * @return com.kanlon.model.CommonResponse
     **/
    @GetMapping("/job/results")
    public CommonResponse getQuartzResultByQuartzId(@RequestParam("quartzId") final Long quartzId,
            @RequestParam(value = "st", required = false, defaultValue = "2000-10-10") final String st,
            @RequestParam(value = "et", required = false, defaultValue = "2042-10-10") final String et,
            @RequestParam(value = "execResult", required = false) final Integer execResult, final PageModel pageModel) {
        //如果不传执行结果，则查询啊所有执行日志结果
        if (execResult == null) {
            return CommonResponse.succeedResult(quartzResultService.getQuartzResultByQuartzId(quartzId, st, et,
                    pageModel));
        } else {
            return CommonResponse.succeedResult(quartzResultService.getQuartzResultByQuartzIdAndResult(quartzId, st,
                    et, execResult, pageModel));
        }

    }


}
