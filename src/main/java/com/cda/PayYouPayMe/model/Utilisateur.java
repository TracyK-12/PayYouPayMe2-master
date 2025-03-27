package com.cda.PayYouPayMe.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(unique = true, nullable = false)
	private String username;
	
	private String lastName;

	//@Column(name="Prenom")
	private String firstName;

	private String email;

	private String iban;

	private Float balance;

	private String login;
	private String password;

	private String role;

	private boolean actif;
	
	private boolean confirmer;
	
	public Utilisateur() {
		super();
	}
	
	
	
	

	public Utilisateur(Integer id, String username, String lastName, String firstName, String email, String iban,
			Float balance, String login, String password, String role, boolean actif, List<Transfert> transferts,
			List<Transaction> transactionSent, List<Transaction> transactionRecieved, List<Message> messageSent,
			List<Utilisateur> contact) {
		super();
		this.id = id;
		this.username = username;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.iban = iban;
		this.balance = balance;
		this.login = login;
		this.password = password;
		this.role = role;
		this.actif = actif;
		this.transferts = transferts;
		this.transactionSent = transactionSent;
		this.transactionRecieved = transactionRecieved;
		this.messageSent = messageSent;
		this.contact = contact;
	}





	public Utilisateur(Integer id, String lastName, String firstName, String email, String iban, Float balance,
			String login, String password, List<Transaction> transactionSent, List<Transaction> transactionRecieved,
			List<Message> messageSent, List<Utilisateur> contact, String role) {
		super();
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.iban = iban;
		this.balance = balance;
		this.login = login;
		this.password = password;
		this.transactionSent = transactionSent;
		this.transactionRecieved = transactionRecieved;
		this.messageSent = messageSent;
		this.contact = contact;
		this.role = role;
	}

	public Utilisateur(Integer id, String username, String lastName, String firstName, String email, String iban,
			Float balance, String login, String password, List<Transaction> transactionSent,
			List<Transaction> transactionRecieved, List<Message> messageSent, List<Utilisateur> contact, String role) {
		super();
		this.id = id;
		this.username = username;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.iban = iban;
		this.balance = balance;
		this.login = login;
		this.password = password;
		this.transactionSent = transactionSent;
		this.transactionRecieved = transactionRecieved;
		this.messageSent = messageSent;
		this.contact = contact;
		this.role = role;
	}

	@OneToMany(mappedBy = "sender")
	private List<Transfert> transferts = new ArrayList<Transfert>();
	
	@OneToMany(mappedBy = "sender")
	private List<Transaction> transactionSent = new ArrayList<Transaction>();

	@OneToMany(mappedBy = "reciever")
	private List<Transaction> transactionRecieved = new ArrayList<Transaction>();

	@OneToMany(mappedBy = "utilisateur")
	private List<Message> messageSent = new ArrayList<Message>();

	@ManyToMany//(mappedBy = "contact")
	private List<Utilisateur> contact = new ArrayList<Utilisateur>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public Float getBalance() {
		return balance;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Transaction> getTransactionSent() {
		return transactionSent;
	}

	public void setTransactionSent(List<Transaction> transactionSent) {
		this.transactionSent = transactionSent;
	}

	public List<Transaction> getTransactionRecieved() {
		return transactionRecieved;
	}

	public void setTransactionRecieved(List<Transaction> transactionRecieved) {
		this.transactionRecieved = transactionRecieved;
	}

	public List<Message> getMessageSent() {
		return messageSent;
	}

	public void setMessageSent(List<Message> messageSent) {
		this.messageSent = messageSent;
	}

	public List<Utilisateur> getContact() {
		return contact;
	}

	public void setContact(List<Utilisateur> contact) {
		this.contact = contact;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<Transfert> getTransferts() {
		return transferts;
	}

	public void setTransferts(List<Transfert> transferts) {
		this.transferts = transferts;
	}

	public boolean isActif() {
		return actif;
	}

	public void setActif(boolean actif) {
		this.actif = actif;
	}

	public boolean isConfirmer() {
		return confirmer;
	}

	public void setConfirmer(boolean confirmer) {
		this.confirmer = confirmer;
	}

	
}
