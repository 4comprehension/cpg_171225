package com.pivovarit.rental.web;

import com.pivovarit.rental.model.MovieAddRequest;
import com.pivovarit.rental.service.RentalService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        rentalService.addMovie(new MovieAddRequest(1, "Spiderman", "NEW"));
        rentalService.addMovie(new MovieAddRequest(2, "Tenet", "REGULAR"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldAddMovie() throws Exception {
        mockMvc.perform(get("/movies/{id}", 42)).andExpect(status().is(404));

        mockMvc.perform(post("/movies")
            .contentType("application/json")
            .content("""
              {"id": 42,"title": "Foo","type": "NEW"}"""))
          .andExpect(status().isOk());

        mockMvc.perform(get("/movies/{id}", 42))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.title", is("Foo")))
          .andExpect(jsonPath("$.id", is(42)))
          .andExpect(jsonPath("$.type", is("NEW")));
    }

    @Test
    void shouldRejectMovieWithNegativeId() throws Exception {
        mockMvc.perform(post("/movies")
            .contentType("application/json")
            .content("""
              {"id": -42,"title": "Foo","type": "NEW"}"""))
          .andExpect(status().is(400));
    }

    @Test
    void shouldGetMoviesIndependentlyOfOrder() throws Exception {
        mockMvc.perform(get("/movies"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", Matchers.hasSize(2)))
          .andExpect(jsonPath("$[?(@.title=='Spiderman')].id").value(hasItem(1)))
          .andExpect(jsonPath("$[?(@.title=='Spiderman')].type").value(hasItem("NEW")))
          .andExpect(jsonPath("$[?(@.title=='Tenet')].id").value(hasItem(2)))
          .andExpect(jsonPath("$[?(@.title=='Tenet')].type").value(hasItem("REGULAR")));
    }


    @Test
    void shouldGetMoviesByType() throws Exception {
        mockMvc.perform(get("/movies").param("type", "NEW"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", Matchers.hasSize(1)))
          .andExpect(jsonPath("$[0].title", is("Spiderman")))
          .andExpect(jsonPath("$[0].id", is(1)))
          .andExpect(jsonPath("$[0].type", is("NEW")));
    }

    @Test
    void shouldGetMoviesById() throws Exception {
        mockMvc.perform(get("/movies/{id}", 1))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.title", is("Spiderman")))
          .andExpect(jsonPath("$.id", is(1)))
          .andExpect(jsonPath("$.type", is("NEW")));
    }
}
