# COALESCE 合并

COALESCE 函数用于返回其参数列表中第一个非 NULL 的值。其基本语法如下：

```sql

COALESCE(value1, value2, ..., valueN)

```
实例：

```sql

SELECT COALESCE(middle_name, 'N/A') AS MiddleName
FROM employees;
```
middle_name 如果是null就返回'N/A'