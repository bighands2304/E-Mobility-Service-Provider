package softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataManager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ResponseDTO.LoginDTO;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.User;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Service.UserService;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDataControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    PasswordEncoder mockPasswordEncoder;
    @Autowired
    UserService userService;
    LoginDTO loginData;
    @BeforeAll
    void setup() throws Exception {
        User user = new User();
        user.setUsername("Username");
        user.setEmail("email@gmail.com");
        user.setPassword(mockPasswordEncoder.encode("Password"));
        user.setName("Name");
        user.setSurname("Surname");
        userService.saveUser(user);

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

        loginData= new Gson().fromJson(response.getContentAsString(), LoginDTO.class);
    }

    @Test
    @Order(1)
    void testAddVehicle() throws Exception {
        String body = "{" +
                "\"userId\":\""+loginData.getUser().getId()+"\"," +
                "\"vin\":\"12345678901234567\"," +
                "\"favourite\":\"false\"" +
                "}";

        final MockHttpServletResponse response = mockMvc.perform(post("/user/addVehicle")
                        .header("Authorization", "Bearer " + loginData.getJwt())
                        .with(csrf())
                        .content(body).contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("user","vehicle","\"favourite\":false");
    }
    @Test
    @Order(2)
    void testSetFavouriteVehicle() throws Exception {
        String body = "{" +
                "\"userId\":\""+loginData.getUser().getId()+"\"," +
                "\"vin\":\"12345678901234567\"" +
                "}";

        final MockHttpServletResponse response = mockMvc.perform(post("/user/setFavouriteVehicle")
                        .header("Authorization", "Bearer " + loginData.getJwt())
                        .with(csrf())
                        .content(body).contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("\"favourite\":true");
    }

    @Test
    @Order(3)
    void testGetUserVehicles() throws Exception{
        final MockHttpServletResponse response = mockMvc.perform(get("/user/getUserVehicles/" + loginData.getUser().getId())
                        .header("Authorization", "Bearer " + loginData.getJwt())
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("[{\"user\":","12345678901234567");
    }



    @Test
    @Order(4)
    void testDeleteVehicle() throws Exception{

        final MockHttpServletResponse response = mockMvc.perform(delete("/user/deleteVehicle/12345678901234567")
                        .header("Authorization", "Bearer " + loginData.getJwt())
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @AfterAll
    void teardown() {
        userService.deleteUser(userService.findById(loginData.getUser().getId()));
    }
}
