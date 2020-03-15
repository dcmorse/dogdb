package org.launchcode.dogdb.web.rest;

import org.launchcode.dogdb.domain.Dogs;
import org.launchcode.dogdb.service.DogsService;
import org.launchcode.dogdb.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.launchcode.dogdb.domain.Dogs}.
 */
@RestController
@RequestMapping("/api")
public class DogsResource {

    private final Logger log = LoggerFactory.getLogger(DogsResource.class);

    private static final String ENTITY_NAME = "dogs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DogsService dogsService;

    public DogsResource(DogsService dogsService) {
        this.dogsService = dogsService;
    }

    /**
     * {@code POST  /dogs} : Create a new dogs.
     *
     * @param dogs the dogs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dogs, or with status {@code 400 (Bad Request)} if the dogs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dogs")
    public ResponseEntity<Dogs> createDogs(@Valid @RequestBody Dogs dogs) throws URISyntaxException {
        log.debug("REST request to save Dogs : {}", dogs);
        if (dogs.getId() != null) {
            throw new BadRequestAlertException("A new dogs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dogs result = dogsService.save(dogs);
        return ResponseEntity.created(new URI("/api/dogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dogs} : Updates an existing dogs.
     *
     * @param dogs the dogs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dogs,
     * or with status {@code 400 (Bad Request)} if the dogs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dogs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dogs")
    public ResponseEntity<Dogs> updateDogs(@Valid @RequestBody Dogs dogs) throws URISyntaxException {
        log.debug("REST request to update Dogs : {}", dogs);
        if (dogs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Dogs result = dogsService.save(dogs);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dogs.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /dogs} : get all the dogs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dogs in body.
     */
    @GetMapping("/dogs")
    public List<Dogs> getAllDogs() {
        log.debug("REST request to get all Dogs");
        return dogsService.findAll();
    }

    /**
     * {@code GET  /dogs/:id} : get the "id" dogs.
     *
     * @param id the id of the dogs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dogs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dogs/{id}")
    public ResponseEntity<Dogs> getDogs(@PathVariable Long id) {
        log.debug("REST request to get Dogs : {}", id);
        Optional<Dogs> dogs = dogsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dogs);
    }

    /**
     * {@code DELETE  /dogs/:id} : delete the "id" dogs.
     *
     * @param id the id of the dogs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dogs/{id}")
    public ResponseEntity<Void> deleteDogs(@PathVariable Long id) {
        log.debug("REST request to delete Dogs : {}", id);
        dogsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
