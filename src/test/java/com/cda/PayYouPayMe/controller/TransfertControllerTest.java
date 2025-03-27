package com.cda.PayYouPayMe.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cda.PayYouPayMe.model.Utilisateur;
import com.cda.PayYouPayMe.service.TransactionService;
import com.cda.PayYouPayMe.service.TransfertService;
import com.cda.PayYouPayMe.service.UtilisateurService;

@WebMvcTest(TransfertController.class)
public class TransfertControllerTest {
	
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransfertService transfertService;

    @MockitoBean
    private UtilisateurService utilisateurService;

    private Utilisateur utilisateurTest;
    private Utilisateur adminTest;
    
    @BeforeEach
    void setUp() {
        // Création d'un utilisateur standard pour les tests
        utilisateurTest = new Utilisateur();
        utilisateurTest.setId(1);
        utilisateurTest.setUsername("user1");
        utilisateurTest.setEmail("user1@example.com");
        utilisateurTest.setRole("USER");
    }    
    
    @Test
    public void getTransfertPageWithoutAuthentication() throws Exception{
    	mockMvc.perform(get("/transfert/"))
    		.andExpect(status().isUnauthorized());
    }
    
    @Test
    @WithMockUser(roles = "USER")
    public void testGetTransfertPageAsUser() throws Exception {
        when(utilisateurService.getCurrentUser()).thenReturn(utilisateurTest);
        mockMvc.perform(get("/transfert/"))
                .andExpect(status().isOk())
                .andExpect(view().name("transfert"))
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(model().attribute("utilisateur", utilisateurTest));;
    }
    
    @Test
    @WithMockUser(roles = "USER")
    public void testCreateTransfertAsUser() throws Exception {
        // Given
        float amount = 100.0f;
        String content = "Test transaction";

        // When
        when(utilisateurService.getCurrentUser()).thenReturn(utilisateurTest);

        // Then
        mockMvc.perform(post("/transfert/createTransfert")
                .param("amount", String.valueOf(amount))
                .param("content", content)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("transfert"));

    }
    

}
