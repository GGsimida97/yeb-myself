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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

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
                /**
                 * exchangeStatus：
                 *  1:消息投递到交换机成功
                 *  2:消息投递到交换机失败
                 * routingStatus：
                 *  1:消息成功路由到队列
                 *  2:消息路由到队列失败
                 */
                log.info("msgId为{}的消息投递到交换机成功", msgId);
                // 更新数据库表mail_log状态为1，交换机成功收到消息
//                mailLogService.update(new UpdateWrapper<MailLog>().set("exchangeStatus", 1).eq("msgId", msgId));
            } else {
                log.error("msgId为{}的消息投递到交换机失败", msgId);
                mailLogService.update(new UpdateWrapper<MailLog>().set("exchangeStatus", 2).eq("msgId", msgId));
            }
        });
        /**
         * 当交换机成功接收了消息，但是路由到队列时，失败，才会回调
         * 注意：当交换机成功接收了消息，但是路由到队列时失败时，ReturnCallback会比ConfirmCallback先执行！！！！
         */
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            String msgId = message.getMessageProperties().getHeader("spring_returned_message_correlation");
            log.error("msgId为{}的消息未被成功路由", msgId);
            mailLogService.update(new UpdateWrapper<MailLog>().set("routingStatus", 2).eq("msgId", msgId));
        });
        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }

    @Bean("mailQueue")
    public Queue mailQueue() {
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }

    @Bean("mailExchange")
    public DirectExchange mailExchange() {
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(@Qualifier("mailQueue") Queue mailQueue, @Qualifier("mailExchange") DirectExchange mailExchange) {
        return BindingBuilder.bind(mailQueue).to(mailExchange).with(MailConstants.MAIL_ROUTING_KEY);
    }
}
