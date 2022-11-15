package com.wangjf.server.task;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wangjf.server.config.rabbitmq.MailConstants;
import com.wangjf.server.pojo.Employee;
import com.wangjf.server.pojo.MailLog;
import com.wangjf.server.service.IEmployeeService;
import com.wangjf.server.service.IMailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 邮件发送定时任务 10秒执行一次
 */
@Component
public class MailSendTask {
    @Autowired
    private IMailLogService mailLogService;
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Scheduled(cron = "0/10 * * * * ?")
    public void mailSendTask() {
        List<MailLog> list = mailLogService.list(new QueryWrapper<MailLog>()
                .eq("status", 2).lt("tryTime", LocalDateTime.now()));
        list.forEach(mailLog -> {
            // 如果重试次数超过三次，更新状态为投递失败，不再重试
            if (3 <= mailLog.getCount()) {
                mailLogService.update(new UpdateWrapper<MailLog>().set("status", 2)
                        .eq("msgId", mailLog.getMsgid()));
                return;
            }
            mailLogService.update(new UpdateWrapper<MailLog>().set("count", mailLog.getCount() + 1)
                    .set("updateTime", LocalDateTime.now())
                    .set("tryTime", LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT))
                    .eq("msgId", mailLog.getMsgid()));
            // 消息重发
            Employee employee = employeeService.getEmployee(mailLog.getEid()).get(0);
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY,
                    employee, new CorrelationData(mailLog.getMsgid()));
        });
    }
}
