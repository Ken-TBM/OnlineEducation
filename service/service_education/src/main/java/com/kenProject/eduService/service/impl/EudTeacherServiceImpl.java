package com.kenProject.eduService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenProject.eduService.entity.EudTeacher;
import com.kenProject.eduService.mapper.EudTeacherMapper;
import com.kenProject.eduService.service.EudTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-07-20
 */
@Service
public class EudTeacherServiceImpl extends ServiceImpl<EudTeacherMapper, EudTeacher> implements EudTeacherService {

    @Override
    public Map<String, Object> getTeacherFrontList(Page<EudTeacher> pageTeacher) {
        QueryWrapper<EudTeacher> wrapper=new QueryWrapper<>();
        wrapper.orderByDesc("id");
        //把分页数据封装到pageTeacher对象
        baseMapper.selectPage(pageTeacher,wrapper);

        List<EudTeacher> records = pageTeacher.getRecords();
        long current = pageTeacher.getCurrent();
        long pages = pageTeacher.getPages();
        long size = pageTeacher.getSize();
        long total = pageTeacher.getTotal();
        boolean hasNext = pageTeacher.hasNext();
        boolean hasPrevious = pageTeacher.hasPrevious();

        //把分页数据放到map集合中
        Map<String,Object> map= new HashMap<>();
        map.put("items",records);
        map.put("current",current);
        map.put("pages",pages);
        map.put("size",size);
        map.put("total",total);
        map.put("hasNext",hasNext);
        map.put("hasPrevious",hasPrevious);
        return map;
    }
}
