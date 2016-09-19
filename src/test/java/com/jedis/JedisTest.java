package com.jedis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JedisTest {

    private Jedis jedis;
    @Before
    public void initJedis(){
        //given
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "120.25.251.32");
        jedis = pool.getResource();
        jedis.auth("redis123");
        jedis.flushDB();
    }

    @Test
    public void jedis_string_get(){
        //when
        jedis.set("key1","chen");
        String name = jedis.get("key1");
        //then
        assertEquals(name,"chen");
    }

    @Test
    public void jedis_string_append(){
        //when
        jedis.set("key2","chen");
        jedis.append("key2","wen");
        String name = jedis.get("key2");
        //then
        assertEquals(name,"chenwen");
    }

    @Test
    public void jedis_string_range(){
        //when
        jedis.set("key3","chenwenbo");
        String name = jedis.getrange("key3",0,3);
        //then
        assertEquals(name,"chen");
    }

    @Test
    public void jedis_string_mget(){
        //when
        jedis.set("key4","chen");
        jedis.set("key5","wenbo");
        List<String> name = jedis.mget("key4", "key5");
        //then
        assertEquals(name.get(0),"chen");
        assertEquals(name.get(1),"wenbo");
    }


    @Test
    public void jedis_string_mset(){
        //when
        jedis.mset("key6","chen","key7","wenbo");
        List<String> name = jedis.mget("key6","key7");
        //then
        assertEquals(name.get(0),"chen");
        assertEquals(name.get(1),"wenbo");
    }

    @Test
    public void jedis_string_msetnx(){
        //msetnx : mset if not exist
        //when
        jedis.msetnx("address","湖北武汉");
        String name = jedis.get("address");
        jedis.msetnx("address","孝感");
        String name1 = jedis.get("address");
        //then
        assertEquals(name,"湖北武汉");
        assertEquals(name1,"湖北武汉");
    }

    @Test
    public void jedis_string_psetex() throws InterruptedException {
        //when expire
        jedis.psetex("address",1000,"湖北武汉");
        Thread.sleep(1500);
        String name = jedis.get("address");
        //then
        assertNull(name);
    }

    @Test
    public void jedis_string_strlen() throws InterruptedException {
        //when expire
        jedis.set("strlen","chenwenbo");
        long len = jedis.strlen("strlen");
        //then
        assertEquals(9,len);
    }


    @Test
    public void jedis_hash_hget() throws InterruptedException {
        //when expire
        jedis.hset("hash1","key1","value1");
        jedis.hset("hash1","key2","value2");
        String value1 = jedis.hget("hash1", "key1");
        Map<String, String> hash1Map = jedis.hgetAll("hash1");
        //then
        assertEquals("value1",value1);
        assertEquals("value1",hash1Map.get("key1"));
        assertEquals("value2",hash1Map.get("key2"));
    }

    @Test
    public void jedis_hash_hmset() throws InterruptedException {
        //when expire
        Map<String,String> map = new HashMap();
        map.put("key1","value1");
        map.put("key2","value2");
        map.put("key3","value3");
        jedis.hmset("hash1", map);
        Map<String, String> hash1Map = jedis.hgetAll("hash1");
        String value1 = jedis.hget("hash1","key1");
        List<String> values = jedis.hmget("hash1", "key1", "key3");
        List<String> lists = jedis.hvals("hash1");
        //then
        assertEquals("value1",value1);
        assertEquals("value1",hash1Map.get("key1"));
        assertEquals("value2",hash1Map.get("key2"));
        assertEquals("value1",values.get(0));
        assertEquals("value3",values.get(1));
        assertEquals("value1",lists.get(0));
        assertEquals("value2",lists.get(1));
        assertEquals("value3",lists.get(2));
    }


    @Test
    public void jedis_list_lpush() throws InterruptedException {
        //when
        for (int i = 0; i <3; i++) {
            jedis.lpush("list","list"+i);
        }
        List<String> list = jedis.lrange("list", 0, -1);
        for (int i = 0; i < list.size(); i++) {
           assertEquals("list"+(list.size()-i-1),list.get(i));
        }

    }


    @Test
    public void jedis_list_rpush() throws InterruptedException {
        //when
        for (int i = 0; i <3; i++) {
            jedis.rpush("list","list"+i);
        }
        List<String> list = jedis.lrange("list", 0, -1);
        for (int i = 0; i < list.size(); i++) {
            assertEquals("list"+i,list.get(i));
        }
    }

    @Test
    public void jedis_set() throws InterruptedException {

    }


}
