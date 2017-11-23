package fid.platform.core.common.commonutil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

public class RedisAPI {  
    private static JedisPool pool = null;  
      
    /** 
     * 构建redis连接池 
     *  
     * @return JedisPool
     */  
    public static JedisPool getPool() {  
        if (pool == null) {  
            JedisPoolConfig config = new JedisPoolConfig();  
            //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；  
            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。  
            config.setMaxIdle(500);  
            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。  
            config.setMaxIdle(1000);  
            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；  
            config.setMaxWaitMillis(1000);  
            //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；  
            config.setTestOnBorrow(true);
//            pool = new JedisPool(config, "127.0.0.1", 6379,5000, "fid123", 0);
            String value = ReadPropertyUtil.getPropertyByName(ReadPropertyUtil.ROBOT_ENV, "robot.env");
            String addr =  null;
            String password = null;
            String port = "6379";
            String database = "2"; 
    		if("product".equals(value)){
    			addr = ReadPropertyUtil.getPropertyByName(ReadPropertyUtil.ROBOT_ENV, "reids_master_addr");
    			password = ReadPropertyUtil.getPropertyByName(ReadPropertyUtil.ROBOT_ENV, "reids_master_password");
    			port = ReadPropertyUtil.getPropertyByName(ReadPropertyUtil.ROBOT_ENV, "reids_master_port");
    			database = ReadPropertyUtil.getPropertyByName(ReadPropertyUtil.ROBOT_ENV, "reids_master_database");
    			if(addr == null){
    				pool = new JedisPool(config, "127.0.0.1", 6379, 50000, "Fid123456", 2);
    			} else if( password == null){
    				pool = new JedisPool(config, addr, 6379,2);
    			}else if( port == null){
        			pool = new JedisPool(config, addr, 6379, 50000, password, 2);
    			} else {
    				pool = new JedisPool(config, addr, new Integer(port), 50000, password, new Integer(database));
    			}
    		} else {
    			addr = "183.2.191.55";
    			pool = new JedisPool(config, addr, 6379, 5000, "Fid123456", 2);
    		}
        }  
        return pool;  
    }  
    
    public static JedisPool getPool(String addr,int port,String password,int dataBase) {  
        if (pool == null) {  
            JedisPoolConfig config = new JedisPoolConfig();  
            //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；  
            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。  
            config.setMaxIdle(500);  
            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。  
            config.setMaxIdle(1000);  
            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；  
            config.setMaxWaitMillis(1000);  
            //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；  
            config.setTestOnBorrow(true);
          
    		pool = new JedisPool(config, addr, port, 5000, password, dataBase);
        } 
        return pool;  
    }
      
    /** 
     * 返还到连接池 
     *  
     * @param pool  
     * @param redis 
     */  
    public static void returnResource(JedisPool pool, Jedis redis) {  
        if (redis != null) {  
            pool.returnResourceObject(redis);
        }  
    }  
      
    
    /** 
     * 判断数据是否存在
     *  
     * @param key 
     * @return 
     */  
    public static Boolean exist(String key){  
    	Boolean isExists = false;  
          
        JedisPool pool = null;  
        Jedis jedis = null;  
        try {  
            pool = getPool();  
            jedis = pool.getResource();  
            isExists = jedis.exists(key);
        } catch (Exception e) {  
            //释放redis对象  
            pool.returnResourceObject(jedis);  
            e.printStackTrace();  
        } finally {  
            //返还到连接池  
            returnResource(pool, jedis);  
        }  
          
        return isExists;  
    }  
    
    
    /** 
     * 获取数据 
     *  
     * @param key 
     * @return 
     */  
    public static String getString(String key){  
        String value = null;  
          
        JedisPool pool = null;  
        Jedis jedis = null;  
        try {  
            pool = getPool();  
            jedis = pool.getResource();  
            value = jedis.get(key);  
        } catch (Exception e) {  
            //释放redis对象  
            pool.returnResourceObject(jedis);  
            e.printStackTrace();  
        } finally {  
            //返还到连接池  
            returnResource(pool, jedis);  
        }  
          
        return value;  
    }  
    
