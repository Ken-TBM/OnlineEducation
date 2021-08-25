package com.kenProject.eduService.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CourseQuery {
    @ApiModelProperty(value = "课程名称，模糊查询")
    private String title;
    @ApiModelProperty(value = "Normal 已发布 ，Draft 未发布")
    private String status;
}
