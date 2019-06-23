package com.kanlon.mapper;

import com.kanlon.model.ProviderInfoModel;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * 服务提供者信息 的mapper
 *
 * @author zhangcanlong
 * @since 2019/6/10 11:56
 **/
@Mapper
public interface ProviderInfoMapper {

    String TABLE_NAME = "tb_provider_service_info";

    /**
     * 查询出所有服务提供者信息集合
     *
     * @return 服务提供者信息集合
     **/
    @Select("select id,provider_name,description,ip_address,ctime from " + TABLE_NAME + " where dr=0")
    List<ProviderInfoModel> selectAllProviderInfo();

    /**
     * 插入一个服务提供者的信息
     *
     * @param model 服务提供者的信息
     * @return java.lang.Integer 插入行数
     **/
    @Insert("insert into " + TABLE_NAME + "(provider_name,description,ip_address,ctime,mtime) values(#{providerName},"
            + "#{description},#{ipAddress},#{ctime},#{mtime})")
    Integer insertOneProviderInfo(ProviderInfoModel model);


    /**
     * 更新一个服务提供者的信息
     *
     * @param model 要更新的提供服务的信息
     * @return java.lang.Integer
     **/
    @UpdateProvider(type = ProviderInfoSqlProvider.class, method = "updateByConditionSql")
    Integer updateOneProviderInfo(ProviderInfoModel model);

    /**
     * 删除一个服务提供者的信息
     *
     * @param id 要删除的id
     * @return java.lang.Integer
     **/
    @Update("update " + TABLE_NAME + " set dr=1 where id=#{id}")
    Integer deletedOneProviderInfo(long id);


    /**
     * 动态sql的提供类
     *
     * @author zhangcanlong
     **/
    class ProviderInfoSqlProvider {

        /**
         * 根据非空条件更新信息
         *
         * @param model 实例条件
         * @return java.lang.String
         **/
        public String updateByConditionSql(ProviderInfoModel model) {
            SQL sql = new SQL();
            sql.UPDATE(TABLE_NAME);
            if (model.getProviderName() != null) {
                sql.SET("provider_name=#{providerName}");
            }
            if (model.getDescription() != null) {
                sql.SET("description=#{description}");
            }
            if (model.getIpAddress() != null) {
                sql.SET("ip_address=#{ipAddress}");
            }
            sql.SET("mtime=#{mtime}");
            sql.WHERE("id=#{id}");
            return sql.toString();
        }
    }
}
