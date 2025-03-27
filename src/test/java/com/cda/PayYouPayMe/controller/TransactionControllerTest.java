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
import com.cda.PayYouPayMe.service.UtilisateurService;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

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

        // Création d'un admin pour les tests
        adminTest = new Utilisateur();
        adminTest.setId(2);
        adminTest.setUsername("admin1");
        adminTest.setEmail("admin1@example.com");
        adminTest.setRole("ADMIN");
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetTransactionPageAsUser() throws Exception {
        // When
        when(utilisateurService.getCurrentUser()).thenReturn(utilisateurTest);

        // Then
        mockMvc.perform(get("/transaction/"))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction"))
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(model().attribute("utilisateur", utilisateurTest));

        verify(utilisateurService, times(1)).getCurrentUser();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetTransactionPageAsAdmin() throws Exception {
        // When
        when(utilisateurService.getCurrentUser()).thenReturn(adminTest);

        // Then
        mockMvc.perform(get("/transaction/"))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction"))
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(model().attribute("utilisateur", adminTest));

        verify(utilisateurService, times(1)).getCurrentUser();
    }

    @Test
    public void testGetTransactionPageUnauthenticated() throws Exception {
        // Then
        mockMvc.perform(get("/transaction/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testCreateTransactionAsUser() throws Exception {
        // Given
        String receiver = "user2";
        float amount = 100.0f;
        String content = "Test transaction";

        // When
        when(utilisateurService.getCurrentUser()).thenReturn(utilisateurTest);

        // Then
        mockMvc.perform(post("/transaction/createtransaction")
                .param("reciever", receiver)
                .param("amount", String.valueOf(amount))
                .param("content", content)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction"));

        verify(transactionService, times(1)).createTransaction(receiver, amount, content);
        verify(utilisateurService, times(1)).getCurrentUser();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateTransactionAsAdmin() throws Exception {
        // Given
        String receiver = "user2";
        float amount = 100.0f;
        String content = "Test transaction by admin";

        // When
        when(utilisateurService.getCurrentUser()).thenReturn(adminTest);

        // Then
        mockMvc.perform(post("/transaction/createtransaction")
                .param("reciever", receiver)
                .param("amount", String.valueOf(amount))
                .param("content", content)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction"));

        verify(transactionService, times(1)).createTransaction(receiver, amount, content);
        verify(utilisateurService, times(1)).getCurrentUser();
    }

    @Test
    public void testCreateTransactionUnauthenticated() throws Exception {
        // Then
        mockMvc.perform(post("/transaction/createtransaction")
                .param("reciever", "user2")
                .param("amount", "100.0")
                .param("content", "Test transaction")
                .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testCreateTransactionWithInvalidAmount() throws Exception {
        // Given
        String receiver = "user2";
        String content = "Test transaction";

        // When
        when(utilisateurService.getCurrentUser()).thenReturn(utilisateurTest);

        // Then
        mockMvc.perform(post("/transaction/createtransaction")
                .param("reciever", receiver)
                .param("amount", "invalidAmount") // Valeur non numérique
                .param("content", content)
                .with(csrf()))
                .andExpect(status().isBadRequest()); // Ou une autre réponse selon votre gestion d'erreur
    }
}