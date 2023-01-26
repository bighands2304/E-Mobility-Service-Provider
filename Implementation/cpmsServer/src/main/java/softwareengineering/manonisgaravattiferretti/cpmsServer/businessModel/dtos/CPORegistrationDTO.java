package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CPORegistrationDTO {
    @NotBlank
    private String cpoCode;
    @Size(min = 8)
    private String password;
    @NotBlank
    private String iban;
}
