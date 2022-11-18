package com.wangjf.server.config.rabbitmq;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wangjf.server.pojo.MailLog;
import com.wangjf.server.service.IMailLogService;
import com.wangjf.server.service.impl.MailLogServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitmqConfig {
    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;
    @Autowired
    private IMailLogService mailLogService;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        /**
         *  交换机确认回调方法
         *  1. 发消息  交换机收到了 回调
         *  1.1 correlationData： 保存回调消息的ID及相关信息
         *  1.2 ack 交换机收到消息 true
         *  1.3 cause 为null
         *  2. 发消息  交换机没收到了 回调
         *  2.1 correlationData： 保存回调消息的ID及相关信息
         *  2.2 ack 交换机没收到消息 false
         *  2.3 cause 为失败的原因
         */
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            String msgId = correlationData.getId();
            if (ack) {
                log.info("交换机成功收到msgId为{}的消息", msgId);
                // 更新数据库表mail_log状态为1，表示投递成功
                // TODO: 2022/11/17 这边没有考虑路由异常的情况！待优化 
                mailLogService.update(new UpdateWrapper<MailLog>().set("status", 1).eq("msgId", msgId));
            } else {
                log.error("交换机未收到msgId为{}的消息", msgId);
                mailLogService.update(new UpdateWrapper<MailLog>().set("status", 2).eq("msgId", msgId));
            }
        });
        /**
         * 当交换机成功接收了消息，但是路由到队列时，失败，才会回调
         */
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.error("msgId为{}的消息未被成功路由", message.getBody().toString());
            // mailLogService.update(new UpdateWrapper<MailLog>().set("status", 2).eq("msgId", message.getMessageProperties().getCorrelationId()));
        });
        return rabbitTemplate;
    }

    @Bean
    public Queue mailQueue() {
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }

    @Bean
    public DirectExchange mailExchange() {
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(MailConstants.MAIL_ROUTING_KEY);
    }
}
