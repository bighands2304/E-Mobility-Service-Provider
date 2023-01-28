package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmspSocketDTO {
    private Integer socketId;
    private String type;
    private String status;
    private String availability;
    private LocalDateTime lastUpdated;
}
