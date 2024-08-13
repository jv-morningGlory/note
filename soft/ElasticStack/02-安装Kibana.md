# 安装
kibana是和es的版本是有对应要求的，下载地址还是在es的官网可以找到。

# 配置
## 1 配置es地址
config/kibana.yml
```
elasticsearch.hosts: ["http://localhost:9200"] #对应的es服务器的ip地址
i18n.locale: "zh-CN" #将kibana操作界面设置成中文，也可以不设置
```
## 2 配置允许访问kibana的ip地址
config/kibana.yml
```
#server.host: "localhost"
server.host: 0.0.0.0

```

# 启动命令
- 前台运行
```
bin/kibana
```
- 后台运行
```
nohup bin/kibana &
```

