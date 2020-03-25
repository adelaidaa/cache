package com.aaj.cache;

import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

@Getter
public class LRUPeopleCache implements Cache<UUID,Person> {

    private final HashMap<UUID, Integer> elementsIndex;
    private final LinkedList<Person> elements;
    private final int maxCapacity;

    public LRUPeopleCache(int maxCapacity) {
        this.elementsIndex = new HashMap<>();
        this.elements = new LinkedList<>();
        this.maxCapacity = maxCapacity;
    }

    @Override
    public void addElement(Person element) {
        if (elements.size() < maxCapacity) {
            addPerson(element);
        }//new element but capacity limit reach
        else if (elements.size() == maxCapacity) {
            //evict least recently used
            evictLeastRecentlyUsed();
            addPerson(element);
        }
    }

    @Override
    public Optional<Person> getElement(UUID key) {
        Optional<Integer> elementIndex = Optional.ofNullable(elementsIndex.get(key));
        //new element as is not found in the cache
        if (!elementIndex.isPresent()) {
            return Optional.empty();
        } else {
            //element exits and we need to update the elements to reflect that it has just been
            //retrieved and therefore recently used
            int index = elementIndex.get().intValue();
            Person person = elements.get(index);
            elements.remove(index);
            elementsIndex.remove(key);
            updateEntriesWhenRemoved(index);
            addPerson(person);
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
        Person firstPerson = elements.removeFirst();
        elementsIndex.remove(firstPerson.getId());
        //update all indices in map as we had removed first element of the linkedList
        elementsIndex.replaceAll((k, v) -> v - 1);
    }

    private void addPerson(Person person) {
        elements.addLast(person);
        elementsIndex.put(person.getId(), elements.size() - 1);
    }
}
