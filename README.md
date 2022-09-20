# 前期准备

## Typora 编辑器 

图片失效问题：点击【文件】→【偏好设置】→【图片插入】→点击下拉列表，选择【复制图片到./assets文件夹】→勾选【优先使用相对路径】

# git教程

https://www.runoob.com/git/git-tutorial.html

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

### 浏览器打开：127.0.0.1:15672 

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

依赖下载速度慢的问题：（报错 先暂时不用）

```
	repositories {
		maven{ url 'http://maven.aliyun.com/nexus/content/groups/public/'}
		mavenCentral()
	}
```

# 细节注意

## 1.金额要使用BigDecimal类型，double和float类型都会存在精度失真

## 2.微服务通讯之间尽量使用包装类，基础类容易为null

## 3.声明的是自己监听的队列，不用管别人的队列 

## 4.对于频繁创建与销毁的线程，必须使用线程池，否则极易线程溢出，造成“线程爆炸“ 

## 5.POJO类单一职责 

- 各种POJO数据结构必须单一职责，混用会导致代码混乱 
- P0/DO :(Persistent Object/Data Object）持久对象 
- DTO :(Data丁ransfer Object）数据传输对象 
- BO:(Business Object）业务对象 
- V0: (ViewObject)显示层对象 

# 目前项目的不足

## 1.消息真的发出去了吗？

- 消息发送后，发送端不知道RabbitMQ是否真的收到消息
- 若RabbitMQ异常，消息丢失后，订单处理流程停止，业务异常
- 需要RabbitMQ发送端**确认机制**，确认消息发送

## 什么是发送端确认机制？

- 消息发送后，若中间件收到消息，会给发送端一个应答 
- 生产者接收应答，用来确认这条消息是否正常发送到中间 件 

### 解决方法：

三种确认机制：单条同步确认，多条同步确认（不推荐），异步确认（不推荐）

### 开启方法:

- 配置channel，开启确认模式 channel.confirmSelect() 
- 每发送一条消息，调用channel.waitForConfirms(）方法,等待确认 

## 2.消息真的被路由了吗？

- 消息发送后，发送端不知道消息是否被正确路由，若路由异常，消息会被丢弃 
- 消息丢弃后，订单处理流程停止，业务异常 
- 需要使用RabbitMQ**消息返回机制**，确认消息被正确路由 

### 解决方法：

### 消息返回机制原理

- 消息发送后，中间件会对消息进行路由 
- 若没有发现目标队列，中间件会通知发送方 
- Return Listener会被调用 

### 开启方法:

- 在RabbitMQ基础配置中有一个关键配置项Mandatory 
- Mandatory若为false, RabbitMQ将直接丢弃无法路由的消息 
- Mandatory若为true, RabbitMQ才会处理无法路由的消息 

```
channel.basicPublish("exchange.order.restaurant", "key.order", """"true"""" ,null, messageToSend.getBytes());
```

## 3.消费端处理的过来吗？

- 业务高峰期，可能出现发送端与接收端性能不一致，大量消息被同时推送给接收端，造成接收端服务崩溃 
- 需要使用RabbitMQ**消费端限流机制**，限制消息推送速度， 保障接收端服务稳定 

### 解决方法：

#### RabbitMQ - QoS——服务质量保证

- QoS功能保证了在一定数目的消息未被确认前，不消费新的消息
- QoS功能的前提是不使用自动确认

```
channel.basicAck(message.getEnvelope().getDeliveryTag(),""""""false""""")
```

ture:确认多条，flase:确认单条

## 4.消费端处理异常怎么办？ 

- 默认情况下，消费端接收消息时，消息会被自动确认（ACK) 
- 消费端消息处理异常时，发送端与消息中间件无法得知消息处理情况 
- 需要使用RabbitMQ**消费端确认机制**，确认消息被正确处理 

## 5队列爆满怎么办？ 

- 默认情况下，消息进入队列，会永远存在，直到被消费 
- 大量堆积的消息会给RabbitMQ产生很大的压力 
- 需要使用RabbitMQ**消息过期时间**，防止消息大量积压 

## 6.如何转移过期消息？ 

- 消息被设置了过期时间，过期后会直接被丢弃 
- 直接被丢弃的消息，无法对系统运行异常发出警报 
- 需要使用RabbitMQ**死信队列**，收集过期消息，以供分析 

# Springboot集成RabbitMQ

## Spring AMQP特性 

- 异步消息监听容器 
- 原生提供Rabbitlemplate，方便收发消息 
- 原生提供RabbitAdmin，方便队列、交换机声明 
- Spring Boot Config原生支持RabbitMQ 

## 异步消息监听容器 

原始实现：自己实现线程池、回调方法，并注册回调方法 
SpringBoot ：自动实现可配置的线程池，并自动注册回调方法，只需实现回调方法 

## RabbitAdmin

声明式提供队列、交换机、绑定关系的注册方法

甚至不需要显示的注册代码

## Spring Boot Config

充分发挥Spring Boot约定大于配置的特性

可以隐士建立Connection、channel

## RabbitAdmin声明式配置 

- 将Exchanges Queues Binding声明为Bean 
- 再将RabbitAdmin声明为Bean 
- Exchanges Queue Binding即可自动创建 

## RabbitAdmin声明式配置的优点 

- 将声明和创建工作分开，解藕多人工作 
- 不需显式声明，减少代码量，减少Bug 

## RabbitTemplate 

- Rabbitlemplate与Restlemplate类似，使用了模板方法设计模式 
- RabbitTemplate提供了丰富的功能，方便消息收发 
- RabbitTemplate可以显式传入配置也可以隐式声明配置

## SimpleMessageListenerContainer 简单消息监听容器 

- 设置同时监听多个队列、自动启动、自动配置RabbitMQ 
- 设置消费者数量（最大数量、最小数量、批量消费） 
- 设置消息确认模式、是否重回队列、异常捕获
- 设置是否独占、其他消费者属性等
- 设置具体的监听器、消息转换器等
- 支持动态设置，运行中修改监听器配置

# RabbitMQ集群

## 使用集群的好处

### 1.扩展规模

- 一般的基础架构中，单机扩容（Scale-Up）很难实现
- 需要扩容时尽量使用扩展数量实现（Scale-Out）
- RabbitMQ集群可以方便地通过Scale-Out扩展规模

### 2.数据冗余 

- 对于单节点RabbitMQ，如果节点宕机，内存数据丢失 
- 对于单节点RabbitMQ，如果节点损坏，磁盘数据丢失 
- RabbitMQ集群可以通过镜像队列，将数据冗余至多个节点 

### 3.高可用

- 如果单节点RabbitMQ宕机，服务不可用
- RabbitMQ集群可以通过负载均衡，将请求转移至可用节点

## RabbitMQ集群原理 

- 多个RabbitMQ单节点，经过配置组成RabbitMQ集群 
- 集群节点之间共享元数据，不共享队列数据（默认） 
- RabbitMQ节点数据互相转发，客户端通过单一节点可以访问所有数据 

![image-20220917170938708](assets/image-20220917170938708.png)

## RabbitMQ集群搭建步骤

- 设置主机名或host,使得节点之间可以通过名称访问
- 安装RabbitMQ单节点
- 复制Erlang cookie
- 启动RabbitMQ并组成集群

## 基于CENT OS 7.9系统

虚拟机中网络情况

![image-20220920111809728](assets/image-20220920111809728.png)

虚拟机启动后ping 百度测试网络

![image-20220920111953033](assets/image-20220920111953033.png)

设置固定IP地址

输入如下命令：

```
vi /etc/sysconfig/network-scripts/ifcfg-ens33
```

![image-20220920112545832](assets/image-20220920112545832.png)

为了使地址生效，需要重新启动网络配置，输入：

```
service network restart
```

修改ip后发现ping不同百度，怀疑是DNS配置的问题，用vim打开文件 resolv.conf

```
vi /etc/resolv.conf
```

其中增加DNS配置

```css
nameserver 8.8.8.8
nameserver 114.114.114.114
```

成功ping通 百度

关闭防火墙

```
systemctl stop firewalld.service           	但是开机之后还会启动防火墙
systemctl disable firewalld.service        	禁止firewall开机启动
systemctl status firewalld					查看是否关闭
```

## [Centos8安装最新版本RabbitMQ和erlang](https://blog.csdn.net/ydl1988915/article/details/116167274)

安装完后查看是否启动成功

```
systemctl status rabbitmq-server
```

![image-20220920122148659](assets/image-20220920122148659.png)

修改主机名

```
hostnamectl set-hostname mq01.localdomain
```

将主机名配置到hosts文件当中，使得三台主机名可互相访问

```
192.168.193.201
192.168.193.202
192.168.193.203
```

```
vi /etc/hosts
```

```
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4 mq01
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6 mq01

192.168.193.201 mq01
192.168.193.202 mq02
192.168.193.203 mq03
```

配置完三台主机后互相ping一哈 

```
ping mq01
```

修改.erlang.cookie权限

```
chmod 777 /var/lib/rabbitmq/.erlang.cookie
```

将mq01主节点的.erlang.cookie文件传输至集群所有节点

```
scp /var/lib/rabbitmq/.erlang.cookie root@mq02:/var/lib/rabbitmq
scp /var/lib/rabbitmq/.erlang.cookie root@mq03:/var/lib/rabbitmq
```

复原01节点上.erlang.cookie权限

```
chmod 400 /var/lib/rabbitmq/.erlang.cookie
```

所有主机都打开mq服务，

然后执行

```
rabbitmqctl stop_app
```

将三个机器的mq暂停

在02和03节点上执行节点加入，并启动

```
rabbitmqctl join_cluster --ram rabbit@mq01
```

```
rabbitmqctl start_app
```

添加mq账户（guest账户默认不能访问管控台）

```
rabbitmqctl add_user test test
```

给test账户打上管理员标签

```
rabbitmqctl set_user_tags test administrator
```

附上所有权限

```
rabbitmqctl set_permissions -p / test ".*" ".*" ".*"
```

然后就可以使用test账户登录

http://192.168.193.201:15672

![image-20220920164148586](assets/image-20220920164148586.png)

此时mq集群搭建完毕

目前只是解决了可扩展性

![image-20220920164707388](assets/image-20220920164707388.png)

## 集群镜像队列设置方法

◆ 搭建集群
◆ 使用set *policy 命令设置镜像队列策略

![image-20220920165212412](assets/image-20220920165212412.png)
rabbitmqctl set* policy [-p Vhost] Name Pattern Definition [Priority]

rabbitmqctl set_ policy Vhost 策咯名称 正则表达式 策略定义 优先级

#### Definition:策略定义

- ha-mode:指明镜像队列的模式

​				all:表示在集群中所有的节点上进行镜像

​				exactly:表示在指定个数的节点上进行镜像，节点的个数由ha-params指定

​				nodes:表示在指定的节点上进行镜像，节点名称通过ha-params指定

- ha-params: ha-mode模式需要用到的参数

- ha-svnc-mode:进行队列中消息的同步方式，有效值为**automatic**和**manual**

#### 设置镜像队列策略案例：

```
匹配所有队列，并将镜像配置到集群中的所有节点
rabbitmqctl set_policy ha-all "^" '{"ha-mode":"all"}'
```

```
名称以"two"开始的队列镜像到群集中的任意两个节点
rabbitmqctl set_policy ha-two "^two." '{"ha-mode" :"exactly" ,"ha-params":2,"ha-sync-mode":" automatic"}'
```

```
以"node"开头的队列镜像到集群中的特定节点
rabbitmqctl set_policy ha-nodes "^nodes." '{"ha-mode":"nodes","ha-params":["rabbit@nodeA","rabbit@nodeB"]}'
```

主节点上使用第一种策略

```
[root@mq01 ~]# rabbitmqctl set_policy ha-all "^" '{"ha-mode":"all"}'
Setting policy "ha-all" for pattern "^" to "{"ha-mode":"all"}" with priority "0" for vhost "/" ...
```

![image-20220920170519175](assets/image-20220920170519175.png)

![image-20220920170535893](assets/image-20220920170535893.png)

**以上RabbitMQ集群+镜像队列解决了 数据冗余的 问题**

## HAproxy+ Keepalived高可用集群搭建

实现高可用的方式

**1.客户端的负载均衡**

![image-20220920171016746](assets/image-20220920171016746.png)

客户端负载均衡设置方法

直接在SpringBoot配置文件中设置多个地址

```
spring.rabbitmq.addresses= 127.0.0.1, 127.0.0.2, 127.0.0.3
```

客户端可以自动的负载均衡，一个挂了，数据不丢失，服务继续跑，业务不中断

**2.服务端的负载均衡**

在只能连接1个ip地址，无法连接多个时的情况下

![image-20220920171900631](assets/image-20220920171900631.png)

## HAProxy简介

- HAProxy是一款提供高可用性、负载均衡以及基于TCP和HTTP应用的代理软件
- HAProxy适用于那些负载较大的web站点
- HAProxy可以支持数以万计的并发连接

HAProxy配置方法

```
建一台虚拟机进行搭建HAProxy
编辑hosts，使得haproxy能够通过主机名访问集群节点
vi /etc/hosts
192.168.193.201 mq01
192.168.193.200 mq02
192.168.193.203 mq03
```

```
yum install haproxy -y
```

 编辑haproxy配置文件

```
vi /etc/haproxy/haproxy.cfg
```

```
global
    # 日志输出配置、所有日志都记录在本机，通过 local0 进行输出
    log 127.0.0.1 local0 info
    # 最大连接数
    maxconn 4096
    # 守护模式
    daemon
# 默认配置
defaults
    # 应用全局的日志配置
    log global
    # 使用4层代理模式，7层代理模式则为"http"
    mode tcp
    # 日志类别
    option tcplog
    # 不记录健康检查的日志信息
    option dontlognull
    # 3次失败则认为服务不可用
    retries 3
    # 每个进程可用的最大连接数
    maxconn 2000
    # 连接超时
    timeout connect 5s
    # 客户端超时
    timeout client 120s
    # 服务端超时
    timeout server 120s
# 绑定配置
listen rabbitmq_cluster
    # 绑定端口，需要不被占用的端口
    bind :5671
    # 配置TCP模式
    mode tcp
    # 采用加权轮询的机制进行负载均衡
    balance roundrobin
    # RabbitMQ 集群节点配置
    server master master:5672 check inter 5000 rise 2 fall 3 weight 1
    server salve1 salve1:5672 check inter 5000 rise 2 fall 3 weight 1
    server salve2 salve2:5672 check inter 5000 rise 2 fall 3 weight 1
    server salve3 salve3:5672 check inter 5000 rise 2 fall 3 weight 1
# 配置监控页面
listen monitor
    bind *:8100
    mode http
    option httplog
    stats enable
    stats uri /rabbitmq
    stats refresh 5s
```

设置seLinux

```
sudo setsebool -P haproxy_connect_any=1
```

关闭防火墙

```
systemctl stop firewalld.service
systemctl disable firewalld.service
```

启动haproxy

```
systemctl start haproxy
```

进入web监控界面

http://192.168.193.201:8100/rabbitmq

![image-20220920173304062](assets/image-20220920173304062.png)

问题：HaProxy挂了怎么办?

- 再用一层负载均衡，不能解决问题，因为最后一层机器永远有风险
- 使用Virtual IP (VIP， 虚拟IP) 解决问题

## Keepalived简介

- 高性能的服务器高可用或热备解决方案
- 主要来防止服务器单点故障的发生问题
- 以VRRP协议为实现基础，用VRRP协议来实现高可用性

![image-20220920173641815](assets/image-20220920173641815.png)

keepalived配置(两个节点都需要)

- 1.安装keepalived

```
yum install keepalived -y
```

- 2.编辑keepalived配置文件

```
vi /etc/keepalived/keepalived.conf
```

主机配置文件：(主节点)

```
! Configuration File for keepalived
global_defs {
    router_id master
    vrrp_skip_check_adv_addr
    vrrp_strict
    vrrp_garp_interval 0
    vrrp_gna_interval 0
}
vrrp_script chk_haproxy {
    script "/etc/keepalived/haproxy_check.sh" ##执行脚本位置
    interval 2 ##检测时间间隔
    weight -20 ##如果条件成立则权重减20
}
vrrp_instance VI_1 {
    # 配置需要绑定的VIP和本机的IP
    state MASTER
    interface ens33
    virtual_router_id 28
    # 本机IP
    mcast_src_ip 192.168.193.201
    priority 100
    advert_int 1
    # 通信之间设置的用户名和密码
    authentication {
        auth_type PASS
        auth_pass 123456
    }
    # 虚拟IP
    virtual_ipaddress {
        192.168.193.238
    }
    # 健康检查
    track_script {
        chk_haproxy
    }
}
```

热备机配置文件：(从节点)

```
global_defs {
    router_id salve1
    vrrp_skip_check_adv_addr
    vrrp_strict
    vrrp_garp_interval 0
    vrrp_gna_interval 0
}
vrrp_script chk_haproxy {
    script "/etc/keepalived/haproxy_check.sh" ##执行脚本位置
    interval 2 ##检测时间间隔
    weight -20 ##如果条件成立则权重减20
}
vrrp_instance VI_1 {
    state BACKUP
    interface ens33
    virtual_router_id 28
    mcast_src_ip 192.168.193.201
    priority 50
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass 123456
    }
    virtual_ipaddress {
        192.168.193.238
    }
    track_script {
        chk_haproxy
    }
}
```

- 3.健康检测脚本

```
vi /etc/keepalived/haproxy_check.sh
```

```
#!/bin/bash
COUNT=`ps -C haproxy --no-header |wc -l`
if [ $COUNT -eq 0 ];then
    systemctl start haproxy
    sleep 2
    if [ `ps -C haproxy --no-header |wc -l` -eq 0 ];then
        systemctl stop keepalived
    fi
fi
```

修改健康检测脚本执行权限

```
chmod +x /etc/keepalived/haproxy_check.sh
```

启动keepalived

```
systemctl start keepalived
```

启动后发现主节点ip中多了一个虚拟ip，而从节点没有
主节点ip：

![image-20220920174418972](assets/image-20220920174418972.png)

从节点ip：
![image-20220920174448506](assets/image-20220920174448506.png)

做故障转移实验时，关闭keepalived即可

```
systemctl stop keepalived
```

关闭主节后发现从节点ip中多了一个虚拟ip，而主节点虚拟ip消失了
主节点ip：

![image-20220920174706680](assets/image-20220920174706680.png)

从节点ip：

![image-20220920174733156](assets/image-20220920174733156.png)

总结

- RabbitMQ集群 + 镜像队列 + HAproxy + Keepalived可以同时解决RabbitMQ的可扩展、数据冗余、高可用.
- 在使用客户端负载均衡时，可以省去HAproxy+ Keepalived

## RabbitMQ集群间通信

问题：如果两个集群间处于异地，需要通讯会有以下问题

- 由于异地网络延时，异地RabbitMQ和业务应用之间很难建立网络连接
- 由于异地网络延时，异地RabbitMQ之间很难建立集群
- 此时如果异地RabbitMQ之间需要共享消息，需要使用集群间通信机制

**RabbitMQ集群间通信方法**

Federation(联邦)简介：

- 通过AMQP协议，使用一个内部交换机，让原本发送到一个集群的消息转发至另一个集群
- 消息可以从交换机转发至交换机，也可以由队列转发至队列
- 消息可以单向转发，也可以双向转发

Federation设置方法

启用Federation插件

```
rabbitmq-plugins enable rabbitmq_federation_management
```

使用管控台具体配置Federation

具体使用的时候再查文档

◆Shovel (铲子)

Federation简介：

- Shovel可以持续地从一 个broker拉取消息转发至另一个broker
- Shovel的使用较为灵活，可以配置从队列至交换机从队列至队列，从交换机至交换机

Shovel设置方法
启用插件

```
rabbitmq-plugins enable rabbitmq *shovel* management
```

使用管控台具体配置Shovel

具体使用的时候再查文档

**总结**

- Federation和Shovel都是在broker之间转发/共享消息的方法
- Federation只能在交换机之间或者队列之间转发消息
- Shovel更加灵活，可以在交换机和队列之间转发消息

## 实际开发经验

- 体系架构升级的根本原因是需求
- 不要盲目升级更高级的架构，更高级的架构意味着对运维有更高的要求
- 多思考架构拓扑，形成更好的架构思维

## 小结

- 为了追求规模的扩展性，搭建RabbitMQ集群
- 为了追求数据的冗余，使用RabbitMQ集群镜像队列
- 为了RabbitMQ服务高可用，使用了服务端的负载均衡技术
- 为了跨地域传送消息，学习了跨broker通信技术
