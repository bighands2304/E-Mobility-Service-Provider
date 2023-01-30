package softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SocketDTO {
    private Long socketId;
    private String socketType;
    private String status;
    private String availability;
    private LocalDateTime lastUpdate;
}