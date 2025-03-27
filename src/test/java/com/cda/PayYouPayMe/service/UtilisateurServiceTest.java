package com.cda.PayYouPayMe.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cda.PayYouPayMe.model.Utilisateur;
import com.cda.PayYouPayMe.repository.UtilisateurRepository;

public class UtilisateurServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private AuthentificationService authService;

    @InjectMocks
    private UtilisateurService utilisateurService;

    private Utilisateur currentUser;
    private Utilisateur userToAdd;
    private List<Utilisateur> contacts;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        currentUser = new Utilisateur();
        currentUser.setUsername("currentUser");
        userToAdd = new Utilisateur();
        userToAdd.setUsername("userToAdd");
        contacts = Arrays.asList(userToAdd);
        currentUser.setContact(contacts);

        when(authService.getCurrentUsername()).thenReturn("currentUser");
        when(utilisateurRepository.findByUsername("currentUser")).thenReturn(Optional.of(currentUser));
        when(utilisateurRepository.findByUsername("userToAdd")).thenReturn(Optional.of(userToAdd));
    }

    @Test
    public void testGetCurrentUser() {
        Utilisateur user = utilisateurService.getCurrentUser();
        assertNotNull(user);
        assertEquals("currentUser", user.getUsername());
    }

    @Test
    public void testGetAllUtilisateurs() {
        List<Utilisateur> users = Arrays.asList(currentUser, userToAdd);
        when(utilisateurRepository.findAll()).thenReturn(users);

        List<Utilisateur> result = utilisateurService.getAllUtilisateurs();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testUpdateUser() {
        Utilisateur userToSave = new Utilisateur();
        userToSave.setFirstName("NewFirstName");
        userToSave.setLastName("NewLastName");
        userToSave.setEmail("newemail@example.com");

        utilisateurService.updateUser(userToSave);

        verify(utilisateurRepository, times(1)).save(currentUser);
        assertEquals("NewFirstName", currentUser.getFirstName());
        assertEquals("NewLastName", currentUser.getLastName());
        assertEquals("newemail@example.com", currentUser.getEmail());
    }

    @Test
    public void testAddUserToContactList() {
        utilisateurService.addUserToContactList("userToAdd");

        verify(utilisateurRepository, times(1)).save(currentUser);
        assertTrue(currentUser.getContact().contains(userToAdd));
    }

    @Test
    public void testGetUserByUserName() {
        Utilisateur user = utilisateurService.getUserByUserName("userToAdd");
        assertNotNull(user);
        assertEquals("userToAdd", user.getUsername());
    }
}
