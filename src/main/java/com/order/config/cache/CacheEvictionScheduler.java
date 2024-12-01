package com.order.config.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheEvictionScheduler {

    // This will clear the "orders" cache every 15 seconds (only for testing)
    @Scheduled(fixedRate = 15000) // hours in milliseconds
    @CacheEvict(value = "orders", allEntries = true)
    public void evictOrdersCache() {
        log.info("Cache 'orders' has been cleared.");
    }
}
