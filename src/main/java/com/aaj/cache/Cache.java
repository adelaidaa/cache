package com.aaj.cache;

import java.util.Optional;

public interface Cache<K,V> {
    Optional<Person> getElement(K key);
    void addElement(V element);

}
