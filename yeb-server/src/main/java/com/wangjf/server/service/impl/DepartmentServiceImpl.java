package com.wangjf.server.service.impl;

import com.wangjf.server.pojo.Department;
import com.wangjf.server.mapper.DepartmentMapper;
import com.wangjf.server.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author joker
 * @since 2022-01-02
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}
