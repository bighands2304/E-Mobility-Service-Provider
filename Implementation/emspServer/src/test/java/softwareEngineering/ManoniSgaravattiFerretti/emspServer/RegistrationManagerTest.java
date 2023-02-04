package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.AuthManager.LoginManager;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.User;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RegistrationManager.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class RegistrationManagerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginManager mockLoginManager;

    @MockBean
    private UserService mockUserService;
    @MockBean
    private PasswordEncoder mockPasswordEncoder;

    @Value("${emsp.path}")
    private String emspPath;

    @Test
    void testRegister() throws Exception {
        // Setup
        String body = "{" +
                        "\"username\":\"Username\"," +
                        "\"email\":\"email@gmail.com\"," +
                        "\"password\":\"Password\"," +
                        "\"name\":\"Name\"," +
                        "\"surname\":\"Surname\"" +
                    "}";

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/register")
                        .with(csrf())
                        .content(body).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("\"enabled\":true");

        User user = new User();
        user.setUsername("Username");
        user.setEmail("email@gmail.com");
        user.setPassword(mockPasswordEncoder.encode("Password"));
        user.setName("Name");
        user.setSurname("Surname");


        verify(mockUserService).saveUser(user);

    }
}
