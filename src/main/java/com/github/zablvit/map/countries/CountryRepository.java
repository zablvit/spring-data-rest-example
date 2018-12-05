package com.github.zablvit.map.countries;

import com.github.zablvit.map.continents.Continent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends MongoRepository<Country, String> {
    @RestResource(exported = false)
    List<Country> findAllByContinent(Continent continent);

    @RestResource(exported = false)
    Optional<Country> findFirstByName(String countryName);

    @RestResource(path = "byContinentName", rel = "findAllByContinentName")
    Page<Country> findAllByContinent_Name(String continentName, Pageable p);
}
