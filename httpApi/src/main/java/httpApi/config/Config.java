package httpApi.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.*;

@Configuration
@ComponentScan("httpApi")
@PropertySource("classpath:cache.properties")
public class Config
{
    @Value("${redis.address}")
    private String redisAddress;

    @Value("${connectionPoolSize}")
    private int poolSize;

    @Value("${connectionMinimumIdleSize}")
    private int minIdleSize;

    @Value("${connectTimeout}")
    private int connectTimeout;

    @Value("${timeout}")
    private int timeout;

    @Value("${idleConnectionTimeout}")
    private int idleConnectionTimeout;

    @Value("${redis.password:}")
    private String redisPassword;

    @Bean
    public JedisPool jedisPool()
    {
        final JedisPoolConfig poolConfig = buildPoolConfig();
        String[] addressParts = redisAddress.replace("redis://", "").split(":");
        String redisHost = addressParts[0];
        int redisPort = Integer.parseInt(addressParts[1]);

            return new JedisPool(poolConfig, redisHost, redisPort, timeout);
    }

    private JedisPoolConfig buildPoolConfig()
    {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(poolSize);
        poolConfig.setMaxIdle(minIdleSize);
        poolConfig.setMinIdle(minIdleSize);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(idleConnectionTimeout);
        poolConfig.setTimeBetweenEvictionRunsMillis(idleConnectionTimeout / 2);
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }

    @Bean
    public Connection connection()
    {
        HostAndPort hostAndPort = HostAndPort.from(redisAddress);
        return new Connection(hostAndPort);
    }

    @Bean
    public Gson gson()
    {
        return new GsonBuilder().enableComplexMapKeySerialization().create();
    }
}
