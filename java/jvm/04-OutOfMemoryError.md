# idea工具使用

设置vm参数 

![img.png](img/04-001.png)

工具Profiler

![img.png](img/04-002.png)


# Java堆溢出

```java
static class OOMObject{

    }

    /**
     * VM Args：-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
     * @param args
     */
    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<OOMObject>();
        while(true){
            list.add(new OOMObject());
        }
    }


```

![img.png](img/04-003.png)

# 虚拟机栈和本地方法栈溢出