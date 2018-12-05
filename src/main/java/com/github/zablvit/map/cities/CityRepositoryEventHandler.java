package com.github.zablvit.map.cities;

import com.github.zablvit.map.exceptions.ResourceNotFoundException;
import com.github.zablvit.map.countries.Country;
import com.github.zablvit.map.countries.CountryRepository;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RepositoryEventHandler(City.class)
public class CityRepositoryEventHandler {

    private final CountryRepository countryRepository;

    public CityRepositoryEventHandler(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @HandleBeforeSave
    @HandleBeforeCreate
    public void checkCountryExistsBeforeSaveOrCreate(City city) {
        Optional.ofNullable(city.getCountry())
                .orElseThrow(() -> new ResourceNotFoundException("Country is empty."));

        Country country =
                countryRepository.findFirstByName(city.getCountry().getName())
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        String.format("Country with name %s not found.",
                                                city.getCountry().getName())));

        city.setCountry(country);
    }
}
