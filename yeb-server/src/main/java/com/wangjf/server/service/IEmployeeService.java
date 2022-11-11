package com.wangjf.server.service;

import com.wangjf.server.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wangjf.server.pojo.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author joker
 * @since 2022-01-02
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 查询员工
     * @param id
     * @return
     */
    List<Employee> getEmployee (Integer id);

    /**
     * 添加员工
     * @param employee
     * @return
     */
    RespBean addEmp(Employee employee);
}
