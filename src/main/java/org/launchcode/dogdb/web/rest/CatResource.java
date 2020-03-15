package org.launchcode.dogdb.web.rest;

import org.launchcode.dogdb.domain.Cat;
import org.launchcode.dogdb.repository.CatRepository;
import org.launchcode.dogdb.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.launchcode.dogdb.domain.Cat}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CatResource {

    private final Logger log = LoggerFactory.getLogger(CatResource.class);

    private static final String ENTITY_NAME = "cat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatRepository catRepository;

    public CatResource(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    /**
     * {@code POST  /cats} : Create a new cat.
     *
     * @param cat the cat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cat, or with status {@code 400 (Bad Request)} if the cat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cats")
    public ResponseEntity<Cat> createCat(@RequestBody Cat cat) throws URISyntaxException {
        log.debug("REST request to save Cat : {}", cat);
        if (cat.getId() != null) {
            throw new BadRequestAlertException("A new cat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cat result = catRepository.save(cat);
        return ResponseEntity.created(new URI("/api/cats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cats} : Updates an existing cat.
     *
     * @param cat the cat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cat,
     * or with status {@code 400 (Bad Request)} if the cat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cats")
    public ResponseEntity<Cat> updateCat(@RequestBody Cat cat) throws URISyntaxException {
        log.debug("REST request to update Cat : {}", cat);
        if (cat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cat result = catRepository.save(cat);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cat.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cats} : get all the cats.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cats in body.
     */
    @GetMapping("/cats")
    public List<Cat> getAllCats() {
        log.debug("REST request to get all Cats");
        return catRepository.findAll();
    }

    /**
     * {@code GET  /cats/:id} : get the "id" cat.
     *
     * @param id the id of the cat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cats/{id}")
    public ResponseEntity<Cat> getCat(@PathVariable Long id) {
        log.debug("REST request to get Cat : {}", id);
        Optional<Cat> cat = catRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cat);
    }

    /**
     * {@code DELETE  /cats/:id} : delete the "id" cat.
     *
     * @param id the id of the cat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cats/{id}")
    public ResponseEntity<Void> deleteCat(@PathVariable Long id) {
        log.debug("REST request to delete Cat : {}", id);
        catRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
