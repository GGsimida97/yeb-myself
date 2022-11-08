package com.wangjf.server.controller;


import com.wangjf.server.pojo.Joblevel;
import com.wangjf.server.pojo.RespBean;
import com.wangjf.server.service.IJoblevelService;
import com.wangjf.server.service.impl.JoblevelServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
@RequestMapping("system/basic/joblevel")
public class JoblevelController {
    @Autowired
    private IJoblevelService iJoblevelService;

    @ApiOperation("查询所有职称")
    @GetMapping("/")
    public List<Joblevel> getAllJoblevels() {
        return iJoblevelService.list();
    }

    @ApiOperation("新增一个职称")
    @PostMapping("/")
    public RespBean addJoblevel(@RequestBody Joblevel joblevel) {
        if (iJoblevelService.save(joblevel)) {
            return RespBean.success("新增职称成功");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation("更新职称")
    @PutMapping("/")
    public RespBean updateJoblevel(@RequestBody Joblevel joblevel) {
        if (iJoblevelService.updateById(joblevel)) {
            return RespBean.success("更新职称成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation("删除一个职称")
    @DeleteMapping ("/{id}")
    public RespBean deleteJoblevel(@PathVariable Integer id) {
        if (iJoblevelService.removeById(id)) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }
    @ApiOperation("批量删除职称")
    @DeleteMapping("/")
    public RespBean deleteJoblevels(Integer[] ids) {
        if (iJoblevelService.removeByIds(Arrays.asList(ids))) {
            return RespBean.success("批量删除成功");
        }
        return RespBean.error("批量删除失败");
    }

}
