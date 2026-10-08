package com.ligue.pokemon.controller;

import com.ligue.pokemon.dto.DresseurDto;
import com.ligue.pokemon.exception.DresseurNotFoundException;
import com.ligue.pokemon.service.DresseurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DresseurController.class)
class DresseurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DresseurService dresseurService;

    @Test
    void getDresseur_existant_retourne200EtLeDto() throws Exception {
        // Arrange
        when(dresseurService.getDresseurDto(1L))
                .thenReturn(new DresseurDto("Sacha", 3, 135));

        // Act + Assert
        mockMvc.perform(get("/dresseurs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomDresseur").value("Sacha"))
                .andExpect(jsonPath("$.nombrePokemons").value(3))
                .andExpect(jsonPath("$.niveauDresseur").value(135));
    }

    @Test
    void getDresseur_inexistant_retourne404() throws Exception {
        // Arrange
        when(dresseurService.getDresseurDto(99L))
                .thenThrow(new DresseurNotFoundException(99L));

        // Act + Assert
        mockMvc.perform(get("/dresseurs/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getDresseur_idNonNumerique_retourne400() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/dresseurs/abc"))
                .andExpect(status().isBadRequest());
    }
}
