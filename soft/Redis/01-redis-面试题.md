# 1. 数据类型
String,Hash,List,Set,Sorted Set

# 2. 一个字符串类型的值能存储最大容量是多少
512M

# 3. Redis如何做持久化的？能说一下RDB和AOF的实现原理吗？

- RDB持久化：能够在指定的时间间隔能对你的数据进行快照存储。
- AOF持久化：记录每次对服务器写的操作，当服务器重启的时候会重新执行这些命令来恢复原始的数据，AOF命令以redis协议追加保存每次写的操作到文件末尾。Redis还能对AOF文件进行后台重写，使得AOF文件的体积不至于过大。
- 不使用持久化：如果你只希望你的数据在服务器运行的时候存在，你也可以选择不使用任何持久化方式。
- 同时开启RDB和AOF：你也可以同时开启两种持久化方式，在这种情况下当redis重启的时候会优先载入AOF文件来恢复原始的数据，因为在通常情况下AOF文件保存的数据集要比RDB文件保存的数据集要完整。

# RDB持久化

RDB(Redis Database)持久化是把当前内存数据生成快照保存到硬盘的过程，触发RDB持久化过程分为手动触发和自动触发。
- 手动触发
手动触发对应save命令，会阻塞当前Redis服务器，直到RDB过程完成为止，对于内存比较大的实例会造成长时间阻塞，线上环境不建议使用。
- 自动触发


https://github.com/CoderLeixiaoshuai/java-eight-part/blob/master/docs/redis/%E4%B8%80%E5%BC%A0%E5%9B%BE%E6%90%9E%E6%87%82Redis%E7%BC%93%E5%AD%98%E9%9B%AA%E5%B4%A9%E3%80%81%E7%BC%93%E5%AD%98%E7%A9%BF%E9%80%8F%E3%80%81%E7%BC%93%E5%AD%98%E5%87%BB%E7%A9%BF.md


  