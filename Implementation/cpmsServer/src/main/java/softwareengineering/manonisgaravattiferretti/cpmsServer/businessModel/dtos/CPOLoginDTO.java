package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CPOLoginDTO {
    @NotBlank
    private String cpoCode;
    @NotBlank
    private String password;
}