    /** 
     * 获取数据 
     *  
     * @param key 
     * @return 
     */  
    public static Boolean get(String key,String value){  
    	Boolean isExists = false;  
          
        JedisPool pool = null;  
        Jedis jedis = null;  
        try {  
            pool = getPool();  
            jedis = pool.getResource();  
//            value = jedis.get(key);  
            isExists = jedis.sismember(key,value);
        } catch (Exception e) {  
            //释放redis对象  
            pool.returnResourceObject(jedis);  
            e.printStackTrace();  
        } finally {  
            //返还到连接池  
            returnResource(pool, jedis);  
        }  
          
        return isExists;  
    }  

    
    
    /**
     * 字符串
     * @param key
     * @param value
     */
    public static void set(String key,String value){  
          
        JedisPool pool = null;  
        Jedis jedis = null;  
        try {  
            pool = getPool();  
            jedis = pool.getResource();  
//            value = jedis.get(key);  
            jedis.sadd(key,value);
        } catch (Exception e) {  
            //释放redis对象  
            pool.returnResourceObject(jedis);  
            e.printStackTrace();  
        } finally {  
            //返还到连接池  
            returnResource(pool, jedis);  
        }  
          
    }
    
    /**
     * 字符串
     * @param key
     * @param value
     */
    public static void set(String key,Collection<String>  value){  
    	value.remove(null);
    	value.remove("");
        JedisPool pool = null;  
        Jedis jedis = null;  
        try {  
            pool = getPool();  
            jedis = pool.getResource();  
//            value = jedis.get(key);  
            if(value.size() !=0){
            	jedis.sadd(key,value.toArray(new String[value.size()]));
            }
        } catch (Exception e) {  
            //释放redis对象  
            pool.returnResourceObject(jedis);  
            e.printStackTrace();  
        } finally {  
            //返还到连接池  
            returnResource(pool, jedis);  
        }  
          
    }
    
    /**
     * set集合
     * @param setKey
     * @return
     */
    public static Set<String> getSet(String setKey){
    	
	  Set<String>  value = null;  
        
        JedisPool pool = null;  
        Jedis jedis = null;  
        try {  
            pool = getPool();  
            jedis = pool.getResource();  
            value = jedis.smembers(setKey);
        } catch (Exception e) {  
            //释放redis对象  
            pool.returnResourceObject(jedis);  
            e.printStackTrace();  
        } finally {  
            //返还到连接池  
            returnResource(pool, jedis);  
        } 
        return value;
    }
    
    
    
    /**set Object*/
    public static String set(Object object,String key)
	   {
	    	
	    	  JedisPool pool = null;  
	          Jedis jedis = null;  
	          String value = null;
			try {  
	              pool = getPool();  
	              jedis = pool.getResource();  
	              value = jedis.set(key.getBytes(), SerializeUtil.serialize(object));
	          } catch (Exception e) {  
	              //释放redis对象  
	              pool.returnResourceObject(jedis);  
	              e.printStackTrace();  
	          } finally {  
	              //返还到连接池  
	              returnResource(pool, jedis);  
	          } 
	          return value;
	          
	   }
    
    public static String setString(String key,String value)
    {
    	
    	JedisPool pool = null;  
    	Jedis jedis = null;  
    	try {  
    		pool = getPool();  
    		jedis = pool.getResource();  
    		value = jedis.set(key, value);
    	} catch (Exception e) {  
    		//释放redis对象  
    		pool.returnResourceObject(jedis);  
    		e.printStackTrace();  
    	} finally {  
    		//返还到连接池  
    		returnResource(pool, jedis);  
    	} 
    	return value;
    	
    }
    
    /**get Object*/
    public  static Object get(String key)
    {
    	
  	  JedisPool pool = null;  
        Jedis jedis = null;  
        Object result = null;
		try {  
            pool = getPool();  
            jedis = pool.getResource();  
            if(jedis.exists(key.getBytes())){
	            byte[] value = jedis.get(key.getBytes());
	            result = SerializeUtil. unserialize(value);
            }
        } catch (Exception e) {  
            //释放redis对象  
            pool.returnResourceObject(jedis);  
            e.printStackTrace();  
        } finally {  
            //返还到连接池  
            returnResource(pool, jedis);  
        } 
        return result;
        
 }
    
    
    /**
     * 设值map  中一个值
     * @param key
     */
    public static void setMapValue(String mapName,String key,String value){
    	JedisPool pool = null;  
        Jedis jedis = null;  
		try {  
            pool = getPool();  
            jedis = pool.getResource(); 
            jedis.hset(mapName, key, value);
        } catch (Exception e) {  
            //释放redis对象  
            pool.returnResourceObject(jedis);  
            e.printStackTrace();  
        } finally {  
            //返还到连接池  
            returnResource(pool, jedis);  
        } 
    }
    
