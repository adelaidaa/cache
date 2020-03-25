package com.aaj.cache;

import java.util.Optional;

public interface Cache<K,V> {
    Optional<V> getElement(K key);
    void addElement(V element);

}
