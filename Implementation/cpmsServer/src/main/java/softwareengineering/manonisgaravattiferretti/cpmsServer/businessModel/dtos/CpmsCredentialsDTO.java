package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CpmsCredentialsDTO {
    private String token;
    private String url;
}
