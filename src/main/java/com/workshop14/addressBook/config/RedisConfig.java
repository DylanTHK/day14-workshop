package com.workshop14.addressBook.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

// Task2: Configure and create RedisTemplate
@Configuration
public class RedisConfig {

    // values obtained from application.properties
    // values are set with export variableName = value in terminal
    @Value("${spring.data.redis.host}")
    private String redisHost;

    // value redis port from appln.properties
    @Value("${spring.data.redis.port}")
    private Optional<Integer> redisPort;

    @Value("${spring.data.redis.username}")
    private String redisUsername;

    @Value("${spring.data.redis.password}")
    private String redisPassword;


    // define the return redis TEMPLATE bean as single Object
    @Bean
    @Scope("singleton")
    // method returns a redisTemplate
    RedisTemplate<String, Object> redisTemplate() {
        // instantiate redis configuration setting
        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        
        // setting redis parameters
        config.setHostName(redisHost);
        config.setPort(redisPort.get());
        
        if (!redisUsername.isEmpty() && !redisPassword.isEmpty()) {
                config.setUsername(redisUsername);
                config.setPassword(redisPassword);
        }

        // set database index to use
        config.setDatabase(0);

        // defining jedis settings
        final JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();
        final JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
        jedisFac.afterPropertiesSet();

        // instantiate redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();

        // link redis connection
        redisTemplate.setConnectionFactory(jedisFac);

        // set serializer for redis key and hashkey
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // set serializer for value and hashvalue
        RedisSerializer<Object> objSerializer = 
                new JdkSerializationRedisSerializer(getClass().getClassLoader()); // why can getClass() method be called like this
        redisTemplate.setValueSerializer(objSerializer);
        redisTemplate.setHashValueSerializer(objSerializer);

        return redisTemplate;
    }
}
