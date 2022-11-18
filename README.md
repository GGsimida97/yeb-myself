受新冠疫情影响，各中小企业的日常办公受到一定程度的影响，为保证其业务正常有序开展，亟需一款轻量、高效的在线办公系统。本项目是开发一款满足中小企业日常事务管理的在线云办公系统，包括流程审批、日常通告、财务、人事、移动办公等功能。采用主流前后端分离开发模式，涉及用户登录、职位职称管理、部门管理、员工管理、工资管理、在线聊天等多个模块。
涉及技术栈：SpringBoot、MyBatis-Plus、SpringSecurity、Redis、RabbitMQ、JWT、EasyPOI、WebSocket等
项目关键技术点：
1. 基于RBAC设计原则，利用SpringSecurity、JWT、Kaptcha实现登录认证与权限管理
2. 利用Redis搭建缓存层，降低数据IO频率，实现热点数据快速获取，提升系统响应速度
3. 利用RabbitMQ实现应用解耦与异步提速，提升用户体验和系统吞吐量
4. 基于数据落库、Redis分布式锁保证RabbitMQ消息投递可靠性与幂等性
5. 使用EasyPOI、FastDFS实现员工个人中心数据上传、下载
6. 基于WebSocket实现用户在线聊天功能
7. 利用Swagger2实现开发文档高效管理
