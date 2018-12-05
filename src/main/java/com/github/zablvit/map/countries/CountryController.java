package com.github.zablvit.map.countries;


import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RepositoryRestController
public class CountryController {

    @PutMapping("/countries/{id}")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public void ignorePutMethod(@PathVariable("id") String id) {
    }

    @PatchMapping("/countries/{id}")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public void ignorePatchMethod(@PathVariable("id") String id) {
    }
}
