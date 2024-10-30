# Mysql数据库的事务是怎么实现的
再事务的实现机制上，Mysql采用的WAL：write-ahead logging, 预写日志。

# redo log

redo log叫做重做日志，是用来实现事务的持久性。该日志文件由两部分组成：
- 重做日志缓冲（redo log buffer）
- 重做日志文件（redo log）

前者是在内存中，后者在磁盘中

```
start transaction;
select balance from bank where name="zhangsan";// 生成 重做日志 balance=600
update bank set balance = balance - 400; // 生成 重做日志 amount=400
update finance set amount = amount + 400;
commit;

```

![alt text](img/02-001.png)

mysql 为了提升性能不会把每次的修改都实时同步到磁盘，而是会先存到Boffer Pool(缓冲池)里头，把这个当作缓存来用。然后使用后台线程去做缓冲池和磁盘之间的同步。

如果还没来的同步的时候宕机或断电了怎么办, redo log持久化到磁盘。

# undo log

undo log 叫做回滚日志，用于记录数据被修改前的信息。他正好跟前面所说的重做日志所记录的相反，重做日志记录数据被修改后的信息。undo log主要记录的是数据的逻辑变化，为了在发生错误时回滚之前的操作，需要将之前的操作都记录下来，然后在发生错误时才可以回滚。

![alt text](img/02-001.png)



https://www.cnblogs.com/jingdongkeji/p/17832295.html
https://cloud.tencent.com/developer/article/1431307