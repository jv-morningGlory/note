# IFNULL
## 语法
```sql
IFNULL(expr1,expr2)
```
## 功能
如果expr1不是NULL，IFNULL()返回expr1，否则它返回expr2。IFNULL()返回一个数字或字符串值，取决于它被使用的上下文。
## 示例
```sql
SELECT IFNULL(NULL,10); -- 10
SELECT IFNULL(10,20); -- 10
SELECT IFNULL(1/0,10); -- 10
SELECT IFNULL(1/1,10); -- 1
SELECT IFNULL(NULL,1/0); -- NULL
