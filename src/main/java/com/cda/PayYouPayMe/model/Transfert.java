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
public class Transfert {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	
	private Float amount;
	
	private String messageContent;
	private LocalDate date;

	@ManyToOne
	private Utilisateur sender;
	
	public Transfert() {
		super();
	}

	
	
	public Transfert(Float amount, String messageContent, LocalDate date, Utilisateur sender) {
		super();
		this.amount = amount;
		this.messageContent = messageContent;
		this.date = date;
		this.sender = sender;
	}



	public Transfert(Integer id, Float amount, String messageContent, LocalDate date, Utilisateur sender) {
		super();
		this.id = id;
		this.amount = amount;
		this.messageContent = messageContent;
		this.date = date;
		this.sender = sender;
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
	
	
	
	
}
