package com.kanlon.controller;

import com.kanlon.model.ProviderInfoModel;
import com.kanlon.response.CommonResponse;
import com.kanlon.service.ProviderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 服务提供者信息cotroller
 *
 * @author zhangcanlong
 * @since 2019/6/23 19:28
 **/
@RestController
@RequestMapping("/provider-info")
public class ProviderInfoController {
    @Autowired
    private ProviderInfoService providerInfoService;

    /**
     * 获取所有服务提供者信息
     *
     * @return com.kanlon.response.CommonResponse
     **/
    @GetMapping("/list")
    public CommonResponse getAllProviderInfo() {
        return CommonResponse.succeedResult(providerInfoService.getAllProviderInfo());
    }

    /**
     * 增加一个服务提供者信息
     *
     * @param model 服务提供者信息
     * @return com.kanlon.response.CommonResponse
     **/
    @PostMapping("/add")
    public CommonResponse addOneProviderInfo(@RequestBody @Valid ProviderInfoModel model) {
        providerInfoService.addOneProviderInfo(model);
        return CommonResponse.succeedResult();
    }

    /**
     * 更新一个服务提供者信息
     *
     * @param model 服务提供者信息
     * @return com.kanlon.response.CommonResponse
     **/
    @PostMapping("/update")
    public CommonResponse updateOneProviderInfo(@RequestBody @Valid ProviderInfoModel model) {
        if (model.getId() == null || model.getId() <= 0) {
            return CommonResponse.failedResult("服务提供者的id发送错误！");
        }
        providerInfoService.updateOneProviderInfo(model);
        return CommonResponse.succeedResult();
    }

    /**
     * 删除一个服务提供者信息
     *
     * @param id 要删除的服务提供者的id
     * @return com.kanlon.response.CommonResponse
     **/
    @PostMapping("/deleted")
    public CommonResponse deletedOneProviderInfo(@RequestBody Long id) {
        providerInfoService.deletedOneProviderInfo(id);
        return CommonResponse.succeedResult();
    }
}

