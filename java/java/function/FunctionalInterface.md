# @FunctionalInterface  

    @FunctionalInterface  是 Java 8 中引入的一个注解，用于标识一个接口是一个函数式接口。  

    函数式接口是只包含一个抽象方法的接口，通常用于Lambda表达式或方法引用 。  

    该注解是非必需的，但它可以提供编译器级别的检查，确保接口仅包含一个抽象方法。

以下是@FunctionalInterface的主要作用：

- 明确表达意图：通过在接口上使用@FunctionalInterface注解，明确表明这个接口设计用于函数式编程风格，其中只有一个抽象方法。


- 编译器检查：编译器会检查被标注为@FunctionalInterface的接口是否符合函数式接口的规范，即是否仅有一个抽象方法。如果接口包含多个抽象方法，则编译器会报错。


- Lambda表达式和方法引用：函数式接口可以与Lambda表达式和方法引用一起使用，使得代码更加简洁和易读。
    

- 默认方法和静态方法：@FunctionalInterface注解并不限制接口中包含默认方法（default methods）或静态方法（static methods），因为它们不影响接口的函数式特性。
    

   
```

@FunctionalInterface
public interface Consumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t);

    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code Consumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default Consumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }
}

```