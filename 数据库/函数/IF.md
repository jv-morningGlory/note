# IF 函数

```sql

IF(condition, true_value, false_value)


```

## 实列

```sql
SELECT IF(salary > 5000, 'High', 'Low') AS SalaryLevel
FROM employees;

```