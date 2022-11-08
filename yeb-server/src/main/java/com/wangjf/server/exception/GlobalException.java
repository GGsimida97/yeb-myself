package com.wangjf.server.exception;

import com.wangjf.server.pojo.RespBean;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalException {
    public RespBean mySqlException (SQLException e) {
        if (e instanceof SQLIntegrityConstraintViolationException) {
            return RespBean.error("该数据存在关联数据，操作失败!");
        }
        return RespBean.error("数据库异常, 操作失败!");
    }
}
