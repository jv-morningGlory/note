使用redis，写一个分布式锁

```

package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.SetParams;

import java.util.*;


/**
 * Redis分布式锁 使用 SET resource-name anystring NX EX max-lock-time 实现
 * 该方案在 Redis 官方 SET 命令页有详细介绍。 http:// doc. redisfans. com/ string/ set. html
 * 在介绍该分布式锁设计之前，我们先来看一下在从 Redis 2.6.12 开始 SET 提供的新特性， 命令 SET key value [EX seconds] [PX milliseconds] [NX|XX]，其中：
 * EX seconds — 以秒为单位设置 key 的过期时间； PX milliseconds — 以毫秒为单位设置 key 的过期时间； NX — 将key 的值设为value ，当且仅当key 不存在，等效于 SETNX。 XX — 将key 的值设为value ，当且仅当key 存在，等效于 SETEX。
 * 命令 SET resource-name anystring NX EX max-lock-time 是一种在 Redis 中实现锁的简单方法。
 * 客户端执行以上的命令：
 * 如果服务器返回 OK ，那么这个客户端获得锁。 如果服务器返回 NIL ，那么客户端获取锁失败，可以在稍后再重试。
 */

public class RedisLock {

    private static Logger logger = LoggerFactory.getLogger(RedisLock.class);

    /**
     * 调用set后的返回值
     */
    public static final String OK = "OK";

    /**
     * 默认锁的有效时间(s)
     */
    public static final int EXPIRE = 60;

    /**
     * 默认请求锁的超时时间(ms 毫秒)
     */
    private static final long TIME_OUT = 100;


    private RedisTemplate<String, String> redisTemplate;

    private volatile boolean locked = false;

    private String lockKey;

    private String lockValue;

    private int expireTime = EXPIRE;

    final Random random = new Random();

    /**
     * 请求锁的超时时间(ms)
     */
    private long timeOut = TIME_OUT;

    public static final String UNLOCK_LUA;

    static {
        UNLOCK_LUA = "if redis.call(\"get\",KEYS[1]) == ARGV[1] " +
                "then " +
                "    return redis.call(\"del\",KEYS[1]) " +
                "else " +
                "    return 0 " +
                "end ";
    }

    public RedisLock(RedisTemplate<String , String> redisTemplate, String lockKey) {
        this.redisTemplate = redisTemplate;
        this.lockKey = lockKey + "_lock";
    }

    public RedisLock(RedisTemplate<String, String> redisTemplate, String lockKey, int expireTime) {
        this(redisTemplate, lockKey);
        this.expireTime = expireTime;
    }

    public RedisLock(RedisTemplate<String, String> redisTemplate, String lockKey, long timeOut) {
        this(redisTemplate, lockKey);
        this.timeOut = timeOut;
    }

    /**
     * 锁的过期时间和请求锁的超时时间都是用指定的值
     *
     * @param redisTemplate
     * @param lockKey       锁的key（Redis的Key）
     * @param expireTime    锁的过期时间(单位：秒)
     * @param timeOut       请求锁的超时时间(单位：毫秒)
     */
    public RedisLock(RedisTemplate<String, String> redisTemplate, String lockKey, int expireTime, long timeOut) {
        this(redisTemplate, lockKey, expireTime);
        this.timeOut = timeOut;
    }

    /**
     * 尝试获取锁 超时返回
     *
     */
    public boolean tryLock() {
        //生成随机的value
        lockValue = UUID.randomUUID().toString();
        //超时时间
        long timeout = timeOut * 1000000;
        //当期时间，纳秒
        long nowTime = System.nanoTime();
        while(System.nanoTime() - nowTime < timeout){
            String result = this.set(lockKey, lockValue, expireTime);
            if(OK.equalsIgnoreCase(result)){
                locked = true;
                // 上锁成功结束请求
                return true;
            }
            sleep(10, 50000);
        }
        return locked;

    }

    /**
     * 尝试获取锁 超时返回
     * 当获取到锁 或者 redis不可用时(不影响业务执行) 返回true
     * @return
     */
    public boolean tryLockOrRedisDisable() {
        boolean res = locked;
        try {
            // 生成随机key
            lockValue = UUID.randomUUID().toString();
            // 请求锁超时时间，纳秒
            long timeout = timeOut * 1000000;
            // 系统当前时间，纳秒
            long nowTime = System.nanoTime();
            while ((System.nanoTime() - nowTime) < timeout) {
                if (OK.equalsIgnoreCase(this.set(lockKey, lockValue, expireTime))) {
                    locked = true;
                    res = locked;
                    // 上锁成功结束请求
                    return true;
                }

                // 每次请求等待一段时间
                sleep(10, 50000);
            }
        }catch (DataAccessException ex){
            res = true;
        }
        return res;
    }

    /**
     * 尝试获取锁 立即返回
     *
     * @return 是否成功获得锁
     */
    public boolean lock() {
        lockValue = UUID.randomUUID().toString();
        //不存在则添加 且设置过期时间（单位ms）
        String result = set(lockKey, lockValue, expireTime);
        locked = OK.equalsIgnoreCase(result);
        return locked;
    }


    /**
     * 尝试获取锁 立即返回
     * 当获取到锁 或者 redis不可用时(不影响业务执行) 返回true
     *
     * @return 是否成功获得锁
     */
    public boolean lockOrRedisDisable() {
        boolean res = false;
        try{
            lockValue = UUID.randomUUID().toString();
            //不存在则添加 且设置过期时间（单位ms）
            String result = set(lockKey, lockValue, expireTime);
            locked = OK.equalsIgnoreCase(result);
            res = locked;
        }catch (DataAccessException ex){
            logger.error("RedisLock3 lockOrRedisDisable error. lockKey:{}", lockKey);
            res = true;
        }
        return res;
    }
    

    /**
     * 以阻塞方式的获取锁
     *
     * @return 是否成功获得锁
     */
    public boolean lockBlock() {
        lockValue = UUID.randomUUID().toString();
        while (true) {
            //不存在则添加 且设置过期时间（单位ms）
            String result = set(lockKey, lockValue, expireTime);
            if (OK.equalsIgnoreCase(result)) {
                locked = true;
                return true;
            }

            // 每次请求等待一段时间
            sleep(10, 50000);
        }
    }


    public Boolean unlock(String lockKey) {
        if (!locked) {
            return true;
        }
        return redisTemplate.execute(
                new RedisCallback<Boolean>() {

                    @Override
                    public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                        Object nativeConnection = connection.getNativeConnection();
                        long  result = 0 ;
                        List<String> keys = Collections.singletonList(lockKey);
                        List<String> values = Collections.singletonList(lockKey);
                        // 集群模式
                        if (nativeConnection instanceof JedisCluster) {
                            try{
                                result =(Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, keys, values);
                            } catch (Exception e){
                                if(lockValue.equals( ((JedisCluster) nativeConnection).get(lockKey))){
                                    result = ((JedisCluster) nativeConnection).del(lockKey);
                                }
                            }

                        }
                        //单机模式
                        if (nativeConnection instanceof Jedis) {
                            try {
                                result = (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, keys, values);
                            } catch (Exception e) {
                                if (lockValue.equals(((Jedis) nativeConnection).get(lockKey))) {
                                    result = ((Jedis) nativeConnection).del(lockKey);
                                }
                            }
                        }
                        locked = (result == 0);
                        return result == 1;
                    }
                }
        );

    }


    private String set(String key, String value, int seconds) {
        Assert.isTrue(!StringUtils.hasLength(key), "key不能为空");
        return redisTemplate.execute(
                new RedisCallback<String>() {
                    @Override
                    public String doInRedis(RedisConnection connection) throws DataAccessException {
                        Object nativeConnection = connection.getNativeConnection();
                        String result = null;
                        //集群
                        if (nativeConnection instanceof JedisCluster) {
                            result = ((JedisCluster) nativeConnection).set(key, value, SetParams.setParams().nx().ex(seconds));
                        }
                        //单机
                        if (nativeConnection instanceof Jedis) {
                            result = ((Jedis) nativeConnection).set(key, value, SetParams.setParams().nx().ex(seconds));
                        }
                        return result;
                    }
                }
        );


    }

    /**
     * @param millis 毫秒
     * @param nanos  纳秒
     */
    private void sleep(long millis, int nanos) {
        try {
            Thread.sleep(millis, random.nextInt(nanos));
        } catch (InterruptedException e) {
            logger.info("获取分布式锁休眠被中断：", e);
        }
    }


}



```