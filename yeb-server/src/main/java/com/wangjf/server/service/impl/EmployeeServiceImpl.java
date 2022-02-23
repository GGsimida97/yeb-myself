package com.wangjf.server.service.impl;

import com.wangjf.server.pojo.Employee;
import com.wangjf.server.mapper.EmployeeMapper;
import com.wangjf.server.service.IEmployeeService;
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
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

}
