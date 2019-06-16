## 1、关于项目

### 1.1 项目的内容介绍

[Spring-reference](https://github.com/Shouheng88/Spring-references) 项目为 Spring 整合 MyBatis + Spring MVC，以及 Java Web 方向上多种常用的技术的演练，作为参考和交流学习的资料。另外，也是为了更好地掌握 Spring Boot 打下基础。该项目注释完备，代码规范，只做技术演练，没有过多复杂的业务逻辑，适合作为新手学习的参考。项目地址：[Shouheng88/Spring=references](https://github.com/Shouheng88/Dreaming)。

另外，笔者整理了 Spring IOC, AOP, MVC, 事务管理以及 Servlet 和 JSP 相关的内容。主要是整理了一些比较重点的内容作为开发的参考，以下是文章的链接地址。在这篇文章中，我们不会讨论如何这些框架的基本的使用，而是对整合它们到框架当中提出解决方案：

1. [《对 Spring IOC 机制及其配置方式的的总结》](https://blog.csdn.net/github_35186068/article/details/88779264)
2. [《对 Spring AOP 机制及其配置方式的的总结》](https://blog.csdn.net/github_35186068/article/details/88780830)
3. [《Spring MVC 机制及其配置方式的总结》](https://blog.csdn.net/github_35186068/article/details/83858981)
4. [《对 Spring 事务管理机制及其配置方式的总结》](https://blog.csdn.net/github_35186068/article/details/88784931)
5. [《理解 Servlet 和 JSP》](https://blog.csdn.net/github_35186068/article/details/88909764)

### 1.2 项目的开发环境

- JDK 1.8及以上
- Maven 管理jar包
- Mysql 数据库存储
- H2 嵌入式数据库
- Tomcat 运行用服务器
- Rabbit 非必须, 队列用, 可在配置中调整
- Lombok 需要开发环境 (IDEA) 支持

### 1.3 项目中整合的技术清单

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

## 2、SSM 整合记录

### 2.1 maven 项目的结构问题

在开发的时候，我们通常有一些基础的类放在一个单独的模块中。在 Gradle 中，我们可以将其放置在各个模块中。然后，我们将其通过在 setting.gradle 中配置模块的文件路径。在 maven 当中，我们可以使用类似的方式来解决同样的问题。在示例项目当中，我们将代码放进两个模块当中：

1. Common 模块：放置一些基础的类，比如 dao 的一些辅组类、验证码生成、常用的工具类等。
2. Service 模块：与业务相关的一些类，比如 po, bo 等、Service 等。

在 maven 当中，我们可以按照下面这样来配置模块的目录：

首先，项目当中的 pom.xml 共有三个，两个子模块各占一个，然后一个公共的父 pom.xml。因此，项目当中的 pom.xml 文件结构如下：

```
|-----
    |----common
            |----pom.xml
    |----service
            |----pom.xml
    |----pom.xml
```

在三个 pom.xml 文件中，我么需要作如下的配置：

1. 首先在顶层的父 pom.xml 中，我们作如下的配置。这里主要是对公共的部分进行管理，比如依赖以及依赖的版本。另外，属于每个模块的 groupId, artifactId 在每个模块当中都是必不可少的。另外，在父 pom 当中还要指定所引用的子模块的名称，不然这些模块就无法被加载到项目当中。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!-- 这里是这个模块的信息，相当于身份标志 -->
    <groupId>spring-references</groupId>
    <artifactId>spring-references</artifactId>
    <version>1.0</version>
    <packaging>pom</packaging>

    <name>Spring-references</name>
    <url>https://github.com/Shouheng88/Spring-references</url>

    <!-- 当前的模块引用的模块 -->
    <modules>
        <module>service</module>
        <module>common</module>
    </modules>

    <!-- 用来对所依赖的库的版本进行管理 -->
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <!-- 其他版本…… -->
    </properties>

    <!-- 用来进行依赖管理，我们在父 pom 中进行配置，然后子模块中引用即可 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>${junit.version}</version>
                <scope>test</scope>
            </dependency>
            <!-- 依赖管理…… -->
        </dependencies>
    </dependencyManagement>

    <build>
        <finalName>spring-references</finalName>
        <pluginManagement>
            <plugins>
                <!-- 插件管理…… -->
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <configuration>
                        <source>8</source>
                        <target>8</target>
                    </configuration>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>

</project>
```

2. 然后对于 service 模块的 pom 做如下处理。这里我们需要先指定本模块的身份信息，即 groupId, artifactId 等。注意，这里的 artifactId 也就是父模块 pom 当中引用的模块的 id. 另外，我们需要通过 parent 标签指定父模块的信息。这样才能正确地从父模块当中继承依赖等信息。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!-- 当前模块的信息 -->
    <artifactId>service</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <name>Service</name>
    <url>https://github.com/Shouheng88/Spring-references</url>

    <!-- 父模块的信息 -->
    <parent>
        <groupId>spring-references</groupId>
        <artifactId>spring-references</artifactId>
        <version>1.0</version>
    </parent>

    <!-- 该模块当中引用的依赖 -->
    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

</project>
```

对于 common 模块的 pom 可以采用类似的处理方式，这里不再进行说明。

### 2.2 整合 Spring

在当前的示例项目当中，我们通过 Spring 提供的一些框架来实现一些常用的功能：

1. 基于 Json 的 restful 风格的接口
2. 基于 Spring MVC 来展示 jsp 页面
3. 文件的上传接口
4. 程序中的异常处理
5. 数据源如何配置

为了实现 json 格式的接口，那么要考虑的问题又包括：

1. 交互的数据如何进行封装；

下面我们来整理下这些东西是如何进行处理的。

#### 2.2.1 前后端交互的数据结构

对于后端自身而言，通常我们会根据业务将数据分成几种类型，包括 

1. PO：对应于数据库的数据结构，字段的信息与数据库列对应；
2. SO：前端传入到后端的数据结构，通常用来传递一些查询信息；
3. VO：后端返回给前端的数据结构，可以根据需要下发的字段进行自定义。

##### 1. 使用 Lombok 简化数据结构

为了简化我们的代码，我们还可以使用一个叫做 lombok 的插件来简化我们的数据结构。当然，lombok 的作用无非就是简化 setter/getter 和一些构造方法。这能让我们的数据实体看上去更加清爽。

为了使用 lombok 我们只需要做两处配置就可以了。

1. 在 maven 当中添加 lombok 的依赖：

```xml
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${lombok}</version>
    </dependency>
```

2. 在 IDEA 的插件当中添加 lombok：在 IDEA 当中搜索 "Lombok Plugin" 并进行安装即可。

##### 2. PO 的设计

首先呢，一个基础的 PO 是必不可少的。我们通常使用它们来定义所有的 PO 都应该均有的基础字段，比如 id, 创建时间, 最后的更新时间, 备注和乐观锁版本信息等。因此，一个基础的 PO 类应该是下面这样：

```java
@Data
public abstract class AbstractPO implements Serializable {

    private static final long serialVersionUID = 6982434571375510313L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    @Column(name = "remark")
    private String remark;

    @Version
    @Column(name = "lock_version")
    private Integer lockVersion;
}
```

##### 3.VO 的设计

VO 的设计包含两部分内容。首先，我们需要定义对应于 PO 的 VO 类。这种类用来定义返回给前端的数据结构。它并不一定与 PO 类的结构完全相同，而是根据接口的需要选择性地下发部分字段或者新增一些字段。

对 VO，我们可以定义一个基础的抽象类如下。也就是定义了一些对应于 PO 的字段信息：

```java
@Data
public abstract class AbstractVO implements Serializable {

    private static final long serialVersionUID = 1;

    private Long id;
    private Date createdTime;
    private Date updatedTime;
    private String remark;
    private Integer lockVersion;
}
```

除了 VO 我们还要定义 PackVo. 它用来包装 VO 的信息，另外提供一些用来返回给前端的冗余字段信息，以及服务端返回的错误信息封装等。对于 PackVo，我们也可以定义一个基础的抽象类 AbstractPackVo：

```java
@Data
public abstract class AbstractPackVo implements Serializable {

    private static final long serialVersionUID = -2119661016457733317L;

    private Boolean success = true;
    private List<ClientMessage> messages;
    private Long udf1;
    private String udf2;
    private String udf3;
    private String udf4;
    private String udf5;
    private String udf6;
}
```

这里的 AbstractPackVo 是一个基础的抽象类。针对具体要返回的业务数据结构，我们需要定义对应的具体的类，并继承 AbstractPackVo:

```java
@Data
public class PackTaskVo extends AbstractPackVo {

    private static final long serialVersionUID = 1L;

    private TaskVo vo;
    private List<TaskVo> voList;
}
```

##### 4.ClientMessage 封装

ClientMessage 就是用来返回给前端的错误信息的包装类。我们可以按照下面这样的方式来进行定义：

```java
@Data
public class ClientMessage implements Serializable {

    private static final long serialVersionUID = -1L;

    private Long id;
    private String code;
    private String message;
    private String messageCN;
}
```

这里的 code 用来表示错误信息的代码，我们可以统一将错误信息定义在 properties 文件中，并将其置于项目的 resources 目录下面。

```properties
#测试消息
E000000000000000=测试消息{0} {1}
#乐观锁异常
E000000000000001=该记录已经被其他用户修改
#空指针异常
E000000000000002=NullPointerException
#系统错误
E000000000000003=SystemErrorException
#DAO错误
E000000000000004=DAOException
```

然后，在我们的项目中，我们可以通过单例的工具类来从 properties 文件中读取错误信息：

```java
public class ErrorDispUtils {

    private static Logger logger = LoggerFactory.getLogger(ErrorDispUtils.class);

    private static final String CONFIG_FILE = "error-disp.properties";

    private static ErrorDispUtils instance = new ErrorDispUtils();

    private static Configuration config;

    public static ErrorDispUtils getInstance() {
        return instance;
    }

    private ErrorDispUtils() {
        try {
            config = new PropertiesConfiguration(CONFIG_FILE);
        } catch (Exception e) {
            logger.error("ErrorDispUtils initialize error" ,e);
        }
    }

    // 用来读取指定 code 的字符串
    public String getValue(String key) {
        return config.getString(key);
    }
}
```

对于 ClientMessage，我们可以在项目中通过 AOP 来进行拦截，然后做一个统一的处理。

##### 5.SO 的设计

SO 是前端提交给客户端的 json 的数据结构，对于它我们也需要做一个简单的封装。这里我们也提供一个基础的类 SearchObject：

```java
public class SearchObject implements Pageable, Sortable, Serializable {

    private static final long serialVersionUID = 4009650343975989289L;

    private int currentPage;
    private int pageSize;
    private List<Sort> sorts = new LinkedList<>();

    // ... setters and getters
}
```

这里的 SearchObject 实现了 Pageable 和 Sortable 两个自定义接口。它们分别用来进行分页和指定用来排序的字段：

```java
public interface Pageable {

    int getCurrentPage();

    void setCurrentPage(int currentPage);

    int getPageSize();

    void setPageSize(int pageSize);
}

public interface Sortable {

    List<Sort> getSorts();

    void addSort(Sort sort);
}

public class Sort implements Serializable {

    private static final long serialVersionUID = 7739709965769082011L;

    private String sortKey;
    private String sortDir;
}
```

这里的 Sort 定义了两个字符串类型的字段，分别表示排序的字段以及排序的方向。

##### 6. 前后端交互

上面我们定义的数据结构是用在服务端自身的，比如 Service 返回数据。但是，它还无法直接用于前后端的交互。因为前端可能需要一些额外的参数代表设备信息，返回给后端的数据也可能包含一些错误信息等，也就是 ClientMessage 中的信息。因此，我们需要设计新的数据结构。下面 BusinessRequest 和 BusinessResponse 就分别用作将前端数据传递给后端以及后端返回数据给前端：

```java
@Data
public class BusinessRequest<T> {
    private Integer clientVersion;
    private Date clientTime;
    private String phoneNumber;
    private String iMEI;
    private String iMSI;
    private String deviceID;
    private Long userID;
    private String token;
    private String requestType;
    private T requestData;
    private List<T> requestDataList;
}


@Data
public class BusinessResponse<T> {
    private Boolean isSuccess;
    private Long serverFlag;
    private String serverMessage;
    private T responseData;
    private List<T> responseDataList;
    private Long udf1;
}
```

##### 7.总结

以上只是我们定义前后端交互的数据结构的一种方式，实际上我们可以有很多不同的方式来定义交互的格式。只是，在项目开发之前，这种基础的交互格式需要前后端进行沟通之后来确定。上面的开发的方式也需要按照我们指定的格式来定义才能正确地把我们返回的数据从 Json 映射成基础的 java 类。

除了前后端的问题，定义数据结构还将影响我们的开发。比如我们可以在 MyBatis 的文件中定义一个 sql 代码片段来在我们的项目中使用 Sort 字段：

```xml
<sql id="Order_By_Clause">
  <if test="sorts != null and sorts.size > 0">
    ORDER BY
      <foreach item="item" collection="sorts" separator = ",">
        <if test="item.sortKey != null and item.sortKey != ''">t.${item.sortKey}</if>
        <if test="item.sortDir != null and item.sortDir != ''">${item.sortDir}</if>
	</foreach>
  </if>
</sql>
```

很显然，这得益于我们以上定义的那一套数据结构规范。而这套规范一旦最终确定下来，我们可以做更多的操作来简化我们的开发。（显然，如果这样做了，一旦数据结构改动，调整的成本也是很高的！）

不过，这种数据交互的格式，我们还是尽量采用一种可靠的、比较成熟都是解决方案，免得在开发的过程中因为数据结构的问题延缓开发进度。而上面的这种格式本身就是一种比较成熟的解决方案了。

#### 2.2.2 Spring 整合

在使用 Spring 开发的过程中几乎必选的三个核心的模块：IoC, AOP 和 MVC. 这里我们主要说明的是 Spring 的这三个模块的整合。

##### 1. 引入依赖

首先我们需要在项目当中引用 Spring 所需的各种依赖：

```xml
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-beans</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aop</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context-support</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aspects</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
        </dependency>
```

这里我们引用的比较多，主要包括：Spring 核心库、Spring 容器、Spring 事务管理框架以及 MVC 框架。

然后，我们需要在项目当中整合 Spring 的各个库。

首先是 Spring MVC. 我们需要在项目的 web.xml 中添加如下的配置：

```xml
    <!--Spring MVC 相关的 Servlet 的配置-->
    <servlet>
        <servlet-name>spring-mvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring/spring-*.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>spring-mvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
```

这里我们指定 Servlet 的信息，这里前面使用了 Spring MVC 的 Servlet 来对请求做分发。然后使用 init-param 标签指定了 Spring 上下文的配置文件的位置。本质上这里作用的原理是通过解析 xml 文件来读取配置信息并生成项目中的类。比如，这里会创建一个 DispatcherServlet 类，然后这里的 init-param 中的键值对会被通过 setter 方法注入到生成的 DispatcherServlet 实例中。本质上这些逻辑都是在 Servlet 即 Tomcat 或者 Netty 等当中完成的。

这里的 servlet 和 servlet-mapping 标签是一一对应的，它们通过 servlet-name 来实现匹配关系。这里的 servlet-mapping 标签用来指定名为 servlet-mvc 的 servlet 能够处理的 url.

上面，我们使用了通配符来指定多个配置文件，它们的规则是名称以 `spring-` 开头的 xml 文件，并且都处于 resources 的 spring 目录下面。这样做是因为我们希望把项目当中的 Spring 的配置文件按照它的功能分配到不同的配置文件当中去。比如，在示例项目中，我们的 Spring 的配置文件如下：

1. `spring-dao.xml`: 用来配置 DAO 相关的各种 Bean；
2. `spring-service.xml`：用来配置 Service 相关的各种 Bean；
3. `spring-shiro.xml`：用来配置 shiro 相关的 Bean 的信息；
4. `spring-web.xml`：用来配置 web 相关的 Bean 的信息。

默认情况下，DispatcherServlet 会加载 `WEB-INF/[DispatcherServlet的Servlet名字]-servlet.xml` 下面的配置文件。根据上面的配置我们需要在当前项目的 WEB-INF 目录下面加入 spring-mvc-servlet.xml 文件。

另外需要注意的地方是，这里我们直接使用 Spring MVC 来分发所有类型的请求。具体是使用 Json 进行交互还是返回 Jsp 页面，我们在代码中使用 Spring 的 RequestMapping 注解来完成。

下面我们先看一下使用 Spring MVC 的时候需要做哪些配置。

##### 2. 整合 MVC

首先让我们看下 Spring 的 web 相关的配置文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns=...>

    <description>Spring Web 层的配置</description>

    <!--启用注解驱动-->
    <mvc:annotation-driven />

    <!--指定 Controller 的扫描位置-->
    <context:component-scan base-package="me.shouheng.service.controller">
    </context:component-scan>

    <!--请求映射器-->
    <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping"/>

    <!--请求适配器-->
    <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter"/>

    <!--配置 Spring MVC 的视图解析器，需要返回简单页面的时候会用到-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/views/"/>
        <property name="suffix" value=".jsp"/>
    </bean>

    <!--用来处理文件上传的请求-->
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <property name="maxUploadSize" value="4194304" />
        <property name="maxInMemorySize" value="4194304" />
    </bean>

</beans>
```

上面的注解已经很完备了，这里我们再简单说明下各个配置项的作用：

1. 启用注解驱动：因为我们使用注解来对请求的路由进行映射，因此我们需要启用注解驱动；
2. 然后，我们需要指定我们的 Controller 的位置，这里我们只需要使用 component-scan 标签来指定扫描的包的路径即可。（另外，说明下在项目开发过程中，我们的包结构应该结构清晰和责任独立。）
3. 然后，我们需要定义请求映射器和请求适配。请求映射器用来指定请求被映射到哪个 Handler. 不同的 Handler 又需要通过适配器以得到情网的结果。这里我们使用的是 RequestMappingHandlerMapping 和 RequestMappingHandlerAdapter. 它们可以帮助我们将请求按照注解的方式进行映射。
4. 至于视图解析器。它主要用在返回 jsp 页面的请求，指定了 jsp 页面的前缀和后缀。
5. 最后的一项配置用来处理文件上传的请求。

本质上按照上面的配置，我们已经可以把前端传入的请求处理之后返回给后端了。我们可以使用下面的例子来进行简单的测试：

```java
@Controller
@RequestMapping(path = {PATH_PREFIX})
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    static final String PATH_PREFIX = "/task";

    private static final String LIST = "/all";

    private static final String PAGE = "/page";

    /**
     * 测试用来发送 restful 类型的请求的接口
     *
     * @param taskSo 请求对象，Json 类型，放置在 body 中
     * @return 返回对象
     */
    @ResponseBody
    @RequestMapping(value = LIST, method = RequestMethod.POST)
    public PackTaskVo listAll(@RequestBody TaskSo taskSo) {
        logger.info("----------- received : " + taskSo);
        return new PackTaskVo();
    }

    /**
     * 测试用来请求 jsp 页面的接口
     *
     * @return jsp 页面（名称），映射到 view/task.jsp
     */
    @RequestMapping(value = PAGE, method = RequestMethod.GET)
    public String testPage() {
        logger.info("----------- requesting test page.");
        return "task";
    }
}
```

在上面的这个例子中，我们使用到了之前定义的数据结构。这里的 @Controller 注解表明这个类是一个控制器。这里的 @RequestMapping 注解用来指定路由的映射关系。

另外，上面我们使用到了 Looger，这是一个日志框架，我们稍后会说明如何集成日志框架。

##### 3. 配置数据源

关于数据源的问题我们几个问题需要关注：

1. 使用哪种数据源，关系型还是非关系型，如果是关系型数据库那么是 MySQL 还是其他数据库；
2. 使用哪种数据库访问框架，MyBatis 还是 Herbinate；
3. 如何根据开发开发环境选择不同的数据源。

对于数据库类型而言，业务开发的时候通常使用关系型数据库，进行缓存的时候会使用非关系型数据库比如 Redis 或者 MemCached. 对于关系型数据库，我们可以使用 MySQL 或者其他数据库。这里我们为了进行演示，使用两种关系型数据库。分别是 MySQL 对应于生成环境，H2 数据库对应于测试开发环境。因此这就需要我们引入 MySQL 和 H2 的数据库连接驱动的依赖：

```xml
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>${mysql-connecotr}</version>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <version>${h2.version}</version>
    </dependency>
```

那么根据开发环境选择数据库的时候，我们有很多种配置方式。比如，我们可以在 MyBatis 中构建数据库连接的时候进行配置或者在 Spring 加载不同的配置文件的时候指定环境。但是，**我们应该尽量使用一种配置方式，避免同时使用多种配置方式**。这里我们使用 Spring 上下文来进行配置：

1. 我们可以在 web.xml 中指定当前要执行的环境，或者在虚拟机启动参数中指定。如果在 web.xml 中指定上下文的话，那么我们需要做在该文件当中增加下面几行代码：

```xml
    <!--选择要激活的 Spring 环境配置-->
    <context-param>
        <param-name>spring.profiles.active</param-name>
        <param-value>dev</param-value>
    </context-param>
```

2. 然后在 spring-dao.xml 配置文件中，我们可以按照下面的方式来指定使用哪个数据源配置文件：

```xml
    <beans profile="dev">
        <context:property-placeholder location="classpath*:configs/jdbc-dev.properties" />
    </beans>

    <beans profile="test">
        <context:property-placeholder location="classpath*:configs/jdbc-test.properties" />
    </beans>

    <beans profile="prod">
        <context:property-placeholder location="classpath*:configs/jdbc-prod.properties" />
    </beans>
```

具体数据库连接等信息被放置在各个配置环境文件当中。

对于数据库访问框架的问题，我们这里使用 MyBatis. 毕竟它当前属于主流的数据库访问框架，相对于 Herbinate 拓展性比较好。对于 MyBatis 的集成我们会在后面进行说明。

##### 4. 使用 Spring AOP 进行异常处理

当程序在运行的过程中出现错误的时候，我们希望能够对错误进行统一的处理，然后将错误信息包装成 ClientMenssage 之后返回给客户端。在 Spring 当中，我们可以使用 AOP，通过切面来实现异常的统一处理。

这里我们使用如下的配置来实现对异常的处理：

```xml
    <!--启用自动扫描-->
    <context:component-scan base-package="me.shouheng.service.*"/>

    <!--Service 方法切入进行事务管理，比较粗粒度的事务管理，所以需要配置事务的传播行为-->
    <bean id="serviceHandler" class="me.shouheng.service.common.aop.ServiceMethodInterceptor"/>
    <aop:config>
        <!--对Service的方法的拦截-->
        <aop:pointcut id="servicePointcut" expression="within(me.shouheng.service.service.impl.*)"/>
        <aop:advisor advice-ref="serviceHandler" id="serviceAdvisor" pointcut-ref="servicePointcut"/>
    </aop:config>
```

这里的第一点是启用注解自动扫描以发现项目中的 Service，然后定义一个拦截器 ServiceMethodInterceptor。这里使用的是 JDK 动态代理来实现对异常的控制。

在下面的 aop:config 标签当中我们定义了要拦截的 Service 的规则。也就是 impl 包下面的所有的类的所有方法。

那么下面我们来看下这个拦截器是如何定义的：

```java
public class ServiceMethodInterceptor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ServiceMethodInterceptor.class);

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method targetMethod = methodInvocation.getMethod();
        Object ret;
        try {
            // 初始化数据库连接
            SqlSessionHolder.initReuseSqlSession();
            // 进行安全校验并触发方法
            ret = checkSecurityAndInvokeBizMethod(methodInvocation);
            // 提交事务
            SqlSessionHolder.commitSession();
        } catch (Exception e) {
            logger.error("Error calling " + targetMethod.getName() + " : " + e);
            // 事务回滚
            SqlSessionHolder.rollbackSession();
            // 包装异常信息之后将其返回给客户端
            return this.createExceptionResult(methodInvocation, e);
        } finally {
            // 清空会话信息
            SqlSessionHolder.clearSession();
        }
        return ret;
    }
}
```

上面是我们的自定义拦截器。这里在执行拦截的方法之前会调用 `SqlSessionHolder.initReuseSqlSession()` 启动数据库连接会话。然后在 `checkSecurityAndInvokeBizMethod()` 方法中执行指定的方法。这里如果方法执行的过程中没有出现任何错误，那么我们就可以使用 `SqlSessionHolder.commitSession()` 提交事务。如果在执行方法的过程中出现了错误，那么我们就在上面的拦截器当中 catch，根据 Service 的返回格式，创建包含异常信息的返回结果。并进行事务回滚。

**不过这里有一个问题就是，如果一个 Controller 调用了多个 Service. 当其中的一个出现问题的时候只能保证这个 Service 的方法返回了错误的信息。但这个 Controller 可能会继续调用其他的 Service 的方法。因此，这种拦截的逻辑最好以 Controller 的方法作为维度进行控制。**

#### 2.2.3 整合 MyBatis

就像前面提到的，我们提供了两种整合 MyBatis 的方式。一种是对 MyBatis 的各个方法做了封装的方式。这种方式有固定的格式，对 Mapper 的命名以及其内部的方法的命名有严格的要求。这种整合方式配合我们的 Generator 可以降低我们开发的复杂度。另一种整合方式即使用 MyBatis 文件内部的规则来实现映射关系。

##### 1. 定义数据源

上面我们已经提到过 MyBatis 的数据源如何根据环境来进行配置。本质上上面提到的只是根据当期的开发环境在 xml 配置文件中引用不同的 properties 文件。下面我们来看下如何进行配置：

```xml
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
        <property name="driverClassName" value="${jdbc.driverClassName}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
        <!-- ... -->
    </bean>
```

这里其实我们使用的是阿里巴巴开源的 Druid 数据库连接池的数据源来实现的。其中的主要的内容是上面的几个占位符，这些也就是我们需要从 properties 文件中读取到的属性的值。比如，开发环境中我们可能指定这些值：

```properties
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8
jdbc.username=root
jdbc.password=qweasdzxc
```

##### 2. 事务管理

Spring 本身已经为我们提供了一套事务管理机制。这里我们使用一套自定义的事务管理机制。上面说明 AOP 的时候我们也提到过事务回滚的和提交的内容，那就是我们用来实现事务的逻辑。这里我们主要说明下这里是如何基于 MyBatis 的事务进行管理的。

因为本质上我们之心 SQL 的时候都是使用 MyBatis 的 SqlSession 来完成的，而 SqlSession 又都是从 SqlSessionFactory 中获取到的，SqlSessionFactory 又是根据各个配置文件来创建的。因此，对于每个线程的会话，我们可以将其放置到线程的局部变量中缓存起来。然后需要执行 SQL 的时候从缓存中提取并使用即可。

因此，在进行数据库访问之前我们需要初始化 SqlSessionFactory 以获取 SqlSession；在执行完 SQL 之后再根据需要使用 SqlSession 的方法提交或者回滚。

这里我们使用类 SqlMapClientHolder 来初始化各个环境对应的 SqlSessionFactory. 然后在 SqlSessionHolder 中使用 SqlMapClientHolder 获取 SqlSessionFactory 并存取各个线程的单例的 SqlSession.

以上就是我们基于 MyBatis 的 SqlSession 进行事务管理的实现逻辑。

##### 3. 第 1 种整合方式：直接借助 MyBatis 

这种配置方式比较简单，我们可以先来说明下这种配置是如何实现的。首先，我们需要在 spring-dao.xml 配置文件中对 MyBatis 进行配置：

```xml
    <!-- 配置SqlSessionFactory对象 -->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!-- 注入数据库连接池 -->
        <property name="dataSource" ref="dataSource" />
        <!-- 配置MyBaties全局配置文件:ibatis-config.xml -->
        <property name="configLocation" value="classpath:ibatis-config.xml" />
        <!-- 扫描entity包 使用别名 -->
        <property name="typeAliasesPackage" value="me.shouheng.service.model" />
        <!-- 扫描sql配置文件:mapper需要的xml文件 -->
        <property name="mapperLocations" value="classpath:mybatis/*.xml" />
        <!-- 类型处理器：用来将获取的值以合适的方式转换成 Java 类型 -->
        <property name="typeHandlers">
            <array>
                <bean class="me.shouheng.common.dao.handler.DateTypeHandler"/>
            </array>
        </property>
    </bean>

    <!-- 4.配置扫描Dao接口包，动态实现Dao接口，注入到spring容器中 -->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <!-- 注入sqlSessionFactory -->
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory" />
        <!-- 给出需要扫描Dao接口包 -->
        <property name="basePackage" value="me.shouheng.service.dao" />
    </bean>
```

在上面的这段代码中，我们指定了数据源、MyBatis 配置文件的位置、entity 包的位置、Mapper 文件的位置以及一些自定义的类型处理器。这样我们就可以实现 DAO 到 Mapper 的映射。

然后，我们只需要定义各个 DAO 的接口即可。这里我们定义了一个顶层的接口，然后具体的 DAO 可以继承这个接口以添加自己的 DAO 方法：

```java
public interface DAO<T extends AbstractPO> {

    void insert(T entity) throws DAOException;

    int update(T entity) throws DAOException;

    int updatePOSelective(T entity) throws DAOException;

    List<T> searchBySo(SearchObject so) throws DAOException;

    <E> List<E> searchVosBySo(SearchObject so) throws DAOException;

    long searchCountBySo(SearchObject so) throws DAOException;

    void deleteByPrimaryKey(Long id) throws DAOException;

    T selectByPrimaryKey(Long id) throws DAOException;

    T selectVoByPrimaryKey(Long id) throws DAOException;
}

public interface TaskDAO extends DAO<Task> {

    // 这个接口中需要增加的方法
}
```

然后，当然就是 DAO 的方法到 Mapper 文件中的 xml 元素的映射关系：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="me.shouheng.service.dao.TaskDAO">

    <cache type="org.mybatis.caches.ehcache.EhcacheCache"/>

    <insert id="insert" parameterType="Task">
        insert into gt_task(
        <!-- 0-->id,
        <!-- 1-->created_time,
        <!-- .... -->
    </insert>

    <update id="update" parameterType="Task">
        update gt_task set
            created_time=#{createdTime:BIGINT},
        <!-- .... -->
    </update>

    <!-- ... -->
</mapper>
```

上面一个一个方法地去写未免过于繁琐，这种工作我们完全可以交给一些脚本程序来完成。后面我们会介绍我们的项目当中的脚本的实现和用途。

##### 4. 第 2 种配置方式

这种配置方式比前面的配置方式略微复杂一些。我们需要使用前面的 SqlSessionHolder 来获取当前线程对应的 SqlSession，然后使用它的方法进行数据库操作。这里我们定义了 BaseDAO，这是一个抽象类:

```java
public abstract class BaseDAO<T extends AbstractPO> implements DAO<T> {

    private static final String POSTFIX_SPLIT = ".";

    private static final String POSTFIX_INSERT = "insert";

    public BaseDAO() {
        entityClass = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected SqlSession getSqlSession() {
        return SqlSessionHolder.getSession();
    }

    protected String getStatementPrefix() {
        return entityClass.getSimpleName() + POSTFIX_SPLIT;
    }

    @Override
    public Long createPO(T entity) throws DAOException {
        try {
            Long start = System.currentTimeMillis();
            getSqlSession().insert(getStatementPrefix() + POSTFIX_INSERT, entity);
            logger.debug("insert cost is :" + (System.currentTimeMillis() - start));
            return entity.getId();
        } catch (Exception e) {
            logger.error(getStatementPrefix() + " createPO", e);
            throw new DAOException(e);
        }
    }

    // ...
}
```

如上所示，这里我们需要先获取到当前 DAO 对应的 Entity 的类名称，然后使用它拼接成映射到 Mapper 的字符串。因此，这里对类名、Mapper 名有一些要求。当然，在这种开发方式中，我们也可以使用 Generator 来生成 Mapper 等各种文件来简化我们开发的复杂度。这种方式的好处就是我们可以在代码中对数据库操作进行包装。

## 3、常用三方库的整合记录

### 3.1 消息队列 RabbitMQ

RabbitMQ 可以用来进行服务器之间的解耦，本质上作用原理是生产者-消费者模式。比如服务器 A 和 B 以及 MQ 服务器，此时 A 发送一个消息到 MQ 服务器，然后 B 监听并获取到了消息之后进行处理。这样服务器 A 和 B 之间没有进行代码上面的耦合而只是通过 MQ 中维护的消息队列进行交互。这同时也意味着 A 和 B 服务器甚至不需要使用同一种语言进行开发。

使用 RabbitMQ 之前需要先安装 ErLang 环境，配置环境变量，然后就可以使用了。参考下面的链接来完成环境配置：

1. ErLang 下载地址：http://www.erlang.org/downloads    
2. RabbitMQ 下载地址：https://www.rabbitmq.com/install-windows.html    
3. RabbitMQ 参考资料：https://www.cnblogs.com/LipeiNet/p/5973061.html

在使用 RabbitMQ 之前需要先添加 RabbitMQ 的依赖：

```xml
<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.amqp</groupId>
    <artifactId>spring-rabbit</artifactId>
</dependency>
```

然后，我们可以使用下面的代码来进行基本的测试：

```java
    private static final String QUEUE_NAME = "rabbitMQ.test";

    @Test
    public void testProducer() throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 RabbitMQ 相关信息
        factory.setHost("localhost");
        // factory.setUsername("lp");
        // factory.setPassword("");
        // factory.setPort(2088);
        // 创建一个新的连接
        Connection connection = factory.newConnection();
        // 创建一个通道
        Channel channel = connection.createChannel();
        // 声明一个队列
        // channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello Rabbit MQ";
        // 发送消息到队列中
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        log.debug("Producer Send +'{}'", message);
        // 关闭通道和连接
        channel.close();
        connection.close();
    }

    @Test
    public void testConsumer() throws IOException, TimeoutException, InterruptedException {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置RabbitMQ地址
        factory.setHost("localhost");
        // 创建一个新的连接
        Connection connection = factory.newConnection();
        // 创建一个通道
        Channel channel = connection.createChannel();
        // 声明要关注的队列
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
        log.debug("Customer Waiting Received messages");
        // DefaultConsumer类实现了Consumer接口，通过传入一个频道，
        // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                log.debug("Customer Received '{}'", message);
            }
        };
        // 自动回复队列应答 -- RabbitMQ中的消息确认机制
        channel.basicConsume(QUEUE_NAME, true, consumer);
        Thread.sleep(10000);
    }
```

以上我们定义了一个生产者和消费者测试方法。当我们在程序中集成 RabbitMQ 的时候实现的方式与之类似。

### 3.2 缓存相关

#### 3.2.1 缓存相关框架对比总结

常用的服务端缓存主要有 Redis、Ehcache 和 Memcached。

Ehcache 与其他两个有明显的不同。Ehcache 与 java 程序是绑在一起的，直接在虚拟机中缓存，速度快，效率高，但是缓存共享麻烦，集群分布式应用不方便。

Redis 是一个独立的程序，我们需要使用 Jedis 客户端，通过 Socket 访问缓存服务，效率比 Ehcache 低，比数据库要快很多，处理集群和分布式缓存方便，有成熟的方案。

Memcached 与 Redis 类似，都是基于键值对的，但是 Redis 支持更多的数据结构。在 Memecached 与 Redis 之间进行选择的时候要基于具体的业务场景：

1. Redis 具有持久化的功能，而 Memcached 不具备持久化功能，重启后数据全部丢失（尽管如此，也不应该让 Redis 完全取代传统数据库，如 MySQL）；
2. 如果键值对需要复杂的数据结构，如哈希、列表、集合、有序集合等的时候，应该使用 Redis。最典型的场景有用户订单列表、用户消息、帖子评论列表等。
3. 存储的内容比较大时，考虑使用 Redis。Memcache 的值要求最大为 1M，如果存储的值很大，只能使用 Redis。
4. 纯键值对存储，数据量非常大，并发量非常大的业务，使用 Memcache 或许更适合。因为Memcache 使用预分配内存池的方式管理内存，能够省去内存分配时间。Redis 则是临时申请空间，可能导致碎片。

因此，如果是单个应用或者对缓存访问要求很高的应用，用 Ehcache。如果是大型系统，存在缓存共享、分布式部署、缓存内容很大的，可以用 Redis 或者 Memecahce。

#### 3.2.2 集成 Redis

我们可以先在 Windows 上面安装 Redis 来进行学习。你可以参考下面这篇来了解如何在 Windows 上面安装 Redis：https://www.cnblogs.com/jaign/articles/7920588.html.

```xml
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
</dependency>
```

然后，为了使用 Redis 进行数据存储，我们需要初始化一个 RedisPool 对象，以用来获取 Jedis 客户端。因此，我们需要做如下的配置：

```xml
    <bean class="redis.clients.jedis.JedisPool">
        <constructor-arg name="poolConfig" ref="jedisPoolConfig"/>
        <constructor-arg name="host" value="${redis.host}"/>
        <constructor-arg name="port" value="${redis.port}"/>
        <constructor-arg name="timeout" value="${redis.timeout}"/>
        <constructor-arg name="password" value="${redis.pass}"/>
    </bean>

    <bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig">
        <property name="maxIdle" value="${redis.maxIdle}"/>
        <property name="maxWaitMillis" value="${redis.maxWait}"/>
        <property name="testOnBorrow" value="${redis.testOnBorrow}"/>
    </bean>

    <context:property-placeholder location="classpath*:configs/redis.properties" ignore-unresolvable="true"/>
```

这里的配置也比较简单，就是初始化一个单例的 JedisPool。这个 JedisPool 对象的属性从 JedisPoolConfig 和 properties 文件两个部分得到。

然后，我们可以使用 JedisPool 来测试我们的 Redis 环境是否搭建成功：

```java
    @Autowired
    private JedisPool jedisPool;

    @Test
    public void testRedisConnection() {
        Jedis jedis = jedisPool.getResource();
        jedis.set("the-key", "the-value");
        String value = jedis.get("the-key");
        Assert.assertEquals(value, "the-value");
    }
```

上面的内容主要是用来集成 Redis 开发环境。经过上面的配置我们已经可以在程序中使用 Redis 来做缓存了。如果要对 SQL 进行缓存中需要通过自定义注解 + AOP 进行拦截即可。

#### 3.2.3 一个与 properties 文件相关的问题：Could not resolve placeholder

这个原因是我们在项目当中引用了多个 properties 文件的原因。我们可以通过在 `<context:property-placeholder>` 标签后面追加一个 `ignore-unresolvable="true"` 属性来避免这个问题。

这个标签的作用是：是否忽略解析不到的属性，如果不忽略，找不到将抛出异常。当 `ignore-unresolvable` 为 true 时，配置文件 `${}` 找不到对应占位符的值不会报错，会直接赋值 `${}`；如果设为 false，会直接报错。
 
这里需要设置它为 true 的主要原因：同个模块中如果引用多个 properties，运行时出现 `Could not resolve placeholder 'key'` 的情况。原因是在加载第一个`context:property-placeholder` 时会扫描所有的 bean，而有的 bean 里面出现第二个 `context:property-placeholder` 引入的 properties 的占位符 `${key2}`，但此时还没有加载第二个 `property-placeholder`，所以解析不了 `${key2}`。

除了追加上面的属性，也可以将多个 properties 文件合并来解决这个问题。

### 3.2.4 Ehcache 集成
Ehcache 的快速集成可以参考官方的相关介绍：https://www.ehcache.org/. 






