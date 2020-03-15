package org.launchcode.dogdb.web.rest;

import org.launchcode.dogdb.DogdbApp;
import org.launchcode.dogdb.domain.Cat;
import org.launchcode.dogdb.repository.CatRepository;
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
 * Integration tests for the {@link CatResource} REST controller.
 */
@SpringBootTest(classes = DogdbApp.class)
public class CatResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CatRepository catRepository;

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

    private MockMvc restCatMockMvc;

    private Cat cat;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CatResource catResource = new CatResource(catRepository);
        this.restCatMockMvc = MockMvcBuilders.standaloneSetup(catResource)
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
    public static Cat createEntity(EntityManager em) {
        Cat cat = new Cat()
            .name(DEFAULT_NAME);
        return cat;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cat createUpdatedEntity(EntityManager em) {
        Cat cat = new Cat()
            .name(UPDATED_NAME);
        return cat;
    }

    @BeforeEach
    public void initTest() {
        cat = createEntity(em);
    }

    @Test
    @Transactional
    public void createCat() throws Exception {
        int databaseSizeBeforeCreate = catRepository.findAll().size();

        // Create the Cat
        restCatMockMvc.perform(post("/api/cats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cat)))
            .andExpect(status().isCreated());

        // Validate the Cat in the database
        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeCreate + 1);
        Cat testCat = catList.get(catList.size() - 1);
        assertThat(testCat.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = catRepository.findAll().size();

        // Create the Cat with an existing ID
        cat.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatMockMvc.perform(post("/api/cats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cat)))
            .andExpect(status().isBadRequest());

        // Validate the Cat in the database
        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCats() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        // Get all the catList
        restCatMockMvc.perform(get("/api/cats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cat.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getCat() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        // Get the cat
        restCatMockMvc.perform(get("/api/cats/{id}", cat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cat.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingCat() throws Exception {
        // Get the cat
        restCatMockMvc.perform(get("/api/cats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCat() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        int databaseSizeBeforeUpdate = catRepository.findAll().size();

        // Update the cat
        Cat updatedCat = catRepository.findById(cat.getId()).get();
        // Disconnect from session so that the updates on updatedCat are not directly saved in db
        em.detach(updatedCat);
        updatedCat
            .name(UPDATED_NAME);

        restCatMockMvc.perform(put("/api/cats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCat)))
            .andExpect(status().isOk());

        // Validate the Cat in the database
        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeUpdate);
        Cat testCat = catList.get(catList.size() - 1);
        assertThat(testCat.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCat() throws Exception {
        int databaseSizeBeforeUpdate = catRepository.findAll().size();

        // Create the Cat

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatMockMvc.perform(put("/api/cats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cat)))
            .andExpect(status().isBadRequest());

        // Validate the Cat in the database
        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCat() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        int databaseSizeBeforeDelete = catRepository.findAll().size();

        // Delete the cat
        restCatMockMvc.perform(delete("/api/cats/{id}", cat.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
