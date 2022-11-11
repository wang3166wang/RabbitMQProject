# redis安装

## 1.下载源码

```
wget http://download.redis.io/releases/redis-6.0.8.tar.gz
```

## 2.移动安装包至/usr/local目录下

```
mv redis-6.0.8.tar.gz /usr/local/
```

## 3.解压

```
tar xzf redis-6.0.8.tar.gz
```

## 4.进入redis解压目录中

```
make
```

## 5.测试

编译完成后redis-6.0.8 的 **src** 目录下会出现编译后的 redis 服务程序 redis-server，还有用于测试的客户端程序 redis-cli

下面在启动 redis 服务：

```
./redis-server
```

## 6.修改密码

Redis默认是没有提供密码的，通过修改配置文件可以打开访问控制。编辑redis.conf可以启动认证。

```
vim /usr/local/redis-6.0.8/redis.conf
```

```
#requirepass foobared
requirepass Wang19950730
```

7.修改后台启动

```
vim /usr/local/redis-6.0.8/redis.conf
```

```
daemonize yes
```

8.查看进程

```
ps -ef|grep redis
```

