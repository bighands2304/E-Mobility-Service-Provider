package softwareengineering.manonisgaravattiferretti.cpmsServer.dsoHandler.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VersionUrlDTO {
    private String version;
    private String url;
}
