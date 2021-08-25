package com.kenProject.eduService.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kenProject.eduService.entity.EduSubject;
import com.kenProject.eduService.entity.SubjectData.SubjectData;
import com.kenProject.eduService.entity.subject.OneSubject;
import com.kenProject.eduService.entity.subject.TwoSubject;
import com.kenProject.eduService.listener.SubjectListener;
import com.kenProject.eduService.mapper.EduSubjectMapper;
import com.kenProject.eduService.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-08-04
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

//    @Autowired
//    SubjectListener subjectListener;

    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService subjectService) {
        try{
            //文件输入流
            InputStream is = file.getInputStream();
            //调用方法
            EasyExcel.read(is, SubjectData.class,new SubjectListener(subjectService)).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //1 查询所有的一级分类 pid=0
        QueryWrapper<EduSubject> wrapperOne=new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);


        //2 查询所有的二级分类 pid!=0
        QueryWrapper<EduSubject> wrapperTwo=new QueryWrapper<>();
        wrapperTwo.ne("parent_id","0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);


        //创建list集合，用于存储最终对象封装
        List<OneSubject> finalSubjectList = new ArrayList<>();


        //3 封装一级分类
        //查询出来的所有的一级分类list集合遍历，得到每个一级分类对象，获取每个一级分类对象值
        //封装到要求的list集合里面List<OneSubject> finalSubject
        for (int i = 0; i < oneSubjectList.size() ; i++) {
            //遍历得到oneSubjectList每个eduSubject
            EduSubject eduSubject = oneSubjectList.get(i);

            //把eduSubject里面的值获取出来，放到OneSubject中
            //多个OneSubject放到finalSubject里面
            OneSubject oneSubject=new OneSubject();
//            oneSubject.setId(eduSubject.getId());
//            oneSubject.setTitle(eduSubject.getTitle());

            //eduSubject里面值获取出来，放到oneSubject对象里面
            BeanUtils.copyProperties(eduSubject,oneSubject);
            finalSubjectList.add(oneSubject);

            //在一级分类循环遍历查询所有的二级分类
            //创建list集合封装每个一级分类的二级分类
            List<TwoSubject> twoFinalSubjectList=new ArrayList<>();
//            System.out.println(twoSubjectList.size());
            //遍历二级分类
            for (int j = 0; j < twoSubjectList.size() ; j++) {
                //获取每个二级分类
                EduSubject tSubject= twoSubjectList.get(j);
                //判断二级分类parent_id和一级分类id是否一样
                if (tSubject.getParentId().equals(eduSubject.getId())){
                    //把tSubject的值放到TwoSubject
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(tSubject,twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }
            //把一级下面所有的二级分类放在一级分类中
            oneSubject.setChildren(twoFinalSubjectList);
        }

        //4 封装二级分类
        return finalSubjectList;
    }
}
