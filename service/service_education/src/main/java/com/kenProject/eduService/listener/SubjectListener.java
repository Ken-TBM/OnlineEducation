package com.kenProject.eduService.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kenProject.Servicebase.exceptionHandler.KenProjectException;
import com.kenProject.eduService.entity.EduSubject;
import com.kenProject.eduService.entity.SubjectData.SubjectData;
import com.kenProject.eduService.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SubjectListener extends AnalysisEventListener<SubjectData> {

    //    @Autowired
    //    private EduSubjectService eduSubjectService;
    //

    //因为SubjectExcelListener不能交给spring进行管理，需要自己new，不能注入其他对象
    //不能用mapper操作数据库
    public EduSubjectService eduSubjectService;
    public SubjectListener(){};
    public SubjectListener(EduSubjectService eduSubjectService){
        this.eduSubjectService=eduSubjectService;
    };

    //一行一行读取excel内容
    @Override
    public void invoke(SubjectData data, AnalysisContext context) {
        if (data==null){
            throw new KenProjectException(20000,"文件数据为空");
        }

        //一行一行读取，每次读取两个值，第一个值一级分类，第二个值二级分类
        //先判断一级分类
        EduSubject oneSubject = this.exitsOneSubject(eduSubjectService, data.getOneSubjectName());
        if (oneSubject==null){//没有相同一级分类，进行添加
            oneSubject= new EduSubject();
            oneSubject.setParentId("0");
            oneSubject.setTitle(data.getOneSubjectName());//一级分类名称
            eduSubjectService.save(oneSubject);
        }


        //获取一级分类的id值
        String pid=oneSubject.getId();
        //添加二级分页
        //判断二级分类是否重复
        EduSubject twoSubject = this.exitsTwoSubject(eduSubjectService, data.getTowSubjectName(), pid);
        if (twoSubject==null){
            twoSubject=new EduSubject();
            twoSubject.setParentId(pid);
            twoSubject.setTitle(data.getTowSubjectName());
            eduSubjectService.save(twoSubject);
        }

    }

    //判断一级分类不能重复添加
    private EduSubject exitsOneSubject(EduSubjectService eduSubjectService,String name){
        QueryWrapper<EduSubject> wrapper=new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",0);
        EduSubject oneSubject=eduSubjectService.getOne(wrapper);
        return oneSubject;
    }


    //二级分类不能重复添加
    private EduSubject exitsTwoSubject(EduSubjectService eduSubjectService,String name,String pid){
        QueryWrapper<EduSubject> wrapper=new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject twoSubject=eduSubjectService.getOne(wrapper);
        return twoSubject;
    }



    //用于读取表头的方法
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {}


    //读取完成之后的方法
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
