package com.kenProject.aclservice.mapper;

import com.kenProject.aclservice.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 权限 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2021-08-24
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    List<String> selectPermissionValueByUserId(String id);
    List<String> selectAllPermissionValue();
    List<Permission> selectPermissionByUserId(String userId);
}
