package com.example.Trade.dao;

import com.example.Trade.model.Tick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class TickRepositoryImpl implements TickRepository {

    private final String TICKS = "TICKS";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, Tick> hashOperations;

    @PostConstruct
    private void initializeHashOperations() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void save(Tick tick) {
        hashOperations.put(TICKS, tick.getMls(), tick);
    }

    @Override
    public void saveAll(Map<String, Tick> map) {
        hashOperations.putAll(TICKS, map);
    }

    @Override
    public Tick findById(String id) {
        return hashOperations.get(TICKS, id);
    }

    @Override
    public Map<String, Tick> findAll() {
        return hashOperations.entries(TICKS);
    }

    @Override
    public void delete(String id) {
        hashOperations.delete(TICKS, id);
    }
}
