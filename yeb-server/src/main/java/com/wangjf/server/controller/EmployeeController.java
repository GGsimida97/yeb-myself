package com.wangjf.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.wangjf.server.pojo.*;
import com.wangjf.server.service.*;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author joker
 * @since 2022-01-02
 */
@RestController
@RequestMapping("/employee/basic")
public class EmployeeController {
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private INationService nationService;
    @Autowired
    private IPoliticsStatusService politicsStatusService;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IJoblevelService joblevelService;
    @Autowired
    private IPositionService positionService;


    @ApiOperation(value = "获取所有员工(分页)")
    @GetMapping("/")
    public RespPageBean getEmployee(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer size,
                                Employee employee, LocalDate[] beginDateScope) {
        return employeeService.getEmployeeByPage(currentPage, size, employee, beginDateScope);

    }
    @ApiOperation(value = "添加员工")
    @PostMapping("/")
    public RespBean addEmp(@RequestBody Employee employee) {
        return employeeService.addEmp(employee);
    }
    @ApiOperation(value = "导出员工数据")
    @GetMapping(value = "/export", produces = "application/octet-stream")
    public void exportEmployee(HttpServletResponse response) {
        List<Employee> list = employeeService.getEmployee(null);
        ExportParams exportParams = new ExportParams("员工表", "员工表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, Employee.class, list);
        ServletOutputStream out = null;
        try {
            // 流的形式
            response.setHeader("content-type", "application/octet-stream");
            // 防止中文乱码
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("员工表.xls", "UTF-8"));
            out = response.getOutputStream();
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation(value = "导入员工数据")
    @PostMapping("/import")
    public RespBean importEmployee(MultipartFile file) {
        ImportParams params = new ImportParams();
        // 去掉标题行
        params.setTitleRows(1);

        List<Nation> nationList = nationService.list();
        List<PoliticsStatus> politicsStatusList = politicsStatusService.list();
        List<Department> departmentList = departmentService.list();
        List<Joblevel> joblevelList = joblevelService.list();
        List<Position> positionList = positionService.list();
        try {
            List<Employee> employees = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, params);
            employees.forEach((employee) -> {
                //根据传入employee对象中的nation的name  获取nationId, 再设置回employee对象
                employee.setNationid(nationList.get(nationList.indexOf( new Nation(employee.getNation().getName()))).getId());
                employee.setPoliticid(politicsStatusList.get(politicsStatusList.indexOf(new PoliticsStatus(employee.getPoliticsStatus().getName()))).getId());
                employee.setDepartmentid(departmentList.get(departmentList.indexOf(new Department(employee.getDepartment().getName()))).getId());
                employee.setJoblevelid(joblevelList.get(joblevelList.indexOf(new Joblevel(employee.getJoblevel().getName()))).getId());
                employee.setPosid(positionList.get(positionList.indexOf(new Position(employee.getPosition().getName()))).getId());
            });
            if (employeeService.saveBatch(employees)) {
                return RespBean.success("导入成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");

    }
}
