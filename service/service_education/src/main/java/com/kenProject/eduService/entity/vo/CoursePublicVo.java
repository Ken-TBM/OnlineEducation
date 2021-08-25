package com.kenProject.eduService.entity.vo;


import lombok.Data;

@Data
public class CoursePublicVo {
    private String id;
    private String title;
    private String cover;
    private Integer lessonNum;
    private String subjectLevelOne;
    private String subjectLevelTwo;
    private String teacherName;
    private String price;//只用来显示
}
