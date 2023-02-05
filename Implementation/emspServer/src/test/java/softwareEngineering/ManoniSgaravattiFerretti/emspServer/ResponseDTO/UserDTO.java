package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ResponseDTO;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String password;
}
