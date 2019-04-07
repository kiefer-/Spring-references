# Future：整合 Spring 开发框架及服务端技术演练

本项目为 Spring 整合 MyBatis + Spring MVC，以及 Java Web 方向上多种常用的技术的演练，作为参考和交流学习的资料。另外，也是为了更好地掌握 Spring Boot 打下基础。该项目注释完备，代码规范，只做技术演练，没有过多复杂的业务逻辑，适合作为新手学习的参考。项目地址：[Shouheng88/Dreaming](https://github.com/Shouheng88/Dreaming)。

另外，笔者整理了 Spring IOC, AOP, MVC, 事务管理以及 Servlet 和 JSP 相关的内容。主要是整理了一些比较重点的内容作为开发的参考，以下是文章的链接地址：

1. [《对 Spring IOC 机制及其配置方式的的总结》](https://blog.csdn.net/github_35186068/article/details/88779264)
2. [《对 Spring AOP 机制及其配置方式的的总结》](https://blog.csdn.net/github_35186068/article/details/88780830)
3. [《Spring MVC 机制及其配置方式的总结》](https://blog.csdn.net/github_35186068/article/details/83858981)
4. [《对 Spring 事务管理机制及其配置方式的总结》](https://blog.csdn.net/github_35186068/article/details/88784931)
5. [《理解 Servlet 和 JSP》](https://blog.csdn.net/github_35186068/article/details/88909764)

下面是该项目详细的介绍：

## 项目环境

- JDK 1.8及以上
- Maven 管理jar包
- Mysql 数据库存储
- H2 嵌入式数据库
- Tomcat 运行用服务器
- Rabbit 非必须, 队列用, 可在配置中调整
- Lombok 需要开发环境 (IDEA) 支持

## 后端-常用服务端技术演练

- 通用的 spring 框架搭建，AOP 全局异常处理
- 前后端通用数据交互格式封装
- 提供返回 json 类型的接口以及返回普通 Web 页面的接口实例
- 文件上传接口
- Jasypt 数据加密
- Quartz 任务调度
- 提供 RSS 订阅实例
- 两种方式整合 Spring 和 MyBatis：只使用 MyBatis 的在 master 或者 milestone1 分支，基于 Spring 的 Mybatis 在 mybatis 分支
- 系统全局配置维护, 能实时刷新内存中最新配置
- 验证码生成、校验
- Log4j, Email 通知异常
- Druid 数据库连接池，对数据进行监控
- Json (fastxml) 序列化与反序列化
- 通用邮件配置及发送
- Excel 文件读写
- CSV 文件读写
- Junit 测试，以及与 Spring 进行集成
- RabbitMQ 队列, 生产-消费, 控制台管理
- 支持多个数据源，可以通过 Spring 激活配置进行更改
- 支持请求使用代理, 及动态选择代理
- 模块化开发，基础通用功能分成独立的模块进行开发
- 集成 Swagger 生成接口文档
- Ehcache 缓存，需要使用基于 Spring 配置的 Mybatis，需要切换分支到 mybatis

## 附录

1. ErLang 下载地址：http://www.erlang.org/downloads    
2. RabbitMQ 下载地址：https://www.rabbitmq.com/install-windows.html    
3. RabbitMQ 参考资料：https://www.cnblogs.com/LipeiNet/p/5973061.html

## 关注作者

1. Github: https://github.com/Shouheng88
2. 掘金：https://juejin.im/user/585555e11b69e6006c907a2a
3. CSDN：https://blog.csdn.net/github_35186068
4. 简书：https://www.jianshu.com/u/e1ad842673e2



