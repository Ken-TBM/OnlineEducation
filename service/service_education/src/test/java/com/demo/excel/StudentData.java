package com.demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class StudentData {

    //设置excel表头的名称
    @ExcelProperty(value = "学生学号",index = 0)
    private Integer num;
    @ExcelProperty(value = "学生姓名",index = 1)
    private String name;


}
