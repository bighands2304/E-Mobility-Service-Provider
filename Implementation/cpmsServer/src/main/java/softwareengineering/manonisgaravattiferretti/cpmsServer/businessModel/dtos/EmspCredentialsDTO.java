package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmspCredentialsDTO {
    @NotBlank
    private String emspToken;
    @NotBlank
    private String url;
}
