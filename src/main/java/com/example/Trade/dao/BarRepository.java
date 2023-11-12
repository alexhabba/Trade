package com.example.Trade.dao;

import com.example.Trade.model.Bar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
public class BarRepository {

    public static final String ONE_SECONDS = "ONE_SECONDS";
    public static final String ONE_MINUTE = "ONE_MINUTE";
    public static final String FIVE_MINUTE = "FIVE_MINUTE";

    @Value("${spring.redis.ttl}")
    private Long ttl;

    @Autowired
    private RedisTemplate<String, Bar> redisTemplate;
    private HashOperations<String, String, Bar> hashOperations;

    @PostConstruct
    private void initializeHashOperations() {
        hashOperations = redisTemplate.opsForHash();
    }

    public void addAll(String key, Map<String, Bar> map) {
        hashOperations.putAll(key, map);
    }

    public void add(String key, Bar bar) {
        hashOperations.put(key, bar.getDateTime(), bar);
    }

    public long getSize(String key) {
        return hashOperations.size(key);
    }

    public List<Bar> findAll(String key) {
        return hashOperations.values(key);
    }

    public Bar findById(String key, String id) {
        return hashOperations.get(key, id);
    }

    public List<Bar> findByIds(String key, List<String> ids) {
        return hashOperations.multiGet(key, ids);
    }

    public void delete(String key, String id) {
        hashOperations.delete(key, id);
    }
}