    /**
     * 去map  中一个值
     * @param key
     */
    public static String getMapValue(String mapName,String key){
    	String value = null;;
    	JedisPool pool = null;  
        Jedis jedis = null;  
		try {  
            pool = getPool();  
            jedis = pool.getResource(); 
//            if(jedis.exists(mapName)){
            	value = jedis.hget(mapName, key);
//            }
        } catch (Exception e) {  
            //释放redis对象  
            pool.returnResourceObject(jedis);  
            e.printStackTrace();  
        } finally {  
            //返还到连接池  
            returnResource(pool, jedis);  
        }
		return value;
    }
    
    /**
     * 去map  中一个值
     * @param key
     */
    public static Boolean hexistsMapValue(String mapName,String key){
    	Boolean value = null;;
    	JedisPool pool = null;  
        Jedis jedis = null;  
		try {  
            pool = getPool();  
            jedis = pool.getResource(); 
            value = jedis.hexists(mapName, key);
        } catch (Exception e) {  
            //释放redis对象  
            pool.returnResourceObject(jedis);  
            e.printStackTrace();  
        } finally {  
            //返还到连接池  
            returnResource(pool, jedis);  
        }
		return value;
    }
    
    /**
     * 去map  中一个值
     */
    public static List<String> getMapValues(String mapName,String... keys){
    	if(keys == null || keys.length ==0) return Collections.emptyList();
    	List<String> values = new ArrayList<>();
    	JedisPool pool = null;  
        Jedis jedis = null;  
		try {  
            pool = getPool();  
            jedis = pool.getResource(); 
            if(jedis.exists(mapName)){
            	values = jedis.hmget(mapName, keys);
            }
        } catch (Exception e) {  
            //释放redis对象  
            pool.returnResourceObject(jedis);  
            e.printStackTrace();  
        } finally {  
            //返还到连接池  
            returnResource(pool, jedis);  
        }
		return values;
    }
    
    /**
     * 设值map
     * @param key
     * @param data
     */
    public static void setMap(String key,Map<String, String> data){
    	JedisPool pool = null;  
        Jedis jedis = null;  
		try {  
            pool = getPool();  
            jedis = pool.getResource(); 

            Pipeline pl = jedis.pipelined();
            for (Entry<String, String> entry : data.entrySet()) {
            	 pl.hset(key, entry.getKey(),entry.getValue());
			}
            pl.sync();
        } catch (Exception e) {  
            //释放redis对象  
            pool.returnResourceObject(jedis);  
            e.printStackTrace();  
        } finally {  
            //返还到连接池  
            returnResource(pool, jedis);  
        } 
    }
  
