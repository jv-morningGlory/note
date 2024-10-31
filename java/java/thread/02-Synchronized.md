
# Synchronized方法锁（也叫对象锁）

修饰在方法上，多个线程调用同一个对象的同步方法会阻塞，调用不同对象的同步方法不会阻塞。
```java

public synchronized void obj3() {
           int i = 5;
           while (i-- > 0) {
               System.out.println(Thread.currentThread().getName() + " : " + i);
               try {
                   Thread.sleep(500);
               } catch (InterruptedException ie) {
               }
           }
   }

```

修饰代码块，这个this就是指当前对象（类的实例），多个线程调用同一个对象的同步方法会阻塞，调用不同对象的同步方法不会阻塞。

```java

public void obj2() {
       synchronized (this) {
           int i = 5;
           while (i-- > 0) {
               System.out.println(Thread.currentThread().getName() + " : " + i);
               try {
                   Thread.sleep(500);
               } catch (InterruptedException ie) {
               }
           }
       }
   }

```

修饰代码块，这个str就是指String对象，多个线程调用同一个对象的同步方法会阻塞，调用不同对象的同步方法不会阻塞。（java对象的内存地址是否相同）

```java
public void obj2() {
       String str=new String("lock");//在方法体内，调用一次就实例化一次，多线程访问不会阻塞，因为不是同一个对象，锁是不同的
       synchronized (str) {
           int i = 5;
           while (i-- > 0) {
               System.out.println(Thread.currentThread().getName() + " : " + i);
               try {
                   Thread.sleep(500);
               } catch (InterruptedException ie) {
               }
           }
       }
   }
```
# Synchronized类锁

Synchronized修饰静态的方法

```
public static synchronized void obj3() {
           int i = 5;
           while (i-- > 0) {
               System.out.println(Thread.currentThread().getName() + " : " + i);
               try {
                   Thread.sleep(500);
               } catch (InterruptedException ie) {
               }
           }
   }

```

synchronized (test.class) ，锁的对象是test.class，即test类的锁。

```java

public void obj1() {
        synchronized (test.class) {
           int i = 5;
           while (i-- > 0) {
               System.out.println(Thread.currentThread().getName() + " : " + i);
               try {
                   Thread.sleep(500);
               } catch (InterruptedException ie) {
               }
           }
       }
   }

``` 

