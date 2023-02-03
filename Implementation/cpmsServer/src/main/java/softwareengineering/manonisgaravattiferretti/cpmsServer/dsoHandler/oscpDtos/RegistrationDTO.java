package softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.oscpDtos;

import lombok.Data;

@Data
public class RegistrationDTO {
    private String token;
    private String dsoId;
    private String companyName;
    private String cpId;
    private String url;
}
