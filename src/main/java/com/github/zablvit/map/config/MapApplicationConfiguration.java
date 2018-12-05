package com.github.zablvit.map.config;

import com.github.zablvit.map.cities.City;
import com.github.zablvit.map.continents.Continent;
import com.github.zablvit.map.countries.Country;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;

@SuppressWarnings("Convert2Lambda") // Spring has trouble resolving Types when using lambdas here
@Configuration
public class MapApplicationConfiguration {
    private final RepositoryEntityLinks entityLinks;

    public MapApplicationConfiguration(RepositoryEntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }

    @Bean
    public ResourceProcessor<Resource<Country>> countryProcessor() {

        return new ResourceProcessor<Resource<Country>>() {

            @Override
            public Resource<Country> process(Resource<Country> resource) {

                resource.add(entityLinks.linkToSingleResource(Continent.class, resource.getContent().getContinent().getId())
                        .withRel("continent"));
                return resource;
            }
        };
    }


    @Bean
    public ResourceProcessor<Resource<City>> cityProcessor() {

        return new ResourceProcessor<Resource<City>>() {

            @Override
            public Resource<City> process(Resource<City> resource) {

                resource.add(entityLinks.linkToSingleResource(Country.class, resource.getContent().getCountry().getId())
                        .withRel("country"));
                resource.add(entityLinks.linkToSingleResource(Continent.class, resource.getContent().getCountry().getContinent().getId())
                        .withRel("continent"));
                return resource;
            }
        };
    }

}
