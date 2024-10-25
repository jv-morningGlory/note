# kafka tool
kafka tool 是kafka的一个可视化工具

官网地址：http://www.kafkatool.com/download.html

![](img/89342342.png)


# 链接

![](img/743892011.png)

Cluster name : 集群的名字

Bootstrap servers: 这个地方对应的ip和地址，需要在kafka中config下
server.properties中去配置

![](img/472817473.png)
listeners和advertised listeners 区别：

listeners：定义 Kafka 服务器监听的地址和端口，用于接受客户端的连接。可以设置为一个或多个地址。

advertised listeners ： 定义 Kafka 服务器向外部客户端通告的地址和端口。这个配置用于让客户端知道如何连接到 Kafka 代理，尤其是在代理有多个网络接口或在集群环境中。

# 使用 

![](img/89327263.png)

解决乱码

![img_1.png](img/8239174672.png)


