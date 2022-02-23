package com.wangjf.server.service.impl;

import com.wangjf.server.pojo.Oplog;
import com.wangjf.server.mapper.OplogMapper;
import com.wangjf.server.service.IOplogService;
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
public class OplogServiceImpl extends ServiceImpl<OplogMapper, Oplog> implements IOplogService {

}
