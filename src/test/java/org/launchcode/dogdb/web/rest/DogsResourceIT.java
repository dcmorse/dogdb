package org.launchcode.dogdb.web.rest;

import org.launchcode.dogdb.DogdbApp;
import org.launchcode.dogdb.domain.Dogs;
import org.launchcode.dogdb.repository.DogsRepository;
import org.launchcode.dogdb.service.DogsService;
import org.launchcode.dogdb.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.launchcode.dogdb.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DogsResource} REST controller.
 */
@SpringBootTest(classes = DogdbApp.class)
public class DogsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private DogsRepository dogsRepository;

    @Autowired
    private DogsService dogsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDogsMockMvc;

    private Dogs dogs;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DogsResource dogsResource = new DogsResource(dogsService);
        this.restDogsMockMvc = MockMvcBuilders.standaloneSetup(dogsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dogs createEntity(EntityManager em) {
        Dogs dogs = new Dogs()
            .name(DEFAULT_NAME);
        return dogs;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dogs createUpdatedEntity(EntityManager em) {
        Dogs dogs = new Dogs()
            .name(UPDATED_NAME);
        return dogs;
    }

    @BeforeEach
    public void initTest() {
        dogs = createEntity(em);
    }

    @Test
    @Transactional
    public void createDogs() throws Exception {
        int databaseSizeBeforeCreate = dogsRepository.findAll().size();

        // Create the Dogs
        restDogsMockMvc.perform(post("/api/dogs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dogs)))
            .andExpect(status().isCreated());

        // Validate the Dogs in the database
        List<Dogs> dogsList = dogsRepository.findAll();
        assertThat(dogsList).hasSize(databaseSizeBeforeCreate + 1);
        Dogs testDogs = dogsList.get(dogsList.size() - 1);
        assertThat(testDogs.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createDogsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dogsRepository.findAll().size();

        // Create the Dogs with an existing ID
        dogs.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDogsMockMvc.perform(post("/api/dogs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dogs)))
            .andExpect(status().isBadRequest());

        // Validate the Dogs in the database
        List<Dogs> dogsList = dogsRepository.findAll();
        assertThat(dogsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dogsRepository.findAll().size();
        // set the field null
        dogs.setName(null);

        // Create the Dogs, which fails.

        restDogsMockMvc.perform(post("/api/dogs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dogs)))
            .andExpect(status().isBadRequest());

        List<Dogs> dogsList = dogsRepository.findAll();
        assertThat(dogsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDogs() throws Exception {
        // Initialize the database
        dogsRepository.saveAndFlush(dogs);

        // Get all the dogsList
        restDogsMockMvc.perform(get("/api/dogs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dogs.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getDogs() throws Exception {
        // Initialize the database
        dogsRepository.saveAndFlush(dogs);

        // Get the dogs
        restDogsMockMvc.perform(get("/api/dogs/{id}", dogs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dogs.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingDogs() throws Exception {
        // Get the dogs
        restDogsMockMvc.perform(get("/api/dogs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDogs() throws Exception {
        // Initialize the database
        dogsService.save(dogs);

        int databaseSizeBeforeUpdate = dogsRepository.findAll().size();

        // Update the dogs
        Dogs updatedDogs = dogsRepository.findById(dogs.getId()).get();
        // Disconnect from session so that the updates on updatedDogs are not directly saved in db
        em.detach(updatedDogs);
        updatedDogs
            .name(UPDATED_NAME);

        restDogsMockMvc.perform(put("/api/dogs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDogs)))
            .andExpect(status().isOk());

        // Validate the Dogs in the database
        List<Dogs> dogsList = dogsRepository.findAll();
        assertThat(dogsList).hasSize(databaseSizeBeforeUpdate);
        Dogs testDogs = dogsList.get(dogsList.size() - 1);
        assertThat(testDogs.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingDogs() throws Exception {
        int databaseSizeBeforeUpdate = dogsRepository.findAll().size();

        // Create the Dogs

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDogsMockMvc.perform(put("/api/dogs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dogs)))
            .andExpect(status().isBadRequest());

        // Validate the Dogs in the database
        List<Dogs> dogsList = dogsRepository.findAll();
        assertThat(dogsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDogs() throws Exception {
        // Initialize the database
        dogsService.save(dogs);

        int databaseSizeBeforeDelete = dogsRepository.findAll().size();

        // Delete the dogs
        restDogsMockMvc.perform(delete("/api/dogs/{id}", dogs.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dogs> dogsList = dogsRepository.findAll();
        assertThat(dogsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
