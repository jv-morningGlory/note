# Future

future 的设计理念就是，用于异步结果的获取。

```java

public interface Future<V> {

    
    boolean cancel(boolean mayInterruptIfRunning);

    
    boolean isCancelled();

    
    boolean isDone();

    
    V get() throws InterruptedException, ExecutionException;

  
    V get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;


```
使用  主线程会阻塞，等待异步线程执行完成才会向下执行。
```java


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(4);
        System.out.println("start！");
        Future<String> future = executor.submit(
                new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        System.out.println("sleep！");
                        Thread.sleep(10 * 1000);
                        return "hello";
                    }
                }
        );
        System.out.println(future.get());
        System.out.println("end！");
    }


```

