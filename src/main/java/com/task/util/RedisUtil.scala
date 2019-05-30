package com.task.util
import redis.clients.jedis.{HostAndPort, JedisCluster, JedisPoolConfig}

import scala.collection.JavaConversions._
import com.task.util.RedisUtil.clients
/**
  * @author limeng
  * @date 2019/5/30 23:54
  * @version 1.0
  */
class RedisUtil extends Serializable{
  /**
    *
    * @param key
    * @param value
    * @return
    */

  def set(key:String,value:Any): Unit ={

    clients.set(key,String.valueOf(value))

  }

  /**
    *
    * @param key
    * @param time
    * @return
    */

  def hget(key:String,time:Long): Option[String] ={
    val value=clients.hget(key,String.valueOf(time))
    Option(value)
  }

  /**
    *
    * @param key
    * @param time
    * @param value
    * @return
    */
  def hset(key:String,time:Long,value:Any): Boolean ={
    clients.hset(key,String.valueOf(time),String.valueOf(value))==1

  }

  def hmset(key:String,map:Map[Long,String]): Unit ={

    val map2=Map[String,String]()
    map.foreach{case (key:Long,value:String)=>
      map2.put(key.toString,value)
    }

    clients.hmset(key,mapAsJavaMap(map2))

  }

  /**
    *
    * @param key
    * @param time
    * @return
    */
  def hdel(key:String,time:Any): Option[Long] ={

    Some(clients.hdel(key,String.valueOf(time)))

  }

  /**
    *
    * @param key
    * @param times
    * @return
    */
  def rpush(key:String,times:Any): Option[Long] ={

    Some(clients.rpush(key,String.valueOf(times)))

  }


  /**
    *
    * @param key
    * @return
    */
  def lpop(key:String): Option[Long] ={

    val time=clients.lpop(key)
    if(time==null)
      None
    else
      Some(time.toLong)
  }


  /**
    *
    * @param key
    * @return
    */
  def lhead(key:String): Option[Long] ={

    val head=clients.lindex(key,0)
    if(head==null)
      None
    else
      Some(head.toLong)
  }

}

object RedisUtil {

  private val jedisClusterNodes = new java.util.HashSet[HostAndPort]()
  jedisClusterNodes.add(new HostAndPort("",6379))
  private val properties = PropertiesUtils.apply()
  properties.getProperty("")
  private val addr = properties.getProperty("redis.addr")
  private val port = properties.getProperty("redis.port").toInt
  private val auth = properties.getProperty("redis.auth")
  private val maxIdle = properties.getProperty("redis.maxIdle").toInt
  private val maxActive = properties.getProperty("redis.maxActive").toInt
  private val maxWait = properties.getProperty("redis.maxWait").toInt
  private val timeOut = properties.getProperty("redis.timeOut").toInt
  private val testOnBorrow = properties.getProperty("redis.testOnBorrow")
  val config = new JedisPoolConfig
  config.setMaxTotal(maxActive)
  config.setMaxIdle(maxIdle)
  config.setMaxWaitMillis(maxWait)
  //config.setTestOnBorrow(testOnBorrow)
  val clients = new JedisCluster(jedisClusterNodes,config)

}
