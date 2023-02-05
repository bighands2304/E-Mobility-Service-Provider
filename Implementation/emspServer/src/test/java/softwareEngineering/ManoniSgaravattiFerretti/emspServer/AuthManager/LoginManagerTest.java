package softwareEngineering.ManoniSgaravattiFerretti.emspServer.AuthManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.User;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.UserService;

import java.util.HashMap;
import java.util.Map;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LoginManagerTest {

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
        Map body = new HashMap<>();
        body.put("username","Username");
        body.put("password", "Password");

        // Run the test
        ResponseEntity<?> response = loginManager.login(body);
        Map resp= (Map) response.getBody();
        // Verify the results
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(resp.get("jwt")).isNotNull();
    }

    @AfterEach
    void teardown() {
        userService.deleteUser(user);
    }
/*
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
    */
}
