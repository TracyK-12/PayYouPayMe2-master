package com.cda.PayYouPayMe.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cda.PayYouPayMe.model.Message;
import com.cda.PayYouPayMe.model.Utilisateur;
import com.cda.PayYouPayMe.repository.MessageRepository;

@Service
public class MessageService {

	private final MessageRepository messageRepository;
	private final UtilisateurService utilisateurService;
	
	public MessageService(MessageRepository messageRepository,
			UtilisateurService utilisateurService) {
		this.messageRepository = messageRepository;
		this.utilisateurService = utilisateurService;
	}

	public List<Message> getAllMessages() {
		return this.messageRepository.findAll();
	}

	public List<Message> getMessageFromCurrentUser() {
		return utilisateurService.getCurrentUser().getMessageSent();
	}

	public void addMessage(String messagecontent) {
		Utilisateur utilisateur = utilisateurService.getCurrentUser();
		Message message = new Message();
		message.setContent(messagecontent);
		message.setDate(LocalDate.now());
		message.setUtilisateur(utilisateur);
		
		messageRepository.save(message);
		
	}

	public void repondreMessage(int id, String content) {
		Message messageToUpdate = messageRepository.findById(id).orElse(null);
		if(messageToUpdate!=null) {
			messageToUpdate.setReponse(content);
			messageRepository.save(messageToUpdate);
		}
		
	}
}
