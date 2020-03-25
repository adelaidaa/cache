package com.aaj.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class Person extends ElementWithId<UUID>{
    private final UUID id;
    private final String firstName;
    private final String lastName;
}
