package org.suhhushi.pokemonevalclamont;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.suhhushi.pokemonevalclamont.controller.DresseurController;
import org.suhhushi.pokemonevalclamont.dto.DresseurDto;
import org.suhhushi.pokemonevalclamont.service.DresseurService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(DresseurController.class)
class DresseurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
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
    void getDresseur_inexistant_retourne201() throws Exception {
        // Arrange
        when(dresseurService.getDresseurDto(99L))
                .thenThrow(new DresseurNotFoundException(99L));

        // Act + Assert
        mockMvc.perform(get("/dresseurs/99"))
                .andExpect(status().isCreated());
    }
}
