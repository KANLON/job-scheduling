package com.kanlon.service;

import com.kanlon.mapper.ProviderInfoMapper;
import com.kanlon.model.ProviderInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务提供者信息的操作service
 *
 * @author zhangcanlong
 * @since 2019/6/23 19:22
 **/
@Service
public class ProviderInfoService {
    @Autowired
    private ProviderInfoMapper providerInfoMapper;

    /**
     * 获取所有服务提供者信息
     *
     * @return 服务提供者信息集合
     **/
    public List<ProviderInfoModel> getAllProviderInfo() {
        return providerInfoMapper.selectAllProviderInfo();
    }

    /**
     * 增加一个服务提供者的信息
     *
     * @param model 服务提供者信息
     **/
    public void addOneProviderInfo(ProviderInfoModel model) {
        providerInfoMapper.insertOneProviderInfo(model);
    }

    /**
     * 更新一个服务提供者的信息
     *
     * @param model 服务提供者信息
     **/
    public void updateOneProviderInfo(ProviderInfoModel model) {
        providerInfoMapper.updateOneProviderInfo(model);
    }

    /**
     * 删除某个服务提供者的信息
     *
     * @param id 要删除的id
     **/
    public void deletedOneProviderInfo(Long id) {
        providerInfoMapper.deletedOneProviderInfo(id);
    }
}
