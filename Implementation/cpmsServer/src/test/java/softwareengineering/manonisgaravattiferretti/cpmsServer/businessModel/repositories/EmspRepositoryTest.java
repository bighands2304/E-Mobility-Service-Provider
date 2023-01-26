package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.EmspDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmspRepositoryTest {
    @Autowired
    private EmspRepository emspRepository;
    private final EmspDetails emspDetails = new EmspDetails();

    @BeforeEach
    void setup() {
        emspDetails.setEmspToken("token_test");
        emspDetails.setUrl("emspUrl");
    }

    @Test
    void updateCpoTokenTest() {
        Assertions.assertTrue(emspRepository.findByEmspToken("token_test").isEmpty());
        emspRepository.save(emspDetails);
        emspRepository.findByEmspToken("token_test");
        Assertions.assertFalse(emspRepository.findByEmspToken("token_test").isEmpty());
        emspRepository.updateEmspAddCpoToken("token_test", "cpo_token");
        Optional<EmspDetails> emspDetailsUpdatedOptional = emspRepository.findByEmspToken("token_test");
        Assertions.assertFalse(emspDetailsUpdatedOptional.isEmpty());
        Assertions.assertEquals("cpo_token", emspDetailsUpdatedOptional.get().getCpoToken());
        emspRepository.delete(emspDetailsUpdatedOptional.get());
        Assertions.assertTrue(emspRepository.findByEmspToken("token_test").isEmpty());
    }



}