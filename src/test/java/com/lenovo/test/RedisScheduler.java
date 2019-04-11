package com.lenovo.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.Scheduler;

public class RedisScheduler implements Scheduler {

    private JedisPool pool;

    private static final String QUEUE_PREFIX = "queue_";

    private static final String SET_PREFIX = "set_";

    public RedisScheduler(String host, int i){
        pool = new JedisPool(new JedisPoolConfig(), host);
    }

    @Override
    public void push(Request request, Task task) {
        Jedis jedis = pool.getResource();
        //ʹ��SortedSet����urlȥ��
        if (jedis.zrank(SET_PREFIX+task.getUUID(),request.getUrl())==null){
            //ʹ��List�������
            jedis.rpush(QUEUE_PREFIX+task.getUUID(),request.getUrl());
            jedis.zadd(SET_PREFIX+task.getUUID(),System.currentTimeMillis(),request.getUrl());
        }
    }

    @Override
    public Request poll(Task task) {
        Jedis jedis = pool.getResource();
        String url = jedis.lpop(QUEUE_PREFIX+task.getUUID());
        if (url==null) {
            return null;
        }
        return new Request(url);
    }
}
