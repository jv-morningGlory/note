```yml
spring:
  kafka:
    # kafka集群 192.168.2.243:9092,192.168.2.244:9092,192.168.2.245:9092
    bootstrap-servers: 117.72.16.114:9092
    ################### 生产者
    producer:
      key-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      #开启事务，kafka提供了事务支持，允许生产者发送事务性消息，下面就是配置的事务的前缀，实际事务可能是 kafka-producer-1 、 kafka-producer-2
      transaction-id-prefix: kafka-producer
      # 发送消息错误的时候，消息重发的次数，开启事务必须设置大于0
      retries: 3
      # 0: 生产者在成功写入消息之前不会等待任何来自服务器的响应
      # 1：leader 成功写入，生产者就会收到服务器的成功反馈
      # all 、 -1 所有leader和follower都需要成功写入
      acks: all
      # 生产者可以通过批量方式来发送消息，batch-size定义了每个批次中可以包含的最大消息数量
      batch-size: 16384
      # 该函数的含义是当一个batch从创建开始过去linger.ms后，不管该batch满不满都会写入缓存区发送
      properties:
        linger:
          ms: 3000
      # 生产者内存缓冲区的大小。
      buffer-memory: 1024000
    ################### 消费者配置
    consumer:
      # 消费组 id
      group-id: group-1
      # 该属性指定了消费者在读取一个没有偏移量的分区或者偏移量无效的情况下该作何处理
      # earliest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费分区的记录
      auto-offset-reset: latest
      # 是否开启自动提交
      enable-auto-commit: false
      #键的序列化
      key-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      # 值的反序列化方式（建议使用Json，这种序列化方式可以无需额外配置传输实体类）
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserialize
      # 配置消费者的 Json 反序列化的可信赖包，反序列化实体类需要
      properties:
        spring:
          json:
            trusted:
              packages: "*"
      # 这个参数定义了poll方法最多可以拉取多少条消息，默认值为500。如果在拉取消息的时候新消息不足500条，那有多少返回多少；如果超过500条，每次只返回500。
      # 这个默认值在有些场景下太大，有些场景很难保证能够在5min内处理完500条消息，
      # 如果消费者无法在5分钟内处理完500条消息的话就会触发reBalance,
      # 然后这批消息会被分配到另一个消费者中，还是会处理不完，这样这批消息就永远也处理不完。
      # 要避免出现上述问题，提前评估好处理一条消息最长需要多少时间，然后覆盖默认的max.poll.records参数
      # 注：需要开启BatchListener批量监听才会生效，如果不开启BatchListener则不会出现reBalance情况
      max-poll-records: 3
    properties:
      # 两次poll之间的最大间隔，默认值为5分钟。如果超过这个间隔会触发reBalance
      max:
        poll:
          interval:
            ms: 600000
      # 当broker多久没有收到consumer的心跳请求后就触发reBalance，默认值是10s
      session:
        timeout:
          ms: 10000
    listener:
      # 在侦听器容器中运行的线程数，一般设置为 机器数*分区数
      concurrency: 4
      # 自动提交关闭，需要设置手动消息确认
      ack-mode: manual_immediate
      # 消费监听接口监听的主题不存在时，默认会报错，所以设置为false忽略错误
      missing-topics-fatal: false
      # 两次poll之间的最大间隔，默认值为5分钟。如果超过这个间隔会触发reBalance
      poll-timeout: 600000






```