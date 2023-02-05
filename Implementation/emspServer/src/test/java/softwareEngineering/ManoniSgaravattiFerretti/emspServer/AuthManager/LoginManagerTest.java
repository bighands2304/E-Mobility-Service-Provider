package softwareEngineering.ManoniSgaravattiFerretti.emspServer.AuthManager;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ResponseDTO.LoginDTO;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.User;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.UserService;

import java.util.HashMap;
import java.util.Map;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class LoginManagerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder mockPasswordEncoder;
    @Autowired
    LoginManager loginManager;

    User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setUsername("Username");
        user.setEmail("email@gmail.com");
        user.setPassword(mockPasswordEncoder.encode("Password"));
        user.setName("Name");
        user.setSurname("Surname");

        userService.saveUser(user);
    }

    @Test
    void testLogin() throws Exception {
        String body = "{" +
                "\"username\":\"Username\"," +
                "\"password\":\"Password\"" +
                "}";

        final MockHttpServletResponse response = mockMvc.perform(post("/login")
                        .with(csrf())
                        .content(body).contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("jwt");
    }

    @AfterEach
    void teardown() {
        userService.deleteUser(user);
    }
}
