# ListenableFuture

使用Future的实现类FutureTask想要实现一旦获取到结果立即执行后续的业务，就需要阻塞主线程等待结果或者使用其他线程循环的判断任务是否结束，这样导致性能较低，且代码负责。ListenableFuture在Future的基础上增加了任务执行后自动调用后续业务处理的逻辑，方便我们使用。

简短来说，避免Future获取阻塞问题，增加回调。

```java

 public static void test() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(executor);

        ListenableFuture<String> listenableFuture = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(6 * 1000);
                return "Hello, ListenableFuture!";
            }
        });

        Futures.addCallback(listenableFuture, new FutureCallback<String>() {

            @Override
            public void onSuccess(@Nullable String result) {
                System.out.println("Success: " + result);
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("Failure: " + t.getMessage());
            }
        }, executorService);


        Thread.sleep(3 * 1000);
        System.out.println("Main thread is done");
    }

```
上面代码会先输出 “Main thread is done”，然后回调函数会输出 “Success: Hello, ListenableFuture!”。
