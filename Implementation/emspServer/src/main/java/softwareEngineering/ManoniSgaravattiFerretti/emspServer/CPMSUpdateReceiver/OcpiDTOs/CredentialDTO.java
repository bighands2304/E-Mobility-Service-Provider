package softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSUpdateReceiver.OcpiDTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CredentialDTO {
    @NotBlank
    private String url;
    @NotBlank
    private String iban;
    private String cpmsToken;
    private String emspToken;

}
