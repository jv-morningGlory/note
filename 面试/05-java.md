# java中的==比较

- 对于原始类型（byte,short,int,long,float,double, char）比较的是值
- 对于原始类型比较的是引用地址

# 为什么要重写equals和hashCode

创建一个类的时候，继承的equals方法比较的是对象的引用，判断两个对象是否是同一个实例（``内存地址``）。

默认的hashCode方法返回的是对象的``内存地址的哈希码``，用map的时候不能准备的判断是不是一个key。


# 为什么要实现java.io.Serializable

Java 序列化是一种将对象转换为``字节流``的过程，以便可以将对象保存到磁盘上，将其传输到网络上，或者将其存储在内存中，以后再进行反序列化，将字节流重新转换为对象。

序列化在 Java 中是通过 java.io.Serializable 接口来实现的，该接口没有任何方法，只是一个标记接口，用于标识类可以被序列化。

# 深拷贝和浅拷贝

浅拷贝

浅拷贝是创建一个新对象，但只复制原对象的基本类型字段和引用类型字段的引用，而不是复制这些引用指向的实际对象。