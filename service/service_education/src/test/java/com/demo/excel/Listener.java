package com.demo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

public class Listener extends AnalysisEventListener<StudentData> {

    //一行一行读取excel内容
    @Override
    public void invoke(StudentData studentData, AnalysisContext analysisContext) {
        System.out.println("*****"+studentData);
    }

    //用于读取表头的方法
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头："+headMap);
    }

    //读取完成之后的方法
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
