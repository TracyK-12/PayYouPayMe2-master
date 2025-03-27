package com.cda.PayYouPayMe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cda.PayYouPayMe.model.Transaction;
import com.cda.PayYouPayMe.model.Utilisateur;
import com.cda.PayYouPayMe.repository.TransactionRepository;
import com.cda.PayYouPayMe.repository.UtilisateurRepository;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AuthentificationService authentificationService;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private UtilisateurService utilisateurService;

    @InjectMocks
    private TransactionService transactionService;

    private Utilisateur sender;
    private Utilisateur receiver;
    private Utilisateur admin;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        // Configuration des utilisateurs
        sender = new Utilisateur();
        sender.setId(1);
        sender.setUsername("sender");
        sender.setEmail("sender@example.com");
        sender.setRole("USER");
        sender.setBalance(1000.0f);
        sender.setConfirmer(false);

        receiver = new Utilisateur();
        receiver.setId(2);
        receiver.setUsername("receiver");
        receiver.setEmail("receiver@example.com");
        receiver.setRole("USER");
        receiver.setBalance(500.0f);
        receiver.setConfirmer(false);

        admin = new Utilisateur();
        admin.setId(3);
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setRole("ADMIN");
        admin.setBalance(2000.0f);
        admin.setConfirmer(true);

        // Configuration d'une transaction
        transaction = new Transaction();
        transaction.setId(1);
        transaction.setSender(sender);
        transaction.setReciever(receiver);
        transaction.setAmount(100.0f);
        transaction.setDate(LocalDate.now());
        transaction.setMessageContent("Test transaction");
        transaction.setValide(false);
        transaction.setRejet(false);
    }

    @Test
    void getAllTransactions_shouldReturnAllTransactions() {
        // Given
        List<Transaction> expectedTransactions = Arrays.asList(transaction);
        when(transactionRepository.findAll()).thenReturn(expectedTransactions);

        // When
        List<Transaction> actualTransactions = transactionService.getAllTransactions();

        // Then
        assertEquals(expectedTransactions, actualTransactions);
        verify(transactionRepository).findAll();
    }

    @Test
    void getCurrentUserTransactions_whenAuthenticated_shouldReturnUserTransactions() {
        // Given
        String username = "sender";
        List<Transaction> expectedTransactions = Arrays.asList(transaction);
        
        when(authentificationService.getCurrentUsername()).thenReturn(username);
        when(transactionRepository.findAllByUserLogin(username)).thenReturn(expectedTransactions);

        // When
        List<Transaction> actualTransactions = transactionService.getCurrentUserTransactions();

        // Then
        assertEquals(expectedTransactions, actualTransactions);
        verify(authentificationService).getCurrentUsername();
        verify(transactionRepository).findAllByUserLogin(username);
    }

    @Test
    void getCurrentUserTransactions_whenNotAuthenticated_shouldReturnEmptyList() {
        // Given
        when(authentificationService.getCurrentUsername()).thenReturn(null);

        // When
        List<Transaction> actualTransactions = transactionService.getCurrentUserTransactions();

        // Then
        assertTrue(actualTransactions.isEmpty());
        verify(authentificationService).getCurrentUsername();
        verify(transactionRepository, never()).findAllByUserLogin(any());
    }

    @Test
    void getUserTransactions_whenUserExists_shouldReturnUserTransactions() {
        // Given
        Integer userId = 1;
        List<Transaction> expectedTransactions = Arrays.asList(transaction);
        
        when(utilisateurRepository.findById(userId)).thenReturn(Optional.of(sender));
        when(transactionRepository.findAllByUser(sender)).thenReturn(expectedTransactions);

        // When
        List<Transaction> actualTransactions = transactionService.getUserTransactions(userId);

        // Then
        assertEquals(expectedTransactions, actualTransactions);
        verify(utilisateurRepository).findById(userId);
        verify(transactionRepository).findAllByUser(sender);
    }

    @Test
    void getUserTransactions_whenUserDoesNotExist_shouldReturnEmptyList() {
        // Given
        Integer userId = 999;
        when(utilisateurRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        List<Transaction> actualTransactions = transactionService.getUserTransactions(userId);

        // Then
        assertTrue(actualTransactions.isEmpty());
        verify(utilisateurRepository).findById(userId);
        verify(transactionRepository, never()).findAllByUser(any());
    }

    @Test
    void createTransaction_withValidData_shouldCreateTransaction() {
        // Given
        String receiverUsername = "receiver";
        Float amount = 100.0f;
        String content = "Valid transaction";
        
        when(utilisateurService.getCurrentUser()).thenReturn(sender);
        when(utilisateurService.getUserByUserName(receiverUsername)).thenReturn(receiver);

        // When
        transactionService.createTransaction(receiverUsername, amount, content);

        // Then
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        
        Transaction savedTransaction = transactionCaptor.getValue();
        assertEquals(amount, savedTransaction.getAmount());
        assertEquals(content, savedTransaction.getMessageContent());
        assertEquals(sender, savedTransaction.getSender());
        assertEquals(receiver, savedTransaction.getReciever());
        assertEquals(LocalDate.now(), savedTransaction.getDate());
        assertFalse(savedTransaction.isValide()); // Non confirmé car l'utilisateur n'est pas confirmeur
        
        // Vérifiez que les soldes ont été mis à jour
        assertEquals(900.0f, sender.getBalance()); // 1000 - 100
        assertEquals(600.0f, receiver.getBalance()); // 500 + 100
        
        verify(utilisateurService).updateUser(sender);
        verify(utilisateurService).updateUser(receiver);
    }

    @Test
    void createTransaction_withAdminSender_shouldCreateValidatedTransaction() {
        // Given
        String receiverUsername = "receiver";
        Float amount = 100.0f;
        String content = "Admin transaction";
        
        when(utilisateurService.getCurrentUser()).thenReturn(admin);
        when(utilisateurService.getUserByUserName(receiverUsername)).thenReturn(receiver);

        // When
        transactionService.createTransaction(receiverUsername, amount, content);

        // Then
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        
        Transaction savedTransaction = transactionCaptor.getValue();
        assertTrue(savedTransaction.isValide()); // Transaction validée car admin est confirmeur
        
        // Vérifiez que les soldes ont été mis à jour
        assertEquals(1900.0f, admin.getBalance()); // 2000 - 100
        assertEquals(600.0f, receiver.getBalance()); // 500 + 100
    }

    @Test
    void createTransaction_withInsufficientFunds_shouldNotCreateTransaction() {
        // Given
        String receiverUsername = "receiver";
        Float amount = 1500.0f; // Plus que le solde du sender (1000.0f)
        String content = "Transaction with insufficient funds";
        
        when(utilisateurService.getCurrentUser()).thenReturn(sender);
        when(utilisateurService.getUserByUserName(receiverUsername)).thenReturn(receiver);

        // When
        transactionService.createTransaction(receiverUsername, amount, content);

        // Then
        verify(transactionRepository, never()).save(any());
        verify(utilisateurService, never()).updateUser(any());
        
        // Vérifiez que les soldes n'ont pas été modifiés
        assertEquals(1000.0f, sender.getBalance());
        assertEquals(500.0f, receiver.getBalance());
    }

    @Test
    void createTransaction_withNegativeAmount_shouldNotCreateTransaction() {
        // Given
        String receiverUsername = "receiver";
        Float amount = -100.0f; // Montant négatif
        String content = "Transaction with negative amount";
        
        when(utilisateurService.getCurrentUser()).thenReturn(sender);
        when(utilisateurService.getUserByUserName(receiverUsername)).thenReturn(receiver);

        // When
        transactionService.createTransaction(receiverUsername, amount, content);

        // Then
        verify(transactionRepository, never()).save(any());
        verify(utilisateurService, never()).updateUser(any());
    }

    @Test
    void createTransaction_withNonExistentReceiver_shouldNotCreateTransaction() {
        // Given
        String receiverUsername = "nonexistent";
        Float amount = 100.0f;
        String content = "Transaction to non-existent user";
        
        when(utilisateurService.getCurrentUser()).thenReturn(sender);
        when(utilisateurService.getUserByUserName(receiverUsername)).thenReturn(null);

        // When
        transactionService.createTransaction(receiverUsername, amount, content);

        // Then
        verify(transactionRepository, never()).save(any());
        verify(utilisateurService, never()).updateUser(any());
    }

    @Test
    void validerTransaction_shouldMarkTransactionAsValid() {
        // Given
        int transactionId = 1;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        // When
        transactionService.validerTransaction(transactionId);

        // Then
        assertTrue(transaction.isValide());
        verify(transactionRepository).save(transaction);
    }

    @Test
    void rejetTransaction_shouldRejectAndRefundTransaction() {
        // Given
        int transactionId = 1;
        
        // La transaction a déjà été effectuée, donc les soldes ont été mis à jour
        sender.setBalance(900.0f); // 1000 - 100
        receiver.setBalance(600.0f); // 500 + 100
        
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        // When
        transactionService.rejetTransaction(transactionId);

        // Then
        assertTrue(transaction.isValide());
        assertTrue(transaction.isRejet());
        
        // Vérifiez que les soldes ont été remis à leur état initial
        assertEquals(1000.0f, sender.getBalance()); // 900 + 100
        assertEquals(500.0f, receiver.getBalance()); // 600 - 100
        
        verify(utilisateurService).updateUser(sender);
        verify(utilisateurService).updateUser(receiver);
        verify(transactionRepository).save(transaction);
    }
}