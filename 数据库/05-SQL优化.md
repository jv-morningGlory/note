# 尽量避免使用子查询

由于MySQL的优化器对于子查询的处理能力比较弱，所以不建议使用子查询，可以改写成Inner Join，之所以 join 连接效率更高，是因为 MySQL不需要在内存中创建临时表。

```sql
        DELETE FROM
                   index_trend
        WHERE EXISTS (
                        SELECT 1 FROM
                                     index_info
                        WHERE index_info.id = index_trend.index_id AND index_info.source_system = '0'
                       
        );

```

```sql
		
DELETE index_trend 
FROM
	index_trend
	INNER JOIN index_info ON index_info.id = index_trend.index_id 
WHERE
	index_info.source_system = '0';
```

# 用IN来替换OR

- 低效查询

```  
SELECT * FROM t WHERE id = 10 OR id = 20 OR id = 30;
```
- 高效

```
select id from table_name where num in(1,2,3)

```

对于连续的数值，能用 between 就不要用 in 了；再或者使用连接来替换

# 读取适当的记录LIMIT M,N，而不要读多余的记录

     

select id,name from t limit 866613, 20

使用上述sql语句做分页的时候，可能有人会发现，随着表数据量的增加，直接使用limit分页查询会越来越慢。

# 禁止不必要的Order By排序

如果我们对结果没有排序的要求，就尽量少用排序；如果排序字段没有用到索引，也尽量少用排序；另外，分组统计查询时可以禁止其默认排序
```
SELECT goods_id,count(*) FROM t GROUP BY goods_id;
```
默认情况下，Mysql会对所有的GROUP BT col1,col2…的字段进行排序，也就是说上述会对 goods_id进行排序，如果想要避免排序结果的消耗，可以指定ORDER BY NULL禁止排序：

```
SELECT goods_id,count(*) FROM t GROUP BY goods_id ORDER BY NULL
```

# 总和查询可以禁止排重用union all

union和union all的差异主要是前者需要将结果集合并后再进行唯一性过滤操作，这就会涉及到排序，增加大量的CPU运算，加大资源消耗及延迟。


当然，union all的前提条件是两个结果集没有重复数据。所以一般是我们明确知道不会出现重复数据的时候才建议使用 union all 提高速度。


# 只返回必要的列，用具体的字段列表代替 select * 语句


# 区分in和exists

- in : 只执行一次

确定给定的值是否与子查询或列表中的值相匹配。

in 在查询的时候，首先查询子查询的表，然后将内表和外表做一个笛卡尔积，然后按照条件进行筛选。

所以相对内表比较小的时候，in 的速度较快。

- exists： 执行n次（外表行数）

指定一个子查询，检测行的存在。

遍历循环外表，检查外表中的记录有没有和内表的的数据一致的。

匹配得上就放入结果集。


in 和 exists 的区别： 如果子查询得出的结果集记录较少，主查询中的表较大且又有索引时应该用 in， 反之如果外层的主查询记录较少，子查询中的表大，又有索引时使用 exists。

其实我们区分 in 和 exists 主要是造成了驱动顺序的改变（这是性能变化的关键），如果是 exists，那么以外层表为驱动表，先被访问，如果是 in ，那么先执行子查询，所以我们会以驱动表的快速返回为目标，那么就会考虑到索引及结果集的关系 ，另外 in 是不对 NULL 进行处理。

in 是把外表和内表作 hash 连接，而 exists 是对外表作 loop 循环，每次 loop 循环再对内表进行查询。一直以来认为 exists 比 in 效率高的说法是不准确的。


# not in 和  not 

如果查询语句使用 not in，那么内外表都进行全表扫描，没有用到索引；

而 not exists 的子查询依然能用到表上的索引。所以无论那个表大，用 not exists 都比not in 要快。


# 优化group by 语句

使用where子句替换Having子句：避免使用having子句，having只会在检索出所有记录之后才会对结果集进行过滤，这个处理需要排序分组，如果能通过where子句提前过滤查询的数目，就可以减少这方面的开销

# 优化JOIN语句

## 当连接查询没有where条件时
- left join 前面的表是驱动表，后面的表是被驱动表
- right join 后面的表是驱动表，前面的表是被驱动表
- inner join / join 会自动选择表数据比较少的作为驱动表


性能优化，left join 是由左边决定的，左边一定都有，所以右边是我们的关键点，建立索引要建在右边。当然如果索引是在左边的，我们可以考虑使用右连接

Tips：Join左连接在右边建立索引；组合索引则尽量将数据量大的放在左边，在左边建立索引。

# 如何避免索引失效

- 最佳左前缀法则
- 不在索引列上做任何操作
- 尽量使用覆盖索引
- 在组合/联合索引中，将有区分度的索引放在前面
- is null, is not null 也无法使用索引，在实际中尽量不要使用null（避免在 where 子句中对字段进行 null 值判断） 不过在mysql的高版本已经做了优化，允许使用索引