package com.cda.PayYouPayMe.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	
	private Float amount;
	
	private String messageContent;
	private LocalDate date;

	@ManyToOne
	private Utilisateur sender;

	@ManyToOne
	private Utilisateur reciever;

	private boolean valide;
	
	private boolean rejet;
	
	
	public Transaction(Float amount, String messageContent, LocalDate date) {
		super();
		this.amount = amount;
		this.messageContent = messageContent;
		this.date = date;
	}

	public Transaction() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Utilisateur getSender() {
		return sender;
	}

	public void setSender(Utilisateur sender) {
		this.sender = sender;
	}

	public Utilisateur getReciever() {
		return reciever;
	}

	public void setReciever(Utilisateur reciever) {
		this.reciever = reciever;
	}

	public boolean isValide() {
		return valide;
	}

	public void setValide(boolean valide) {
		this.valide = valide;
	}

	public boolean isRejet() {
		return rejet;
	}

	public void setRejet(boolean rejet) {
		this.rejet = rejet;
	}

	
}
