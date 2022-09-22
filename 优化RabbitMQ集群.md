# 优化RabbitMQ集群

## 为什么需要优化

### 什么是真正的高可用

- 在传统以物理机/虚拟机为基础的架构中，服务宕机往往需要人工处理
- 随着容器技术的发展，容器编排框架可以很好的解决高可用问题
- K8S已经成为容器编排的事实标准，能够承载RabbitMQ集群

### 网络分区故障

- 在实际生产中，网络分区是非常常见的故障原因
- 网络分区的排查和处理难度较大，需要专门门研究

### RabbitMQ状态监控

- 在生产环境中，需要实时关注RabbitMQ集群状态
- RabbitMQ状态包括流量、内存占用、CPU占用等

## 什么是docker

- 将应用的Libs（函数库）、Deps（依赖）、配置与应用一起打包
- 将每个应用放到一个隔离**容器**去运行，避免互相干扰

## 虚拟机上安装docker

相关依赖包：

```
sudo yum install yum-utils device-mapper-persistent-data lvm2 -y
```

引入docker相关路径：

```
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
```

安装docker

```
sudo yum install docker-ce
```

启动docker

```
systemctl enable docker
systemctl start docker
```

查看docker是否启动，查看版本命令

```
docker -v 
```

如何部署一个镜像，docker官方提供了一个hello-world项目，直接运行即可

```
docker run hello-world
```

![image-20220922163757644](assets/image-20220922163757644.png)

Docker运行RabbitMQ(下载必备依赖，安装，运行，一条命令搞定)

```
docker run -d --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

查看docker启动的服务

```
docker ps
```

![image-20220922164724036](assets/image-20220922164724036.png)

## 使用DockerCompose部署高可用集群

### 什么是Docker Compose

- Compose是用于定义和运行多容器Docker应用程序的工具
- 通过Compose,可以使用YAML文件来配置应用程序需要的所有服务
- 使用一个命令，就可以从YAML文件配置中创建并启动所有服务

### 编写DockerCompose配置文件docker-compose.yml

```
version: "2.0"
services:
    rabbit1:
        image: rabbitmq:3-management
        hostname: rabbit1
        ports:
            - 5672:5672 #集群内部访问的端口
            - 15672:15672 #外部访问的端口
        environment:
            - RABBITMQ_DEFAULT_USER=guest #用户名
            - RABBITMQ_DEFAULT_PASS=guest #密码
            - RABBITMQ_ERLANG_COOKIE='rabbitmq'
    rabbit2:
        image: rabbitmq:3-management
        hostname: rabbit2
        ports:
            - 5673:5672 #集群内部访问的端口
        environment:
            - RABBITMQ_ERLANG_COOKIE='rabbitmq'
        links:
            - rabbit1
    rabbit3:
        image: rabbitmq:3-management
        hostname: rabbit3
        ports:
            - 5674:5672 #集群内部访问的端口
        environment:
            - RABBITMQ_ERLANG_COOKIE='rabbitmq'
        links:
            - rabbit1
            - rabbit2
```

### 安装DockerCompose

安装相关依赖(python包管理器)

```
yum install epel-release
yum install dnf
dnf install python3-pip
python3 -m pip install -U pip(下面命令报错时使用这个，再使用这个)
pip3 install docker-compose
```

安装完成后查看版本

```
docker-compose version
```

![image-20220922175008926](assets/image-20220922175008926.png)

配置docker-compose.yml(位置好像不重要)

```
vi docker-compose.yml
```

启动docker-compose，按照脚本启动集群

```
docker-compose up -d
```

如果启动不成功是因为端口被占用，如：

```
[root@mq01 ~]# docker-compose up -d
/usr/local/lib/python3.6/site-packages/paramiko/transport.py:33: CryptographyDeprecationWarning: Python 3.6 is no longer supported by the Python core team. Therefore, support for it is deprecated in cryptography and will be removed in a future release.
  from cryptography.hazmat.backends import default_backend
Starting root_rabbit1_1 ... 
Starting root_rabbit1_1 ... error

ERROR: for root_rabbit1_1  Cannot start service rabbit1: driver failed programming external connectivity on endpoint root_rabbit1_1 (282f3b2ce8ce43fdd49a0815da8e079cc21a8a9140150a4b89d3b664c9369fe3): Bind for 0.0.0.0:15672 failed: port is already allocated

ERROR: for rabbit1  Cannot start service rabbit1: driver failed programming external connectivity on endpoint root_rabbit1_1 (282f3b2ce8ce43fdd49a0815da8e079cc21a8a9140150a4b89d3b664c9369fe3): Bind for 0.0.0.0:15672 failed: port is already allocated
ERROR: Encountered errors while bringing up the project.
```

先使用docker ps命令查看是否有其他服务启动，关闭即可

```
docker stop rabbitmq
```

启动成功

<img src="assets/image-20220922180753514.png" alt="image-20220922180753514" style="zoom:200%;" />

再次查看启动的服务

<img src="assets/image-20220922181005277.png" alt="image-20220922181005277" style="zoom: 150%;" />

所有salve关联加入集群   

1.进入第二个容器

```
docker exec -it root_rabbit2_1 bash
```

2.停掉服务

```
rabbitmqctl stop_app
```

3.配置rabbit2节点，加入集群

```
rabbitmqctl join_cluster rabbit@rabbit1
```

4.启动rabbitmq

```
rabbitmqctl start_app
```

5.退出

```
exit
```

第三个容器和第二个容器操作相同







