package com.kanlon.model;

import java.util.Date;

/**
 * 服务提供者信息
 *
 * @author zhangcanlong
 * @since 2019/6/10 11:58
 **/
public class ProviderServiceInfoModel {
    /**
     * 主键
     */
    private Long id;

    /**
     * 服务提供者在eruka上注册的应用名
     */
    private String providerName;

    /**
     * 服务的ip地址，选填,可以多个
     */
    private String ipAddress;

    /**
     * 创建时间
     */
    private Date ctime;

    /**
     * 修改时间
     */
    private Date mtime;

    /**
     * 是否有效,标记删除
     */
    private Integer dr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getMtime() {
        return mtime;
    }

    public void setMtime(Date mtime) {
        this.mtime = mtime;
    }

    public Integer getDr() {
        return dr;
    }

    public void setDr(Integer dr) {
        this.dr = dr;
    }
}
