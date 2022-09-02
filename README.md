# 前期准备

## Typora 编辑器 

图片失效问题：点击【文件】→【偏好设置】→【图片插入】→点击下拉列表，选择【复制图片到./assets文件夹】→勾选【优先使用相对路径】

# 中间件

定义：提供**软件**和**软件**之间连接的**软件**，以便于软件各部件之间的沟通

作用：异步处理，系统解耦

额外：消息手机广播，流量控制

总结：

1. ActiveMQ 最“老“，老牌，但维护较慢令 
2. RabbitMQ 最“火“，适合大小公司，各种场景通杀令 
3. RocketMQ 最“猛“，功强，但考验公司运维 
4. Kafka 最“强“，支持超大量数据，但消息可靠性弱

# RabbitMQ

## erlang语言：一门为交换机软件开发诞生的编程语言

特点：

- 通用的面向并发的编程语言，适用于分布式系统

- 基于虚拟机解释运行，跨平台部署

- 进程间上下文切换效率远高于C语言

- 有着和原生Socket一样的延迟

## AMQP协议



![image-20220901123530384](assets/image-20220901123530384.png)

- Broker：接收和分发消息的应用，RabbitMQ就是Message Broker
- Virtual Host：虚拟Broker，将多个单元隔离开 
- Connection：publisher／consumer和broker之间的TCP连接
- Channel：connection内部建立的逻辑连接，通常每个线程创建单独的channel
- Routing Key：路由键，用来指示消息的路由转发， 相当于快递的地址 
- Exchange：交换机，相当于快递的分拨中心 
- Queue:消息队列，消息最终被送到这里等待consumer取走 
- Binding：exchange和queue之间的虚拟连接， 用于message的分发依据 

**AMQP协议直接定义了RabbitMQ的内部结构和外部行为**

**我们使用RabbitMQ本质上是在使用AMQP协议**

**发送者不能直接将消息发送给最终队列，必须发送给交换机**  

## Exchange的作用 

- Exchange是AMQP协议和RabbitMQ的核心组件 
- Exchange的功能是根据绑定关系和路由键为消息提供路由，将消息转发至相应的队列 
- Exchange有4种类型 Direct/Topic/Fanout/Headers, 以前三种为主 

## Exchange主要有3种类型

- Direct（直接路由）:Routing Key=Binding Key，容易配置和使用 

- Fanout（广播路由）：群发绑定的所有队列，适用于消息广播 

- Topic（话题路由）：功能较为复杂，但能降级为Direct, 

### Topit Exchange 

- 根据Routing Key及通配规则，Topic Exchange将消息分发到目标Queue中 
- 全匹配 ：与Direct类似 
- Binding Key中的＃ ：匹配任意个数的word 
- Binding Key中的 * ：匹配任意1个word  

### www.tryrabbitmq.com便于理解的练习网站

## 安装erlang

- 下载并安装Erlang OTP (Open Telecom Platform) 相当于Java的jdk
- 任选其一
- https://www.erlang.org/downloads 
- https://www.erlang-solutions.com/resources/download.html 

## 安装RabbitMQ

下载并安装RabbitMQ : https://www.rabbitmq.com/

“Get Started“->“Download+Installer"->"Windows Installer“->‘'Using the official installer“

安装完成后，查看系统服务中会出现RabbitMQ 

## 网页端管理工具

### 启用前端插件 

D:\RabbitMQ Server\rabbitmq_server-3.8.3\sbin  安装目录下

#### cmd命令：

cd D:\RabbitMQ Server\rabbitmq_server-3.8.3\sbin 跳转至安装目录下

rabbitmq-plugins --help 可以看到帮助文档

rabbitmq-plugins enable rabbitmq_management  启动应用

### 浏览器打开：127.0.0.1:1 5672 

### 默认用户名：guest  默认密码：guest 

# 外卖项目

## 需求分析

- 一个外卖后端系统，用户可以在线下单外卖

- 用户下单后，可以实时查询订单进度

- 系统可以承受短时间的大量并发请求

## 架构设计

- 使用微服务系统，组件之间充分解耦
- 使用消息中间件，解耦业务逻辑
- 使用数据库，持久化业务数据

## 什么是微服务架构

- 将应用程序构建为**松耦合**、可独立部署的一组**服务**
- 服务：一个单个的、可独立部署的软件组件，实现了一些有用的功能
- 松耦合：封装服务的实现细节，通过API调用

## 架构设计

![image-20220901155607633](assets/image-20220901155607633.png)

## 业务流程

![image-20220901121835721](assets/image-20220901121835721.png)

## 接口需求

- 新建订单接口
- 查询订单接口
- 接口采用REST风格

## 数据库设计原则

- 每个微服务使用自己的数据库
- 不要使用共享数据库的方式进行通信
- 不要使用外键，对于数据量非常少的表慎用索引

![image-20220901161627232](assets/image-20220901161627232.png)

## SpringBoot项目搭建

https://start.spring.io/

![image-20220901165827927](assets/image-20220901165827927.png)

依赖下载速度慢的问题：

```
	repositories {
		maven{ url 'http://maven.aliyun.com/nexus/content/groups/public/'}
		mavenCentral()
	}
```

