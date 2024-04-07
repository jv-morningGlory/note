## 定义
Consumer是Java8中的一个函数式接口，它位于java.util.function。
定义了2个方法， accept()、andThen(Consumer consumer)。主要作用式消费接受一个参数，不返回任何结果。  
Consumer是一个消费者，可以对给定的对象执行某些操作，但不产生任何结果。

```
@FunctionalInterface
public interface Consumer<T> {

    
    void accept(T t);


    default Consumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }
}

```

## 用法
主要的思想就是，设计代码的时候明白自己是***生产者***还是***消费者***，  
如果是***生产者***就不应该关系***消费者***的消费逻辑，在方法的入参需要一个Consumer类，在方法中调用Consumer.accept()。

- forEach  
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
  在Iterable中定义了一个forEach方法，方法接受一个Consumer类，由使用者传入。使用者在Consumer中怎么消费，forEach并不关心。

    ```
    
    public static void main(String[] args) {

        List<String> names = new ArrayList<>();
        
        names.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println();
            }
        });
        
        names.forEach(name -> {
            System.out.println(name);
        });
        
        names.forEach(System.out::println);

    }
  
    ```
  
    
