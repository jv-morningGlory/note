Iterable接口是java集合框架中的一个核心接口，它表示实现了此接口的类可以被迭代遍历。

- iterator() 返回一个迭代器


- forEach(Consumer<? super T> action) 可以遍历


- spliterator()反回一个Spliterator  
Spliterator 是 Java 8 中引入的一个接口，用于支持更高效的并行遍历和分割流（Stream）中的元素。Spliterator 可以被看作是 Iterator 的并行版本。



```
public interface Iterable<T> {
    
    Iterator<T> iterator();

    
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }

    
    default Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}





```