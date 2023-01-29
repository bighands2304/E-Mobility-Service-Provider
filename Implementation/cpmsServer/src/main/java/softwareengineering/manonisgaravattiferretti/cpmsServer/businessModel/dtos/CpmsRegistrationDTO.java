package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CpmsRegistrationDTO {
    private String emspToken;
    private String cpmsUrl;
    private String iban;
}
