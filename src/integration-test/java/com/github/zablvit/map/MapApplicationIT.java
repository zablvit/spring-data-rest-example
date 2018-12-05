package com.github.zablvit.map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.github.zablvit.map.cities.City;
import com.github.zablvit.map.continents.Continent;
import com.github.zablvit.map.countries.Country;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkDiscoverer;
import org.springframework.hateoas.hal.HalLinkDiscoverer;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MapApplicationIT {

    private final String CONTINENTS_ENDPOINT = "/continents";
    private final String COUNTRIES_ENDPOINT = "/countries";
    private final String CITIES_ENDPOINT = "/cities";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MongoTemplate mongoTemplate;


    @After
    public void tearDown() {
        mongoTemplate.dropCollection(City.class);
        mongoTemplate.dropCollection(Country.class);
        mongoTemplate.dropCollection(Continent.class);
    }

    @Test
    public void shouldCreateAndDeleteContinent() throws Exception {
        Continent continent = newContinent("Europe");

        String continentURL = createResourceForStatus(continent, CONTINENTS_ENDPOINT, status().isCreated())
                .getRedirectedUrl();

        String continentJsonString = queryResource(continentURL)
                .getContentAsString();

        assertEquals("Europe", JsonPath.read(continentJsonString, "$.name"));

        deleteResourceForStatus(continentURL, status().isNoContent());
    }


    @Test
    public void shouldNotCreateCountryWithoutContinent() throws Exception {
        String name = "Some Country Without Continent";
        Country country = newCountry(name, null);

        createResourceForStatus(country, COUNTRIES_ENDPOINT, status().isBadRequest());
    }

    @Test
    public void shouldCreateAndDeleteCountry() throws Exception {
        Continent continent = newContinent("Europe");
        Country country = newCountry("Some Country", continent);

        String continentUrl = createResourceForStatus(continent, CONTINENTS_ENDPOINT, status().isCreated())
                .getRedirectedUrl();

        String countryUrl = createResourceForStatus(country, COUNTRIES_ENDPOINT, status().isCreated())
                .getRedirectedUrl();

        String countryJsonString = queryResource(countryUrl)
                .getContentAsString();

        assertEquals("Some Country", JsonPath.read(countryJsonString, "$.name"));
        assertEquals("Europe", JsonPath.read(countryJsonString, "$.continent.name"));

        assertJsonContainsRelToResource(countryJsonString, "continent", continentUrl);

        deleteResourceForStatus(countryUrl, status().isNoContent());
    }


    @Test
    public void shouldCreateMultipleCountriesOnSameContinent() throws Exception {
        Continent australia = newContinent("Australia");
        Continent northAmerica = newContinent("North America");


        Country usa = newCountry("USA", northAmerica);
        Country canada = newCountry("Canada", northAmerica);
        Country newZealanad = newCountry("New Zealanad", australia);


        createResourceForStatus(australia, CONTINENTS_ENDPOINT, status().isCreated());
        createResourceForStatus(northAmerica, CONTINENTS_ENDPOINT, status().isCreated());

        createResourceForStatus(usa, COUNTRIES_ENDPOINT, status().isCreated());
        createResourceForStatus(canada, COUNTRIES_ENDPOINT, status().isCreated());
        createResourceForStatus(newZealanad, COUNTRIES_ENDPOINT, status().isCreated());
    }

    @Test
    public void shouldOnlyAcceptContinentsWithUniqueNames() throws Exception {
        Continent continent = newContinent("Australia");

        createResourceForStatus(continent, CONTINENTS_ENDPOINT, status().isCreated());
        createResourceForStatus(continent, CONTINENTS_ENDPOINT, status().isConflict());
    }

    @Test
    public void shouldOnlyAcceptCountriesWithUniqueNames() throws Exception {
        Continent continent = newContinent("Australia");

        Country country = newCountry("New Zealand", continent);

        createResourceForStatus(continent, CONTINENTS_ENDPOINT, status().isCreated());

        createResourceForStatus(country, COUNTRIES_ENDPOINT, status().isCreated());
        createResourceForStatus(country, COUNTRIES_ENDPOINT, status().isConflict());
    }


    @Test
    public void shouldNotDeleteContinentWithCountries() throws Exception {
        Continent continent = newContinent("Europe");
        Country country = newCountry("Some Country", continent);

        String continentUrl = createResourceForStatus(continent, CONTINENTS_ENDPOINT, status().isCreated())
                .getRedirectedUrl();

        createResourceForStatus(country, COUNTRIES_ENDPOINT, status().isCreated())
                .getRedirectedUrl();

        deleteResourceForStatus(continentUrl, status().isBadRequest());
    }

    @Test
    public void shouldCreateAndDeleteCity() throws Exception {
        Continent continent = newContinent("Africa");
        Country country = newCountry("Egypt", continent);
        City city = newCity("Cairo", country);

        String continentUrl = createResourceForStatus(continent, CONTINENTS_ENDPOINT, status().isCreated())
                .getRedirectedUrl();

        String countryUrl = createResourceForStatus(country, COUNTRIES_ENDPOINT, status().isCreated())
                .getRedirectedUrl();

        String cityUrl = createResourceForStatus(city, CITIES_ENDPOINT, status().isCreated())
                .getRedirectedUrl();

        String cityJsonString = queryResource(cityUrl)
                .getContentAsString();

        assertEquals("Cairo", JsonPath.read(cityJsonString, "$.name"));
        assertEquals("Egypt", JsonPath.read(cityJsonString, "$.country.name"));
        assertEquals("Africa", JsonPath.read(cityJsonString, "$.country.continent.name"));

        assertJsonContainsRelToResource(cityJsonString, "country", countryUrl);
        assertJsonContainsRelToResource(cityJsonString, "continent", continentUrl);

        deleteResourceForStatus(cityUrl, status().isNoContent());
    }

    @Test
    public void shouldNotDeleteCountryWithCities() throws Exception {
        Continent continent = newContinent("Africa");
        Country country = newCountry("Egypt", continent);
        City city = newCity("Cairo", country);

        createResourceForStatus(continent, CONTINENTS_ENDPOINT, status().isCreated());

        String countryUrl = createResourceForStatus(country, COUNTRIES_ENDPOINT, status().isCreated())
                .getRedirectedUrl();

        createResourceForStatus(city, CITIES_ENDPOINT, status().isCreated());

        deleteResourceForStatus(countryUrl, status().isBadRequest());
    }

    @Test
    public void shouldNotDeleteCountryWithCity() throws Exception {
        Continent continent = newContinent("Africa");
        Country country = newCountry("Egypt", continent);
        City city = newCity("Hurghada", country);

        createResourceForStatus(continent, CONTINENTS_ENDPOINT, status().isCreated())
                .getRedirectedUrl();

        String countryUrl = createResourceForStatus(country, COUNTRIES_ENDPOINT, status().isCreated())
                .getRedirectedUrl();

        createResourceForStatus(city, CITIES_ENDPOINT, status().isCreated())
                .getRedirectedUrl();

        deleteResourceForStatus(countryUrl, status().isBadRequest());
    }

    @Test
    public void shouldSearchCountriesByContinentName() throws Exception {
        Continent africa = newContinent("Africa");
        Continent europe = newContinent("Europe");

        Country guinea = newCountry("Guinea", africa);
        Country morocco = newCountry("Morocco", africa);

        Country poland = newCountry("Poland", europe);


        createResourceForStatus(africa, CONTINENTS_ENDPOINT, status().isCreated());
        createResourceForStatus(europe, CONTINENTS_ENDPOINT, status().isCreated());

        createResourceForStatus(guinea, COUNTRIES_ENDPOINT, status().isCreated());
        createResourceForStatus(morocco, COUNTRIES_ENDPOINT, status().isCreated());
        createResourceForStatus(poland, COUNTRIES_ENDPOINT, status().isCreated());


        String africanCountriesJsonString = queryResource(COUNTRIES_ENDPOINT + "/search/byContinentName?continentName=Africa&sort=name,asc")
                .getContentAsString();

        assertEquals("Guinea", JsonPath.read(africanCountriesJsonString, "$._embedded.countries[0].name"));
        assertEquals("Morocco", JsonPath.read(africanCountriesJsonString, "$._embedded.countries[1].name"));
        assertEquals(2, ((List) JsonPath.read(africanCountriesJsonString, "$._embedded.countries")).size());
    }

    @Test
    public void shouldSearchCitiesByCountryName() throws Exception {
        Continent europe = newContinent("Europe");

        Country poland = newCountry("Poland", europe);
        Country germany = newCountry("Germany", europe);

        City warsaw = newCity("Warsaw", poland);
        City berlin = newCity("Berlin", germany);
        City frankfurt = newCity("Frankfurt", germany);


        createResourceForStatus(europe, CONTINENTS_ENDPOINT, status().isCreated());

        createResourceForStatus(poland, COUNTRIES_ENDPOINT, status().isCreated());
        createResourceForStatus(germany, COUNTRIES_ENDPOINT, status().isCreated());

        createResourceForStatus(warsaw, CITIES_ENDPOINT, status().isCreated());
        createResourceForStatus(frankfurt, CITIES_ENDPOINT, status().isCreated());
        createResourceForStatus(berlin, CITIES_ENDPOINT, status().isCreated());


        String germanCitiesJsonString = queryResource(CITIES_ENDPOINT + "/search/byCountryName?countryName=Germany&sort=name,asc")
                .getContentAsString();

        assertEquals("Berlin", JsonPath.read(germanCitiesJsonString, "$._embedded.cities[0].name"));
        assertEquals("Frankfurt", JsonPath.read(germanCitiesJsonString, "$._embedded.cities[1].name"));
        assertEquals(2, ((List) JsonPath.read(germanCitiesJsonString, "$._embedded.cities")).size());
    }

    @Test
    public void shouldSearchCitiesByContinentName() throws Exception {
        Continent europe = newContinent("Europe");
        Continent africa = newContinent("Africa");

        Country poland = newCountry("Poland", europe);
        Country germany = newCountry("Germany", europe);
        Country morocco = newCountry("Morocco", africa);

        City warsaw = newCity("Warsaw", poland);
        City berlin = newCity("Berlin", germany);
        City frankfurt = newCity("Frankfurt", germany);
        City casablanca = newCity("Casablanca", morocco);


        createResourceForStatus(europe, CONTINENTS_ENDPOINT, status().isCreated());
        createResourceForStatus(africa, CONTINENTS_ENDPOINT, status().isCreated());

        createResourceForStatus(poland, COUNTRIES_ENDPOINT, status().isCreated());
        createResourceForStatus(germany, COUNTRIES_ENDPOINT, status().isCreated());
        createResourceForStatus(morocco, COUNTRIES_ENDPOINT, status().isCreated());

        createResourceForStatus(warsaw, CITIES_ENDPOINT, status().isCreated());
        createResourceForStatus(berlin, CITIES_ENDPOINT, status().isCreated());
        createResourceForStatus(frankfurt, CITIES_ENDPOINT, status().isCreated());
        createResourceForStatus(casablanca, CITIES_ENDPOINT, status().isCreated());


        String europeanCitiesJsonString = queryResource(CITIES_ENDPOINT + "/search/byContinentName?continentName=Europe&sort=name,asc")
                .getContentAsString();

        assertEquals("Berlin", JsonPath.read(europeanCitiesJsonString, "$._embedded.cities[0].name"));
        assertEquals("Frankfurt", JsonPath.read(europeanCitiesJsonString, "$._embedded.cities[1].name"));
        assertEquals("Warsaw", JsonPath.read(europeanCitiesJsonString, "$._embedded.cities[2].name"));
        assertEquals(3, ((List) JsonPath.read(europeanCitiesJsonString, "$._embedded.cities")).size());
    }


    @Test
    public void shouldNotAllowContinentUpdate() throws Exception {
        Continent originalContinent = newContinent("Europe");

        Continent modifiedContinent = newContinent("Africa");

        String modifiedContinentJsonString = mapper.writeValueAsString(modifiedContinent);

        String continentUrlString = createResourceForStatus(originalContinent, CONTINENTS_ENDPOINT, status().isCreated())
                .getRedirectedUrl();

        mockMvc.perform(
                put(continentUrlString)
                        .content(modifiedContinentJsonString))
                .andExpect(status().isMethodNotAllowed());

        mockMvc.perform(
                patch(continentUrlString)
                        .content(modifiedContinentJsonString))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void shouldNotAllowCountryUpdate() throws Exception {
        Continent continent = newContinent("Europe");

        Country originalCountry = newCountry("Poland", continent);

        Country modifiedCountry = newCountry("Germany", continent);

        String modifiedCountryJsonString = mapper.writeValueAsString(modifiedCountry);

        createResourceForStatus(continent, CONTINENTS_ENDPOINT, status().isCreated());

        String countryUrlString = createResourceForStatus(originalCountry, COUNTRIES_ENDPOINT, status().isCreated())
                .getRedirectedUrl();

        mockMvc.perform(
                put(countryUrlString)
                        .content(modifiedCountryJsonString))
                .andExpect(status().isMethodNotAllowed());

        mockMvc.perform(
                patch(countryUrlString)
                        .content(modifiedCountryJsonString))
                .andExpect(status().isMethodNotAllowed());
    }

    private MockHttpServletResponse createResourceForStatus(Object payload, String requestUrl, ResultMatcher expectedResult) throws Exception {
        String payloadJson = mapper.writeValueAsString(payload);

        return mockMvc.perform(
                post(requestUrl)
                        .content(payloadJson))
                .andDo(print())
                .andExpect(expectedResult)
                .andReturn()
                .getResponse();
    }

    private MockHttpServletResponse queryResource(String resourceURL) throws Exception {
        return mockMvc.perform(get(resourceURL))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
    }

    private void deleteResourceForStatus(String resourceUrl, ResultMatcher expectedResult) throws Exception {
        mockMvc.perform(delete(resourceUrl))
                .andExpect(expectedResult);
    }

    private Continent newContinent(String continentName) {
        Continent continent = new Continent();
        continent.setName(continentName);
        return continent;
    }

    private Country newCountry(String countryName, Continent continent) {
        Country country = new Country();
        country.setContinent(continent);
        country.setName(countryName);
        return country;
    }

    private City newCity(String cityName, Country country) {
        City city = new City();
        city.setName(cityName);
        city.setCountry(country);
        return city;
    }

    private void assertJsonContainsRelToResource(String jsonString, String rel, String resourceUrl) {
        LinkDiscoverer discoverer = new HalLinkDiscoverer();
        Link link = discoverer.findLinkWithRel(rel, jsonString);
        assertEquals(rel, link.getRel());
        assertEquals(resourceUrl, link.getHref());
    }
}