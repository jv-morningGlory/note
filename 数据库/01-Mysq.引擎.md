# 常用引擎
InnoDB、MyISAM、MEMORY

- InnoDB: InnoDB给MySQL的表提供了事务处理、回滚、崩溃修复能力和多版本并发控制的事务安全。
- MyISAM: 优势是占用空间少，处理速度快。缺点不支持事务。
- MEMOMY：所有的数据都在内存中，数据的处理速度快，但是安全性不高。如果需要很快的读写速度，对数据的安全性要求较低，可以选择MEMOEY。

一般主从同步，主库用InnoDB,从库用MyISAM。MEMOMY一般用来存储临时表。

