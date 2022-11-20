package com.wangjf.mail;

import com.wangjf.server.config.rabbitmq.MailConstants;
import com.wangjf.server.mapper.MailLogMapper;
import com.wangjf.server.service.IAdminService;
import com.wangjf.server.service.IMailLogService;
import com.wangjf.server.service.impl.AdminServiceImpl;
import com.wangjf.server.service.impl.MailLogServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


//@ComponentScan(basePackages = {"com.wangjf.server.service"})
//@MapperScan({"com.wangjf.server.mapper"})

// 去掉连接数据库相应的依赖
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MailApplication {
    public static void main(String[] args) {
        SpringApplication.run(MailApplication.class,args);
    }
    @Bean
    public Queue queue() {
      return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }
//    @Bean
//    public IMailLogService mailLogService() {
//        return new MailLogServiceImpl();
//    }

}
