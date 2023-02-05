package softwareengineering.manonisgaravattiferretti.cpmsServer.registrationManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.CPORegistrationDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.CPOService;

import java.lang.module.ResolutionException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RegistrationManagerTest {
    @Autowired
    RegistrationManager registrationManager;
    @Autowired
    CPOService cpoService;

    @Test
    void loadContextTest() {
        Assertions.assertNotNull(registrationManager);
    }

    @Test
    void registerCPO() {
        CPORegistrationDTO cpoRegistrationDTO = new CPORegistrationDTO();
        cpoRegistrationDTO.setCpoCode("test cpo");
        cpoRegistrationDTO.setIban("test iban");
        cpoRegistrationDTO.setPassword("test password");
        ResponseEntity<?> response = registrationManager.registerCPO(cpoRegistrationDTO);
        Assertions.assertEquals(201, response.getStatusCode().value());
        cpoService.deleteCpoByCode("test cpo");
    }

    @Test
    void registerCPO_invalid() {
        CPORegistrationDTO cpoRegistrationDTO = new CPORegistrationDTO();
        cpoRegistrationDTO.setCpoCode("test cpo");
        cpoRegistrationDTO.setIban("test iban");
        cpoRegistrationDTO.setPassword(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> registrationManager.registerCPO(cpoRegistrationDTO));
    }
}