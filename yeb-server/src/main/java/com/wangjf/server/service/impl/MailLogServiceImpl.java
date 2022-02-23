package com.wangjf.server.service.impl;

import com.wangjf.server.pojo.MailLog;
import com.wangjf.server.mapper.MailLogMapper;
import com.wangjf.server.service.IMailLogService;
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
public class MailLogServiceImpl extends ServiceImpl<MailLogMapper, MailLog> implements IMailLogService {

}
