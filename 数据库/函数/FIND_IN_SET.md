# FIND_IN_SET

FIND_IN_SET 是 MySQL 提供的一个函数，用于在一个由逗号分隔的字符串中查找特定的值。它可以用来检测某个值是否存在于给定的列表中，并返回该值的位置（从 1 开始），或者如果没有找到，则返回 0。

## 语法
```sql
FIND_IN_SET(str, str_list)
```
- `str`：要查找的字符串。
- `str_list`：一个由逗号分隔的字符串列表。

## 返回值

- 如果找到 `str`，则返回其在 `str_list` 中的位置（从 1 开始）。
- 如果没有找到 `str`，则返回 0。
- 如果 `str_list` 为空字符串或 NULL，则返回 0。

## 示例
假设有一个名为 `users` 的表，其中有一个名为 `interests` 的列，该列包含一个由逗号分隔的字符串，表示用户的兴趣。
```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    interests VARCHAR(255)
);
```
插入一些示例数据：
```sql
INSERT INTO users (name, interests) VALUES
    ('Alice', 'reading, swimming, hiking'),
    ('Bob', 'reading, cooking, painting'),
    ('Charlie', 'swimming, hiking, photography');
```
使用 `FIND_IN_SET` 函数查找特定兴趣的用户：
```sql
SELECT name, interests
FROM users
WHERE FIND_IN_SET('reading', interests) > 0;
```




