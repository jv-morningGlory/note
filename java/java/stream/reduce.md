Stream流中的 reduce方法
主要是流中的数据做规约操作。

## 不带初始值
```java

Optional<T> reduce(BinaryOperator<T> accumulator);


```

## 带有初始值

```java

T reduce(T identity, BinaryOperator<T> accumulator);

```

## 并行规约

```java

<U> U reduce(U identity,
                 BiFunction<U, ? super T, U> accumulator,
                 BinaryOperator<U> combiner);

```