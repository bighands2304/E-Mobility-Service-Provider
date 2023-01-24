package softwareengineering.manonisgaravattiferretti.cpmsServer.model.dtos;

import lombok.Data;

@Data
public class CPORegistrationDTO {
    private String cpoCode;
    private String password;
    private String iban;
}