    /**
     * 将对象序列化到 redis
     * @param key
     * @param bean
     */
	public static void setBean(String key, Object bean) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.set(key.getBytes(), SerializeUtil.serialize(bean));
		} catch (Exception e) {
			// 释放redis对象
			pool.returnResourceObject(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}
    
    public static Long removeBean(String... keys) {
    	JedisPool pool = null;
    	Jedis jedis = null;
    	Long l = 0L;
    	try {
    		pool = getPool();
    		jedis = pool.getResource();
    		l = jedis.del(keys);
    	} catch (Exception e) {
    		//释放redis对象
    		pool.returnResourceObject(jedis);
    		e.printStackTrace();
    	} finally {
    		//返还到连接池
    		returnResource(pool, jedis);
    	}
    	return l;
    }
    
    
    
    
    /**
     * 删除map中的值
     * @param key
     */
    public static Long deleteFromMap(String key,String value){
    	Long  deleteCount = 0L;
    	JedisPool pool = null;  
        Jedis jedis = null;  
		try {  
            pool = getPool();  
            jedis = pool.getResource(); 
            deleteCount =  jedis.hdel(key, value);
        } catch (Exception e) {  
            //释放redis对象  
            pool.returnResourceObject(jedis);  
            e.printStackTrace();  
        } finally {  
            //返还到连接池  
            returnResource(pool, jedis);  
        } 
		return deleteCount;
    }
    
    
    /**
     * 取值map
     * @param key
     * @return
     */
    public static Map<String,String>  getMap(String key){
    	Map<String,String> mapResult = new HashMap<>();
    	JedisPool pool = null;  
        Jedis jedis = null;  
		try {  
            pool = getPool();  
            jedis = pool.getResource();  
            if(jedis.exists(key)){
            	mapResult = jedis.hgetAll(key);
            }
        } catch (Exception e) {  
            //释放redis对象  
            pool.returnResourceObject(jedis);  
            System.out.println("redis取map:"+key+"错误");
            e.printStackTrace();  
        } finally {  
            //返还到连接池  
            returnResource(pool, jedis);  
        } 
		return mapResult;
    }
    
    /**
     * @Description 将list放入redis
     * @param key
     * @param list
     */
    public static void setList(String key,List<String> list){
    	JedisPool pool = null;  
        Jedis jedis = null;  
		try {  
            pool = getPool();  
            jedis = pool.getResource();  
            for(String value:list){
            	jedis.lpush(key.getBytes(), value.getBytes());
            }
            
        } catch (Exception e) {  
            //释放redis对象  
            pool.returnResourceObject(jedis);  
            System.out.println("redis添加list:"+key+"错误");
            e.printStackTrace();  
        } finally {  
            //返还到连接池  
            returnResource(pool, jedis);  
        } 
    }
    /**
     * @Description 根据key获取redis中的list
     * @param key
     * @return
     */
    public static List<String> getList(String key){
    	JedisPool pool = null;  
        Jedis jedis = null;  
        List<String> lrange = new ArrayList<>();
		try {  
            pool = getPool();  
            jedis = pool.getResource();  
            lrange = jedis.lrange(key, 0, -1);
            
        } catch (Exception e) {  
            //释放redis对象  
            pool.returnResourceObject(jedis);  
            System.out.println("redis添加list:"+key+"错误");
            e.printStackTrace();  
        } finally {  
            //返还到连接池  
            returnResource(pool, jedis);  
        }
		
		return lrange;
    }
    
    public static void main(String[] args) {
    	String[] strs = {"1121", "823", "264", "238"};
//		List<RbsTagsWords> list  = RedisEntityUtil.getObjectByColumn(new RbsTagsWords(), "wordsId", strs);
//		List<String> idsList = RedisAPI.getMapValues(RedisEntityUtil.getMapKey(RbsTagsWords.class.getSimpleName(),"wordsId"), strs);
//		System.out.println(idsList);
//    	System.out.println(RedisAPI.getMapValues("RbsTagsWords_wordsId","238"));
    	JedisPool pool = null;  
        Jedis jedis = null;  
		try {  
            pool = getPool();  
            jedis = pool.getResource();
//            System.out.println(jedis.hget("RbsTagsWords_wordsId", "238"));
            Map<String,String> map = jedis.hgetAll("RbsTagsWords");
            System.out.println(map.size());
            for (Entry<String, String> entry : map.entrySet()) {
            	if(entry.getValue().contains(":238,")){
            		System.out.println(entry);
            	}
			}
		 } catch (Exception e){
			 
		 }
    	
//    	System.out.println(RedisAPI.getMapValues("RbsTagsWords_wordsId","238"));
//    	RedisAPI.set("ss", "1");
//    	RedisAPI.set("ss", "2");
//    	RedisAPI.set("ss", "2");
//    	RedisAPI.set("ss", "3");
    	Set<String> set = new HashSet<String>();
//    	set.add("1");
    	set.add("2");
//    	set.add("2");
//    	set.add("3");
//    	for (int i = 0; i <100000; i++) {
////    		RedisAPI.setMap("aa", new HashMap<>());
//    		RedisAPI.set(i, "a");
////    		System.out.println(i+"===="+RedisAPI.getMap("aa"));
//    		System.out.println(i+"===="+RedisAPI.get(i+""));
//    		System.gc();
//		}
		
	}
}  