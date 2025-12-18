package com.pivovarit.rental.web;

import com.pivovarit.rental.model.MovieAddRequest;
import com.pivovarit.rental.service.RentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    }

    @Test
    void shouldGetMovies() throws Exception {
        mockMvc.perform(get("/movies"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].title").value("Spiderman"))
          .andExpect(jsonPath("$[0].id").value("1"))
          .andExpect(jsonPath("$[0].type").value("NEW"));
    }
}
