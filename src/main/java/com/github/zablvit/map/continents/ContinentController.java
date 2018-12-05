package com.github.zablvit.map.continents;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RepositoryRestController
public class ContinentController {

    @PutMapping("/continents/{id}")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public void ignorePutMethod(@PathVariable("id") String id) {
    }

    @PatchMapping("/continents/{id}")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public void ignorePatchMethod(@PathVariable("id") String id) {
    }
}
