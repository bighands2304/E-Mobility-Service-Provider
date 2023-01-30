package softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.dtos;

import lombok.Data;

@Data
public class RegistrationDTO {
    private String token;
    private String dsoId;
    private String companyName;
    private String cpId;
    private VersionUrlDTO versionUrl;
}
