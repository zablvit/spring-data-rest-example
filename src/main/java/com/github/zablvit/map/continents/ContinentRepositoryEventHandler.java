package com.github.zablvit.map.continents;

import com.github.zablvit.map.exceptions.DuplicateResourceException;
import com.github.zablvit.map.exceptions.ResourceStillInUseException;
import com.github.zablvit.map.countries.CountryRepository;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(Continent.class)
public class ContinentRepositoryEventHandler {

    private final CountryRepository countryRepository;
    private final ContinentRepository continentRepository;

    public ContinentRepositoryEventHandler(CountryRepository countryRepository, ContinentRepository continentRepository) {
        this.countryRepository = countryRepository;
        this.continentRepository = continentRepository;
    }

    @HandleBeforeCreate
    public void checkContinentIsUniqueBeforeCreate(Continent continent) {
        if (continentRepository.findByName(continent.getName()).isPresent()) {
            throw new DuplicateResourceException(String.format("Continent with name %s already exists", continent.getName()));
        }
    }

    @HandleBeforeDelete
    public void checkContinentHasNoCountry(Continent continent) {
        if (!countryRepository.findAllByContinent(continent).isEmpty()) {
            throw new ResourceStillInUseException(String.format(
                    "Unable to delete continent %s. Still in use.", continent.getName()
            ));
        }
    }
}
