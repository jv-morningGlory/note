# CASE WHEN

在 MySQL 中，CASE WHEN 语句用于进行条件判断和选择性返回值，类似于 IF 但更灵活。它可以用于 SQL 查询中的 SELECT、ORDER BY 和
WHERE 子句。

## 语法

```sql
CASE
    WHEN condition1 THEN result1
    WHEN condition2 THEN result2
    ...
    ELSE result
END;
```

- condition1, condition2, ...: 各种条件表达式。
- result1, result2, ...: 对应条件为真时返回的结果。
- result: 如果没有条件为真，则返回的默认结果。

## 示例

### 1.基本用法

```sql
SELECT name,
       CASE
           WHEN salary > 5000 THEN 'High'
           WHEN salary BETWEEN 3000 AND 5000 THEN 'Medium'
           ELSE 'Low'
           END AS SalaryLevel
FROM employees;
```

### 2.与order by结合使用

```sql
SELECT name, salary
FROM employees
ORDER BY CASE
             WHEN salary > 5000 THEN 1
             WHEN salary BETWEEN 3000 AND 5000 THEN 2
             ELSE 3
             END;
```

### 3.在 WHERE 子句中使用

```sql
SELECT name, salary
FROM employees
WHERE
    CASE
        WHEN department = 'Sales' THEN salary > 3000
        ELSE salary > 2000
    END;

 ```


