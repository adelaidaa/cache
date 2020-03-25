package com.aaj.cache;

import java.util.UUID;

public class LRUPeopleCache extends LRUCache<UUID,Person> {
    public LRUPeopleCache(int maxCapacity) {
       super(maxCapacity);
    }
}
