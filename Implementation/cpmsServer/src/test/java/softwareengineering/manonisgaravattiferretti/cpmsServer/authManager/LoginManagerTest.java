package softwareengineering.manonisgaravattiferretti.cpmsServer.authManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.CPOLoginDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.CPORegistrationDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.ChangePasswordDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.CPO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.services.CPOService;
import softwareengineering.manonisgaravattiferretti.cpmsServer.registrationManager.RegistrationManager;

import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginManagerTest {
    @Autowired
    LoginManager loginManager;
    @Autowired
    RegistrationManager registrationManager;
    @Autowired
    CPOService cpoService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        CPO cpo = new CPO();
        cpo.setCpoCode("test login");
        cpo.setPassword(passwordEncoder.encode("test"));
        cpoService.insertCPO(cpo);
    }

    @Test
    void login() {
        CPOLoginDTO cpoLoginDTO = new CPOLoginDTO();
        cpoLoginDTO.setCpoCode("test login");
        cpoLoginDTO.setPassword("test");
        ResponseEntity<Map<String, Object>> response = loginManager.login(cpoLoginDTO);
        Assertions.assertEquals(201, response.getStatusCode().value());
        Assertions.assertNotNull(Objects.requireNonNull(response.getBody()).get("jwt"));
    }

    @AfterEach
    void teardown() {
        cpoService.deleteCpoByCode("test login");
    }
}