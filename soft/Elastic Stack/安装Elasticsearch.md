# 1.Linux安装ElasticSearch


官网地址 https://www.elastic.co/cn/elasticsearch

![](img/382130123981.png)

## 1.1 下载解压

```
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-8.14.3-linux-x86_64.tar.gz

tar -zxvf elasticsearch-8.14.3-linux-aarch64.tar.gz

```

## 1.2 解决内存问题

直接启动会出现下面得问题差点

![img.png](img/748393212872.png)

可以设置 config/jvm.options,这样就可以了

![img.png](img/983729782333.png)

## 1.3 创建es专用用户

```
adduser es
chown -R es /soft/elasticsearch
su user es

```

## 1.4 修改es核心配置

vi /soft/elasticsearch/config/elasticsearch.yml

- 数据和日志

![img.png](img/274323472344.png)

- 修改绑定得ip允许远程访问

![img.png](img/237123612134.png)

- 初始化节点名称
```
cluster.name: elasticsearch 
node.name: es-node0
cluster.initial_master_nodes: ["es-node0"]

```

## 1.5 vm.max_map_count [65530] is too low问题

```
su root
vi /etc/sysctl.conf

```
在文件中添加下面内容
```
vm.max_map_count=262144

```
退出保存，刷新配置文件
```
sysctl -p

```

## 1.6 max file descriptors [4096]问题

```
[1]: max file descriptors [4096] for elasticsearch process is too low

```
切换到root用户

```
vi /etc/security/limits.conf
```
添加如下内容

```
* soft nofile 65536
* hard nofile 131072
* soft nproc 2048
* hard nproc 4096

```
重启Linux

## 1.7 ES服务的启动与停止

- 前台启动  bin/elasticsearch
- 后台启动  bin/elasticsearch -d

## 1.8 为elasticsearch为用户重新设置密码

bin/elasticsearch-reset-password -u elastic


## 1.9 登录

![img.png](img/842023832385.png)

