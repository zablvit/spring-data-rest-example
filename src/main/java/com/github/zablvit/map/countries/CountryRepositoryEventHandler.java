package com.github.zablvit.map.countries;

import com.github.zablvit.map.continents.Continent;
import com.github.zablvit.map.exceptions.DuplicateResourceException;
import com.github.zablvit.map.exceptions.ResourceStillInUseException;
import com.github.zablvit.map.cities.CityRepository;
import com.github.zablvit.map.continents.ContinentRepository;
import com.github.zablvit.map.exceptions.ResourceNotFoundException;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RepositoryEventHandler
public class CountryRepositoryEventHandler {

    private final ContinentRepository continentRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    public CountryRepositoryEventHandler(ContinentRepository continentRepository, CountryRepository countryRepository, CityRepository cityRepository) {
        this.continentRepository = continentRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
    }

    @HandleBeforeCreate
    public void checkCountryIsUniqueAndContinentExistsBeforeCreate(Country country) {
        if (countryRepository.findFirstByName(country.getName()).isPresent()) {
            throw new DuplicateResourceException(String.format("Country with name %s already exists", country.getName()));
        }

        Optional.ofNullable(country.getContinent())
                .orElseThrow(() -> new ResourceNotFoundException("Continent is empty."));

        Continent continent =
                continentRepository.findByName(country.getContinent().getName())
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        String.format("Continent with name %s not found.",
                                                country.getContinent().getName())));

        country.setContinent(continent);
    }

    @HandleBeforeDelete
    public void checkCountryHasNoCity(Country country) {
        if (!cityRepository.findAllByCountry_Name(country.getName()).isEmpty()) {
            throw new ResourceStillInUseException(String.format(
                    "Unable to delete country %s. Still in use.", country.getName()
            ));
        }
    }
}