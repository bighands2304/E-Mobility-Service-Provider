package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.EmspCredentialsDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.EmspRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmspDetailsService {

    private final EmspRepository emspRepository;

    @Autowired
    public EmspDetailsService(EmspRepository emspRepository) {
        this.emspRepository = emspRepository;
    }

    public List<EmspDetails> findAll() {
        return emspRepository.findAll();
    }

    public Optional<EmspDetails> findEmspById(String emspId) {
        return emspRepository.findById(emspId);
    }

    public Optional<EmspDetails> findByEmspToken(String emspToken) {
        return emspRepository.findByEmspToken(emspToken);
    }

    public EmspDetails insertEmsp(EmspDetails emspDetails) {
        return emspRepository.save(emspDetails);
    }

    public void updateEmspCredentials(EmspCredentialsDTO emspCredentials, String emspToken) {
        emspRepository.updateEmspCredentials(emspToken, emspCredentials);
    }

    public void updateEmspAddCpoToken(String emspToken, String cpoToken) {
        emspRepository.updateEmspAddCpoToken(emspToken, cpoToken);
    }
}
