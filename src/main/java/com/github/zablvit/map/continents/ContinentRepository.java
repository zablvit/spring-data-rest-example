package com.github.zablvit.map.continents;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

public interface ContinentRepository extends MongoRepository<Continent, String> {
    @RestResource(exported = false)
    Optional<Continent> findByName(String continentName);
}
