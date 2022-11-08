package com.wangjf.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.wangjf.server.pojo.Employee;
import com.wangjf.server.service.IEmployeeService;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private IEmployeeService employeeService;

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
}
