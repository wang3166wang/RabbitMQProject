# 前期准备

## Atom工具

### 配置readme.md文件的本地编辑操作

#### 参考文档：https://www.jianshu.com/p/4c68bb302969

### 摘录如下：

#### 1.在本地电脑上创建一个项目文件夹

#### 2.右键单击文件夹，选择Git Bash Here

#### 3.依次输入命令

git init

git add README.md

git commit -m "first commit"

git remote add origin https://github.com/wang3166wang/RabbitMQProject.git

git push -u origin master

### 用此配置可利用Atom随时提交本地文件到GitHub

## typora 编辑器

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





# 项目架构

![image-20220901121835721](assets/image-20220901121835721.png)

