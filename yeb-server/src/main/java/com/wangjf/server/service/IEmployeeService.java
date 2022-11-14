package com.wangjf.server.service;

import com.wangjf.server.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wangjf.server.pojo.RespBean;
import com.wangjf.server.pojo.RespPageBean;

import java.time.LocalDate;
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

    /**
     * 获取所有员工（分页）
     * @param currentPage
     * @param size
     * @param employee
     * @param beginDateScope
     * @return
     */

    RespPageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope);
}
