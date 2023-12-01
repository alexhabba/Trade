package com.example.Trade.dao;

import com.example.Trade.model.Bar;
import com.example.Trade.model.Fractal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class FractalRepository {

    public static final String FRACTAL_ONE_MINUTE = "FRACTAL_ONE_MINUTE";
    public static final String FRACTAL_FIVE_MINUTE = "FRACTAL_FIVE_MINUTE";

    @Value("${spring.redis.ttl}")
    private Long ttl;

    @Autowired
    private RedisTemplate<String, Bar> redisTemplate;
    private HashOperations<String, String, Fractal> hashOperations;

    @PostConstruct
    private void initializeHashOperations() {
        hashOperations = redisTemplate.opsForHash();
    }

    public void add(String key, Fractal bar) {
        hashOperations.put(key, bar.getDateTime(), bar);
    }

    public long getSize(String key) {
        return hashOperations.size(key);
    }

    public List<Fractal> findAll(String key) {
        return hashOperations.values(key);
    }

    public Fractal findById(String key, String id) {
        return hashOperations.get(key, id);
    }

    public List<Fractal> findByIds(String key, List<String> ids) {
        return hashOperations.multiGet(key, ids);
    }

    public void delete(String key, String id) {
        hashOperations.delete(key, id);
    }
}
