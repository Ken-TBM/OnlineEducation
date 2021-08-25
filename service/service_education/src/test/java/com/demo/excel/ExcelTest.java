package com.demo.excel;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ExcelTest {
    List<StudentData> list=new ArrayList<>();

    @Test
    public void createExcelFile(){
        //实现excel操作
        //设置写入文件地址和excel名称
        String fileName="F:\\write.xls";
        //调用easyExcel里面的参数
        EasyExcel.write(fileName,StudentData.class).sheet("学生列表").doWrite(getData(list));
    }

    @Test
    public void read(){
        String fileName="F:\\write.xls";
        EasyExcel.read(fileName,StudentData.class,new Listener()).sheet().doRead();
    }


    private static List<StudentData> getData( List<StudentData> list){
        for (int i=0;i< 10;i++){
            StudentData data=new StudentData();
            data.setName("ken"+i);
            data.setNum(i);
            list.add(data);
        }
        return list;
    }

}
