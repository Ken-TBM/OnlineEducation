package com.kenProject.eduService.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TeacherQuery implements Serializable {

    @ApiModelProperty(value = "教师名称，模糊查询")
    private String name;
    @ApiModelProperty(value = "级别 1 高级讲师 2 中级讲师")
    private Integer level;
    @ApiModelProperty(value = "查询时间",example = "2021-07-21 15:10:52")
    private String begin;
    @ApiModelProperty(value = "查询结束时间",example ="2021-07-21 15:10:52")
    private String end;

}
