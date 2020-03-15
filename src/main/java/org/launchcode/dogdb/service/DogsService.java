package org.launchcode.dogdb.service;

import org.launchcode.dogdb.domain.Dogs;
import org.launchcode.dogdb.repository.DogsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Dogs}.
 */
@Service
@Transactional
public class DogsService {

    private final Logger log = LoggerFactory.getLogger(DogsService.class);

    private final DogsRepository dogsRepository;

    public DogsService(DogsRepository dogsRepository) {
        this.dogsRepository = dogsRepository;
    }

    /**
     * Save a dogs.
     *
     * @param dogs the entity to save.
     * @return the persisted entity.
     */
    public Dogs save(Dogs dogs) {
        log.debug("Request to save Dogs : {}", dogs);
        return dogsRepository.save(dogs);
    }

    /**
     * Get all the dogs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Dogs> findAll() {
        log.debug("Request to get all Dogs");
        return dogsRepository.findAll();
    }

    /**
     * Get one dogs by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Dogs> findOne(Long id) {
        log.debug("Request to get Dogs : {}", id);
        return dogsRepository.findById(id);
    }

    /**
     * Delete the dogs by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Dogs : {}", id);
        dogsRepository.deleteById(id);
    }
}
