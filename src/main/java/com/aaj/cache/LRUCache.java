package com.aaj.cache;

import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

@Getter
public class LRUCache<K,V extends ElementWithId<K>> implements Cache<K,V>{

    private final HashMap<K, Integer> elementsIndex;
    private final LinkedList<V> elements;
    private final int maxCapacity;

    public LRUCache(int maxCapacity) {
        this.elementsIndex = new HashMap<>();
        this.elements = new LinkedList<>();
        this.maxCapacity = maxCapacity;
    }

    @Override
    public void addElement(V element) {
        if (elements.size() < maxCapacity) {
            add(element);
        }//new element but capacity limit reach
        else if (elements.size() == maxCapacity) {
            //evict least recently used
            evictLeastRecentlyUsed();
            add(element);
        }
    }

    @Override
    public Optional<V> getElement(K key) {
        Optional<Integer> elementIndex = Optional.ofNullable(elementsIndex.get(key));
        //new element as is not found in the cache
        if (!elementIndex.isPresent()) {
            return Optional.empty();
        } else {
            //element exits and we need to update the elements to reflect that it has just been
            //retrieved and therefore recently used
            int index = elementIndex.get().intValue();
            V element = elements.get(index);
            elements.remove(index);
            elementsIndex.remove(key);
            updateEntriesWhenRemoved(index);
            add(element);
            return Optional.of(elements.get(elementIndex.get()));
        }
    }

    private void updateEntriesWhenRemoved(int index) {
        elementsIndex.entrySet()
                .stream()
                .filter(entry -> entry.getValue() > index)
                .forEach(entry -> elementsIndex.replace(entry.getKey(), entry.getValue()-1));
    }

    private void evictLeastRecentlyUsed() {
        V firstElement = elements.removeFirst();
        elementsIndex.remove(firstElement.getId());
        //update all indices in map as we had removed first element of the linkedList
        elementsIndex.replaceAll((k, v) -> v - 1);
    }

    private void add(V element) {
        elements.addLast(element);
        elementsIndex.put(element.getId(), elements.size() - 1);
    }
}
