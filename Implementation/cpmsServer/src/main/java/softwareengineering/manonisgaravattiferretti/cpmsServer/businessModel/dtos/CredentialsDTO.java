package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos;

import lombok.Data;

@Data
public class CredentialsDTO {
    private String emspToken;
    private String emspUrl;
    private String cpmsUrl;
    private String cpmsToken;
    private String iban;
}
