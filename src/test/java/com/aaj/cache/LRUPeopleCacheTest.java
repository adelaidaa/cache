package com.aaj.cache;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LRUPeopleCacheTest {
    private static final int MAX_CAPACITY = 3;
    private LRUPeopleCache lruPeopleCache;

    @BeforeEach
    public void setup(){
        lruPeopleCache = new LRUPeopleCache(MAX_CAPACITY);
    }
    @Test
    public void given__cache_gets_to_max_capacity__should_evict_oldest_person() {
        Person person1 = Person.builder().id(UUID.randomUUID()).firstName("Ade").lastName("Alonso").build();
        Person person2 = Person.builder().id(UUID.randomUUID()).firstName("Ade2").lastName("Alonso2").build();
        Person person3 = Person.builder().id(UUID.randomUUID()).firstName("Ade3").lastName("Alonso3").build();

        lruPeopleCache.addElement(person1);
        assertThat(lruPeopleCache.getElements()).containsExactly(person1);
        assertThat(lruPeopleCache.getElementsIndex().get(person1.getId())).isEqualTo(0);

        lruPeopleCache.addElement(person2);
        assertThat(lruPeopleCache.getElements()).containsExactly(person1, person2);
        assertThat(lruPeopleCache.getElementsIndex().get(person1.getId())).isEqualTo(0);
        assertThat(lruPeopleCache.getElementsIndex().get(person2.getId())).isEqualTo(1);

        lruPeopleCache.addElement(person3);
        assertThat(lruPeopleCache.getElements()).containsExactly(person1, person2, person3);
        assertThat(lruPeopleCache.getElementsIndex().get(person1.getId())).isEqualTo(0);
        assertThat(lruPeopleCache.getElementsIndex().get(person2.getId())).isEqualTo(1);
        assertThat(lruPeopleCache.getElementsIndex().get(person3.getId())).isEqualTo(2);

        //cache if full
        Person person4 = Person.builder().id(UUID.randomUUID()).firstName("Ade4").lastName("Alonso4").build();
        lruPeopleCache.addElement(person4);
        assertThat(lruPeopleCache.getElements()).containsExactly(person2, person3, person4);
        assertThat(lruPeopleCache.getElementsIndex().get(person2.getId())).isEqualTo(0);
        assertThat(lruPeopleCache.getElementsIndex().get(person3.getId())).isEqualTo(1);
        assertThat(lruPeopleCache.getElementsIndex().get(person4.getId())).isEqualTo(2);
    }

    @Test
    public void given__get_element_from_cache__should_make_element_the_most_recently_used() {
        Person person1 = Person.builder().id(UUID.randomUUID()).firstName("Ade").lastName("Alonso").build();
        Person person2 = Person.builder().id(UUID.randomUUID()).firstName("Ade2").lastName("Alonso2").build();
        Person person3 = Person.builder().id(UUID.randomUUID()).firstName("Ade3").lastName("Alonso3").build();

        lruPeopleCache.addElement(person1);
        lruPeopleCache.addElement(person2);
        lruPeopleCache.addElement(person3);
        assertThat(lruPeopleCache.getElements()).containsExactly(person1, person2, person3);
        assertThat(lruPeopleCache.getElementsIndex().get(person1.getId())).isEqualTo(0);
        assertThat(lruPeopleCache.getElementsIndex().get(person2.getId())).isEqualTo(1);
        assertThat(lruPeopleCache.getElementsIndex().get(person3.getId())).isEqualTo(2);

        lruPeopleCache.getElement(person2.getId());
        assertThat(lruPeopleCache.getElements()).containsExactly(person1, person3, person2);
        assertThat(lruPeopleCache.getElementsIndex().get(person1.getId())).isEqualTo(0);
        assertThat(lruPeopleCache.getElementsIndex().get(person3.getId())).isEqualTo(1);
        assertThat(lruPeopleCache.getElementsIndex().get(person2.getId())).isEqualTo(2);

        lruPeopleCache.getElement(person1.getId());
        assertThat(lruPeopleCache.getElements()).containsExactly(person3, person2, person1);
        assertThat(lruPeopleCache.getElementsIndex().get(person3.getId())).isEqualTo(0);
        assertThat(lruPeopleCache.getElementsIndex().get(person2.getId())).isEqualTo(1);
        assertThat(lruPeopleCache.getElementsIndex().get(person1.getId())).isEqualTo(2);
    }

    @Test
    public void given__an_element_that_is_not_in_cache__should_add_to_cache() {
        assertThat(lruPeopleCache.getElement(UUID.randomUUID())).isEmpty();
    }
}