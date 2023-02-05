package softwareEngineering.ManoniSgaravattiFerretti.emspServer.ResponseDTO;

import lombok.Data;

@Data
public class LoginDTO {
    private String jwt;
    private UserDTO user;
}
