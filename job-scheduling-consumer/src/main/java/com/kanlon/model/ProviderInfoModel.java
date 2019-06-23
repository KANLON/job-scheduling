package com.kanlon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 服务提供者信息
 *
 * @author zhangcanlong
 * @since 2019/6/10 11:58
 **/
@Data
public class ProviderInfoModel {
    /**
     * 主键
     */
    private Long id;

    /**
     * 服务提供者在eruka上注册的应用名
     */
    @NotNull
    @Size(max = 255)
    @Pattern(regexp = "[a-zA-Z0-9\\-_]+", message = "服务提供者名字只能是英文字符、数字和_-")
    private String providerName;

    /**
     * 服务提供者描述
     **/
    @NotNull
    @Size(max = 255)
    private String description;

    /**
     * 服务的ip地址，选填,可以多个
     */
    private String ipAddress;

    /**
     * 创建时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date ctime = new Date();

    /**
     * 修改时间
     */
    @JsonIgnore
    private Date mtime = new Date();

    /**
     * 是否有效,标记删除
     */
    @JsonIgnore
    private Integer dr = 0;
}
