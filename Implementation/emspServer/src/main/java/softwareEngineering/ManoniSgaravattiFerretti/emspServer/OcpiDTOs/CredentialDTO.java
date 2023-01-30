package softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs;

import lombok.Data;

@Data
public class CredentialDTO {
    private String cpmsUrl;
    private String emspUrl;
    private String iban;
    private String cpmsToken;
    private String emspToken;
}
