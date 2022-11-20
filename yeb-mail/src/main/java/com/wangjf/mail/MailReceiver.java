package com.wangjf.mail;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rabbitmq.client.Channel;
import com.wangjf.server.config.rabbitmq.MailConstants;
import com.wangjf.server.pojo.Employee;
import com.wangjf.server.pojo.MailLog;
import com.wangjf.server.service.IMailLogService;
import com.wangjf.server.service.impl.MailLogServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;


/**
 * 消息接收者
 */
@Component
@Slf4j
public class MailReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private MailProperties mailProperties;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private RedisTemplate redisTemplate;
//    @Autowired
//    private IMailLogService mailLogService;
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    /**
     * message为spring中的Message
     */
    public void handler(Message message, Channel channel) {
        Employee employee = (Employee) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        // 获取消息序号
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        // 获取messageId
        String msgId = (String) headers.get("spring_returned_message_correlation");

        ValueOperations valueOperations = redisTemplate.opsForValue();
        Boolean hasNotExist = valueOperations.setIfAbsent(msgId, msgId + "mail_log");
        try {
            if (!hasNotExist) {
                // TODO: 2022/11/18 待优化！还未设置ttl
                log.error("msgId为{}的消息，已经被消费！", msgId);
                /**
                 * 手动确认消息
                 * tag：消息序号
                 * multiple：是否确认多条
                 */
                channel.basicAck(tag, false);
                return;
            }
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);
            // 发件人
            helper.setFrom(mailProperties.getUsername());
            // 收件人
            helper.setTo(employee.getEmail());
            // 主题
            helper.setSubject("入职欢迎邮件");
            // 发送日期
            helper.setSentDate(new Date());
            // 邮件内容
            Context context = new Context();
            context.setVariable("name", employee.getName());
            context.setVariable("posName", employee.getPosition().getName());
            context.setVariable("joblevelName", employee.getJoblevel().getName());
            context.setVariable("departmentName", employee.getDepartment().getName());
            String mail = templateEngine.process("mail", context);
            helper.setText(mail, true);
            // 发送邮件
            javaMailSender.send(msg);
//            System.out.println("==========================>邮件发送成功");
            log.info("邮件发送成功");
            channel.basicAck(tag, false);
            /**
             * 更新mail_log中routingStatus、exchangeStatus的状态都为1，表示路由到队列正常
             * 换个字段
             */
            // TODO: 下面三行代码有异常 不能这样操作数据库（空指针异常）
            IMailLogService mailLogService = new MailLogServiceImpl();
            mailLogService.update(new UpdateWrapper<MailLog>().set("exchangeStatus", 1).eq("msgId", msgId));
            mailLogService.update(new UpdateWrapper<MailLog>().set("routingStatus", 1).eq("msgId", msgId));

        } catch (Exception e) {
            try {
                /**
                 * tag:
                 * multiple：
                 *requeue：是否退回队列
                 */
                channel.basicNack(tag, false, false);
            } catch (IOException ioException) {
                log.error("邮件发送失败111===========>{}", ioException.getMessage());
                ioException.printStackTrace();
            }
            log.error("邮件发送失败222===========>{}",e.getMessage());
        }
    }
}
