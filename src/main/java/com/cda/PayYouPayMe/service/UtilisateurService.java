package com.cda.PayYouPayMe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import com.cda.PayYouPayMe.model.Utilisateur;
import com.cda.PayYouPayMe.repository.UtilisateurRepository;

@Service
public class UtilisateurService {

	private final UtilisateurRepository utilisateurRepository;
    private final AuthentificationService authService;

	public UtilisateurService(UtilisateurRepository utilisateurRepository,
			AuthentificationService authService) {
		this.utilisateurRepository = utilisateurRepository;
		this.authService = authService;
	}

    public Utilisateur getCurrentUser() {
        String username = authService.getCurrentUsername();
        if (username != null) {
            Optional<Utilisateur> utilisateur = utilisateurRepository.findByUsername(username);
            return utilisateur.orElse(null);
        }
        return null;
    }
	
	
	public List<Utilisateur> getAllUtilisateurs() {
		return this.utilisateurRepository.findAll();
	}

	public void createUser(Utilisateur utilisateurToSave) {
		   if (utilisateurRepository.existsByUsername(utilisateurToSave.getUsername())) {
		        throw new RuntimeException("Ce nom d'utilisateur est déjà pris.");
		    }
		utilisateurRepository.save(utilisateurToSave);
	}
	
	public void updateUser(Utilisateur userToSave) {
		Utilisateur utilisateurToModify = getCurrentUser();
		utilisateurToModify.setFirstName(userToSave.getFirstName());
		utilisateurToModify.setLastName(userToSave.getLastName());
		utilisateurToModify.setEmail(userToSave.getEmail());
		utilisateurRepository.save(utilisateurToModify);
	
	}

	public void addUserToContactList(String userNameToAdd) {
		Utilisateur userConnected = getCurrentUser();
		Utilisateur userToAdd = getUserByUserName(userNameToAdd);
		if(userToAdd!=null
				&& userToAdd!= userConnected
				&& !userConnected.getContact().contains(userToAdd)) {
			userConnected.getContact().add(userToAdd);
		}
		utilisateurRepository.save(userConnected);

	}
	
	public Utilisateur getUserByUserName(String usernametoadd) {
		return utilisateurRepository.findByUsername(usernametoadd).orElse(null);
	}

	public void deleteContactById(Integer id) {
		Utilisateur userConnected = getCurrentUser();
		Utilisateur contactToDelete = utilisateurRepository.findById(id).orElse(null);
		if(contactToDelete!=null) {
			userConnected.getContact().remove(contactToDelete);
		}
		utilisateurRepository.save(userConnected);
		
	}

	public void suspendreCompte() {
		Utilisateur userConnected = getCurrentUser();
		userConnected.setRole("SUSPENDU");
		utilisateurRepository.save(userConnected);		
	}

	public void suspendreCompte(int id) {
		Utilisateur utilisateurToSuspendre = utilisateurRepository.findById(id).get();
		utilisateurToSuspendre.setActif(false);
		utilisateurToSuspendre.setRole("SUSPENDU");
		utilisateurRepository.save(utilisateurToSuspendre);
		
	}

	public void confirmerUser(int id) {
		Utilisateur utilisateurToConfirm = utilisateurRepository.getById(id);
		utilisateurToConfirm.setConfirmer(true);
		utilisateurRepository.save(utilisateurToConfirm);
		
	}
}
