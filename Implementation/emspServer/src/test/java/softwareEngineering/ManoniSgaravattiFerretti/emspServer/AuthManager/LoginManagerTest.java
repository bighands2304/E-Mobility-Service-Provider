package softwareEngineering.ManoniSgaravattiFerretti.emspServer.AuthManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.ChargingPointOperatorService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.User;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoginManager.class)
class LoginManagerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService mockUserService;
    @MockBean
    private ChargingPointOperatorService mockCpoService;
    @MockBean
    private AuthenticationManager mockAuthenticationManager;
    @MockBean
    private TokenManager mockTokenManager;
    @MockBean
    private PasswordEncoder mockPasswordEncoder;



    @Test
    void testLogin() throws Exception {

        User user = new User();
        user.setUsername("Username");
        user.setEmail("email@gmail.com");
        user.setPassword(mockPasswordEncoder.encode("Password"));
        user.setName("Name");
        user.setSurname("Surname");

        (mockUserService).saveUser(user);

        String body = "{" +
                "\"username\":\"Username\"," +
                "\"password\":\"Password\"" +
                "}";

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/login")
                        .with(csrf())
                        .content(body).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("jwt");
        verify(mockAuthenticationManager).authenticate(null);
    }

    @Test
    void testLogin_AuthenticationManagerThrowsAuthenticationException() throws Exception {
        // Setup
        when(mockAuthenticationManager.authenticate(null)).thenThrow(AuthenticationException.class);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/login")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testLogin_UserServiceFindByAnyCredentialReturnsNull() throws Exception {
        // Setup
        when(mockAuthenticationManager.authenticate(null)).thenReturn(null);
        when(mockUserService.findByAnyCredential("credential")).thenReturn(null);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/login")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
        verify(mockAuthenticationManager).authenticate(null);
    }
}
