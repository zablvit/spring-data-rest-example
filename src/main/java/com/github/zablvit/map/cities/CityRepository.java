package com.github.zablvit.map.cities;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface CityRepository extends MongoRepository<City, String> {
    @RestResource(exported = false)
    List<City> findAllByCountry_Name(String countryName);

    @RestResource(path = "byCountryName", rel = "findAllByCountryName")
    Page<City> findAllByCountry_Name(String countryName, Pageable p);

    @RestResource(path = "byContinentName", rel = "findAllByContinentName")
    Page<City> findAllByCountry_Continent_Name(String continentName, Pageable p);
}
