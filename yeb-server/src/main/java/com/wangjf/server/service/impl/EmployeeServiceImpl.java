package com.wangjf.server.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangjf.server.pojo.Employee;
import com.wangjf.server.mapper.EmployeeMapper;
import com.wangjf.server.pojo.RespBean;
import com.wangjf.server.pojo.RespPageBean;
import com.wangjf.server.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author joker
 * @since 2022-01-02
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    /**
     * 查询员工
     * @param id
     * @return
     */
    public List<Employee> getEmployee(Integer id) {
        return employeeMapper.getEmployee(id);
    }

    /**
     * 添加员工
     *
     * @param employee
     * @return
     */
    @Override
    public RespBean addEmp(Employee employee) {
        // 处理合同期限，保留两位小时
        LocalDate beginContract = employee.getBegincontract();
        LocalDate endContract = employee.getEndcontract();
        long days = beginContract.until(endContract, ChronoUnit.DAYS);
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        employee.setContractterm(Double.parseDouble(decimalFormat.format(days / 365)));
        if (1 == employeeMapper.insert(employee)) {
            // 发送邮件
            Employee emp = employeeMapper.getEmployee(employee.getId()).get(0);
            rabbitTemplate.convertAndSend("mail.welcome", emp);
            return RespBean.success("添加成功!");
        }
        return RespBean.error("添加失败");
    }

    /**
     * 获取所有员工（分页）
     * @param currentPage
     * @param size
     * @param employee
     * @param beginDateScope
     * @return
     */

    @Override
    public RespPageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope) {
        // 开启分页
        Page<Employee> page =  new Page<>(currentPage, size);
        IPage<Employee> employeeByPage = employeeMapper.getEmployeeByPage(page, employee, beginDateScope);
        RespPageBean respPageBean = new RespPageBean(employeeByPage.getSize(), employeeByPage.getRecords());
        return respPageBean;
    }
}
